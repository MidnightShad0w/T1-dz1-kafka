package com.danila.weatherproducer.application.scheduler;

import com.danila.weatherproducer.domain.port.WeatherGenerator;
import com.danila.weatherproducer.domain.port.WeatherPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherReportScheduler {

    private final WeatherGenerator generator;
    private final WeatherPublisher publisher;

    @Autowired
    public WeatherReportScheduler(WeatherGenerator generator, WeatherPublisher publisher) {
        this.generator = generator;
        this.publisher = publisher;
    }

    @Scheduled(fixedRateString = "${weather.send-rate-ms}")
    public void tick() {
        publisher.publish(generator.next());
    }
}
