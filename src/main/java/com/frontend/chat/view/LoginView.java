package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.annotations.PropertyId;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.GridLayout;


@Route(value = "login")
public class LoginView extends VerticalLayout {
    private ChatService chatService = ChatService.getInstance();

    @PropertyId("mail")
    private TextField mail = new TextField("Mail:");
    @PropertyId("password")
    private TextField password = new TextField("Password:");
    private Label noSuchUser = new Label("Wrong credentials, try again");
    private Button login = new Button("Login");
    private Button register = new Button("Register");
    private ChatUserDto chatUserDto = new ChatUserDto();
    private Binder binder = new Binder<>(ChatUserDto.class);

    GridLayout grid = new GridLayout(3, 3);

    public LoginView() {

        grid.setWidth(400, Sizeable.UNITS_PIXELS);
        grid.setHeight(400, Sizeable.UNITS_PIXELS);

        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        register.addThemeVariants(ButtonVariant.LUMO_SMALL);
        add(noSuchUser, mail, password, login, register);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);


        noSuchUser.setVisible(false);
        setBinder();
        binder.bindInstanceFields(this);
        login.addClickListener(event -> {
            boolean logged = login();
            if (logged) {
                login.getUI().ifPresent(ui -> ui.navigate("mainView"));
            } else {
                login.getUI().ifPresent(ui -> ui.navigate("login"));
                noSuchUser.setVisible(true);
            }
        });
        register.addClickListener(event -> register.getUI().ifPresent(ui->ui.navigate("new_user")));
    }

    private Binder setBinder() {
        binder.setBean(chatUserDto);
        return binder;
    }

    private boolean login() {
        ChatUserDto user = (ChatUserDto) binder.getBean();
        ChatUserDto logged = chatService.login(user.getMail(), user.getPassword());
        return !logged.getName().equals("noSuchUser");
    }
}