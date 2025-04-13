package com.klm.weather.service;

import com.klm.weather.model.Weather;
import com.klm.weather.repository.WeatherRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class WeatherService {
    @Autowired
    private WeatherRepository weatherRepository;

    public Weather saveWeather(Weather weather) {
        return weatherRepository.save(weather);
    }

    public List<Weather> getWeathers(Optional<String> dateOpt, Optional<String> cityOpt, Optional<String> sortOpt) {
        List<Weather> result;
        boolean hasDate = dateOpt.isPresent();
        boolean hasCity = cityOpt.isPresent();
        LocalDate date = hasDate ? LocalDate.parse(dateOpt.get()) : null;
        List<String> cities = hasCity ? Arrays.stream(cityOpt.get().split(","))
                .map(String::toLowerCase)
                .collect(Collectors.toList()) : Collections.emptyList();

        if (hasDate && hasCity) {
            result = weatherRepository.findByDateAndCityIgnoreCaseIn(date, cities);
        } else if (hasDate) {
            result = weatherRepository.findByDate(date);
        } else if (hasCity) {
            result = weatherRepository.findByCityIgnoreCaseIn(cities);
        } else {
            result = weatherRepository.findAll();
        }

        if (sortOpt.isPresent()) {
            String sortValue = sortOpt.get();
            Comparator<Weather> comparator = Comparator.comparing(Weather::getDate)
                    .thenComparing(Weather::getId);
            if (sortValue.equals("-date")) {
                comparator = comparator.reversed();
            }
            result.sort(comparator);
        } else {
            result.sort(Comparator.comparing(Weather::getId));
        }

        return result;
    }

    public Optional<Weather> getWeatherById(Integer id) {
        return weatherRepository.findById(id);
    }

}
