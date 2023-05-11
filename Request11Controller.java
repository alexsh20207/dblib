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

public class Request11Controller implements Initializable {
    @FXML public Button buttonReturnBack;
    @FXML public TableView<Request11> table;
    @FXML public Button buttonReload;
    @FXML public TableColumn tableBookName;
    @FXML public TableColumn tableLocID;
    @FXML public TableColumn tableCount;
    @FXML public TextField fnBook;
    private final ObservableList<Request11> data = FXCollections.observableArrayList();
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
        tableLocID.setCellValueFactory(new PropertyValueFactory<>("locId"));
        tableLocID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        tableCount.setCellFactory(TextFieldTableCell.forTableColumn());
        addInfo();
        table.setItems(data);
    }

    public void addInfo() {
        String str = "SELECT books.name, location_id, count(books.location_id)\n" +
                "from books\n" +
                "where books.status in ('inLib', 'в библиотеке')\n";
        String bookName = fnBook.getText().equals("") ? "" :
                "and books.name = '" + fnBook.getText() + "'\n";
        str = String.join("", str, bookName, "group by books.name, location_id");
        ResultSet books = dbHandler.sendRequest(str);
        try {
            while (books.next()) {
                Request11 request11 = new Request11(books.getString(1),
                        books.getString(2),
                        books.getString(3));
                data.add(request11);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
