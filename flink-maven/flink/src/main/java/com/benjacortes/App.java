package com.benjacortes;

import java.util.concurrent.TimeUnit;

import com.benjacortes.models.ClickEvent;
import com.benjacortes.operations.RollingAdditionMapper;
import com.benjacortes.serdesschemas.ClickEventSchema;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * Hello world!
 *
 */
public class App 
{

    public static final String CHECKPOINTING_OPTION = "checkpointing";
	public static final String EVENT_TIME_OPTION = "event-time";
	public static final String BACKPRESSURE_OPTION = "backpressure";
	public static final String OPERATOR_CHAINING_OPTION = "chaining";
	public static final Time WINDOW_SIZE = Time.of(15, TimeUnit.SECONDS);
    private static final String KAFKA_BROKER = "kafka1:9092,kafka2:9092,kafka3:9092";
    public static void main( String[] args )
    {

        final ParameterTool params = ParameterTool.fromArgs(args);

		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		configureEnvironment(params, env);

		KafkaSource<ClickEvent> source = KafkaSource.<ClickEvent>builder()
                .setBootstrapServers(KAFKA_BROKER)
                .setTopics("flink-input")
                .setGroupId("kafka-sandbox")
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setValueOnlyDeserializer(new ClickEventSchema())
                .build();

        

        DataStream<ClickEvent> text = env.fromSource(source, WatermarkStrategy.noWatermarks(), "Kafka Source");


        DataStream<ClickEvent> counts =    
                text.keyBy(
                    new KeySelector<ClickEvent, Tuple2<Long, Long>>() {
                        @Override
                        public Tuple2<Long, Long> getKey(ClickEvent value) throws Exception {
                        return Tuple2.of(value.getSessionId(), value.getPageId());
                        }
                    })
                    .map(new RollingAdditionMapper());
        

        KafkaSink<ClickEvent> sink = KafkaSink.<ClickEvent>builder()
            .setBootstrapServers("kafka1:9092,kafka2:9092,kafka3:9092")
            .setRecordSerializer( KafkaRecordSerializationSchema.builder()
                .setTopic("flink-output")
                .setValueSerializationSchema(new ClickEventSchema())
                .build())
            .build();

        counts.sinkTo(sink);
        counts.windowAll(TumblingEventTimeWindows.of(Time.seconds(5)));
        try {
            env.execute("Streaming WordCount");
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        
    }

    private static void configureEnvironment(
			final ParameterTool params,
			final StreamExecutionEnvironment env) {

		boolean checkpointingEnabled = params.has(CHECKPOINTING_OPTION);
		boolean enableChaining = params.has(OPERATOR_CHAINING_OPTION);

		if (checkpointingEnabled) {
			env.enableCheckpointing(1000);
		}

		if(!enableChaining){
			//disabling Operator chaining to make it easier to follow the Job in the WebUI
			env.disableOperatorChaining();
		}
	}
}
