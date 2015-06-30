package urb.ml.algorithms;

import io.prediction.controller.java.P2LJavaAlgorithm;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.tree.RandomForest;
import org.apache.spark.mllib.tree.model.RandomForestModel;
import urb.ml.data.PreparedData;
import urb.ml.serving.PredictedResult;
import urb.ml.serving.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by moises on 6/27/15.
 */
public class RandomForestAlgorithm extends P2LJavaAlgorithm<PreparedData, RandomForestModel, Query, PredictedResult>{

    private final RandomForestAlgorithmParams params;

    public RandomForestAlgorithm(RandomForestAlgorithmParams params){
        this.params = params;
    }

    @Override
    public RandomForestModel train(SparkContext sc, PreparedData preparedData) {
        Map<Integer, Integer> categoricalFeaturesInfo = new HashMap<Integer, Integer>();
        RandomForestModel model = RandomForest.trainRegressor(preparedData.getRegressionData(),categoricalFeaturesInfo, params.getNumTrees(), "auto", "variance", params.getMaxDepth(), 100, 1);
        return model;
    }

    @Override
    public PredictedResult predict(RandomForestModel model, Query query) {
        return new PredictedResult(model.predict(Vectors.dense(query.getFeatures())));
    }
}
