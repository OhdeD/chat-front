package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.domain.MessageDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
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

    public ConversationView() {
        grid.setColumns("message");
        grid.setWidth("600px");
        messageField.setWidth("500px");
        messageField.setHeight("125px");
        messageField.setPlaceholder("Wright a message");
        horizontalLayout.add(messageField, send);
        add(grid, horizontalLayout);
        refreshConversation(OTHER_PARTICIPANT);
        send.addClickListener(event -> {
            sendMessage(messageField.getValue(), OTHER_PARTICIPANT);
            refresh(OTHER_PARTICIPANT);
            messageField.clear();
        });
    }

    public void sendMessage(String value, ChatUserDto to) {
        String message = chatService.sendMessage(value, to);
        if (message != null) LOGGER.info("Message sent to" + OTHER_PARTICIPANT);
    }

    public void refreshConversation(ChatUserDto chatUserDto) {
        OTHER_PARTICIPANT = chatUserDto;
        grid.getDataProvider().refreshAll();
        if (chatUserDto != null){
            grid.setItems(chatService.getConversation(chatUserDto));
        }else grid.setItems(new ArrayList<>());
    }

    public void refresh(ChatUserDto chatUserDto) {
        OTHER_PARTICIPANT = chatUserDto;
        grid.setItems(chatService.getConversation(chatUserDto));
    }

}
