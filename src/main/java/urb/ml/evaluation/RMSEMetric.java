package urb.ml.evaluation;

import io.prediction.controller.EmptyParams;
import io.prediction.controller.Metric;
import io.prediction.controller.java.SerializableComparator;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.rdd.RDD;
import scala.Tuple2;
import scala.Tuple3;
import scala.collection.JavaConversions;
import scala.collection.Seq;
import urb.ml.serving.PredictedResult;
import urb.ml.serving.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moises on 6/30/15.
 */
public class RMSEMetric extends Metric<EmptyParams, Query, PredictedResult, ActualResult, Double>{

    private static final class MetricComparator implements SerializableComparator<Double> {
        @Override
        public int compare(Double o1, Double o2) {
            return o1.compareTo(o2);
        }
    }

    public RMSEMetric(){
        super(new MetricComparator());
    }

    @Override
    public Double calculate(SparkContext sc, Seq<Tuple2<EmptyParams, RDD<Tuple3<Query, PredictedResult, ActualResult>>>> evalDataSet) {
        List<Tuple2<EmptyParams, RDD<Tuple3<Query, PredictedResult, ActualResult>>>> evalDataSetList = JavaConversions.asJavaList(evalDataSet);
        List<Double> partialRMSEList = new ArrayList<>();
        for (Tuple2<EmptyParams, RDD<Tuple3<Query, PredictedResult, ActualResult>>> qpaTuple : evalDataSetList){
            double squaredError = qpaTuple._2().toJavaRDD().map(new Function<Tuple3<Query,PredictedResult,ActualResult>, Double>() {
                @Override
                public Double call(Tuple3<Query, PredictedResult, ActualResult> t) throws Exception {
                    return Math.pow(t._2().getResult() - t._3().getActualResult() , 2);
                }
            }).reduce(new Function2<Double, Double, Double>() {
                @Override
                public Double call(Double a, Double b) throws Exception {
                    return a + b;
                }
            });
            double rootMeanSquaredError = Math.sqrt(squaredError / qpaTuple._2().count());
            partialRMSEList.add(rootMeanSquaredError);
        }
        double totalRMSE = 0;
        for (double partialRMSE : partialRMSEList){
            totalRMSE += partialRMSE;
        }

        totalRMSE = totalRMSE / partialRMSEList.size();

        return totalRMSE;
    }
}

