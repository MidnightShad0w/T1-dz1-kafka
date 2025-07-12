package org.danila.weather;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public record WeatherRecord(
        String city,
        @JsonFormat(pattern = "dd-MM-yyyy") LocalDate date,
        @Min(-50) @Max(60) int temperatureC,
        Condition condition
) {
}
