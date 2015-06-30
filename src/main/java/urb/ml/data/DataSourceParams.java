package urb.ml.data;

import io.prediction.controller.Params;

public class DataSourceParams implements Params{
    private final String appName;

    public DataSourceParams(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }
}
