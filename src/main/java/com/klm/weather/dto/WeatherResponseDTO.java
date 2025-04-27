package com.klm.weather.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
public class WeatherResponseDTO {
    private Integer id;
    private LocalDate date;
    private Float lat;
    private Float lon;
    private String city;
    private String state;
    private List<Double> temperatures;
}