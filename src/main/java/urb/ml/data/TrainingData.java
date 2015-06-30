package urb.ml.data;


import io.prediction.controller.SanityCheck;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.LabeledPoint;

import java.io.Serializable;

public class TrainingData implements Serializable, SanityCheck {
    private final JavaRDD<LabeledPoint> records;

    public TrainingData(JavaRDD<LabeledPoint> records) {
        this.records = records;
    }

    public JavaRDD<LabeledPoint> getRecords(){
        return records;
    }
    @Override
    public void sanityCheck() {
        if (records.isEmpty()) {
            throw new AssertionError("Record data is empty");
        }
    }
}
