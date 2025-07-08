package com.assignment.weather;

import com.assignment.weather.model.PincodeLocation;
import com.assignment.weather.model.WeatherInfo;
import com.assignment.weather.repository.PincodeLocationRepository;
import com.assignment.weather.repository.WeatherInfoRepository;
import com.assignment.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class WeatherServiceTest {
    @Mock
    private PincodeLocationRepository pincodeLocationRepository;
    @Mock
    private WeatherInfoRepository weatherInfoRepository;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private Environment env;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWeatherInfo_Cached() {
        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);
        WeatherInfo cached = WeatherInfo.builder().pincode(pincode).date(date).weatherJson("{\"weather\":\"sunny\"}").build();
        when(weatherInfoRepository.findByPincodeAndDate(pincode, date)).thenReturn(Optional.of(cached));

        WeatherInfo result = weatherService.getWeatherInfo(pincode, date);
        assertEquals(cached, result);
        verify(weatherInfoRepository, times(1)).findByPincodeAndDate(pincode, date);
        verifyNoMoreInteractions(weatherInfoRepository, pincodeLocationRepository, restTemplate);
    }
} 