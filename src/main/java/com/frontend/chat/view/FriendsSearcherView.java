package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class FriendsSearcherView extends VerticalLayout {
    private ChatService chatService = ChatService.getInstance();
    private TextField search = new TextField("Search friends...");
    private Grid<ChatUserDto> grid = new Grid<>(ChatUserDto.class);
    private Button addFriend = new Button("Add a friend");
    private FriendsListView friendsListView = new FriendsListView(new ConversationView());

    public FriendsSearcherView(FriendsListView friendsListView) {
        this.friendsListView = friendsListView;
        grid.setColumns("name", "surname", "city");
        grid.setWidth("400px");
        add(search, addFriend, grid);
        addFriend.setVisible(false);
        setDefaultHorizontalComponentAlignment(Alignment.END);
        search.setPlaceholder("Type name");
        search.setClearButtonVisible(true);
        search.setValueChangeMode(ValueChangeMode.EAGER);
        search.addValueChangeListener(e -> {
            if (search.getValue().length() > 1) {
                search();
            } else {
                grid.getDataProvider().refreshAll();
            }
        });

        grid.asSingleSelect( ).addValueChangeListener(event ->      revealedAddButton());
        addFriend.addClickListener(e -> {
            chatService.addFriendToFriendsList(grid.asSingleSelect().getValue());
            friendsListView.refresh();
            hideAddButton();
            search.clear();
        });
    }

    private void revealedAddButton() {
        addFriend.setVisible(true);
    }

    private void hideAddButton() {
        addFriend.setVisible(false);
    }

    private void search() {
        grid.setItems(chatService.searchUser(search.getValue()));
    }
}