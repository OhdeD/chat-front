package com.frontend.chat.view;

import com.frontend.chat.domain.ChatUserDto;
import com.frontend.chat.domain.RolesDto;
import com.frontend.chat.services.ChatService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("adminPanel")
public class AdminPanelView extends VerticalLayout {
    private ChatService chatService = ChatService.getInstance();
    private HorizontalLayout body = new HorizontalLayout();
    private HorizontalLayout top = new HorizontalLayout();
    private HorizontalLayout bottom = new HorizontalLayout();
    private HorizontalLayout searchAndButtons = new HorizontalLayout();
    private Label label = new Label("You can do more via DB manger app. ");
    private Button goBack = new Button("Go back");
    private Button assignAsAdmin = new Button("Set as Admin");
    private Button assignAsUser = new Button("Set as User");
    private TextField searchText = new TextField();
    private Grid<ChatUserDto> allUsers = new Grid<>(ChatUserDto.class);
    private Grid<RolesDto> roles = new Grid<>(RolesDto.class);
    private ChatUserDto currentlyPickedUser = new ChatUserDto();

    public AdminPanelView() {
        add(top, body, bottom);
        top.add(goBack, label);
        goBack.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        allUsers.setColumns("name", "surname", "mail", "city", "logged");
        roles.setColumns("role");
        searchAndButtons.add(searchText, assignAsAdmin, assignAsUser);
        add(searchAndButtons, allUsers, roles);

        searchText.setPlaceholder("Search user");
        searchText.setClearButtonVisible(true);
        searchText.setValueChangeMode(ValueChangeMode.EAGER);
        searchText.addValueChangeListener(e -> {
            if (searchText.getValue().length() > 1) {
                search();
            } else {
                allUsers.getDataProvider().refreshAll();
            }
        });
        allUsers.asSingleSelect().addValueChangeListener(a -> {
            roles.getDataProvider().refreshAll();
            currentlyPickedUser = allUsers.asSingleSelect().getValue();
            roles.setItems(getRoles(currentlyPickedUser));
        });
        assignAsAdmin.addClickListener(event -> {
            setRole(currentlyPickedUser, "ADMIN");
            refreshRoles();
        });
        assignAsUser.addClickListener(x -> {
            setRole(currentlyPickedUser, "USER");
            refreshRoles();
        });

        goBack.addClickListener(a -> goBack.getUI().ifPresent(e -> e.navigate("mainView")));
    }

    private void search() {
        allUsers.setItems(chatService.searchUser(searchText.getValue()));
    }

    private void setRole(ChatUserDto chatUserDto, String role) {
        chatService.setRoleAsAdmin(chatUserDto, role);
    }

    private void refreshUsers() {
        allUsers.setItems(chatService.getAllUsers());
    }

    private RolesDto getRoles(ChatUserDto chatUserDto) {
        return chatService.getRole(chatUserDto);
    }

    private void refreshRoles() {
        roles.getDataProvider().refreshAll();
        roles.setItems(getRoles(currentlyPickedUser));
    }
}
