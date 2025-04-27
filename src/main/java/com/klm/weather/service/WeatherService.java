package com.klm.weather.service;

import com.klm.weather.dto.WeatherRequestDTO;
import com.klm.weather.dto.WeatherResponseDTO;
import com.klm.weather.model.Weather;
import com.klm.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Transactional
    public WeatherResponseDTO saveWeather(WeatherRequestDTO requestDTO) {
        Weather weather = mapToEntity(requestDTO);
        Weather saved = weatherRepository.save(weather);
        return mapToResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<WeatherResponseDTO> getWeathers(Optional<String> dateOpt, Optional<String> cityOpt, Optional<String> sortOpt) {
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
        LocalDate date = hasDate ? LocalDate.parse(dateOpt.get()) : null;
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
        if (CollectionUtils.isEmpty(result)) {
            return Collections.emptyList();
        }
        return result.stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    public WeatherResponseDTO getWeatherById(Integer id) {
        Weather weather = weatherRepository.findById(id).orElse(null);
        return mapToResponseDTO(weather);
    }

    private Weather mapToEntity(WeatherRequestDTO dto) {
        return Weather.builder()
                .date(dto.toDate())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .city(dto.getCity())
                .state(dto.getState())
                .temperatures(dto.getTemperatures())
                .build();
    }

    private WeatherResponseDTO mapToResponseDTO(Weather entity) {
        if (entity == null) {
            return null;
        }
        WeatherResponseDTO dto = new WeatherResponseDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.toLocalDate());
        dto.setLat(entity.getLat());
        dto.setLon(entity.getLon());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setTemperatures(entity.getTemperatures());
        return dto;
    }
}