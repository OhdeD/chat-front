package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.domain.MessageDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ConversationView extends VerticalLayout {
    private ChatService chatService = ChatService.getInstance();
    private Grid<MessageDto> grid = new Grid<>(MessageDto.class);
    private static ChatUserDto OTHER_PARTICIPANT = new ChatUserDto();
    private TextField messageField = new TextField();
    private Button send = new Button("Send");
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private static final Logger LOGGER = LoggerFactory.getLogger(ConversationView.class);
    private Button delM = new Button("Delete selected");


    public ConversationView() {
        grid.setColumns("message", "sent");
        grid.setWidth("600px");

        messageField.setWidth("500px");
        messageField.setHeight("125px");
        messageField.setPlaceholder("Wright a message");
        delM.addThemeVariants(ButtonVariant.LUMO_SMALL);
        delM.setVisible(false);
        horizontalLayout.add(messageField, send);
        horizontalLayout.setVisible(false);
        horizontalLayout.setWidthFull();

        add(grid, delM, horizontalLayout);

        refreshEmpty();

        send.addClickListener(event -> {
            sendMessage(messageField.getValue(), OTHER_PARTICIPANT);
            refresh(OTHER_PARTICIPANT);
            messageField.clear();
        });

        grid.asSingleSelect().addValueChangeListener(event -> {
            MessageDto me = grid.asSingleSelect().getValue();

            delM.addClickListener(e -> {
                chatService.deleteMessage(me);
                LOGGER.info("Message deleted");
                Notification.show("Message deleted");
                refresh(OTHER_PARTICIPANT);
            });

        });

    }

    public void sendMessage(String value, ChatUserDto to) {
        String message = chatService.sendMessage(value, to);
        if (message != null) LOGGER.info("Message sent to" + OTHER_PARTICIPANT.toString());
    }

    public void refresh(ChatUserDto chatUserDto) {
        OTHER_PARTICIPANT = chatUserDto;
        if (chatUserDto != null) {
            grid.getDataProvider().refreshAll();
            grid.setItems(chatService.getConversation(chatUserDto));
            delM.setVisible(true);
            horizontalLayout.setVisible(true);
        } else refreshEmpty();
    }

    public void refreshEmpty() {
        grid.getDataProvider().refreshAll();
        grid.setItems(new ArrayList<>());
        horizontalLayout.setVisible(false);
        delM.setVisible(false);
    }

}
