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

public class Request2Controller implements Initializable {
    @FXML public Button buttonReturnBack;
    @FXML public TextField fnPropValue;
    @FXML public ComboBox fnProperty;
    @FXML public Button buttonReload;
    @FXML public TableView<Request2> table;
    @FXML public TableColumn<Request2, String> tableFirstName;
    @FXML public TableColumn<Request2, String> tableReaderID;
    @FXML public TableColumn<Request2, String> tableLastName;
    @FXML public TableColumn<Request2, String> tableType;
    @FXML public Text TextCount;
    private final ObservableList<Request2> data = FXCollections.observableArrayList();
    DBHandler dbHandler = new DBHandler();
    int count = 0;

    public void returnBack() {
        try {
            App.setRoot("selectRequests");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void reloadTable() {
        table.getItems().clear();
        if (!fnPropValue.getText().equals("")) {
            addInfo();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!fnPropValue.getText().equals("")) {
            addInfo();
        }
        fnProperty.getItems().removeAll(fnProperty.getItems());
        fnProperty.getItems().addAll(  "факультет", "группа", "id абонемента");
        fnProperty.getSelectionModel().select("id абонемента");
        table.setEditable(false);
        tableReaderID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableReaderID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFirstName.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        tableFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLastName.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        tableLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableType.setCellFactory(TextFieldTableCell.forTableColumn());
        table.setItems(data);
    }

    public void addInfo() {
        count = 0;
        String str = "";
        switch (fnProperty.getSelectionModel().getSelectedItem().toString()) {
            case "id абонемента": {
                str = "SELECT readers.id, readers.first_name, readers.last_name, readers.type\n" +
                        "from readers inner join lib_cards on readers.id = lib_cards.reader_id\n" +
                        "inner join belongings b on lib_cards.id = b.libcard_id\n" +
                        "where b.end_date < current_date - 10 and " +
                        "lib_cards.location_id = " + fnPropValue.getText();
                break;
            }
            case "группа": {
                str = "SELECT readers.id, readers.first_name, readers.last_name, readers.type\n" +
                        "from readers inner join lib_cards on readers.id = lib_cards.reader_id\n" +
                        "inner join belongings b on lib_cards.id = b.libcard_id\n" +
                        "from readers inner join students on readers.id = students.id\n" +
                        "where b.end_date < current_date - 10 and " +
                        "students.group_num = " + fnPropValue.getText();
                break;
            }
            case "факультет": {
                str = "SELECT readers.id, readers.first_name, readers.last_name, readers.type\n" +
                        "from readers inner join lib_cards on readers.id = lib_cards.reader_id\n" +
                        "inner join belongings b on lib_cards.id = b.libcard_id\n" +
                        "from (readers left outer join students on readers.id = students.id)\n" +
                        "left outer join applicants on readers.id = applicants.id\n" +
                        "left outer join fpk on readers.id = fpk.id\n" +
                        "left outer join graduates on readers.id = graduates.id\n" +
                        "left outer join \"listenersPO\" on readers.id = \"listenersPO\".id\n" +
                        "left outer join teachers on readers.id = teachers.id\n" +
                        "\n" +
                        "where b.end_date < current_date - 10 and " +
                        "students.department_name = '" + fnPropValue.getText() + "' or\n" +
                        "      applicants.department_name = '" + fnPropValue.getText() + "' or \n" +
                        "      fpk.department_name = '" + fnPropValue.getText() + "' or \n" +
                        "      graduates.department_name = '" + fnPropValue.getText() + "' or \n" +
                        "      \"listenersPO\".department_name = '" + fnPropValue.getText() + "'or \n" +
                        "      teachers.department_name = '" + fnPropValue.getText() + "'";
                break;
            }
        }
        System.out.println(str);
        ResultSet readers = dbHandler.sendRequest(str);
        try {
            while (readers.next()) {
                Request2 request1 = new Request2(readers.getString(1),
                        readers.getString(2),
                        readers.getString(3),
                        readers.getString(4));
                data.add(request1);
                count++;
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        TextCount.setText("Всего читателей: " + count);
    }
}
