package com.danila.weatherconsumer.infrastructure.publisher;

import com.danila.weatherconsumer.domain.port.WeatherStatisticsStorage.Summary;
import com.danila.weatherconsumer.domain.port.WeatherSummaryPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogSummaryPublisher implements WeatherSummaryPublisher {

    private static final Logger log = LoggerFactory.getLogger(LogSummaryPublisher.class);
    @Override
    public void publish(Summary summary) {
        log.info("""
                 ---Итоги периода---
                 Больше всего дождей:   {} ({} дн.)
                 Самая жаркая погода:   {} ({} C)
                 Самая низк. средн. T: {} ({})""",
                summary.rainiestCity(), summary.rainDays(),
                summary.hottestCity(),  summary.maxTemperature(),
                summary.coldestAvgCity(), String.format("%.1f °C", summary.avgTemperature()));
    }
}
