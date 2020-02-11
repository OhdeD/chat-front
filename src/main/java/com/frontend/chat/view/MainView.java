package com.frontend.chat.view;

import com.frontend.chat.mapper.Mapper;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route("mainView")
public class MainView extends VerticalLayout {
    private ContentView contentView = new ContentView();
    private WeatherAndNamedayView weatherAndNamedayView = new WeatherAndNamedayView();
    private Button changeData = new Button("Update Your Data");
    private Label currentData = new Label();
    private ChatService chatService = ChatService.getInstance();
    private Button logout = new Button("Logout");
    private HorizontalLayout profile = new HorizontalLayout();
    private Mapper mapper = Mapper.getInstance();
    private Button admin = new Button("Enter admin service");

    public MainView() {
        add(weatherAndNamedayView, admin, contentView, logout, profile);
        if (!chatService.getRole().getRole().equals("ADMIN")) admin.setVisible(false);
        setAlignItems(Alignment.CENTER);
        profile.add(currentData, changeData);
        profile.setAlignItems(Alignment.END);
        currentData.setText("YOUR DATA: " + mapper.mapLetters(ChatService.CURRENT_USER.getName()) +
                " " + mapper.mapLetters(ChatService.CURRENT_USER.getSurname()) +
                ", mail: " + ChatService.CURRENT_USER.getMail() +
                ", city: " + mapper.mapLetters(ChatService.CURRENT_USER.getCity()) +
                ". You can always change it. Just use this panel ->");
        changeData.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        changeData.addClickListener(event -> changeData.getUI().ifPresent(ui -> ui.navigate("update_user")));
        logout.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        logout.addClickListener(a -> {
            chatService.logout();
            Notification.show("User " + ChatService.CURRENT_USER.getName() + "logged out");
            logout.getUI().ifPresent(ui -> ui.navigate("login"));
        });
        admin.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        admin.addClickListener(a -> admin.getUI().ifPresent(ui ->ui.navigate("adminPanel")));

    }


}
