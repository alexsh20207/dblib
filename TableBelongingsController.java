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

public class TableBelongingsController implements Initializable {
    @FXML public Button buttonBack;
    @FXML public TableView<Belonging> tableCat;
    @FXML public TableColumn<Belonging, String> tableID;
    @FXML public TableColumn<Belonging, String> tableLibID;
    @FXML public TableColumn<Belonging, String> tableBookId;
    @FXML public TableColumn<Belonging, String> tableDate;
    @FXML public MenuItem menuDelete;
    @FXML public TextField fnLibId;
    @FXML public Button buttonAdd;
    @FXML public Button buttonReload;
    @FXML public TextField fnSearch;
    @FXML public TextField fnBookId;
    @FXML public TextField fnDate;
    private final ObservableList<Belonging> data  = FXCollections.observableArrayList();
    DBHandler dbHandler = new DBHandler();

    public void returnBack() {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadTable() {
        tableCat.getItems().clear();
        addInfo();
    }

    public void addInfo() {
        ResultSet belongings = dbHandler.sendRequest("SELECT * FROM belongings");
        try {
            while (belongings.next()) {
                Belonging belonging = new Belonging(belongings.getInt(1),
                        belongings.getString(2),
                        belongings.getString(3),
                        belongings.getString(4));
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(belonging.getId()).contains(fnSearch.getText()) ||
                            belonging.getLib_card_id().contains(fnSearch.getText()) ||
                            belonging.getBook_id().contains(fnSearch.getText()) ||
                            belonging.getEnd_date().contains(fnSearch.getText()))) {
                        continue;
                    }
                }
                data.add(belonging);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
        tableCat.setEditable(true);
        tableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableID.setEditable(false);
        tableBookId.setCellValueFactory(new PropertyValueFactory<>("book_id"));
        tableBookId.setCellFactory(TextFieldTableCell.forTableColumn());
        tableBookId.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Belonging, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Belonging, String> belongingStringCellEditEvent) {
                Belonging belonging = belongingStringCellEditEvent.getRowValue();
                belonging.setBook_id(belongingStringCellEditEvent.getNewValue());
                String str = "UPDATE belongings SET book_id = " + belonging.getBook_id() +
                        " WHERE id = " + belonging.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableDate.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        tableDate.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Belonging, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Belonging, String> belongingStringCellEditEvent) {
                Belonging belonging = belongingStringCellEditEvent.getRowValue();
                belonging.setEnd_date(belongingStringCellEditEvent.getNewValue());
                String str = "UPDATE belongings SET end_date = " + belonging.getEnd_date() +
                        " WHERE id = " + belonging.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLibID.setCellValueFactory(new PropertyValueFactory<>("lib_card_id"));
        tableLibID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLibID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Belonging, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Belonging, String> belongingStringCellEditEvent) {
                Belonging belonging = belongingStringCellEditEvent.getRowValue();
                belonging.setLib_card_id(belongingStringCellEditEvent.getNewValue());
                String str = "UPDATE belongings SET libcard_id = " + belonging.getLib_card_id() +
                        " WHERE id = " + belonging.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Belonging belonging = tableCat.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM belongings WHERE id = " + belonging.getId());
                reloadTable();
            }
        });
        tableCat.setItems(data);
    }
    public void insertInfo() {
        if (!(fnLibId.getText().equals("") || fnBookId.getText().equals(""))) {
            String date = fnDate.getText().equals("") ? "DEFAULT" : String.join("",  "'", fnDate.getText(), "'");
            dbHandler.sendRequest("insert into belongings(libcard_id, book_id, end_date) VALUES ('" +
                    fnLibId.getText() + "', '" + fnBookId.getText() + "', " + date + ")");
            reloadTable();
        }
    }
}
