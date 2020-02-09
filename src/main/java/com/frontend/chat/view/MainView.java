package com.frontend.chat.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route("mainView")
public class MainView extends VerticalLayout {
    private ContentView contentView = new ContentView();
    private WeatherAndNamedayView weatherAndNamedayView = new WeatherAndNamedayView();


    public MainView() {


        add(weatherAndNamedayView, contentView);


    }


}
