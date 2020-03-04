package com.frontend.chat.services;

import com.frontend.chat.domain.AccuWeatherDto;
import com.frontend.chat.domain.Metric;
import com.frontend.chat.domain.Temperature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class WeatherService {
    private RestTemplate restTemplate = new RestTemplate();
    private AccuWeatherDto accuWeatherDto = new AccuWeatherDto();
    private final static String CHAT = "https://floating-sea-70446.herokuapp.com/v1/chat/";
    private ChatService chatService = ChatService.getInstance();
    private static WeatherService weatherService = null;
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);

    private WeatherService() {
    }

    public static WeatherService getInstance() {
        if (weatherService == null) {
            synchronized (ChatService.class) {
                if (weatherService == null) {
                    weatherService = new WeatherService();
                }
            }
        }
        return weatherService;
    }

    public AccuWeatherDto getWeather() {
        LOGGER.info("Currrent User: " + ChatService.CURRENT_USER.getName() + " id: " + ChatService.CURRENT_USER.getId());
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + ChatService.CURRENT_USER.getId() + "/weather").build().encode().toUri();
       try {
           return restTemplate.getForObject(uri,AccuWeatherDto.class);
       }catch (HttpServerErrorException.InternalServerError error){
           LOGGER.warn("Accu Weather server overloaded. No weather available");
           AccuWeatherDto accuWeatherDto = new AccuWeatherDto();
           accuWeatherDto.setText("Sorry, our fairy hasn't determined the weather yet..");
           accuWeatherDto.setTemperature(new Temperature(new Metric( 1000.00 )));
           return accuWeatherDto;
       }
    }
}
