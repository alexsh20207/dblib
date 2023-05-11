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
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Request12Controller implements Initializable {
    @FXML public Button buttonReturnBack;
    @FXML public TableView<Request12> table;
    @FXML public Button buttonReload;
    @FXML public TableColumn<Request12, String> tableReaderId;
    @FXML public TableColumn<Request12, String> tableFirstName;
    @FXML public TableColumn<Request12, String> tableDate;
    @FXML public TableColumn<Request12, String> tableLastName;
    @FXML public TextField fnBook;
    private final ObservableList<Request12> data = FXCollections.observableArrayList();
    @FXML public Text textReader;
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
        tableReaderId.setCellValueFactory(new PropertyValueFactory<>("readerId"));
        tableReaderId.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableFirstName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableLastName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableDate.setCellFactory(TextFieldTableCell.forTableColumn());
        addInfo();
        table.setItems(data);
    }

    public void addInfo() {
        if (fnBook.getText().equals("")) return;
        String info = "";
        Boolean check = true;
        String name = fnBook.getText();
        String str = "SELECT readers.id, readers.first_name, readers.last_name, b.end_date\n" +
                "from readers inner join lib_cards lc on readers.id = lc.reader_id\n" +
                "inner join belongings b on lc.id = b.libcard_id\n" +
                "left outer join books on b.book_id = books.id\n" +
                "where books.name = '" + name + "' \n" +
                "order by b.end_date";
        ResultSet books = dbHandler.sendRequest(str);
        try {
            while (books.next()) {
                Request12 request12 = new Request12(books.getString(1),
                        books.getString(2),
                        books.getString(3),
                        books.getString(4));
                if (check) {
                    info = String.join("", request12.getReaderId(), " ", request12.getFirstName(), " ",
                            request12.getLastName(), " ", request12.getDate());
                    check = false;
                }
                data.add(request12);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        textReader.setText(info);
    }
}
