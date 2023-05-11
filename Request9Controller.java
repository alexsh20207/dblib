package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Request9Controller implements Initializable {
    @FXML public Button buttonReturnBack;
    @FXML public TextField fnPropValue;
    @FXML public ComboBox fnProperty;
    @FXML public Button buttonReload;
    @FXML public TableView<Request9> table;
    @FXML public TableColumn<Request9, String> tableReaderID;
    @FXML public TableColumn<Request9, String> tableFirstName;
    @FXML public TableColumn<Request9, String> tableLastName;
    @FXML public TableColumn<Request9, String> tableType;
    @FXML public Text TextCount;
    @FXML public ComboBox fnType;
    private final ObservableList<Request9> data = FXCollections.observableArrayList();
    @FXML public TextField fnDateFree;
    DBHandler dbHandler = new DBHandler();
    int count;
    public void returnBack() {
        try {
            App.setRoot("selectRequests");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void reloadTable() {
        table.getItems().clear();
        addInfo();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fnProperty.getItems().removeAll(fnProperty.getItems());
        fnProperty.getItems().addAll(  "факультет", "группа", "без признака");
        fnProperty.getSelectionModel().select("без признака");
        fnType.getItems().removeAll(fnProperty.getItems());
        fnType.getItems().addAll(  "кто угодно","студент", "преподаватель",
                "абитуриенты", "ассистенты", "ФПК", "выпускники",
                "слушатели ПО", "стажёры");
        fnType.getSelectionModel().select("кто угодно");
        table.setEditable(false);
        tableReaderID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableReaderID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFirstName.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        tableFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLastName.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        tableLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableType.setCellFactory(TextFieldTableCell.forTableColumn());
        addInfo();
        table.setItems(data);
    }

    public void addInfo() {
        count = 0;
        String str = "";
        switch (fnProperty.getSelectionModel().getSelectedItem().toString()) {
            case "без признака": {
                str = "SELECT readers.id, readers.first_name, readers.last_name, readers.type\n" +
                        "from readers inner join lib_cards lc on readers.id = lc.reader_id";
                break;
            }
            case "группа": {
                str = "SELECT readers.id, readers.first_name, readers.last_name, readers.type\n" +
                        "from readers inner join lib_cards lc on readers.id = lc.reader_id\n" +
                        "        inner join students on readers.id = students.id\n" +
                        "where students.group_num = " + fnPropValue.getText();
                break;
            }
            case "факультет": {
                str = "SELECT readers.id, readers.first_name, readers.last_name, readers.type\n" +
                        "from readers inner join lib_cards lc on readers.id = lc.reader_id\n" +
                        "left outer join students on readers.id = students.id\n" +
                        "left outer join applicants on readers.id = applicants.id\n" +
                        "left outer join fpk on readers.id = fpk.id\n" +
                        "left outer join graduates on readers.id = graduates.id\n" +
                        "left outer join \"listenersPO\" on readers.id = \"listenersPO\".id\n" +
                        "left outer join teachers on readers.id = teachers.id\n" +
                        "\n" +
                        "where students.department_name = '" + fnPropValue.getText() + "' or\n" +
                        "      applicants.department_name = '" + fnPropValue.getText() + "' or \n" +
                        "      fpk.department_name = '" + fnPropValue.getText() + "' or \n" +
                        "      graduates.department_name = '" + fnPropValue.getText() + "' or \n" +
                        "      \"listenersPO\".department_name = '" + fnPropValue.getText() + "'or \n" +
                        "      teachers.department_name = '" + fnPropValue.getText() + "'";
                break;
            }
        }
        switch (fnType.getSelectionModel().getSelectedItem().toString()) {
            case "студент": {
                Boolean check = fnProperty.getSelectionModel().getSelectedItem().toString().equals("без признака");
                String addString = check ? "\n where " : "\n and ";
                str = String.join("",str, addString, "readers.type = 'student'");
                break;
            }
            case "преподаватель": {
                Boolean check = fnProperty.getSelectionModel().getSelectedItem().toString().equals("без признака");
                String addString = check ? "\n where " : "\n and ";
                str = String.join("",str, addString, "readers.type = 'teacher'");
                break;
            }
            case "абитуриенты": {                Boolean check = fnProperty.getSelectionModel().getSelectedItem().toString().equals("без признака");
                String addString = check ? "\n where " : "\n and ";
                str = String.join("",str, addString, "readers.type = 'applicant'");
                break;
            }
            case "ассистенты": {
                Boolean check = fnProperty.getSelectionModel().getSelectedItem().toString().equals("без признака");
                String addString = check ? "\n where " : "\n and ";
                str = String.join("",str, addString, "readers.type = 'assistand and others'");
                break;
            }
            case "ФПК": {
                Boolean check = fnProperty.getSelectionModel().getSelectedItem().toString().equals("без признака");
                String addString = check ? "\n where " : "\n and ";
                str = String.join("",str, addString, "readers.type = 'fpk'");
                break;
            }
            case "выпускники": {
                Boolean check = fnProperty.getSelectionModel().getSelectedItem().toString().equals("без признака");
                String addString = check ? "\n where " : "\n and ";
                str = String.join("",str, addString, "readers.type = 'graduate'");
                break;
            }
            case "слушатели ПО": {
                Boolean check = fnProperty.getSelectionModel().getSelectedItem().toString().equals("без признака");
                String addString = check ? "\n where " : "\n and ";
                str = String.join("",str, addString, "readers.type = 'listener PO'");
                break;
            }
            case "стажёры": {
                Boolean check = fnProperty.getSelectionModel().getSelectedItem().toString().equals("без признака");
                String addString = check ? "\n where " : "\n and ";
                str = String.join("",str, addString, "readers.type = 'trainee'");
                break;
            }
        }
        Boolean checkDate = fnProperty.getSelectionModel().getSelectedItem().toString().equals("без признака") &&
                fnType.getSelectionModel().getSelectedItem().equals("кто угодно");
        String addString = checkDate ? "\n where " : "\n and ";
        String date = fnDateFree.getText().equals("") ? "365" : fnDateFree.getText();
        str = String.join("", str, addString,
                "start_date > current_date - ", date);
        System.out.println(str);
        ResultSet readers = dbHandler.sendRequest(str);
        try {
            while (readers.next()) {
                Request9 request9 = new Request9(readers.getString(1),
                        readers.getString(2),
                        readers.getString(3),
                        readers.getString(4));
                data.add(request9);
                count++;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        TextCount.setText("Всего читателей: " + count);
    }
}
