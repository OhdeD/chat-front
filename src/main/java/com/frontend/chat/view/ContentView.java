package com.frontend.chat.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import lombok.Getter;


@Route("main")
@Getter
public class ContentView extends HorizontalLayout {
    private FriendsListView friendsListView = new FriendsListView(this);
    private ConversationView conversationView = new ConversationView();

    public ContentView() {
        add(friendsListView,conversationView);

    }


}
