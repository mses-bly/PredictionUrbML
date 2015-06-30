package urb.ml.algorithms;

import io.prediction.controller.EmptyParams;
import io.prediction.controller.Engine;
import io.prediction.controller.EngineFactory;
import io.prediction.core.BaseAlgorithm;
import io.prediction.core.BaseEngine;
import urb.ml.algorithms.RandomForestAlgorithm;
import urb.ml.data.DataSource;
import urb.ml.data.Preparator;
import urb.ml.data.PreparedData;
import urb.ml.serving.PredictedResult;
import urb.ml.serving.Query;
import urb.ml.serving.Serving;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RegressionEngine extends EngineFactory {

    @Override
    public BaseEngine<EmptyParams, Query, PredictedResult, Set<String>> apply() {
        //Algorithms
        Map<String, Class<? extends BaseAlgorithm<PreparedData, ?, Query, PredictedResult>>> algorithmsMap = new HashMap<>();
        algorithmsMap.put("RandomForestAlgorithm", RandomForestAlgorithm.class);
        return new Engine<>(DataSource.class,Preparator.class,algorithmsMap,Serving.class);
    }
}



