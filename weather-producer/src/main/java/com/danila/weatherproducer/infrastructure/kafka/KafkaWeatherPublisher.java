package com.danila.weatherproducer.infrastructure.kafka;

import com.danila.weatherproducer.domain.port.WeatherPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.danila.weather.WeatherRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class KafkaWeatherPublisher implements WeatherPublisher {

    private final KafkaTemplate<String, String> kafka;
    private final ObjectMapper mapper;

    @Value("${weather.topic}")
    private String topic;

    private static final Logger log = LoggerFactory.getLogger(KafkaWeatherPublisher.class);

    @Autowired
    public KafkaWeatherPublisher(KafkaTemplate<String, String> kafka, ObjectMapper mapper) {
        this.kafka = kafka;
        this.mapper = mapper;
    }

    @Override
    public void publish(WeatherRecord record) {
        try {
            String json = mapper.writeValueAsString(record);
            kafka.send(topic, record.city(), json).whenComplete((res, ex) -> {
                if (ex == null) {
                    log.debug("Sent {}", json);
                } else {
                    log.error("Kafka send failed", ex);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Serialization failed", e);
        }
    }
}
