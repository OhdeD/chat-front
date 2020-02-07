package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.router.Route;


@Route("new_user")
public class NewUserView extends VerticalLayout {
    private ChatService chatService = ChatService.getInstance();
    @PropertyId("name")
    private TextField name = new TextField("Name:");
    @PropertyId("surname")
    private TextField surname = new TextField("Surname:");
    @PropertyId("mail")
    private TextField mail = new TextField("Mail:");
    @PropertyId("password")
    private TextField password = new TextField("Password:");
    @PropertyId("city")
    private TextField city = new TextField("City:");

    private Button save = new Button("Create");
    private Button start = new Button("start");
    private ChatUserDto chatUserDto = new ChatUserDto();
    private Binder binder = new Binder<>(ChatUserDto.class);

    public NewUserView() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        start.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        add(name, surname, mail, password, city, save, start);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setBinder();
        binder.bindInstanceFields(this);
        save.addClickListener(event -> save());
        start.addClickListener(event -> start.getUI().ifPresent(ui->ui.navigate("main")));
    }

    private Binder setBinder() {
        binder.setBean(chatUserDto);
        return binder;
    }

    private void save() {
        ChatUserDto user = (ChatUserDto) binder.getBean();
        chatService.newUser(user);
        binder.setBean(null);
    }

}
