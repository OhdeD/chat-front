package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.annotations.PropertyId;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;


@Route(value = "login")
public class LoginView extends VerticalLayout {
    private ChatService chatService = ChatService.getInstance();
    private ContentView contentView;

    @PropertyId("mail")
    private TextField mail = new TextField("Mail:");
    @PropertyId("password")
    private TextField password = new TextField("Password:");

    private Button login = new Button("Login");
    private ChatUserDto chatUserDto = new ChatUserDto();
    private Binder binder = new Binder<>(ChatUserDto.class);

    public static long currentUserId;

    public LoginView() {

        login.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(mail, password, login);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setBinder();
        binder.bindInstanceFields(this);

        login.addClickListener(event ->{ login(); navigate();});


    }

    private Binder setBinder() {
        binder.setBean(chatUserDto);
        return binder;
    }

    private void navigate() {
        currentUserId = chatService.getUsersId();
        contentView = new ContentView(currentUserId);
        login.getUI().ifPresent(ui -> ui.navigate("contentView"));
    }

    private void login() {
        ChatUserDto user = (ChatUserDto) binder.getBean();
        chatService.login(user.getMail(), user.getPassword());
    }
}