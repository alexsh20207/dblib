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

public class TableLocationsController implements Initializable {

    @FXML
    public Button buttonBack;
    @FXML
    public TableView<Location> tableLoc;
    @FXML
    public TableColumn<Location, String> locID;
    @FXML
    public TableColumn<Location, String> locAddress;
    @FXML
    public TableColumn<Location, String> locType;
    @FXML
    public MenuItem menuDelete;
    @FXML
    public TextField fnAddress;
    @FXML
    public Button buttonAdd;
    @FXML
    public Button buttonReload;
    @FXML
    public TextField fnSearch;
    @FXML
    public ComboBox fnType;
    private final ObservableList<Location> data = FXCollections.observableArrayList();

    DBHandler dbHandler = new DBHandler();
    public void reloadTable() {
        tableLoc.getItems().clear();
        addInfo();
    }
    public void insertInfo() {
        if (!fnAddress.toString().equals("")) {
            dbHandler.sendRequest("INSERT INTO locations(address, type) VALUES ('" +
                    fnAddress.getText() + "','" +
                    fnType.getSelectionModel().getSelectedItem().toString() +
                    "');");
            reloadTable();
        }
    }

    public void returnBack() {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    public void addInfo() {
        ResultSet locations = dbHandler.sendRequest("SELECT * FROM locations");
        try {
            while (locations.next()) {
                Location location = new Location(locations.getInt(1),
                        locations.getString(2), locations.getString(3));
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(location.getId()).contains(fnSearch.getText()) ||
                            location.getAddress().contains(fnSearch.getText()) ||
                            location.getType().contains(fnSearch.getText()))) {
                        continue;
                    }
                }
                data.add(location);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
        tableLoc.setEditable(true);
        locID.setCellValueFactory(new PropertyValueFactory<>("id"));
        locID.setEditable(false);

        fnType.getItems().removeAll(fnType.getItems());
        fnType.getItems().addAll("абонемент", "читальный зал");
        fnType.getSelectionModel().select("абонемент");

        locAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        locAddress.setCellFactory(TextFieldTableCell.forTableColumn());
        locAddress.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Location, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Location, String> locationStringCellEditEvent) {
                Location location = locationStringCellEditEvent.getRowValue();
                location.setAddress(locationStringCellEditEvent.getNewValue());
                String str = "UPDATE catalogs SET address = '" + location.getAddress() +
                        "' WHERE id = " + location.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        locType.setCellValueFactory(new PropertyValueFactory<>("type"));
        locType.setCellFactory(TextFieldTableCell.forTableColumn());
        locType.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Location, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Location, String> locationStringCellEditEvent) {
                Location location = locationStringCellEditEvent.getRowValue();
                location.setType(locationStringCellEditEvent.getNewValue());
                if (location.getType().equals("абонемент") || location.getType().equals("читальный зал")) {
                    String str = "UPDATE locations SET type = '" + location.getType() +
                            "' WHERE id = " + location.getId();
                    System.out.println(str);
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Location location = tableLoc.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM locations WHERE id = " + location.getId());
                reloadTable();
            }
        });
        tableLoc.setItems(data);
    }
}
