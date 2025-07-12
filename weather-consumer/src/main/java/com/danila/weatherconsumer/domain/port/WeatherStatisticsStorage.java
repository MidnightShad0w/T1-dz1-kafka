package com.danila.weatherconsumer.domain.port;

import org.danila.weather.WeatherRecord;

public interface WeatherStatisticsStorage {

    void accept(WeatherRecord record);

    Summary snapshotAndReset();

    record Summary(String rainiestCity,
                   int rainDays,
                   String hottestCity,
                   int maxTemperature,
                   String coldestAvgCity,
                   double avgTemperature) {
    }
}
