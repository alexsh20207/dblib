package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DbTestController implements Initializable {
    @FXML
    public TableColumn<Reader, String> reader_id;
    @FXML
    public TableView<Reader> reader_table;
    @FXML
    public TableColumn<Reader, String> reader_fn;
    @FXML
    public TableColumn<Reader, String> reader_ln;
    @FXML
    public TableColumn<Reader, Boolean> reader_from_uni;
    @FXML
    public TableColumn<Reader, String> reader_type;

    private final ObservableList<Reader> data = FXCollections.observableArrayList();
    @FXML
    public TextField fnField;
    @FXML
    public TextField lnField;
    @FXML
    public ComboBox fromuniField;
    @FXML
    public ComboBox<String> typeField;
    @FXML
    public Button addButton;

    DataBaseHandler dataBaseHandler = new DataBaseHandler();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfAboutReader();
        fromuniField.getItems().removeAll(fromuniField.getItems());
        fromuniField.getItems().addAll("true", "false");
        fromuniField.getSelectionModel().select("true");

        typeField.getItems().removeAll(typeField.getItems());
        typeField.getItems().addAll("applicant", "assistant and other", "fpk", "graduate", "student", "teacher", "trainee");
        typeField.getSelectionModel().select("student");

        addButton.setOnAction(actionEvent -> {
                    Reader reader = new Reader(0, fnField.getText(), lnField.getText(),
                            fromuniField.getSelectionModel().getSelectedItem().toString(),
                            typeField.getSelectionModel().getSelectedItem());
                    data.add(reader);
                });

        reader_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        reader_fn.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        reader_ln.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        reader_from_uni.setCellValueFactory(new PropertyValueFactory<>("from_uni"));
        reader_type.setCellValueFactory((new PropertyValueFactory<>("type")));
        reader_table.setItems(data);
    }

    private void addInfAboutReader() {
        ResultSet readers = dataBaseHandler.getReaders();
        try {
            while (readers.next()) {
                Reader reader = new Reader(readers.getInt(4),
                        readers.getString(1),
                        readers.getString(2),
                        readers.getString(3),
                        readers.getString(5));
                data.add(reader);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
