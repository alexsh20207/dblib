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

public class TableLibCardsController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public Button buttonReload;
    @FXML
    public TableView<LibCard> table;
    @FXML
    public TableColumn<LibCard, String> tableID;
    @FXML
    public TableColumn<LibCard, String> tableStartDate;
    @FXML
    public TableColumn<LibCard, String> tableAbonAllowed;
    @FXML
    public TableColumn<LibCard, String> tableReader;
    @FXML
    public TableColumn<LibCard, String> tableLoc;
    @FXML
    public TableColumn<LibCard, String> tableLastReg;
    @FXML
    public Button buttonAdd;
    @FXML
    public TextField fnStartDate;
    @FXML
    public TextField fnReader;
    @FXML
    public TextField fnLoc;
    @FXML
    public TextField fnLastReg;
    @FXML
    public TextField fnSearch;
    @FXML
    MenuItem menuDelete;

    private final ObservableList<LibCard> data = FXCollections.observableArrayList();
    DBHandler dbHandler = new DBHandler();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
        table.setEditable(true);

        tableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableID.setEditable(false);

        tableAbonAllowed.setCellValueFactory(new PropertyValueFactory<>("is_abonement_allowed"));
        tableAbonAllowed.setEditable(false);
        tableStartDate.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        tableStartDate.setEditable(false);

        tableReader.setCellValueFactory(new PropertyValueFactory<>("reader_id"));
        tableReader.setCellFactory(TextFieldTableCell.forTableColumn());
        tableReader.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LibCard, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<LibCard, String> libCardStringCellEditEvent) {
                LibCard libCard = libCardStringCellEditEvent.getRowValue();
                libCard.setReader_id(libCardStringCellEditEvent.getNewValue());
                String str = "UPDATE lib_cards set reader_id = '" +
                        libCard.getReader_id() + "' WHERE id = " + libCard.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLoc.setCellValueFactory(new PropertyValueFactory<>("location_id"));
        tableLoc.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLoc.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LibCard, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<LibCard, String> libCardStringCellEditEvent) {
                LibCard libCard = libCardStringCellEditEvent.getRowValue();
                libCard.setLocation_id(libCardStringCellEditEvent.getNewValue());
                String str = "UPDATE lib_cards set location_id = '" +
                        libCard.getLocation_id() + "' WHERE id = " + libCard.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLastReg.setCellValueFactory(new PropertyValueFactory<>("last_registration"));
        tableLastReg.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLastReg.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<LibCard, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<LibCard, String> libCardStringCellEditEvent) {
                LibCard libCard = libCardStringCellEditEvent.getRowValue();
                libCard.setLast_registration(libCardStringCellEditEvent.getNewValue());
                String str = "UPDATE lib_cards set last_registration = '" +
                        libCard.getLast_registration() + "' WHERE id = " + libCard.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                LibCard libCard = table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM lib_cards WHERE id = " + libCard.getId());
                reloadTable();
            }
        });
        table.setItems(data);
    }

    public void returnBack() {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertInfo() {
        if (!(fnReader.getText().equals("") ||
                fnLoc.getText().equals(""))) {
            String startDate = fnStartDate.getText().equals("") ? "DEFAULT" : fnStartDate.getText();
            String lastRegDate = fnLastReg.getText().equals("") ? "DEFAULT" : fnLastReg.getText();
            String isAbolAllowed = isAllowed(fnReader.getText()) ? "t" : "f";

            String str = "INSERT INTO lib_cards(start_date, is_abonement_allowed, " +
                    "reader_id, location_id, last_registration) VALUES ('" +
                    startDate + "', " + isAbolAllowed + "', '" + fnReader.getText() + "', " +
                    fnLoc.getText() + "', " + lastRegDate;

            dbHandler.sendRequest(str);
            reloadTable();
        }

    }

    private boolean isAllowed(String readerId) {
        ResultSet reader = dbHandler.sendRequest("SELECT * FROM readers WHERE id = " + readerId);
        try {
            String type = reader.getString(5);
            if (type.equals("fpk") ||
                    type.equals("trainee") ||
                    type.equals("applicant")) {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void reloadTable() {
        table.getItems().clear();
        addInfo();
    }

    public void addInfo() {
        ResultSet libCards = dbHandler.sendRequest("SELECT * FROM lib_cards");
        try {
            while (libCards.next()) {
                LibCard libCard = new LibCard(
                        libCards.getInt(1),
                        libCards.getString(2),
                        libCards.getString(4),
                        libCards.getString(5),
                        libCards.getString(6),
                        libCards.getString(3)
                );
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(libCard.getId()).contains(fnSearch.getText()) ||
                            libCard.getStart_date().contains(fnSearch.getText()) ||
                            libCard.getIs_abonement_allowed().contains(fnSearch.getText()) ||
                            libCard.getReader_id().contains(fnSearch.getText()) ||
                            libCard.getLocation_id().contains(fnSearch.getText()) ||
                            libCard.getLast_registration().contains(fnSearch.getText()))) {
                        continue;
                    }
                }
                data.add(libCard);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
