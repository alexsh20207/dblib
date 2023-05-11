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

public class TableLocationChangingsController implements Initializable {

    @FXML public Button buttonBack;
    @FXML
    public TableView<LocationChanging> tableLoc;
    @FXML public TableColumn<LocationChanging, String> lcLibID;
    @FXML public TableColumn<LocationChanging, String> lcDate;
    @FXML public TableColumn<LocationChanging, String> lucid;
    @FXML public TableColumn<LocationChanging, String> lcOldLoc;
    @FXML public TableColumn<LocationChanging, String> lcNewLoc;
    @FXML public MenuItem menuDelete;
    @FXML public TextField fnDate;
    @FXML public Button buttonAdd;
    @FXML public Button buttonReload;
    @FXML public TextField fnSearch;
    @FXML public TextField fnLibID;
    @FXML public TextField fnOldLoc;
    @FXML public TextField fnNewLoc;
    private final ObservableList<LocationChanging> data = FXCollections.observableArrayList();

    DBHandler dbHandler = new DBHandler();

    public void reloadTable() {
        tableLoc.getItems().clear();
        addInfo();
    }

    public void insertInfo() {
        if (!(fnLibID.toString().equals("")) ||
        fnNewLoc.toString().equals("") ||
        fnOldLoc.toString().equals("") ||
        fnNewLoc.toString().equals(fnOldLoc.toString())) {
            String date = fnDate.getText().equals("") ? "DEFAULT" : String.join("", "'", fnDate.getText(), "'");
            System.out.println(date);
            dbHandler.sendRequest("INSERT INTO location_changings(lib_card_id, old_location_id, new_location_id, date_of_changing)"
                    + "VALUES('" + fnLibID.getText() + "', '" +
                    fnOldLoc.getText() + "', '" + fnNewLoc.getText() + "', " + date + ")");
        }
        reloadTable();
    }

    public void returnBack() {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addInfo() {
        ResultSet locChangings = dbHandler.sendRequest("SELECT * FROM location_changings");
        try {
            while (locChangings.next()) {
                LocationChanging locationChanging = new LocationChanging(locChangings.getInt(1),
                        locChangings.getString(2),
                        locChangings.getString(3),
                        locChangings.getString(4),
                        locChangings.getString(5));
                System.out.println(locationChanging.getId());
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(locationChanging.getId()).contains(fnSearch.getText()) ||
                            locationChanging.getDate().contains(fnSearch.getText()) ||
                            locationChanging.getLib_card_id().contains(fnSearch.getText()) ||
                            locationChanging.getNew_loc_id().contains(fnSearch.getText()) ||
                            locationChanging.getOld_loc_id().contains(fnSearch.getText()))) {
                        continue;
                    }
                }
                data.add(locationChanging);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
        tableLoc.setEditable(true);
        lucid.setCellValueFactory(new PropertyValueFactory<>("id"));
        lucid.setEditable(false);

        lcLibID.setCellValueFactory(new PropertyValueFactory<>("lib_card_id"));
        lcLibID.setCellFactory(TextFieldTableCell.forTableColumn());
        lcLibID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LocationChanging, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<LocationChanging, String> locationChangingStringCellEditEvent) {
                LocationChanging locationChanging = locationChangingStringCellEditEvent.getRowValue();
                locationChanging.setLib_card_id(locationChangingStringCellEditEvent.getNewValue());
                String str = "UPDATE location_changings SET lib_card_id = '" + locationChanging.getLib_card_id() +
                        "' WHERE id = " + locationChanging.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        lcNewLoc.setCellValueFactory(new PropertyValueFactory<>("new_loc_id"));
        lcNewLoc.setCellFactory(TextFieldTableCell.forTableColumn());
        lcNewLoc.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LocationChanging, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<LocationChanging, String> locationChangingStringCellEditEvent) {
                LocationChanging locationChanging = locationChangingStringCellEditEvent.getRowValue();
                locationChanging.setLib_card_id(locationChangingStringCellEditEvent.getNewValue());
                String str = "UPDATE location_changings SET new_location_id = '" + locationChanging.getNew_loc_id() +
                        "' WHERE id = " + locationChanging.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        lcOldLoc.setCellValueFactory(new PropertyValueFactory<>("old_loc_id"));
        lcOldLoc.setCellFactory(TextFieldTableCell.forTableColumn());
        lcOldLoc.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LocationChanging, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<LocationChanging, String> locationChangingStringCellEditEvent) {
                LocationChanging locationChanging = locationChangingStringCellEditEvent.getRowValue();
                locationChanging.setLib_card_id(locationChangingStringCellEditEvent.getNewValue());
                String str = "UPDATE location_changings SET old_location_id = '" + locationChanging.getOld_loc_id() +
                        "' WHERE id = " + locationChanging.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        lcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        lcDate.setCellFactory(TextFieldTableCell.forTableColumn());
        lcDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LocationChanging, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<LocationChanging, String> locationChangingStringCellEditEvent) {
                LocationChanging locationChanging = locationChangingStringCellEditEvent.getRowValue();
                locationChanging.setLib_card_id(locationChangingStringCellEditEvent.getNewValue());
                String str = "UPDATE location_changings SET date_of_changing = '" + locationChanging.getDate() +
                        "' WHERE id = " + locationChanging.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LocationChanging locationChanging = tableLoc.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM location_changings WHERE id = " + locationChanging.getId());
                reloadTable();
            }
        });
        tableLoc.setItems(data);
    }
}
