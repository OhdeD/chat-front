package com.frontend.chat.view;


import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("adminPanel")
public class AdminPanelView extends VerticalLayout {
    private HorizontalLayout body = new HorizontalLayout();
    private HorizontalLayout top = new HorizontalLayout();
    private HorizontalLayout bottom = new HorizontalLayout();
    private Label label = new Label("It's not important APP part, U can manage APP via workbench or smth. ");

    public AdminPanelView() {
        add(top, body, bottom);
        body.add(label);

    }
}
