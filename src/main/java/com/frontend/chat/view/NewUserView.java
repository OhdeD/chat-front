package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.json.JSONException;

@Route("v1/chat/new")
public class NewUserView extends FormLayout {
    private ChatService chatService = ChatService.getInstance();
    private TextField name = new TextField("Name:");
    private TextField surname = new TextField("Surname:");
    private TextField mail = new TextField("Mail:");
    private TextField password = new TextField("Password:");
    private TextField city = new TextField("City:");

    private Button save = new Button("Create");
    private Button login = new Button("Login");

    private Binder<ChatUserDto> binder = new Binder<>(ChatUserDto.class);

    public NewUserView() {
        HorizontalLayout button = new HorizontalLayout(save, login);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(name, surname, mail, password, city, button);
//        binder.bindInstanceFields(this);
        binder.forField(name).bind(ChatUserDto::getName, ChatUserDto::setName);
        binder.forField(surname).bind(ChatUserDto::getSurname, ChatUserDto::setSurname);
        binder.forField(mail).bind(ChatUserDto::getMail, ChatUserDto::setMail);
        binder.forField(password).bind(ChatUserDto::getPassword, ChatUserDto::setPassword);
        binder.forField(city).bind(ChatUserDto::getCity, ChatUserDto::setCity);


        save.addClickListener(event -> save());
    }
//    private void login() {
//        final Link link = new Link("Google", new ExternalResource("http://www.google.com"));
//    }

    private void save() {
        ChatUserDto user = binder.getBean();
        try {
            chatService.newUser(user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binder.setBean(null);
    }
}
