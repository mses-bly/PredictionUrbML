package urb.ml.data;

import io.prediction.controller.EmptyParams;
import io.prediction.controller.java.PJavaDataSource;
import io.prediction.data.storage.PropertyMap;
import io.prediction.data.store.java.OptionHelper;
import io.prediction.data.store.java.PJavaEventStore;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.rdd.RDD;
import org.joda.time.DateTime;
import scala.Tuple2;
import scala.Tuple3;
import scala.collection.JavaConversions$;
import scala.collection.Seq;
import urb.ml.serving.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DataSource extends PJavaDataSource<TrainingData, EmptyParams, Query, Set<String>> {

    private final DataSourceParams dsp;

    public DataSource(DataSourceParams dsp) {
        this.dsp = dsp;
    }

    @Override
    public TrainingData readTraining(SparkContext sc) {
        JavaRDD<Record> recordRDD = PJavaEventStore.aggregateProperties(
                dsp.getAppName(),
                "data",
                OptionHelper.<String>none(),
                OptionHelper.<DateTime>none(),
                OptionHelper.<DateTime>none(),
                OptionHelper.<List<String>>none(),
                sc).map(new Function<Tuple2<String, PropertyMap>, Record>() {
            @Override
            public Record call(Tuple2<String, PropertyMap> stringPropertyMapTuple2) throws Exception {
                Set<String> keys = JavaConversions$.MODULE$.setAsJavaSet(stringPropertyMapTuple2._2().keySet());
                HashMap<String, String> properties = new HashMap<>();
                for (String key : keys) {
                    properties.put(key, stringPropertyMapTuple2._2().get(key, String.class));
                }
                return new Record(properties);
            }
        });
        return new TrainingData(recordRDD);
    }

    @Override
    public Seq<Tuple3<TrainingData, EmptyParams, RDD<Tuple2<Query, Set<String>>>>> readEval(SparkContext sc) {
        return null;
    }
}
