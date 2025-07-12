package com.danila.weatherconsumer.application.scheduler;

import com.danila.weatherconsumer.domain.port.WeatherStatisticsStorage;
import com.danila.weatherconsumer.domain.port.WeatherSummaryPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.danila.weatherconsumer.domain.port.WeatherStatisticsStorage.Summary;

@Component
public class WeeklySummaryScheduler {
    private final WeatherStatisticsStorage storage;
    private final WeatherSummaryPublisher publisher;

    @Autowired
    public WeeklySummaryScheduler(WeatherStatisticsStorage storage, WeatherSummaryPublisher publisher) {
        this.storage = storage;
        this.publisher = publisher;
    }

    @Scheduled(fixedRateString = "${weather.summary-interval-ms}", initialDelayString = "${weather.summary-interval-ms}")
    public void summarize() {
        Summary s = storage.snapshotAndReset();
        if (s != null) publisher.publish(s);
    }
}
