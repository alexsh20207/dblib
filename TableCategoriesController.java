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

public class TableCategoriesController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public TableView<BookCategory> tableCat;
    @FXML
    public TableColumn<BookCategory, String> catID;
    @FXML
    public TableColumn<BookCategory, String> catName;
    @FXML
    public TextField fnName;
    @FXML
    public Button buttonAdd;

    private final ObservableList<BookCategory> data = FXCollections.observableArrayList();
    @FXML
    public Button buttonReload;
    @FXML
    public MenuItem menuDelete;
    @FXML
    public TextField fnSearch;

    DBHandler dbHandler = new DBHandler();

    public void returnBack() {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    public void reloadTable() {
        tableCat.getItems().clear();
        addInfo();
    }

    public void insertInfo() {
        if (!fnName.toString().equals("")) {
            dbHandler.sendRequest("INSERT INTO catalogs(name) VALUES ('" + fnName.getText() + "');");
            reloadTable();
        }
    }

    public void addInfo() {
        ResultSet categories = dbHandler.sendRequest("SELECT * FROM catalogs");
        try {
            while (categories.next()) {
                BookCategory bookCategory = new BookCategory(categories.getInt(1),
                        categories.getString(2));
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(bookCategory.getId()).contains(fnSearch.getText()) ||
                            bookCategory.getName().contains(fnSearch.getText()))) {
                        continue;
                    }
                }
                data.add(bookCategory);
            }
        }
        catch (SQLException e) {
                throw new RuntimeException(e);
            }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
        tableCat.setEditable(true);
        catID.setCellValueFactory(new PropertyValueFactory<>("id"));
        catID.setEditable(false);
        catName.setCellValueFactory(new PropertyValueFactory<>("name"));
        catName.setCellFactory(TextFieldTableCell.forTableColumn());
        catName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BookCategory, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BookCategory, String> bookCategoryStringCellEditEvent) {
                BookCategory bookCategory = bookCategoryStringCellEditEvent.getRowValue();
                bookCategory.setName(bookCategoryStringCellEditEvent.getNewValue());
                String str = "UPDATE catalogs SET name = '" + bookCategory.getName() +
                        "' WHERE id = " + bookCategory.getId();
                System.out.println(str);
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                BookCategory bookCategory = tableCat.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM catalogs WHERE id = " + bookCategory.getId());
                reloadTable();
            }
        });
        tableCat.setItems(data);
    }
}
