package com.assignment.weather.controller;

import com.assignment.weather.service.WeatherService;
import com.assignment.weather.model.WeatherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @GetMapping
    public WeatherInfo getWeather(
            @RequestParam String pincode,
            @RequestParam("for_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate forDate
    ) {
        return weatherService.getWeatherInfo(pincode, forDate);
    }
} 