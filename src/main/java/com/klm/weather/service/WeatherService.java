package com.klm.weather.service;

import com.klm.weather.model.Weather;
import com.klm.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
        Sort weatherSort = Sort.by("id").ascending();
        if (sortOpt.isPresent()) {
            String sortField = sortOpt.get();
            if (sortField.startsWith("-")) {
                sortField = sortField.substring(1, sortField.length());
                weatherSort = Sort.by(sortField).descending().and(Sort.by("id").ascending());
            } else {
                weatherSort = Sort.by(sortField).ascending().and(Sort.by("id").ascending());
            }
        }
        boolean hasDate = dateOpt.isPresent();
            boolean hasCity = cityOpt.isPresent();
            LocalDate localDate = hasDate ? LocalDate.parse(dateOpt.get()) : null;
            DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateOpt.map(value -> {
                try {
                    return simpleDateFormat.parse(value);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }).orElse(null);
            List<String> cities = hasCity ? Arrays.stream(cityOpt.get().split(","))
                    .map(String::toLowerCase)
                    .collect(Collectors.toList()) : Collections.emptyList();

            if (hasDate && hasCity) {
                result = weatherRepository.findByDateAndCityIgnoreCaseIn(date, cities, weatherSort);
            } else if (hasDate) {
                result = weatherRepository.findByDate(date, weatherSort);
            } else if (hasCity) {
                result = weatherRepository.findByCityIgnoreCaseIn(cities, weatherSort);
            } else {
                result = weatherRepository.findAll(weatherSort);
            }
        return result;
    }

    public Optional<Weather> getWeatherById(Integer id) {
        return weatherRepository.findById(id);
    }
}
