package com.danila.weatherconsumer.infrastructure.storage;

import com.danila.weatherconsumer.domain.port.WeatherStatisticsStorage;
import org.danila.weather.Condition;
import org.danila.weather.WeatherRecord;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

@Component
public class InMemoryStatisticsStorage implements WeatherStatisticsStorage {

    private record CityStats(LongAdder rainDays, LongAdder tempSum, LongAdder count, LongAccumulator maxTemp) {
        CityStats() {
            this(new LongAdder(), new LongAdder(), new LongAdder(), new LongAccumulator(Math::max, Integer.MIN_VALUE));
        }
    }

    private final Map<String, CityStats> stats = new ConcurrentHashMap<>();

    @Override
    public void accept(WeatherRecord record) {
        CityStats s = stats.computeIfAbsent(record.city(), c -> new CityStats());

        if (record.condition() == Condition.RAIN) {
            s.rainDays().increment();
        }
        s.tempSum().add(record.temperatureC());
        s.count().increment();
        s.maxTemp().accumulate(record.temperatureC());
    }

    @Override
    public Summary snapshotAndReset() {

        if (stats.isEmpty()) {
            return null;
        }

        String rainiest = null, hottest = null, coldestAvg = null;
        int rainMax = -1, maxT = Integer.MIN_VALUE;
        double coldestAvgTemp = Double.MAX_VALUE;

        for (var e : stats.entrySet()) {
            String city = e.getKey();
            CityStats s = e.getValue();

            int rains = s.rainDays.intValue();
            int cityMaxT = s.maxTemp.intValue();
            double avg = s.tempSum.doubleValue() / s.count.doubleValue();

            if (rains > rainMax) {
                rainMax = rains;
                rainiest = city;
            }
            if (cityMaxT > maxT) {
                maxT = cityMaxT;
                hottest = city;
            }
            if (avg < coldestAvgTemp) {
                coldestAvgTemp = avg;
                coldestAvg = city;
            }
        }
        stats.clear();
        return new Summary(rainiest, rainMax, hottest, maxT, coldestAvg, coldestAvgTemp);
    }
}
