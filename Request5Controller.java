package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Request5Controller implements Initializable {
    @FXML public Button buttonReturnBack;
    @FXML public TableView<Request5> table;
    @FXML public Button buttonReload;
    @FXML public TableColumn<Request5, String> tableReaderCount;
    @FXML public TableColumn<Request5, String> tableLocID;
    @FXML public TableColumn<Request5, String> tableDeadLineCount;
    private final ObservableList<Request5> data = FXCollections.observableArrayList();
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
        addInfo();
        tableLocID.setCellValueFactory(new PropertyValueFactory<>("locID"));
        tableLocID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableReaderCount.setCellValueFactory(new PropertyValueFactory<>("countReader"));
        tableReaderCount.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDeadLineCount.setCellValueFactory(new PropertyValueFactory<>("countDead"));
        tableDeadLineCount.setCellFactory(TextFieldTableCell.forTableColumn());
        table.setItems(data);
    }

    public void addInfo() {
        String str = "SELECT location_id, count(location_id), count(end_date)\n" +
                "from lib_cards\n" +
                "left outer join belongings b on lib_cards.id = b.libcard_id and b.end_date < current_date\n" +
                "group by location_id";
        ResultSet elements = dbHandler.sendRequest(str);
        try {
            while(elements.next()) {
                Request5 request5 = new Request5(elements.getString(1),
                        elements.getString(2),
                        elements.getString(3));
                data.add(request5);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
