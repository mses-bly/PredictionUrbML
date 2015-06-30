package urb.ml.serving;

import java.io.Serializable;

public class Query implements Serializable{
    double[] features;

    public Query(double[] features){
        this.features = features;
    }

    public double[] getFeatures() {
        return features;
    }
}
