package com.frontend.chat.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class NamedayService {
    private static NamedayService namedayService = null;
    private RestTemplate restTemplate = new RestTemplate();
    private static final String CHAT = "https://floating-sea-70446.herokuapp.com/v1/nameday";
    private static final Logger LOGGER = LoggerFactory.getLogger(NamedayService.class);

    private NamedayService() {
    }

    public static NamedayService getInstance() {
        if (namedayService == null) {
            synchronized (ChatService.class) {
                if (namedayService == null) {
                    namedayService = new NamedayService();
                }
            }
        }
        return namedayService;
    }

    public String getTodayNames() {
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/today").build().encode().toUri();
        try {
            return restTemplate.getForObject(uri, String.class);
        } catch (NullPointerException e) {
            LOGGER.info("Can't connect to NamesDay");
            e.getMessage();
            return "Read the stars by yourself.. I'm out.";
        }
    }

    public String getTomorrowNames(){
        URI uri = UriComponentsBuilder.fromHttpUrl(CHAT + "/tomorrow").build().encode().toUri();
        try{
            return restTemplate.getForObject(uri,String.class);
        }catch (NullPointerException e){
            LOGGER.info("Can't connect to NamesDay");
            e.getMessage();
            return "Read the stars by yourself.. I'm out.";
        }
    }
}
