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

public class TableFPKController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public TableView<FPK> table;
    @FXML
    public TableColumn<FPK, String > tableID;
    @FXML
    public TableColumn<FPK, String> tableFN;
    @FXML
    public TableColumn<FPK, String> tableLN;
    @FXML
    public TableColumn<FPK, String> tableFromUni;
    @FXML
    public TableColumn<FPK, String> tableDepartment;
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

    private final ObservableList<FPK> data = FXCollections.observableArrayList();
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
                    "fpk (first_name, last_name, from_uni, department_name, " +
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
        ResultSet fpks = dbHandler.sendRequest("SELECT * FROM fpk");
        try {
            while(fpks.next()) {
                FPK fpk = new FPK(
                        fpks.getInt(5),
                        fpks.getString(1),
                        fpks.getString(2),
                        fpks.getString(3),
                        "fpk",
                        fpks.getString(4)
                );
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(fpk.getId()).contains(fnSearch.getText()) ||
                            fpk.getFirst_name().contains(fnSearch.getText()) ||
                            fpk.getLast_name().contains(fnSearch.getText()) ||
                            fpk.getFrom_uni().contains(fnSearch.getText()) ||
                            fpk.getDepartment().contains(fnSearch.getText()))                    ) {
                        continue;
                    }
                }
                data.add(fpk);
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
        tableFN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<FPK, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<FPK, String> FPKStringCellEditEvent) {
                FPK fpk = FPKStringCellEditEvent.getRowValue();
                fpk.setFirst_name(FPKStringCellEditEvent.getNewValue());
                String str = "UPDATE fpk set first_name = '" +
                        fpk.getFirst_name() + "' WHERE id = " + fpk.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLN.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        tableLN.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<FPK, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<FPK, String> FPKStringCellEditEvent) {
                FPK fpk = FPKStringCellEditEvent.getRowValue();
                fpk.setLast_name(FPKStringCellEditEvent.getNewValue());
                String str = "UPDATE fpk set last_name = '" +
                        fpk.getLast_name() + "' WHERE id = " + fpk.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableFromUni.setCellValueFactory(new PropertyValueFactory<>("from_uni"));
        tableFromUni.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFromUni.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<FPK, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<FPK, String> FPKStringCellEditEvent) {
                FPK fpk = FPKStringCellEditEvent.getRowValue();
                fpk.setFrom_uni(FPKStringCellEditEvent.getNewValue());
                if (fpk.getFrom_uni().equals("t") || fpk.getFrom_uni().equals("f")) {
                    String str = "UPDATE fpk set from_uni = '" +
                            fpk.getFrom_uni() + "' WHERE id = " + fpk.getId();
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        tableDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        tableDepartment.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDepartment.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<FPK, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<FPK, String> FPKStringCellEditEvent) {
                FPK fpk = FPKStringCellEditEvent.getRowValue();
                fpk.setDepartment(FPKStringCellEditEvent.getNewValue());
                String str = "UPDATE fpk set department_name = '" +
                        fpk.getDepartment() + "' WHERE id = " + fpk.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FPK fpk = table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM fpk WHERE id = " + fpk.getId());
                reloadTable();
            }
        });
        table.setItems(data);
    }
}
