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

public class TableOperationsController implements Initializable {
    @FXML public Button buttonBack;
    @FXML public TableView<Operation> tableCat;
    @FXML public TableColumn<Operation, String> tableLibID;
    @FXML public TableColumn<Operation, String> tableID;
    @FXML public TableColumn<Operation, String> tableLocID;
    @FXML public TableColumn<Operation, String> tableBookID;
    @FXML public TableColumn<Operation, String> tableStatus;
    @FXML public TableColumn<Operation, String> tableDate;
    @FXML public MenuItem menuDelete;
    @FXML public TextField fnLibID;
    @FXML public Button buttonAdd;
    @FXML public Button buttonReload;
    @FXML public TextField fnLocID;
    @FXML public TextField fnSearch;
    @FXML public TextField fnBookID;
    @FXML public TextField fnDate;
    @FXML public ComboBox fnStatus;
    private final ObservableList<Operation> data = FXCollections.observableArrayList();
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
        ResultSet operations = dbHandler.sendRequest("SELECT * FROM requests");
        try {
            while (operations.next()) {
                Operation operation = new Operation(operations.getInt(1),
                        operations.getString(2),
                        operations.getString(3),
                        operations.getString(4),
                        operations.getString(5),
                        operations.getString(6));
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(operation.getId()).contains(fnSearch.getText()) ||
                            operation.getDate().contains(fnSearch.getText()) ||
                            operation.getBook_id().contains(fnSearch.getText()) ||
                            operation.getStatus().contains(fnSearch.getText()) ||
                            operation.getLib_card_id().contains(fnSearch.getText()) ||
                            operation.getLoc_id().contains(fnSearch.getText()))) {
                        continue;
                    }
                }
                data.add(operation);
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

        fnStatus.getItems().removeAll(fnStatus.getItems());
        fnStatus.getItems().addAll("отправлено на ожидание", "выдано клиенту", "возвращено в библиотеку", "возмещён ущерб");
        fnStatus.getSelectionModel().select("отправлено на ожидание");

        tableDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableDate.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Operation, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Operation, String> operationStringCellEditEvent) {
                Operation operation = operationStringCellEditEvent.getRowValue();
                operation.setDate(operationStringCellEditEvent.getNewValue());
                String str = "UPDATE requests set date = '" + operation.getDate() +
                        " where id = " + operation.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLibID.setCellValueFactory(new PropertyValueFactory<>("lib_card_id"));
        tableLibID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLibID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Operation, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Operation, String> operationStringCellEditEvent) {
                Operation operation = operationStringCellEditEvent.getRowValue();
                operation.setLib_card_id(operationStringCellEditEvent.getNewValue());
                String str = "UPDATE requests set lib_card_id = '" + operation.getLib_card_id() +
                        " where id = " + operation.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLocID.setCellValueFactory(new PropertyValueFactory<>("loc_id"));
        tableLocID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLocID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Operation, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Operation, String> operationStringCellEditEvent) {
                Operation operation = operationStringCellEditEvent.getRowValue();
                operation.setLoc_id(operationStringCellEditEvent.getNewValue());
                String str = "UPDATE requests set location_id = '" + operation.getLoc_id() +
                        " where id = " + operation.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        tableStatus.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Operation, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Operation, String> operationStringCellEditEvent) {
                Operation operation = operationStringCellEditEvent.getRowValue();
                operation.setStatus(operationStringCellEditEvent.getNewValue());
                String str = "UPDATE requests set status = '" + operation.getStatus() +
                        " where id = " + operation.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableBookID.setCellValueFactory(new PropertyValueFactory<>("book_id"));
        tableBookID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableBookID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Operation, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Operation, String> operationStringCellEditEvent) {
                Operation operation = operationStringCellEditEvent.getRowValue();
                operation.setBook_id(operationStringCellEditEvent.getNewValue());
                String str = "UPDATE requests set book_id = '" + operation.getBook_id() +
                        " where id = " + operation.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Operation operation = tableCat.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM requests WHERE id = " + operation.getId());
                reloadTable();
            }
        });
        tableCat.setItems(data);
    }

    public void insertInfo() {
        if (!(fnLibID.getText().equals("")) ||
        fnBookID.getText().equals("") ||
        fnLocID.getText().equals("")) {
            String date = fnDate.getText().equals("") ? "DEFAULT ": String.join("", "'", fnDate.getText(), "'");
            String str = "INSERT INTO requests(lib_card_id, book_id, location_id, status, date) VALUES('" +
                    fnLibID.getText() + "', '" + fnBookID.getText() + "' , '" + fnLocID.getText() +
                    "' , '" + fnStatus.getSelectionModel().getSelectedItem().toString() + "', " + date + ")";
            dbHandler.sendRequest(str);
            reloadTable();
        }
    }
}
