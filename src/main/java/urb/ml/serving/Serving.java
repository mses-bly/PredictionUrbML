package urb.ml.serving;

import io.prediction.controller.java.LJavaServing;
import scala.collection.Seq;

public class Serving extends LJavaServing<Query, PredictedResult> {

    @Override
    public PredictedResult serve(Query query, Seq<PredictedResult> predictions) {
        double avg = 0;
        int size = 0;
        for (int i = 0; i < predictions.length(); i++){
            PredictedResult pr = predictions.apply(i);
            avg += pr.getResult();
            size++;
        }
        if (size > 0)
            return new PredictedResult(avg/size);
        return new PredictedResult(0);
    }
}