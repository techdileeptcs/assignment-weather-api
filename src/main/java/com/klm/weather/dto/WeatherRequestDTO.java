package com.klm.weather.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
public class WeatherRequestDTO {
    @NotNull
    private Date date;
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;

    @NotNull
    @Size(min = 1, message = "City cannot be empty")
    private String city;
    private String state;
    private List<Double> temperatures;
}



