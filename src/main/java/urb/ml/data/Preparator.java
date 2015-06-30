package urb.ml.data;

import io.prediction.controller.java.PJavaPreparator;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

public class Preparator extends PJavaPreparator<TrainingData, PreparedData> {
    @Override
    public PreparedData prepare(SparkContext sc, TrainingData trainingData) {
        return new PreparedData(trainingData.getRecords());
    }
}

