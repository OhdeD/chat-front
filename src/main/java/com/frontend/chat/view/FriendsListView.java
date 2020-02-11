package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FriendsListView extends VerticalLayout {
    private ConversationView conversationView = new ConversationView();
    private ChatService chatService = ChatService.getInstance();
    private Grid<ChatUserDto> grid = new Grid<>(ChatUserDto.class);
    private Label label = new Label("My friends:");
    private Button delete = new Button();
    private HorizontalLayout horizontalLayout = new HorizontalLayout();
    private static final Logger LOGGER = LoggerFactory.getLogger(FriendsListView.class);

    public FriendsListView(ConversationView conversationView) {
        this.conversationView = conversationView;
        delete.setVisible(false);
        delete.setText("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        grid.setColumns("name", "surname", "logged");
        grid.setWidth("400px");

        add(label, grid, delete);

        horizontalLayout.add(this, conversationView);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        refresh();

        grid.asSingleSelect().addValueChangeListener(event -> {
            ChatUserDto u = grid.asSingleSelect().getValue();
            conversationView.refresh(u);
            delete.setVisible(true);
        });
        delete.addClickListener(e -> {
            ChatUserDto u = grid.asSingleSelect().getValue();
            chatService.deleteFromFriendsList(u);
            delete.setVisible(false);
            refresh();
            conversationView.refreshEmpty();
            LOGGER.info("User " + u.toString() + " deleted");
            Notification.show("User " + u.getName() + " deleted");
        });

    }

    public void refresh() {
        grid.getDataProvider().refreshAll();
        grid.setItems(chatService.getFriendsList(ChatService.CURRENT_USER));
    }
}
