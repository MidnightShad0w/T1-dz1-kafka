package com.danila.weatherproducer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.danila.weather.WeatherRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "weather")
@TestPropertySource(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
public class WeatherProducerIT {

    private static final ObjectMapper JSON = new ObjectMapper();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Test
    void producerSendsValidMessage() throws Exception {

        Map<String,Object> props = KafkaTestUtils.consumerProps("testGrp", "false", embeddedKafka);
        Consumer<String,String> consumer = new DefaultKafkaConsumerFactory<String,String>(props).createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, "weather");

        ConsumerRecord<String,String> rec = KafkaTestUtils.getSingleRecord(consumer, "weather", Duration.ofSeconds(5));

        WeatherRecord wr = JSON.readValue(rec.value(), WeatherRecord.class);

        assertThat(wr.temperatureC()).isBetween(0, 35);
        assertThat(wr.city()).isNotBlank();
    }
}
