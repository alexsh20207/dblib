package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TableLStudentsController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public TableView<Student> table;
    @FXML
    public TableColumn<Student, String> tableID;
    @FXML
    public TableColumn<Student, String> tableFN;
    @FXML
    public TableColumn<Student, String> tableLN;
    @FXML
    public TableColumn<Student, String> tableFromUni;
    @FXML
    public TableColumn<Student, String> tableGroup;
    @FXML
    public TableColumn<Student, String> tableDepartment;
    @FXML
    public MenuItem menuDelete;
    @FXML
    public TextField fnFN;
    @FXML
    public Button buttonAdd;
    @FXML
    public Button buttonReload;
    @FXML
    public TextField fnSearch;
    @FXML
    public ComboBox fnFromUni;
    @FXML
    public TextField fnGroupNum;
    @FXML
    public TextField fnLN;
    @FXML
    public TextField fnDepartment;

    private final ObservableList<Student> data = FXCollections.observableArrayList();
    DBHandler dbHandler = new DBHandler();

    public void returnBack() {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertInfo() {
        if (!(fnFN.getText().equals("") ||
                fnLN.getText().equals("") ||
                fnGroupNum.getText().equals("") ||
                !fnGroupNum.getText().chars().allMatch(Character::isDigit) ||
                fnDepartment.getText().equals(""))) {
            String str = "INSERT INTO students(first_name, last_name, from_uni, " +
                    "group_num, department_name, id) " +
                    "VALUES ('" + fnFN.getText() + "', '" + fnLN.getText() + "', '" +
                    fnFromUni.getSelectionModel().getSelectedItem().toString() + "', " + fnGroupNum.getText() +
                    ", '" + fnDepartment.getText() + "', " +
                    "(SELECT MAX(id) + 1 FROM readers))";
            dbHandler.sendRequest(str);
            reloadTable();
        }
    }

    public void reloadTable() {
        table.getItems().clear();
        addInfo();
    }

    public void addInfo() {
        ResultSet students = dbHandler.sendRequest("SELECT * FROM students");
        try {
            while (students.next()) {
                Student student = new Student(
                        students.getInt(6),
                        students.getString(1),
                        students.getString(2),
                        students.getString(3),
                        "student",
                        students.getString(4),
                        students.getString(5)
                );
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(student.getId()).contains(fnSearch.getText()) ||
                            student.getFirst_name().contains(fnSearch.getText()) ||
                            student.getLast_name().contains(fnSearch.getText()) ||
                            student.getGroup_num().contains(fnSearch.getText()) ||
                            student.getDepartment().contains(fnSearch.getText()) ||
                            student.getFrom_uni().contains(fnSearch.getText()))) {
                        continue;
                    }
                }
                data.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
        table.setEditable(true);
        tableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableID.setEditable(false);

        fnFromUni.getItems().removeAll(fnFromUni.getItems());
        fnFromUni.getItems().addAll("t", "f");
        fnFromUni.getSelectionModel().select("t");

        tableFN.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        tableFN.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> studentStringCellEditEvent) {
                Student student = studentStringCellEditEvent.getRowValue();
                student.setFirst_name(studentStringCellEditEvent.getNewValue());
                String str = "UPDATE students set first_name = '" +
                        student.getFirst_name() + "' WHERE id = " + student.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLN.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        tableLN.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> studentStringCellEditEvent) {
                Student student = studentStringCellEditEvent.getRowValue();
                student.setLast_name(studentStringCellEditEvent.getNewValue());
                String str = "UPDATE students set last_name = '" +
                        student.getLast_name() + "' WHERE id = " + student.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableFromUni.setCellValueFactory(new PropertyValueFactory<>("from_uni"));
        tableFromUni.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFromUni.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> studentStringCellEditEvent) {
                Student student = studentStringCellEditEvent.getRowValue();
                student.setFirst_name(studentStringCellEditEvent.getNewValue());
                if (student.getFrom_uni().equals("t") || student.getFrom_uni().equals("f")) {
                    String str = "UPDATE students set from_uni = '" +
                            student.getFrom_uni() + "' WHERE id = " + student.getId();
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        tableGroup.setCellValueFactory(new PropertyValueFactory<>("group_num"));
        tableGroup.setCellFactory(TextFieldTableCell.forTableColumn());
        tableGroup.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> studentStringCellEditEvent) {
                Student student = studentStringCellEditEvent.getRowValue();
                if (student.getGroup_num().chars().allMatch(Character::isDigit)) {
                    student.setGroup_num(studentStringCellEditEvent.getNewValue());
                    String str = "UPDATE students set group_num = '" +
                            student.getGroup_num() + "' WHERE id = " + student.getId();
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        tableDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        tableDepartment.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDepartment.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Student, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Student, String> studentStringCellEditEvent) {
                Student student = studentStringCellEditEvent.getRowValue();
                student.setDepartment(studentStringCellEditEvent.getNewValue());
                String str = "UPDATE students set department_name = '" +
                        student.getDepartment() + "' WHERE id = " + student.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Student student = table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM students WHERE id = " + student.getId());
                reloadTable();
            }
        });
        table.setItems(data);
    }
}
