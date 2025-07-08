package com.assignment.weather;

import com.assignment.weather.controller.WeatherController;
import com.assignment.weather.model.WeatherInfo;
import com.assignment.weather.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void testGetWeather_Success() throws Exception {
        String pincode = "411014";
        LocalDate date = LocalDate.of(2020, 10, 15);
        WeatherInfo mockWeather = WeatherInfo.builder()
                .pincode(pincode)
                .date(date)
                .weatherJson("{\"weather\":\"sunny\"}")
                .build();
        Mockito.when(weatherService.getWeatherInfo(pincode, date)).thenReturn(mockWeather);

        mockMvc.perform(get("/weather")
                .param("pincode", pincode)
                .param("for_date", date.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"pincode\":\"411014\",\"date\":\"2020-10-15\",\"weatherJson\":\"{\\\"weather\\\":\\\"sunny\\\"}\"}", false));
    }
} 