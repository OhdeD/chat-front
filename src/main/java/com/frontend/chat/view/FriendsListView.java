package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class FriendsListView extends VerticalLayout {
    private ChatService chatService = ChatService.getInstance();
    private Grid<ChatUserDto> grid = new Grid<>(ChatUserDto.class);

    public FriendsListView() {
        grid.setColumns("name", "surname", "id");
//        grid.addColumn(new NativeButtonRenderer<>("conversation", val -> {
//            //dopisać resztę
//        }));
        add(grid);
        refresh();
    }

    private void refresh() {
        grid.setItems(chatService.getFriendsList("2"));
    }
}
