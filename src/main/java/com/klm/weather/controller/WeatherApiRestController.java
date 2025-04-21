package com.klm.weather.controller;


import com.klm.weather.dto.WeatherRequestDTO;
import com.klm.weather.dto.WeatherResponseDTO;
import com.klm.weather.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(summary = "Create a new weather entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Weather record created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<WeatherResponseDTO> createWeather(@Valid @RequestBody WeatherRequestDTO requestDTO) {
        WeatherResponseDTO saved = weatherService.saveWeather(requestDTO);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve weather records based on optional filters")
    @GetMapping
    public ResponseEntity<List<WeatherResponseDTO>> getWeathers(
            @RequestParam Optional<String> date,
            @RequestParam Optional<String> city,
            @RequestParam Optional<String> sort) {
        List<WeatherResponseDTO> weathers = weatherService.getWeathers(date, city, sort);
        return new ResponseEntity<>(weathers, HttpStatus.OK);
    }

    @Operation(summary = "Get weather details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<WeatherResponseDTO> getWeatherById(@PathVariable Integer id) {
        WeatherResponseDTO weather = weatherService.getWeatherById(id);
        return new ResponseEntity<>(weather, HttpStatus.OK);
    }
}