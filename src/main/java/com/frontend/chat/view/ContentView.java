package com.frontend.chat.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class ContentView extends HorizontalLayout {

    private ConversationView conversationView = new ConversationView();
    private FriendsListView friendsListView = new FriendsListView(conversationView);
    private FriendsSearcherView friendsSearcherView = new FriendsSearcherView(friendsListView);

    public ContentView() {

    add(friendsListView,conversationView,friendsSearcherView);
    setDefaultVerticalComponentAlignment(Alignment.CENTER);
    setAlignItems(Alignment.CENTER);

    }

}
