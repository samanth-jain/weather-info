package com.assignment.weather.service;

import com.assignment.weather.model.PincodeLocation;
import com.assignment.weather.model.WeatherInfo;
import com.assignment.weather.repository.PincodeLocationRepository;
import com.assignment.weather.repository.WeatherInfoRepository;
import com.assignment.weather.exception.PincodeNotFoundException;
import com.assignment.weather.exception.WeatherApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final PincodeLocationRepository pincodeLocationRepository;
    private final WeatherInfoRepository weatherInfoRepository;
    private final RestTemplate restTemplate;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public WeatherInfo getWeatherInfo(String pincode, LocalDate date) {
        Optional<WeatherInfo> cachedWeather = weatherInfoRepository.findByPincodeAndDate(pincode, date);
        if (cachedWeather.isPresent()) {
            return cachedWeather.get();
        }
        PincodeLocation location = pincodeLocationRepository.findByPincode(pincode)
                .orElseGet(() -> fetchAndSaveLatLong(pincode));
        return fetchAndSaveWeather(location, date, pincode);
    }

    private PincodeLocation fetchAndSaveLatLong(String pincode) {
        String url = String.format("https://api.openweathermap.org/geo/1.0/zip?zip=%s,IN&appid=%s", pincode, apiKey);
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null || response.get("lat") == null || response.get("lon") == null) {
                throw new PincodeNotFoundException("Invalid or unknown pincode: " + pincode);
            }
            Double lat = Double.valueOf(response.get("lat").toString());
            Double lon = Double.valueOf(response.get("lon").toString());
            PincodeLocation location = PincodeLocation.builder()
                    .pincode(pincode)
                    .latitude(lat)
                    .longitude(lon)
                    .lastFetched(LocalDateTime.now())
                    .build();
            return pincodeLocationRepository.save(location);
        } catch (Exception ex) {
            throw new PincodeNotFoundException("Failed to fetch lat/lon for pincode: " + pincode);
        }
    }

    private WeatherInfo fetchAndSaveWeather(PincodeLocation location, LocalDate date, String pincode) {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", location.getLatitude(), location.getLongitude(), apiKey);
        try {
            String weatherJson = restTemplate.getForObject(url, String.class);
            if (weatherJson == null || weatherJson.isEmpty()) {
                throw new WeatherApiException("No weather data returned for pincode: " + pincode);
            }
            WeatherInfo weatherInfo = WeatherInfo.builder()
                    .pincode(pincode)
                    .date(date)
                    .weatherJson(weatherJson)
                    .lastFetched(LocalDateTime.now())
                    .build();
            return weatherInfoRepository.save(weatherInfo);
        } catch (Exception ex) {
            throw new WeatherApiException("Failed to fetch weather data for pincode: " + pincode, ex);
        }
    }
} 