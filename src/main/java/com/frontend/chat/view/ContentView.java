package com.frontend.chat.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import lombok.Getter;


@Route(value = "contentView")
@Getter
public class ContentView extends HorizontalLayout {
    private Long currentUserId;
    private FriendsListView friendsListView = new FriendsListView(this);
    private ConversationView conversationView = new ConversationView();

    public ContentView(Long currentUserId) {
        this.currentUserId = currentUserId;
        add(friendsListView,conversationView);

    }


}
