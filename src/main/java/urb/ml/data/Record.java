package urb.ml.data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Moises on 6/26/15.
 */
public class Record implements Serializable{
    private HashMap<String, String> features;

    public Record(HashMap<String, String> features){
        this.features = features;
    }

    public boolean containsFeature(String featureName){
        return features.containsKey(featureName);
    }

    public String getFeature(String featureName){
        return features.get(featureName);
    }
}
