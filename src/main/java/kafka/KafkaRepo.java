package kafka;

import controllers.BookEventQueueController;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaRepo {
    //this will be consumer

    public static String LIBRARY_TOPIC = "library_topic";
    public static String LIBRARY_CONSUMER_GROUP = "library_consumers";

    public static KafkaConsumer consumer;
    public void initConsumer() {
        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, LIBRARY_CONSUMER_GROUP);
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka2:9292, kafka3:9392");

        consumer = new KafkaConsumer<>(consumerConfig);
        consumer.subscribe(List.of(LIBRARY_TOPIC));
    }
    public void createTopic() throws InterruptedException {
        Properties properties = new Properties();
        properties.put (AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka1:9292, kafka1:9392");
        int partitionsNumber = 5;
        short replicationFactor = 3;
        try (Admin admin = Admin.create(properties)) {
            NewTopic newTopic = new NewTopic(LIBRARY_TOPIC, partitionsNumber, replicationFactor);
            CreateTopicsOptions options = new CreateTopicsOptions()
                    .timeoutMs (10000)
                    .validateOnly (false)
                    .retryOnQuotaViolation (true);
            CreateTopicsResult result = admin.createTopics(List.of (newTopic), options);
            KafkaFuture<Void> futureResult = result.values().get(LIBRARY_TOPIC);
            futureResult.get();
        } catch (ExecutionException ee) {
            if (ee.getCause() instanceof TopicExistsException) {
                System.out.println("Topic already exists");
                return;
            }
            System.out.println(ee.getCause());
            //assertThat(ee.getCause(), is (instanceof(TopicExistsException.class)));
        }
    }


    public void receiveLendingsMessages() {
        int messagesReceived = 0;
        Map<Integer, Long> offsets = new HashMap<>();
        Duration timeout = Duration.of(100, ChronoUnit.MILLIS);
        MessageFormat formatter = new MessageFormat("Temat {0}, partycja {1}, offset {2, number, integer}, klucz {3}, wartość {4}");
        for(;;) {
            ConsumerRecords<String, String> records = consumer.poll(timeout);
            for (ConsumerRecord<String, String> record: records) {
                String result = formatter.format(new Object[]{record.topic(), record.partition(), record.offset(), record.key(), record.value()});
                System.out.println(result);
                System.out.println(record.key() + " " + record.value());
                offsets.put(record.partition(), record.offset());
                //TODO implemet this
               // BookEventQueueController.addLendingTransaction(userId, bookId);
                messagesReceived++;
            }
        }
        //System.out.println(offsets);
        //consumer.commitAsync();
    }

}
