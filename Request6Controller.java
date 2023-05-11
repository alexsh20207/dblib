package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Request6Controller implements Initializable {
    @FXML public Button buttonReturnBack;
    @FXML public Button buttonReload;
    @FXML public TableView<Request6> table;
    @FXML public TableColumn<Request6, String> tableBookName;
    @FXML public TableColumn<Request6, String> tableBookID;
    @FXML public TableColumn<Request6, String> tableDate;
    @FXML public TextField fnCountDay;
    private final ObservableList<Request6> data = FXCollections.observableArrayList();
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
        addInfo();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table.setEditable(false);
        addInfo();
        tableBookID.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        tableBookID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableBookName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableBookName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableDate.setCellFactory(TextFieldTableCell.forTableColumn());
        table.setItems(data);
    }

    public void addInfo() {
        String date = fnCountDay.getText().equals("") ? "365" : fnCountDay.getText();
        String str = "SELECT books.name, count(books.name), requests.date\n" +
                "from (requests inner join books on requests.book_id = books.id)\n" +
                "where requests.date > current_date - " + date + "\n" +
                "group by books.name, requests.date";
        ResultSet elements = dbHandler.sendRequest(str);
        try {
            while (elements.next()) {
                Request6 request6 = new Request6(elements.getString(1),
                        elements.getString(2),
                        elements.getString(3));
                data.add(request6);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
