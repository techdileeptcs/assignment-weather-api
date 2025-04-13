package com.klm.weather.controller;

import com.klm.weather.model.Weather;
import com.klm.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/weather")
public class WeatherApiRestController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping
    public ResponseEntity<Weather> createWeather(@RequestBody Weather weather) {
        Weather saved = weatherService.saveWeather(weather);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Weather>> getWeathers(
            @RequestParam Optional<String> date,
            @RequestParam Optional<String> city,
            @RequestParam Optional<String> sort) {
        List<Weather> weathers = weatherService.getWeathers(date, city, sort);
        return new ResponseEntity<>(weathers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Weather> getWeatherById(@PathVariable Integer id) {
        return weatherService.getWeatherById(id)
                .map(weather -> new ResponseEntity<>(weather, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
