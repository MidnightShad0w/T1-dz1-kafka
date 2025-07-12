package com.danila.weatherproducer.domain.port;

import org.danila.weather.WeatherRecord;

public interface WeatherGenerator {
    WeatherRecord next();
}
