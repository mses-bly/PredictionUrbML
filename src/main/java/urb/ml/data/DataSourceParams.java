package urb.ml.data;

import io.prediction.controller.Params;

import java.util.List;

public class DataSourceParams implements Params{
    private final String appName;
    private final List<String> features;
    private final String response;
    private final int numFolds;

    public DataSourceParams(String appName, List<String> features, String response, int numFolds) {
        this.appName = appName;
        this.features = features;
        this.response = response;
        this.numFolds = numFolds;
    }

    public String getAppName() {
        return appName;
    }

    public List<String> getFeatures() {
        return features;
    }

    public String getResponse() {
        return response;
    }

    public int getNumFolds() {
        return numFolds;
    }
}
