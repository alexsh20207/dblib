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

public class TableFinesController implements Initializable {
    @FXML public Button buttonBack;
    @FXML public TableView<Fine> tableCat;
    @FXML public TableColumn<Fine, String > tableID;
    @FXML public TableColumn<Fine, String> tableLibID;
    @FXML public TableColumn<Fine, String> tableFine;
    @FXML public MenuItem menuDelete;
    @FXML public TextField fnLibID;
    @FXML public Button buttonAdd;
    @FXML public Button buttonReload;
    @FXML public TextField fnSearch;
    @FXML public TextField fnFine;

    private final ObservableList<Fine> data  = FXCollections.observableArrayList();
    DBHandler dbHandler = new DBHandler();


    public void returnBack() {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    public void reloadTable() {
        tableCat.getItems().clear();
        addInfo();
    }

    public void addInfo() {
        ResultSet fines = dbHandler.sendRequest("SELECT * FROM fines");
        try {
            while (fines.next()) {
                Fine fine = new Fine(fines.getInt(1),
                        fines.getString(2),
                        fines.getString(3));
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(fine.getId()).contains(fnSearch.getText()) ||
                            fine.getFine().contains(fnSearch.getText()) ||
                            fine.getLib_card_id().contains(fnSearch.getText()))) {
                        continue;
                    }
                }
                data.add(fine);
            }
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
        tableCat.setEditable(true);
        tableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableID.setEditable(false);
        tableFine.setCellValueFactory(new PropertyValueFactory<>("fine"));
        tableFine.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFine.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Fine, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Fine, String> fineStringCellEditEvent) {
                Fine fine = fineStringCellEditEvent.getRowValue();
                fine.setFine(fineStringCellEditEvent.getNewValue());
                String str = "UPDATE fines SET price = " + fine.getFine() +
                        "WHERE id = " + fine.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLibID.setCellValueFactory(new PropertyValueFactory<>("fine"));
        tableLibID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLibID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Fine, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Fine, String> fineStringCellEditEvent) {
                Fine fine = fineStringCellEditEvent.getRowValue();
                fine.setLib_card_id(fineStringCellEditEvent.getNewValue());
                String str = "UPDATE fines SET libcard_id = " + fine.getLib_card_id() +
                        "WHERE id = " + fine.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Fine fine = tableCat.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM fines WHERE id = " + fine.getId());
                reloadTable();
            }
        });
        tableCat.setItems(data);
    }

    public void insertInfo() {
        if (!(fnLibID.getText().equals("") || fnFine.getText().equals(""))) {
            dbHandler.sendRequest("INSERT INTO fines(libcard_id, price) VALUES(" +
                    fnLibID.getText() + ", " + fnFine.getText() + ")");
            reloadTable();
        }
    }
}
