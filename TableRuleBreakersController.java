package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.Rule;
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

public class TableRuleBreakersController implements Initializable {
    @FXML public Button buttonBack;
    @FXML public TableView<RuleBreaker> tableCat;
    @FXML public TableColumn<RuleBreaker, String> tableID;
    @FXML public TableColumn<RuleBreaker, String> tableLibID;
    @FXML public TableColumn<RuleBreaker, String> tableDate;
    @FXML public TableColumn<RuleBreaker, String> tableReason;
    @FXML public MenuItem menuDelete;
    @FXML public TextField fnLibId;
    @FXML public Button buttonAdd;
    @FXML public Button buttonReload;
    @FXML public TextField fnSearch;
    @FXML public TextField fnReason;
    @FXML public TextField fnDate;

    private final ObservableList<RuleBreaker> data  = FXCollections.observableArrayList();
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
        ResultSet ruleBreakers = dbHandler.sendRequest("SELECT * FROM rule_breakers");
        try {
            while (ruleBreakers.next()) {
               RuleBreaker ruleBreaker = new RuleBreaker(ruleBreakers.getInt(1),
                       ruleBreakers.getString(2),
                       ruleBreakers.getString(3),
                       ruleBreakers.getString(4));
               if (!fnSearch.getText().equals("")) {
                   if (!(String.valueOf(ruleBreaker.getId()).contains(fnSearch.getText()) ||
                           ruleBreaker.getEnd_date().contains(fnSearch.getText()) ||
                           ruleBreaker.getReason().contains(fnSearch.getText()) ||
                           ruleBreaker.getLib_card_id().contains(fnSearch.getText()))) {
                       continue;
                   }
               }
               data.add(ruleBreaker);
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
        tableLibID.setCellValueFactory(new PropertyValueFactory<>("lib_card_id"));
        tableLibID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLibID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RuleBreaker, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<RuleBreaker, String> ruleBreakerStringCellEditEvent) {
                RuleBreaker ruleBreaker = ruleBreakerStringCellEditEvent.getRowValue();
                ruleBreaker.setLib_card_id(ruleBreakerStringCellEditEvent.getNewValue());
                String str = "UPDATE rule_breakers SET lib_card_id = " + ruleBreaker.getLib_card_id() +
                        " WHERE id = " + ruleBreaker.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableReason.setCellValueFactory(new PropertyValueFactory<>("reason"));
        tableReason.setCellFactory(TextFieldTableCell.forTableColumn());
        tableReason.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RuleBreaker, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<RuleBreaker, String> ruleBreakerStringCellEditEvent) {
                RuleBreaker ruleBreaker = ruleBreakerStringCellEditEvent.getRowValue();
                ruleBreaker.setReason(ruleBreakerStringCellEditEvent.getNewValue());
                String str = "UPDATE rule_breakers SET reason = " + ruleBreaker.getReason() +
                        " WHERE id = " + ruleBreaker.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableDate.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        tableDate.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<RuleBreaker, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<RuleBreaker, String> ruleBreakerStringCellEditEvent) {
                RuleBreaker ruleBreaker = ruleBreakerStringCellEditEvent.getRowValue();
                ruleBreaker.setEnd_date(ruleBreakerStringCellEditEvent.getNewValue());
                String str = "UPDATE rule_breakers SET end_date = " + ruleBreaker.getEnd_date() +
                        " WHERE id = " + ruleBreaker.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                RuleBreaker ruleBreaker = tableCat.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM rule_breakers WHERE id = " + ruleBreaker.getId());
                reloadTable();
            }
        });
        tableCat.setItems(data);
    }
    public void insertInfo() {
        if (!(fnLibId.getText().equals("") || fnReason.getText().equals(""))) {
            String date = fnDate.getText().equals("") ? "DEFAULT" : String.join("",  "'", fnDate.getText(), "'");
            dbHandler.sendRequest("insert into rule_breakers(lib_card_id, reason, end_date) " +
                    "VALUES ('" + fnLibId.getText() + "', '" + fnReason.getText() + "' , " + date + ")");
            reloadTable();
            System.out.println(fnLibId.getText());
        }
    }
}
