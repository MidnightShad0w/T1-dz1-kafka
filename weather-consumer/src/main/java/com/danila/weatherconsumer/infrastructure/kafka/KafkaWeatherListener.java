package com.danila.weatherconsumer.infrastructure.kafka;

import com.danila.weatherconsumer.domain.port.WeatherStatisticsStorage;
import jakarta.validation.Valid;
import org.danila.weather.WeatherRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class KafkaWeatherListener {

    private final WeatherStatisticsStorage storage;

    private static final Logger log = LoggerFactory.getLogger(KafkaWeatherListener.class);

    @Autowired
    public KafkaWeatherListener(WeatherStatisticsStorage storage) {
        this.storage = storage;
    }

    @KafkaListener(topics = "${weather.topic}", groupId = "${spring.kafka.consumer.group-id}",
            properties = "spring.json.value.default.type=org.danila.weather.WeatherRecord")
    public void onMessage(@Valid WeatherRecord record) {
        storage.accept(record);
        log.debug("Accepted {}", record);
    }
}
