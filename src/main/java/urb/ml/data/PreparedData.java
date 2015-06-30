package urb.ml.data;

import java.io.Serializable;

import io.prediction.controller.SanityCheck;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.regression.LabeledPoint;


public class PreparedData implements Serializable, SanityCheck {

    private final JavaRDD<LabeledPoint> regressionData;

    public PreparedData(JavaRDD<LabeledPoint> regressionData) {
        this.regressionData = regressionData;
    }

    public JavaRDD<LabeledPoint> getRegressionData() {
        return regressionData;
    }

    @Override
    public void sanityCheck() {
        if (regressionData.isEmpty()){
            throw new AssertionError("Prepared data is empty");
        }
    }
}
