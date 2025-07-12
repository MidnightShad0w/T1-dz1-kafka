package com.danila.weatherconsumer.domain.port;

import com.danila.weatherconsumer.domain.port.WeatherStatisticsStorage.Summary;

public interface WeatherSummaryPublisher {
    void publish(Summary summary);
}
