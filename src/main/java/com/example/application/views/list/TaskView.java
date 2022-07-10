package com.example.application.views.list;


import com.example.application.data.entity.Employees;
import com.example.application.data.service.CrmService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridLazyDataView;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Task")
@PermitAll
public class TaskView extends Div {

    private final CrmService service;

    private GridPro<Employees> grid;
    private GridListDataView<Employees> gridListDataView;

    private Grid.Column<Employees> employeeColumn;
    private Grid.Column<Employees> taskColumn;
    private Grid.Column<Employees> statusColumn;
    private Grid.Column<Employees> dateColumn;

    public TaskView(CrmService service) {
        this.service = service;

        addClassName("task-view");
        setSizeFull();
        createGrid();
        add(grid);
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }


    private void createGridComponent() {
        grid = new GridPro<>();
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        List<Employees> employees = getEmployees();
        gridListDataView = grid.setItems(employees);
    }

    private List<Employees> getEmployees() {
        return Arrays.asList(
                createEmployee(10, "https://randomuser.me/api/portraits/women/42.jpg", "Eula Lane", "Evaluate the interface between hardware and software",
                        "In progress", "2022-03-03"),
                createEmployee(20, "https://randomuser.me/api/portraits/women/24.jpg", "Eugenia Selvi", "Create web templates or prototypes", "Pending",
                        "2022-03-05"),
                createEmployee(30, "https://randomuser.me/api/portraits/men/42.jpg", "Barry Rodriquez", "Create technical documentation for reference and reporting",
                        "In progress", "2022-03-03"),
                createEmployee(40, "https://randomuser.me/api/portraits/men/24.jpg", "Alejandro Miles", "Design and implement firewalls or message encryption",
                        "Pending", "2022-03-10"),
                createEmployee(50, "https://randomuser.me/api/portraits/women/94.jpg", "Rose Gray", "Test programs or databases and make necessary changes",
                        "Close", "2022-02-25"));
    }


    private Employees createEmployee(int id, String img, String employee, String task, String status, String date){
        Employees c = new Employees();
        c.setId(id);
        c.setImg(img);
        c.setEmployee(employee);
        c.setTask(task);
        c.setStatus(status);
        c.setDate(date);

        return c;
    }


    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField employeeFilter = new TextField();
        employeeFilter.setPlaceholder("Filter");
        employeeFilter.setClearButtonVisible(true);
        employeeFilter.setWidth("100%");
        employeeFilter.setValueChangeMode(ValueChangeMode.EAGER);

        employeeFilter.addValueChangeListener(event -> gridListDataView.addFilter(employee -> StringUtils.containsIgnoreCase(employee.getEmployee(), employeeFilter.getValue())));

        filterRow.getCell(employeeColumn).setComponent(employeeFilter);

        ComboBox<String> statusFilter = new ComboBox<>();
        statusFilter.setItems(Arrays.asList("Pending", "Success", "Close", "In progress", "Done"));
        statusFilter.setPlaceholder("Filter");
        statusFilter.setClearButtonVisible(true);
        statusFilter.setWidth("100%");
        statusFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(employee -> areStatusesEqual(employee, statusFilter)));
        filterRow.getCell(statusColumn).setComponent(statusFilter);


        ComboBox<String> taskFilter = new ComboBox<>();
        taskFilter.setItems(Arrays.asList("Evaluate the interface between hardware and software",
                "Prepare designs and determine the specifications of a product",
                "Create web templates or prototypes",
                "Design and implement firewalls or message encryption",
                "Create technical documentation for reference and reporting"));
        taskFilter.setPlaceholder("Filter");
        taskFilter.setClearButtonVisible(true);
        taskFilter.setWidth("100%");
        taskFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(employee -> areTasksEqual(employee, taskFilter)));
        filterRow.getCell(taskColumn).setComponent(taskFilter);


        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filter");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(employee -> areDatesEqual(employee, dateFilter)));
        filterRow.getCell(dateColumn).setComponent(dateFilter);
    }

    private boolean areTasksEqual(Employees employee, ComboBox<String> taskFilter) {
        String taskFilterValue = taskFilter.getValue();
        if (taskFilterValue != null){
            return StringUtils.equals(employee.getTask(), taskFilterValue);
        }
        return true;
    }


    private boolean areStatusesEqual(Employees employee, ComboBox<String> statusFilter) {
        String statusFilterValue = statusFilter.getValue();
        if (statusFilterValue != null){
            return StringUtils.equals(employee.getStatus(), statusFilterValue);
        }
        return true;
    }


    private boolean areDatesEqual(Employees employee, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null){
            LocalDate employeeDate = LocalDate.parse(employee.getDate());
            return dateFilterValue.equals(employeeDate);
        }
        return true;
    }

    private void addColumnsToGrid() {
        createEmployeeColumn();
        createStatusColumn();
        createDateColumn();
        createTaskColumn();
    }

    private void createTaskColumn() {
        taskColumn = grid.addEditColumn(Employees::getEmployee, new ComponentRenderer<>(employee -> {
            Span span = new Span();
            span.setText(employee.getTask());
            span.getElement().setAttribute("theme", "badge " + employee.getTask().toLowerCase());
            return span;
        })).select((item, newValue) -> item.setTask(newValue), Arrays.asList("Evaluate the interface between hardware and software",
                "Prepare designs and determine the specifications of a product",
                "Create web templates or prototypes",
                "Design and implement firewalls or message encryption",
                "Create technical documentation for reference and reporting"))
                .setComparator(employee -> employee.getTask()).setHeader("Task");
    }

    private void createEmployeeColumn() {
        employeeColumn = grid.addColumn(new ComponentRenderer<>(employee -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(FlexComponent.Alignment.CENTER);
            Image img = new Image(employee.getImg(), "");
            Span span = new Span();
            span.setClassName("name");
            span.setText(employee.getEmployee());
            hl.add(img, span);
            return hl;
        })).setComparator(employee -> employee.getEmployee()).setHeader("Employee");
    }

    private void createStatusColumn() {
        statusColumn = grid.addEditColumn(Employees::getEmployee, new ComponentRenderer<>(employee -> {
            Span span = new Span();
            span.setText(employee.getStatus());
            span.getElement().setAttribute("theme", "badge " + employee.getStatus().toLowerCase());
            return span;
        })).select((item, newValue) -> item.setStatus(newValue), Arrays.asList("Pending", "Success", "Close", "In progress", "Done"))
                .setComparator(employee -> employee.getStatus()).setHeader("Status");
    }


    private void createDateColumn() {
        dateColumn = grid.addColumn(new LocalDateRenderer<>(employee -> LocalDate.parse(employee.getDate()), DateTimeFormatter.ofPattern("d/M/yyyy")))
                .setComparator(employee -> employee.getDate()).setHeader("Date").setWidth("180px").setFlexGrow(0);
    }

}
