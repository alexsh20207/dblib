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

public class TableRepaysController implements Initializable {
    @FXML public Button buttonBack;
    @FXML public TableView<Repay> tableCat;
    @FXML public TableColumn<Repay, String> tableID;
    @FXML public TableColumn<Repay, String> tableLibID;
    @FXML public TableColumn<Repay, String> tableBookId;
    @FXML public TableColumn<Repay, String> tableType;
    @FXML public TableColumn<Repay, String> tableDate;
    @FXML public MenuItem menuDelete;
    @FXML public TextField fnLibId;
    @FXML public Button buttonReload;
    @FXML public Button buttonAdd;
    @FXML public TextField fnSearch;
    @FXML public TextField fnBookId;
    @FXML public ComboBox fnType;
    @FXML public TextField fnDate;

    private final ObservableList<Repay> data  = FXCollections.observableArrayList();
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
        ResultSet repays = dbHandler.sendRequest("SELECT * FROM repays");
        try {
            while (repays.next()) {
                Repay repay = new Repay(repays.getInt(1),
                        repays.getString(2),
                        repays.getString(3),
                        repays.getString(4),
                        repays.getString(5));
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(repay.getId()).contains(fnSearch.getText()) ||
                            repay.getType_of_repay().contains(fnSearch.getText()) ||
                            repay.getBook_id().contains(fnSearch.getText()) ||
                            repay.getEnd_date().contains(fnSearch.getText()) ||
                            repay.getLib_card_id().contains(fnSearch.getText()))) {
                        continue;
                    }
                }
                data.add(repay);
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
        tableBookId.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Repay, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Repay, String> repayStringCellEditEvent) {
                Repay repay = repayStringCellEditEvent.getRowValue();
                repay.setBook_id(repayStringCellEditEvent.getNewValue());
                String str = "UPDATE repays SET book_id = " + repay.getBook_id() +
                        " WHERE id = " + repay.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLibID.setCellValueFactory(new PropertyValueFactory<>("lib_card_id"));
        tableLibID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLibID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Repay, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Repay, String> repayStringCellEditEvent) {
                Repay repay = repayStringCellEditEvent.getRowValue();
                repay.setBook_id(repayStringCellEditEvent.getNewValue());
                String str = "UPDATE repays SET lib_card_id = " + repay.getLib_card_id() +
                        " WHERE id = " + repay.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableDate.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        tableDate.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Repay, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Repay, String> repayStringCellEditEvent) {
                Repay repay = repayStringCellEditEvent.getRowValue();
                repay.setBook_id(repayStringCellEditEvent.getNewValue());
                String str = "UPDATE repays SET end_date = " + repay.getEnd_date() +
                        " WHERE id = " + repay.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        fnType.getItems().removeAll(fnType.getItems());
        fnType.getItems().addAll("замена", "оплата");
        fnType.getSelectionModel().select("замена");

        tableType.setCellValueFactory(new PropertyValueFactory<>("type_of_repay"));
        tableType.setCellFactory(TextFieldTableCell.forTableColumn());
        tableType.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Repay, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Repay, String> repayStringCellEditEvent) {
                Repay repay = repayStringCellEditEvent.getRowValue();
                repay.setType_of_repay(repayStringCellEditEvent.getNewValue());
                String str = "UPDATE repays set type = '" + repay.getType_of_repay() +
                        "' WHERE id = " + repay.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Repay repay = tableCat.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM repays WHERE id = " + repay.getId());
                reloadTable();
            }
        });
        tableCat.setItems(data);
    }

    public void insertInfo() {
        if (!(fnLibId.getText().equals("") || fnBookId.getText().equals(""))) {
            String type  = fnType.getSelectionModel().getSelectedItem().toString().equals("") ? "DEFAULT" : String.join("", "'", fnType.getSelectionModel().getSelectedItem().toString(), "'");
            String date = fnDate.getText().equals("") ? "DEFAULT" : String.join("",  "'", fnDate.getText(), "'");
            dbHandler.sendRequest("insert into repays(lib_card_id, book_id, type_of_repay, end_of_date) " +
                    "VALUES ('" + fnLibId.getText() + "', '" + fnBookId.getText() + "'," + type + ", " + date + ")");
            reloadTable();
        }
    }
}
