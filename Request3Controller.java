package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class Request3Controller implements Initializable {
    @FXML public Button buttonReturnBack;
    @FXML public ComboBox fnProperty;
    @FXML public TextField fnPropValue;
    @FXML public Button buttonReload;
    @FXML public TableView<Request3> table;
    @FXML public TableColumn<Request3, String> tableBookName;
    @FXML public TableColumn<Request3, String> tableCountRequest;

    private final ObservableList<Request3> data = FXCollections.observableArrayList();
    DBHandler dbHandler = new DBHandler();
    public void returnBack() {
        try {
            App.setRoot("selectRequests");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void reloadTable() {
        table.getItems().clear();
        if (!fnPropValue.getText().equals("") || fnProperty.getSelectionModel().getSelectedItem().toString().equals("во всей библиотеке")) {
            addInfo();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fnProperty.getItems().removeAll(fnProperty.getItems());
        fnProperty.getItems().addAll(  "во всей библиотеке", "в определённом абонементе");
        fnProperty.getSelectionModel().select("во всей библиотеке");
        if (!fnPropValue.getText().equals("") || fnProperty.getSelectionModel().getSelectedItem().toString().equals("во всей библиотеке")) {
            addInfo();
        }
        table.setEditable(false);
        tableBookName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableBookName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableCountRequest.setCellValueFactory(new PropertyValueFactory<>("count"));
        tableCountRequest.setCellFactory(TextFieldTableCell.forTableColumn());
        table.setItems(data);
    }

    public void addInfo() {
        String str = "";
        switch (fnProperty.getSelectionModel().getSelectedItem().toString()) {
            case "во всей библиотеке": {
                str = "SELECT distinct books.name, count(books.name)\n" +
                        "from books inner join requests on books.id = requests.book_id\n" +
                        "where requests.status = 'ожидание выдачи'\n" +
                        "group by books.name\n" +
                        "LIMIT 20";
                break;
            }
            case "в определённом абонементе": {
                str = "SELECT distinct books.name, count(books.name)\n" +
                        "from books inner join requests on books.id = requests.book_id\n" +
                        "where requests.status = 'ожидание выдачи' and requests.location_id = " + fnPropValue.getText() + "\n" +
                        "group by books.name\n" +
                        "LIMIT 20";
                break;
            }
        }
        ResultSet books = dbHandler.sendRequest(str);
        try {
            while(books.next()) {
                Request3 request3 = new Request3(books.getString(1),
                        books.getString(2));
                data.add(request3);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
