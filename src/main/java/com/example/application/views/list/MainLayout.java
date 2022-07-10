package com.example.application.views.list;

import com.example.application.security.SecurityService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import java.security.AuthProvider;

public class MainLayout extends AppLayout {

    private  SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService =  securityService;
        createHeader();
        createDrawer();
    }


    private void createHeader() {
        H1 logo = new H1("Task Application");
        logo.addClassNames("text-l", "m-m");  /*putem sa le gasim in documentatie*/

        Button logOut= new Button("Log out", e -> securityService.logout());

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logOut);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink listView = new RouterLink("Employees", ListView.class);
        listView.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                listView,
                new RouterLink("Task", TaskView.class),
                new RouterLink("Dashboard", DashboardView.class)
                ));

        RouterLink taskView = new RouterLink("Task", TaskView.class);
        taskView.setHighlightCondition(HighlightConditions.sameLocation());
    }
}
