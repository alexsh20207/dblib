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

public class TableTraineesController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public TableView<Trainee> table;
    @FXML
    public TableColumn<Trainee, String > tableID;
    @FXML
    public TableColumn<Trainee, String> tableFN;
    @FXML
    public TableColumn<Trainee, String> tableLN;
    @FXML
    public TableColumn<Trainee, String> tableFromUni;
    @FXML
    public TableColumn<Trainee, String> tableJob;
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

    private final ObservableList<Trainee> data = FXCollections.observableArrayList();
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
                    "trainees (first_name, last_name, from_uni, job_title, " +
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
        ResultSet trainees = dbHandler.sendRequest("SELECT * FROM trainees");
        try {
            while(trainees.next()) {
                Trainee trainee = new Trainee(
                        trainees.getInt(5),
                        trainees.getString(1),
                        trainees.getString(2),
                        trainees.getString(3),
                        "trainee",
                        trainees.getString(4)
                );
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(trainee.getId()).contains(fnSearch.getText()) ||
                            trainee.getFirst_name().contains(fnSearch.getText()) ||
                            trainee.getLast_name().contains(fnSearch.getText()) ||
                            trainee.getFrom_uni().contains(fnSearch.getText()) ||
                            trainee.getJob().contains(fnSearch.getText()))                    ) {
                        continue;
                    }
                }
                data.add(trainee);
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
        tableFN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Trainee, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Trainee, String> traineeStringCellEditEvent) {
                Trainee trainee = traineeStringCellEditEvent.getRowValue();
                trainee.setFirst_name(traineeStringCellEditEvent.getNewValue());
                String str = "UPDATE trainees set first_name = '" +
                        trainee.getFirst_name() + "' WHERE id = " + trainee.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLN.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        tableLN.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Trainee, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Trainee, String> traineeStringCellEditEvent) {
                Trainee trainee = traineeStringCellEditEvent.getRowValue();
                trainee.setLast_name(traineeStringCellEditEvent.getNewValue());
                String str = "UPDATE trainees set last_name = '" +
                        trainee.getLast_name() + "' WHERE id = " + trainee.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableFromUni.setCellValueFactory(new PropertyValueFactory<>("from_uni"));
        tableFromUni.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFromUni.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Trainee, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Trainee, String> traineeStringCellEditEvent) {
                Trainee trainee = traineeStringCellEditEvent.getRowValue();
                trainee.setFrom_uni(traineeStringCellEditEvent.getNewValue());
                if (trainee.getFrom_uni().equals("t") || trainee.getFrom_uni().equals("f")) {
                    String str = "UPDATE trainees set from_uni = '" +
                            trainee.getFrom_uni() + "' WHERE id = " + trainee.getId();
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        tableJob.setCellValueFactory(new PropertyValueFactory<>("job"));
        tableJob.setCellFactory(TextFieldTableCell.forTableColumn());
        tableJob.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Trainee, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Trainee, String> traineeStringCellEditEvent) {
                Trainee trainee = traineeStringCellEditEvent.getRowValue();
                trainee.setJob(traineeStringCellEditEvent.getNewValue());
                String str = "UPDATE trainees set job_title = '" +
                        trainee.getJob() + "' WHERE id = " + trainee.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Trainee trainee =  table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM trainees WHERE id = " + trainee.getId());
                reloadTable();
            }
        });
        table.setItems(data);
    }
}
