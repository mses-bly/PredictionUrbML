package urb.ml.data;

import io.prediction.controller.EmptyParams;
import io.prediction.controller.java.PJavaDataSource;
import io.prediction.data.storage.PropertyMap;
import io.prediction.data.store.java.OptionHelper;
import io.prediction.data.store.java.PJavaEventStore;
import org.apache.log4j.Logger;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.rdd.RDD;
import org.joda.time.DateTime;
import scala.Tuple2;
import scala.Tuple3;
import scala.collection.JavaConversions;
import scala.collection.Seq;
import urb.ml.evaluation.ActualResult;
import urb.ml.serving.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataSource extends PJavaDataSource<TrainingData, EmptyParams, Query, ActualResult> {

    private static final Logger LOGGER = Logger.getLogger(DataSource.class);

    private final DataSourceParams dsp;

    public DataSource(DataSourceParams dsp) {
        this.dsp = dsp;
    }

    @Override
    public TrainingData readTraining(SparkContext sc) {

        if (dsp.getAppName().isEmpty()) {
            throw new AssertionError("Expected application name parameter");
        }

        if (dsp.getFeatures() == null || dsp.getFeatures().isEmpty()) {
            throw new AssertionError("Expected features list for training model");
        }
        LOGGER.info("Features for this model are: " + dsp.getFeatures());

        if (dsp.getResponse() == null || dsp.getResponse().isEmpty()) {
            throw new AssertionError("Expected response value for training model");
        }
        LOGGER.info("Response variable name for this model is: " + dsp.getResponse());

        List<String> mandatoryProperties = new ArrayList<>(dsp.getFeatures());
        mandatoryProperties.add(dsp.getResponse());

        JavaRDD<LabeledPoint> recordRDD = PJavaEventStore.aggregateProperties(
                dsp.getAppName(),
                "data",
                OptionHelper.<String>none(),
                OptionHelper.<DateTime>none(),
                OptionHelper.<DateTime>none(),
                OptionHelper.<List<String>>some(mandatoryProperties),
                sc).map(new Function<Tuple2<String, PropertyMap>, LabeledPoint>() {
            @Override
            public LabeledPoint call(Tuple2<String, PropertyMap> stringPropertyMapTuple2) throws Exception {
                int numFeatures = dsp.getFeatures().size();
                double features[] = new double[numFeatures];
                for (int i = 0; i < numFeatures; i++) {
                    features[i] = Double.valueOf(stringPropertyMapTuple2._2().get(dsp.getFeatures().get(i), String.class));
                }
                double response = Double.valueOf(stringPropertyMapTuple2._2().get(dsp.getResponse(), String.class));
                return new LabeledPoint(response, Vectors.dense(features));
            }
        });

        return new TrainingData(recordRDD);
    }

    @Override
    public Seq<Tuple3<TrainingData, EmptyParams, RDD<Tuple2<Query, ActualResult>>>> readEval(SparkContext sc) {
        if (dsp.getNumFolds() < 1) {
            throw new AssertionError("Number of folds for evaluation must be greater than 0");
        }

        int numFolds = dsp.getNumFolds();
        LOGGER.info("K-Fold cross validation with K: " + dsp.getNumFolds());

        JavaRDD<LabeledPoint> allTrainingData = readTraining(sc).getRecords();
        JavaPairRDD<LabeledPoint, Long> indexedRecords = allTrainingData.zipWithIndex();

        List<Tuple3<TrainingData, EmptyParams, RDD<Tuple2<Query, ActualResult>>>> evaluationData = new ArrayList<>();

        for (int k = 0; k < numFolds; k++){
            final int K = k;
            JavaRDD<LabeledPoint> kFoldTrainingRecords = indexedRecords.filter(new Function<Tuple2<LabeledPoint, Long>, Boolean>() {
                @Override
                public Boolean call(Tuple2<LabeledPoint, Long> t) throws Exception {
                    return t._2() % 2 != K;
                }
            }).map(new Function<Tuple2<LabeledPoint,Long>, LabeledPoint>(){
                @Override
                public LabeledPoint call(Tuple2<LabeledPoint, Long> t) throws Exception {
                    return t._1();
                }
            });

            JavaRDD<LabeledPoint> kFoldEvaluationRecords = indexedRecords.filter(new Function<Tuple2<LabeledPoint, Long>, Boolean>() {
                @Override
                public Boolean call(Tuple2<LabeledPoint, Long> t) throws Exception {
                    return t._2() % 2 == K;
                }
            }).map(new Function<Tuple2<LabeledPoint,Long>, LabeledPoint>(){
                @Override
                public LabeledPoint call(Tuple2<LabeledPoint, Long> t) throws Exception {
                    return t._1();
                }
            });

            TrainingData kTrainingData = new TrainingData(kFoldTrainingRecords);
            JavaRDD<Tuple2<Query, ActualResult>> kEvaluationData = kFoldEvaluationRecords.map(new Function<LabeledPoint, Tuple2<Query, ActualResult>>() {
                @Override
                public Tuple2<Query, ActualResult> call(LabeledPoint labeledPoint) throws Exception {
                    return new Tuple2<Query, ActualResult>(new Query(labeledPoint.features().toArray()),new ActualResult(labeledPoint.label()));
                }
            });
            Tuple3<TrainingData, EmptyParams, RDD<Tuple2<Query, ActualResult>>> kTuple = new Tuple3<>(kTrainingData,new EmptyParams(), JavaRDD.toRDD(kEvaluationData));
            evaluationData.add(kTuple);
        }
        return JavaConversions.asScalaIterable(evaluationData).toSeq();
    }
}