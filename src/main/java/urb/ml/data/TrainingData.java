package urb.ml.data;


import io.prediction.controller.SanityCheck;
import org.apache.spark.api.java.JavaRDD;

import java.io.Serializable;

public class TrainingData implements Serializable, SanityCheck {
    private final JavaRDD<Record> records;

    public TrainingData(JavaRDD<Record> records) {
        this.records = records;
    }

    public JavaRDD<Record> getRecords(){
        return records;
    }
    @Override
    public void sanityCheck() {
        if (records.isEmpty()) {
            throw new AssertionError("Record data is empty");
        }
    }
}
