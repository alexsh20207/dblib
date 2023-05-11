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

public class TableReadersController implements Initializable {
    @FXML
    public TableView<Reader> reader_table;
    @FXML
    public TableColumn<Reader, String>reader_id;
    @FXML
    public TableColumn<Reader, String > reader_ln;
    @FXML
    public TableColumn<Reader, String> reader_fn;
    @FXML
    public TableColumn<Reader, String > reader_from_uni;
    @FXML
    public TableColumn<Reader, String> reader_type;
    @FXML
    public MenuItem menuDelete;

    private final ObservableList<Reader> data = FXCollections.observableArrayList();
    @FXML
    public Button buttonBack;
    @FXML
    public Button buttonReload;
    @FXML
    public TextField fnSearch;
    DBHandler dbHandler = new DBHandler();

    public void returnBack() {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void reloadTable() {
        reader_table.getItems().clear();
        addInfo();
    }

    public void addInfo() {
        ResultSet readers = dbHandler.sendRequest("SELECT * FROM readers;");
        try {
            while (readers.next()) {
                Reader reader = new Reader(
                        readers.getInt(4),
                        readers.getString(1),
                        readers.getString(2),
                        readers.getString(3),
                        readers.getString(5)
                );
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(reader.getId()).contains(fnSearch.getText()) ||
                            reader.getFirst_name().contains(fnSearch.getText()) ||
                            reader.getLast_name().contains(fnSearch.getText()) ||
                            reader.getFrom_uni().contains(fnSearch.getText()) ||
                            reader.getType().contains(fnSearch.getText())
                    )) {
                        continue;
                    }
                }
                data.add(reader);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
        reader_table.setEditable(true);
        reader_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        reader_id.setEditable(false);
        reader_type.setEditable(true);
        reader_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        reader_type.setEditable(false);

        reader_fn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        reader_fn.setCellFactory(TextFieldTableCell.forTableColumn());
        reader_fn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Reader, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Reader, String> readerStringCellEditEvent) {
                Reader reader = readerStringCellEditEvent.getRowValue();
                reader.setFirst_name(readerStringCellEditEvent.getNewValue());
                String str = "UPDATE readers set first_name = '" +
                        reader.getFirst_name() + "' WHERE id = " + reader.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        reader_ln.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        reader_ln.setCellFactory(TextFieldTableCell.forTableColumn());
        reader_ln.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Reader, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Reader, String> readerStringCellEditEvent) {
                Reader reader = readerStringCellEditEvent.getRowValue();
                reader.setFirst_name(readerStringCellEditEvent.getNewValue());
                String str = "UPDATE readers set last_name = '" +
                        reader.getLast_name() + "' WHERE id = " + reader.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        reader_from_uni.setCellValueFactory(new PropertyValueFactory<>("from_uni"));
        reader_from_uni.setCellFactory(TextFieldTableCell.forTableColumn());
        reader_from_uni.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Reader, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Reader, String> readerStringCellEditEvent) {
                Reader reader = readerStringCellEditEvent.getRowValue();
                reader.setFirst_name(readerStringCellEditEvent.getNewValue());
                if (reader.getFrom_uni().equals("t") || reader.getFrom_uni().equals("f")) {
                    String str = "UPDATE readers set from_uni = '" +
                            reader.getFrom_uni() + "' WHERE id = " + reader.getId();
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Reader reader = reader_table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM readers WHERE id = " + reader.getId());
                reloadTable();
            }
        });
        reader_table.setItems(data);
    }
}
