package urb.ml.evaluation;

import io.prediction.controller.java.JavaEvaluation;

public class EvaluationSpec extends JavaEvaluation {
    public EvaluationSpec() {
      /*  this.setEngineMetric(
                new Engine<>(
                        DataSource.class,
                        Preparator.class,
                        Collections.<String, Class<? extends BaseAlgorithm<PreparedData, ?, Query, PredictedResult>>>singletonMap("algo", Algorithm.class),
                        Serving.class
                ),
                new PrecisionMetric()
        );*/
    }
}
