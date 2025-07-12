package com.danila.weatherproducer.domain.port;

import jakarta.validation.Valid;
import org.danila.weather.WeatherRecord;

public interface WeatherPublisher {
    void publish(@Valid WeatherRecord record);
}
