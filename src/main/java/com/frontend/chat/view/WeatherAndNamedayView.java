package com.frontend.chat.view;

import com.frontend.chat.domain.AccuWeatherDto;
import com.frontend.chat.services.ChatService;
import com.frontend.chat.services.NamedayService;
import com.frontend.chat.services.WeatherService;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class WeatherAndNamedayView extends HorizontalLayout {
    private Label city = new Label();
    private Label weather = new Label();
    private Label todayNames = new Label();
    private Label tomorrowNames = new Label();
    private WeatherService weatherService = WeatherService.getInstance();
    private NamedayService namedayService = NamedayService.getInstance();

    public WeatherAndNamedayView() {
        city.setText("Your city: \n" + setLocalizationText());
        weather.setText("Today's weather for Your city:\n" + setWeateherText());
        todayNames.setText("Today we celebrate: \n" + getTodayNames() + " name day!");
        tomorrowNames.setText("Tomorrow we'll celebrate: \n" + getTomorrowNames() + " name day!");
        add(city, weather, todayNames, tomorrowNames);
    }

    private String getTomorrowNames() {
        return namedayService.getTomorrowNames();
    }

    private String getTodayNames() {
        return namedayService.getTodayNames();
    }

    public String setWeateherText() {
        AccuWeatherDto accuWeather = weatherService.getWeather();

        return "" + accuWeather.getText().toUpperCase() +
                " with temperature: " +
                accuWeather.getTemperature().getMetric().getValue() +
                " Celsius degree.";
    }

    public String setLocalizationText() {
        return ChatService.CURRENT_USER.getCity().toUpperCase();
    }

}
