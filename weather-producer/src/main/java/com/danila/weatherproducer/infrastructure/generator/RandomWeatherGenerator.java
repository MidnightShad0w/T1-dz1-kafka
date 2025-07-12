package com.danila.weatherproducer.infrastructure.generator;

import com.danila.weatherproducer.domain.port.WeatherGenerator;
import org.danila.weather.Condition;
import org.danila.weather.WeatherRecord;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
public class RandomWeatherGenerator implements WeatherGenerator {

    private static final List<String> CITIES = List.of("Москва", "Санкт-Петербург", "Казань","Новосибирск", "Сочи");
    private static final Condition[] CONDITIONS = Condition.values();
    private final Random rnd = new Random();


    @Override
    public WeatherRecord next() {
        String city = CITIES.get(rnd.nextInt(CITIES.size()));
        LocalDate date = LocalDate.now().minusDays(rnd.nextInt(7));
        int t = rnd.nextInt(35);
        Condition cond = CONDITIONS[rnd.nextInt(CONDITIONS.length)];
        return new WeatherRecord(city, date, t, cond);
    }
}
