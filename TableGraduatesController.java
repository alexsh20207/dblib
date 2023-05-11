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

public class TableGraduatesController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public TableView<Graduate> table;
    @FXML
    public TableColumn<Graduate, String > tableID;
    @FXML
    public TableColumn<Graduate, String> tableFN;
    @FXML
    public TableColumn<Graduate, String> tableLN;
    @FXML
    public TableColumn<Graduate, String> tableFromUni;
    @FXML
    public TableColumn<Graduate, String> tableDepartment;
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

    private final ObservableList<Graduate> data = FXCollections.observableArrayList();
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
                    "graduates (first_name, last_name, from_uni, department_name, " +
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
        ResultSet graduates = dbHandler.sendRequest("SELECT * FROM graduates");
        try {
            while(graduates.next()) {
                Graduate graduate = new Graduate(
                        graduates.getInt(5),
                        graduates.getString(1),
                        graduates.getString(2),
                        graduates.getString(3),
                        "graduate",
                        graduates.getString(4)
                );
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(graduate.getId()).contains(fnSearch.getText()) ||
                            graduate.getFirst_name().contains(fnSearch.getText()) ||
                            graduate.getLast_name().contains(fnSearch.getText()) ||
                            graduate.getFrom_uni().contains(fnSearch.getText()) ||
                            graduate.getDepartment().contains(fnSearch.getText()))                    ) {
                        continue;
                    }
                }
                data.add(graduate);
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
        tableFN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Graduate, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Graduate, String> graduateStringCellEditEvent) {
                Graduate graduate = graduateStringCellEditEvent.getRowValue();
                graduate.setFirst_name(graduateStringCellEditEvent.getNewValue());
                String str = "UPDATE graduates set first_name = '" +
                        graduate.getFirst_name() + "' WHERE id = " + graduate.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLN.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        tableLN.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Graduate, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Graduate, String> GraduateStringCellEditEvent) {
                Graduate graduate = GraduateStringCellEditEvent.getRowValue();
                graduate.setLast_name(GraduateStringCellEditEvent.getNewValue());
                String str = "UPDATE graduates set last_name = '" +
                        graduate.getLast_name() + "' WHERE id = " + graduate.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableFromUni.setCellValueFactory(new PropertyValueFactory<>("from_uni"));
        tableFromUni.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFromUni.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Graduate, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Graduate, String> graduateStringCellEditEvent) {
                Graduate graduate = graduateStringCellEditEvent.getRowValue();
                graduate.setFrom_uni(graduateStringCellEditEvent.getNewValue());
                if (graduate.getFrom_uni().equals("t") || graduate.getFrom_uni().equals("f")) {
                    String str = "UPDATE graduates set from_uni = '" +
                            graduate.getFrom_uni() + "' WHERE id = " + graduate.getId();
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        tableDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        tableDepartment.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDepartment.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Graduate, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Graduate, String> graduateStringCellEditEvent) {
                Graduate graduate = graduateStringCellEditEvent.getRowValue();
                graduate.setDepartment(graduateStringCellEditEvent.getNewValue());
                String str = "UPDATE graduates set department_name = '" +
                        graduate.getDepartment() + "' WHERE id = " + graduate.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Graduate graduate = table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM graduates WHERE id = " + graduate.getId());
                reloadTable();
            }
        });
        table.setItems(data);
    }
}
