package com.klm.weather.repository;

import com.klm.weather.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Integer> {


    List<Weather> findByDateAndCityIgnoreCaseIn(LocalDate date, List<String> cities);

    List<Weather> findByDate(LocalDate date);

    List<Weather> findByCityIgnoreCaseIn(List<String> cities);
}
