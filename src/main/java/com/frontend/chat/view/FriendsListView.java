package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;

public class FriendsListView extends VerticalLayout {
    private ChatService chatService = ChatService.getInstance();
    private ContentView contentView;
    private Grid<ChatUserDto> grid = new Grid<>(ChatUserDto.class);

    public FriendsListView(ContentView contentView) {
        this.contentView = contentView;

        grid.setColumns("name", "surname", "id");
        grid.addColumn(new NativeButtonRenderer<>("conversation", val -> {
            //dopisać resztę
        }));
        add(grid);
        refresh();
    }

    private void refresh() {
        grid.setItems(chatService.getFriendsList("2"));
    }
}
