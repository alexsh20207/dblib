package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Request4Controller implements Initializable {
    @FXML public Button buttonReturnBack;
    @FXML public ComboBox fnLoc;
    @FXML public TextField fnLocVal;
    @FXML public Button buttonReload;
    @FXML public TableColumn<Request4, String> tableBookID;
    @FXML public TableView<Request4> table;
    @FXML public TableColumn<Request4, String> tableBookName;
    @FXML public TextField fnPropValue;
    @FXML public ComboBox fnProperty;
    private final ObservableList<Request4> data = FXCollections.observableArrayList();
    public TableColumn<Request4, String> tableBookType;
    public Text textCount;
    DBHandler dbHandler = new DBHandler();
    int countNew, countDead;
    public void returnBack() {
        try {
            App.setRoot("selectRequests");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void reloadTable() {
        table.getItems().clear();
        if (!fnLocVal.getText().equals("") || fnLoc.getSelectionModel().getSelectedItem().toString().equals("во всей библиотеке")) {
            addInfo();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fnLoc.getItems().removeAll(fnLoc.getItems());
        fnLoc.getItems().addAll("во всей библиотеке", "в определённом абонементе");
        fnLoc.getSelectionModel().select("во всей библиотеке");

        fnProperty.getItems().removeAll(fnProperty.getItems());
        fnProperty.getItems().addAll("без признака","автор", "год", "издание");
        fnProperty.getSelectionModel().select("без признака");
        if (!fnLocVal.getText().equals("") || fnLoc.getSelectionModel().getSelectedItem().toString().equals("во всей библиотеке")) {
            addInfo();
        }
        table.setEditable(false);
        tableBookID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableBookID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableBookName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableBookName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableBookType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tableBookType.setCellFactory(TextFieldTableCell.forTableColumn());
        table.setItems(data);
    }

    public void addInfo() {
        String str = "SELECT books.id, books.name, books.status from books\n" +
                "where books.date_of_adding > current_date - 365";
        switch (fnProperty.getSelectionModel().getSelectedItem().toString()) {
            case "без признака": {
                break;
            }
            case "год": {
                str = String.join("", str, " and EXTRACT(YEAR from books.date_of_adding) = ", fnPropValue.getText());
                break;
            }
            case "автор": {
                str = String.join("", str, " and books.author = '", fnPropValue.getText(), "'");
                break;
            }
            case "издание": {
                str = String.join("", str, " and books.edition = ", fnPropValue.getText());
                break;
            }
        }
        if (fnLoc.getSelectionModel().getSelectedItem().equals("в определённом абонементе") &&
        !fnLocVal.getText().equals("")) {
            str = String.join("", str, " and books.location_id = ", fnLocVal.getText());
        }
        System.out.println(str);
        ResultSet books = dbHandler.sendRequest(str);
        countNew = 0;
        countDead = 0;
        try {
            while (books.next()) {
                Request4 request4 = new Request4(books.getString(1),
                        books.getString(2),
                        books.getString(3));
                if (!request4.getType().equals("испорчен") ||
                request4.getType().equals("утерян")) {
                    request4.setType("Поступлена");
                    countNew++;
                }
                else {
                    request4.setType("Утеряна");
                    countDead++;
                }
                data.add(request4);
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
        textCount.setText("Поступившие книги: " + countNew + "\n" +
                "Утерянные книги: " + countDead);
    }
}
