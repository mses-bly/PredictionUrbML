package urb.ml.data;

import io.prediction.controller.java.PJavaPreparator;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

public class Preparator extends PJavaPreparator<TrainingData, PreparedData> {
    private final String[] featureNames = {"V1", "V2", "V3", "V4", "V5", "V6", "V7", "V8", "V9", "V10"};
    private final String responseName = "resp2";

    @Override
    public PreparedData prepare(SparkContext sc, TrainingData trainingData) {
        JavaRDD<LabeledPoint> regressionTrainingData = trainingData.getRecords().map(new Function<Record, LabeledPoint>() {
            @Override
            public LabeledPoint call(Record record) throws Exception {
                double[] features = new double[featureNames.length];
                for (int i = 0; i < featureNames.length; i++) {
                    String featureName = featureNames[i];
                    if (record.containsFeature(featureName)) {
                        features[i] = Double.valueOf(record.getFeature(featureName));
                    } else {
                        throw new AssertionError("Expected feature [" + featureName + "] to be present");
                    }
                }
                if (record.containsFeature(responseName)) {
                    double response = Double.valueOf(record.getFeature(responseName));
                    LabeledPoint labeledPoint = new LabeledPoint(response, Vectors.dense(features));
                    return labeledPoint;
                } else {
                    throw new AssertionError("Expected response variable [" + responseName + "] to be present");
                }
            }
        });
        return new PreparedData(regressionTrainingData);
    }
}

