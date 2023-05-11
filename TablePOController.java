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

public class TablePOController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public TableView<PO> table;
    @FXML
    public TableColumn<PO, String > tableID;
    @FXML
    public TableColumn<PO, String> tableFN;
    @FXML
    public TableColumn<PO, String> tableLN;
    @FXML
    public TableColumn<PO, String> tableFromUni;
    @FXML
    public TableColumn<PO, String> tableDepartment;
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

    private final ObservableList<PO> data = FXCollections.observableArrayList();
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
                    "\"listenersPO\" (first_name, last_name, from_uni, department_name, " +
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
        ResultSet pos = dbHandler.sendRequest("SELECT * FROM \"listenersPO\"");
        try {
            while(pos.next()) {
                PO po = new PO(
                        pos.getInt(5),
                        pos.getString(1),
                        pos.getString(2),
                        pos.getString(3),
                        "listener_po",
                        pos.getString(4)
                );
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(po.getId()).contains(fnSearch.getText()) ||
                            po.getFirst_name().contains(fnSearch.getText()) ||
                            po.getLast_name().contains(fnSearch.getText()) ||
                            po.getFrom_uni().contains(fnSearch.getText()) ||
                            po.getDepartment().contains(fnSearch.getText()))                    ) {
                        continue;
                    }
                }
                data.add(po);
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
        tableFN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PO, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PO, String> POStringCellEditEvent) {
                PO po = POStringCellEditEvent.getRowValue();
                po.setFirst_name(POStringCellEditEvent.getNewValue());
                String str = "UPDATE \"listenersPO\" set first_name = '" +
                        po.getFirst_name() + "' WHERE id = " + po.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLN.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        tableLN.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PO, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PO, String> POStringCellEditEvent) {
                PO po = POStringCellEditEvent.getRowValue();
                po.setLast_name(POStringCellEditEvent.getNewValue());
                String str = "UPDATE \"listenersPO\" set last_name = '" +
                        po.getLast_name() + "' WHERE id = " + po.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableFromUni.setCellValueFactory(new PropertyValueFactory<>("from_uni"));
        tableFromUni.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFromUni.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PO, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PO, String> POStringCellEditEvent) {
                PO po = POStringCellEditEvent.getRowValue();
                po.setFrom_uni(POStringCellEditEvent.getNewValue());
                if (po.getFrom_uni().equals("t") || po.getFrom_uni().equals("f")) {
                    String str = "UPDATE \"listenersPO\" set from_uni = '" +
                            po.getFrom_uni() + "' WHERE id = " + po.getId();
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        tableDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        tableDepartment.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDepartment.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<PO, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<PO, String> POStringCellEditEvent) {
                PO po = POStringCellEditEvent.getRowValue();
                po.setDepartment(POStringCellEditEvent.getNewValue());
                String str = "UPDATE \"listenersPO\" set department_name = '" +
                        po.getDepartment() + "' WHERE id = " + po.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                PO po = table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM \"listenersPO\" WHERE id = " + po.getId());
                reloadTable();
            }
        });
        table.setItems(data);
    }
}
