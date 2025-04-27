package com.klm.weather.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.klm.weather.databind.LocalDateDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Data
public class WeatherRequestDTO {

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;

    @NotNull(message = "{weather.lat.nonexist}")
    private float lat;

    @NotNull(message = "{weather.lon.nonexist}")
    private float lon;

    @NotBlank(message = "{weather.city.nonexist}")
    @Size(min = 1, message = "City cannot be empty")
    private String city;

    @NotBlank(message = "{weather.city.nonexist}")
    private String state;

    @NotEmpty(message = "Temperatures list cannot be empty")
    private List<@NotNull(message = "Temperature must not be null") Double> temperatures;

    public Date toDate() {
        return Date.from(this.date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}



