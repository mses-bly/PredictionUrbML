package urb.ml.algorithms;

import io.prediction.controller.Params;

/**
 * Created by moises on 6/27/15.
 */
public class RandomForestAlgorithmParams implements Params{
    private final int numTrees;
    private final int maxDepth;

    public RandomForestAlgorithmParams(int numTrees, int maxDepth) {
        this.numTrees = numTrees;
        this.maxDepth = maxDepth;
    }

    public int getNumTrees() {
        return numTrees;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}
