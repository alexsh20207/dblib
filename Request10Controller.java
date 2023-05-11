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

public class Request10Controller implements Initializable {
    @FXML public Button buttonReturnBack;
    @FXML public TextField fnLibID;
    @FXML public Button buttonReload;
    @FXML public TableView<Request10> table;
    @FXML public TableColumn<Request10, String> tableBookID;
    @FXML public TableColumn<Request10, String> tableName;
    @FXML public TableColumn<Request10, String> tableStatus;
    @FXML public TextField fnDate;
    @FXML public Text TextCount;
    private final ObservableList<Request10> data = FXCollections.observableArrayList();
    DBHandler dbHandler = new DBHandler();
    int countGet, countRet;
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
        tableBookID.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        tableBookID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        addInfo();
        table.setItems(data);
    }

    public void addInfo() {
        countGet = 0;
        countRet = 0;
        String str = "SELECT books.id, books.name, r.status\n" +
                "from books inner join requests r on books.id = r.book_id\n" +
                "where r.status in ('ожидание выдачи', 'возвращено в библиотеку')\n";
        String libID = fnLibID.getText().equals("") ? "0" : fnLibID.getText();
        str = String.join("", str, "and lib_card_id = ", libID, "\n");
        String date = fnDate.getText().equals("") ? "365" : fnDate.getText();
        str = String.join("", str, "and r.date > current_date - ", date, "\n");
        str = String.join("", str, "order by status");
        ResultSet books = dbHandler.sendRequest(str);
        try {
            while(books.next()) {
                Request10 request10 = new Request10(books.getString(1),
                        books.getString(2),
                        books.getString(3));
                data.add(request10);
                if (request10.getStatus().equals("ожидание выдачи")) {
                    countGet++;
                }
                else {
                    countRet++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TextCount.setText("Всего заказано: " + countGet + "\n" +
                "Всего на руках: " + (countGet - countRet));
    }
}
