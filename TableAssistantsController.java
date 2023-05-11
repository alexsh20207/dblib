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

public class TableAssistantsController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public TableView<Assistant> table;
    @FXML
    public TableColumn<Assistant, String > tableID;
    @FXML
    public TableColumn<Assistant, String> tableFN;
    @FXML
    public TableColumn<Assistant, String> tableLN;
    @FXML
    public TableColumn<Assistant, String> tableFromUni;
    @FXML
    public TableColumn<Assistant, String> tableJob;
    @FXML
    public MenuItem menuDelete;
    @FXML
    public TextField fnFN;
    @FXML
    public Button buttonAdd;
    @FXML
    public Button buttonReload;
    @FXML
    public TextField fnSearch;
    @FXML
    public TextField fnLN;
    @FXML
    public ComboBox fnFromUni;

    private final ObservableList<Assistant> data = FXCollections.observableArrayList();
    public TextField fnJob;
    DBHandler dbHandler = new DBHandler();

    public void returnBack() {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertInfo() {
        if (!(fnFN.getText().equals("") ||
                fnLN.getText().equals("") ||
                fnJob.getText().equals(""))) {
            String str = "INSERT INTO " +
                    "assistants_and_others (first_name, last_name, from_uni, job_title, " +
                    "id) VALUES ('" +
                    fnFN.getText() + "', '" + fnLN.getText() + "', '" +
                    fnFromUni.getSelectionModel().getSelectedItem().toString() + "', '" +
                    fnJob.getText() +  "', " +
                    "(SELECT MAX(id) + 1 FROM readers))";
            dbHandler.sendRequest(str);
            reloadTable();
        }
    }

    public void reloadTable() {
        table.getItems().clear();
        addInfo();
    }

    public void addInfo() {
        ResultSet assistants = dbHandler.sendRequest("SELECT * FROM assistants_and_others");
        try {
            while(assistants.next()) {
                Assistant assistant = new Assistant(
                        assistants.getInt(5),
                        assistants.getString(1),
                        assistants.getString(2),
                        assistants.getString(3),
                        "assistant",
                        assistants.getString(4)
                );
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(assistant.getId()).contains(fnSearch.getText()) ||
                            assistant.getFirst_name().contains(fnSearch.getText()) ||
                            assistant.getLast_name().contains(fnSearch.getText()) ||
                            assistant.getFrom_uni().contains(fnSearch.getText()) ||
                            assistant.getJob().contains(fnSearch.getText()))                    ) {
                        continue;
                    }
                }
                data.add(assistant);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
        table.setEditable(true);
        tableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableID.setEditable(false);

        fnFromUni.getItems().removeAll(fnFromUni.getItems());
        fnFromUni.getItems().addAll("t", "f");
        fnFromUni.getSelectionModel().select("t");

        tableFN.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        tableFN.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Assistant, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Assistant, String> assistantStringCellEditEvent) {
                Assistant assistant = assistantStringCellEditEvent.getRowValue();
                assistant.setFirst_name(assistantStringCellEditEvent.getNewValue());
                String str = "UPDATE assistants_and_others set first_name = '" +
                        assistant.getFirst_name() + "' WHERE id = " + assistant.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLN.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        tableLN.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Assistant, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Assistant, String> assistantStringCellEditEvent) {
                Assistant assistant = assistantStringCellEditEvent.getRowValue();
                assistant.setLast_name(assistantStringCellEditEvent.getNewValue());
                String str = "UPDATE assistants_and_others set last_name = '" +
                        assistant.getLast_name() + "' WHERE id = " + assistant.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableFromUni.setCellValueFactory(new PropertyValueFactory<>("from_uni"));
        tableFromUni.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFromUni.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Assistant, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Assistant, String> assistantStringCellEditEvent) {
                Assistant assistant = assistantStringCellEditEvent.getRowValue();
                assistant.setFrom_uni(assistantStringCellEditEvent.getNewValue());
                if (assistant.getFrom_uni().equals("t") || assistant.getFrom_uni().equals("f")) {
                    String str = "UPDATE assistants_and_others set from_uni = '" +
                            assistant.getFrom_uni() + "' WHERE id = " + assistant.getId();
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        tableJob.setCellValueFactory(new PropertyValueFactory<>("job"));
        tableJob.setCellFactory(TextFieldTableCell.forTableColumn());
        tableJob.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Assistant, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Assistant, String> assistantStringCellEditEvent) {
                Assistant assistant = assistantStringCellEditEvent.getRowValue();
                assistant.setJob(assistantStringCellEditEvent.getNewValue());
                String str = "UPDATE assistants_and_others set job_title = '" +
                        assistant.getJob() + "' WHERE id = " + assistant.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Assistant assistant =  table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM assistants_and_others WHERE id = " + assistant.getId());
                reloadTable();
            }
        });
        table.setItems(data);
    }
}
