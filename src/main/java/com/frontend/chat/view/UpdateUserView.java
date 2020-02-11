package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.PropertyId;
import com.vaadin.flow.router.Route;



@Route("update_user")
public class UpdateUserView extends VerticalLayout {
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

    private Button save = new Button("Save changes");
    private ChatUserDto chatUserDto = new ChatUserDto();
    private Binder binder = new Binder<>(ChatUserDto.class);

    public UpdateUserView() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(name, surname, mail, password, city, save);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setBinder();
        binder.bindInstanceFields(this);
        save.addClickListener(event -> { save(); save.getUI().ifPresent(ui->ui.navigate("mainView"));
            Notification.show("Personal data changed");
        });
    }

    private Binder setBinder() {
        binder.setBean(chatUserDto);
        return binder;
    }

    private void save() {
        ChatUserDto user = (ChatUserDto) binder.getBean();
        chatService.updateUser(user);
        binder.setBean(null);
        ChatService.CURRENT_USER = chatService.findCurrentUser();
    }

}
