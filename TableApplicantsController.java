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

public class TableApplicantsController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public TableView<Applicant> table;
    @FXML
    public TableColumn<Applicant, String > tableID;
    @FXML
    public TableColumn<Applicant, String> tableFN;
    @FXML
    public TableColumn<Applicant, String> tableLN;
    @FXML
    public TableColumn<Applicant, String> tableFromUni;
    @FXML
    public TableColumn<Applicant, String> tableDepartment;
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

    private final ObservableList<Applicant> data = FXCollections.observableArrayList();
    public TextField fnDepartment;
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
                fnDepartment.getText().equals(""))) {
            String str = "INSERT INTO " +
                    "applicants (first_name, last_name, from_uni, department_name, " +
                    "id) VALUES ('" +
                    fnFN.getText() + "', '" + fnLN.getText() + "', '" +
                    fnFromUni.getSelectionModel().getSelectedItem().toString() + "', '" +
                    fnDepartment.getText() +  "', " +
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
        ResultSet applicants = dbHandler.sendRequest("SELECT * FROM applicants");
        try {
            while(applicants.next()) {
                Applicant applicant = new Applicant(
                        applicants.getInt(5),
                        applicants.getString(1),
                        applicants.getString(2),
                        applicants.getString(3),
                        "applicant",
                        applicants.getString(4)
                );
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(applicant.getId()).contains(fnSearch.getText()) ||
                            applicant.getFirst_name().contains(fnSearch.getText()) ||
                            applicant.getLast_name().contains(fnSearch.getText()) ||
                            applicant.getFrom_uni().contains(fnSearch.getText()) ||
                            applicant.getDepartment().contains(fnSearch.getText()))                    ) {
                        continue;
                    }
                }
                data.add(applicant);
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
        tableFN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Applicant, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Applicant, String> applicantStringCellEditEvent) {
                Applicant applicant = applicantStringCellEditEvent.getRowValue();
                applicant.setFirst_name(applicantStringCellEditEvent.getNewValue());
                String str = "UPDATE applicants set first_name = '" +
                        applicant.getFirst_name() + "' WHERE id = " + applicant.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLN.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        tableLN.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Applicant, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Applicant, String> applicantStringCellEditEvent) {
                Applicant applicant = applicantStringCellEditEvent.getRowValue();
                applicant.setLast_name(applicantStringCellEditEvent.getNewValue());
                String str = "UPDATE applicants set last_name = '" +
                        applicant.getLast_name() + "' WHERE id = " + applicant.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableFromUni.setCellValueFactory(new PropertyValueFactory<>("from_uni"));
        tableFromUni.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFromUni.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Applicant, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Applicant, String> applicantStringCellEditEvent) {
                Applicant applicant = applicantStringCellEditEvent.getRowValue();
                applicant.setFrom_uni(applicantStringCellEditEvent.getNewValue());
                if (applicant.getFrom_uni().equals("t") || applicant.getFrom_uni().equals("f")) {
                    String str = "UPDATE applicants set from_uni = '" +
                            applicant.getFrom_uni() + "' WHERE id = " + applicant.getId();
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        tableDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        tableDepartment.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDepartment.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Applicant, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Applicant, String> applicantStringCellEditEvent) {
                Applicant applicant = applicantStringCellEditEvent.getRowValue();
                applicant.setDepartment(applicantStringCellEditEvent.getNewValue());
                String str = "UPDATE applicants set department_name = '" +
                    applicant.getDepartment() + "' WHERE id = " + applicant.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Applicant applicant = table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM applicants WHERE id = " + applicant.getId());
                reloadTable();
            }
        });
        table.setItems(data);
    }
}
