package urb.ml.serving;

import java.io.Serializable;

public class PredictedResult implements Serializable{
    double result;

    public PredictedResult(double result){
        this.result = result;
    }

    public double getResult() {
        return result;
    }
}
