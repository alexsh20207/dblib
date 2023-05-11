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

public class TableBooksInCategoriesController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML public TableView<BookInCategory> tableCat;
    @FXML public TableColumn<BookInCategory, String> tableID;
    @FXML public TableColumn<BookInCategory, String> tableBookId;
    @FXML public TableColumn<BookInCategory, String > tableCategoryID;
    @FXML public MenuItem menuDelete;
    @FXML public TextField fnBook;
    @FXML public Button buttonAdd;
    @FXML public Button buttonReload;
    @FXML public TextField fnSearch;
    @FXML public TextField fnCategory;

    private final ObservableList<BookInCategory> data  = FXCollections.observableArrayList();
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

    public void addInfo() {
        ResultSet bics = dbHandler.sendRequest("SELECT * FROM books_in_catalogs");
        try {
            while (bics.next()) {
                BookInCategory bookInCategory = new BookInCategory(bics.getInt(1),
                        bics.getString(2),
                        bics.getString(3));
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(bookInCategory.getId()).contains(fnSearch.getText()) ||
                            bookInCategory.getBook_id().contains(fnSearch.getText()) ||
                            bookInCategory.getCategory_id().contains(fnSearch.getText()))) {
                        continue;
                    }
                }
                data.add(bookInCategory);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
        tableCat.setEditable(true);
        tableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableID.setEditable(false);
        tableBookId.setCellValueFactory(new PropertyValueFactory<>("book_id"));
        tableBookId.setCellFactory(TextFieldTableCell.forTableColumn());
        tableBookId.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BookInCategory, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BookInCategory, String> bookInCategoryStringCellEditEvent) {
                BookInCategory bookInCategory = bookInCategoryStringCellEditEvent.getRowValue();
                bookInCategory.setBook_id(bookInCategoryStringCellEditEvent.getNewValue());
                String str = "UPDATE books_in_catalogs SET book_id = " + bookInCategory.getBook_id() +
                        " WHERE id = " + bookInCategory.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableCategoryID.setCellValueFactory(new PropertyValueFactory<>("category_id"));
        tableCategoryID.setCellFactory(TextFieldTableCell.forTableColumn());
        tableCategoryID.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<BookInCategory, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<BookInCategory, String> bookInCategoryStringCellEditEvent) {
                BookInCategory bookInCategory = bookInCategoryStringCellEditEvent.getRowValue();
                bookInCategory.setBook_id(bookInCategoryStringCellEditEvent.getNewValue());
                String str = "UPDATE books_in_catalogs SET catalog_id = " + bookInCategory.getCategory_id() +
                        " WHERE id = " + bookInCategory.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                BookInCategory bookInCategory = tableCat.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM books_in_catalogs WHERE id = " + bookInCategory.getId());
                reloadTable();
            }
        });
        tableCat.setItems(data);
    }

    public void insertInfo() {
        if (!(fnBook.toString().equals("") || fnCategory.toString().equals(""))) {
            dbHandler.sendRequest("INSERT INTO books_in_catalogs(book_id, catalog_id) VALUES ('" + fnBook.getText() + "', '" + fnCategory.getText() +"');");
            reloadTable();
        }
    }
}
