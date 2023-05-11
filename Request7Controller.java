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

public class Request7Controller  implements Initializable {
    @FXML public Button buttonReturnBack;
    @FXML public ComboBox fnProperty;
    @FXML public TextField fnPropValue;
    @FXML public Button buttonReload;
    @FXML public TableColumn<Request7, String> tableBookName;
    @FXML public TableColumn<Request7, String> tableCount;
    @FXML public TableView<Request7> table;
    private final ObservableList<Request7> data = FXCollections.observableArrayList();
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
        addInfo();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table.setEditable(false);
        tableBookName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableBookName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        tableCount.setCellFactory(TextFieldTableCell.forTableColumn());
        fnProperty.getItems().removeAll(fnProperty.getItems());
        fnProperty.getItems().addAll(  "во всей библиотеке", "в определённом абонементе");
        fnProperty.getSelectionModel().select("во всей библиотеке");
        if (!fnPropValue.getText().equals("") || fnProperty.getSelectionModel().getSelectedItem().toString().equals("во всей библиотеке")) {
            addInfo();
        }
        table.setItems(data);
    }

    public void addInfo() {
        String str = "";
        switch (fnProperty.getSelectionModel().getSelectedItem().toString()) {
            case "во всей библиотеке": {
                str = "SELECT books.name, count(books.location_id)\n" +
                        "from books\n" +
                        "group by books.name";
                break;
            }
            case "в определённом абонементе": {
                str = "SELECT books.name, count(books.location_id)\n" +
                        "from books\n" +
                        "where location_id = " +  fnPropValue.getText() + "\n" +
                        "group by books.name";
                break;
            }
        }
        ResultSet elements = dbHandler.sendRequest(str);
        try {
            while (elements.next()) {
                Request7 request7 = new Request7(elements.getString(1),
                        elements.getString(2));
                data.add(request7);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
