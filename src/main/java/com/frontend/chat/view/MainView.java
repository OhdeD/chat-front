package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import java.util.List;


@Route("")
public class MainView extends VerticalLayout {
    private ChatService chatService = ChatService.getInstance();
    private Grid<ChatUserDto> grid = new Grid<>(ChatUserDto.class);

    public MainView() {
        grid.setColumns("name", "surname");
        add(grid);
        refresh();

    }

    private void refresh() {
        List<ChatUserDto> friends = chatService.getFriendsList("2");
        for (ChatUserDto c : friends) {
            grid.setItems(c);
        }
    }
}
