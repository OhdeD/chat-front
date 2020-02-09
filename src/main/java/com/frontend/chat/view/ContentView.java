package com.frontend.chat.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ContentView extends HorizontalLayout {

    private FriendsListView friendsListView = new FriendsListView();
    private ConversationView conversationView = new ConversationView();
    private FriendsSearcherView friendsSearcherView = new FriendsSearcherView();

    public ContentView() {

    add(friendsListView,conversationView,friendsSearcherView);

    }

}
