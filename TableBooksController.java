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

public class TableBooksController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public TableView<Book> table;
    @FXML
    public TableColumn<Book, String> tableID;
    @FXML
    public TableColumn<Book, String> tableName;
    @FXML
    public TableColumn<Book, String> tableAuthor;
    @FXML
    public TableColumn<Book, String> tablePrice;
    @FXML
    public TableColumn<Book, String> tableEdition;
    @FXML
    public TableColumn<Book, String> tableDate;
    @FXML
    public TableColumn<Book, String> tableStatus;
    @FXML
    public TableColumn<Book, String> tableLoc;
    @FXML
    public MenuItem menuDelete;
    @FXML
    public TextField fnName;
    @FXML
    public Button buttonAdd;
    @FXML
    public Button buttonReload;
    @FXML
    public TextField fnSearch;
    @FXML
    public TextField fnPrice;
    @FXML
    public TextField fnAuthor;
    @FXML
    public TextField fnEdition;
    @FXML
    public TextField fnDate;
    @FXML
    public TextField fnLoc;
    @FXML
    public ComboBox fnStatus;
    private final ObservableList<Book> data = FXCollections.observableArrayList();
    DBHandler dbHandler = new DBHandler();

    public void returnBack() {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void insertInfo() {
        if (!(fnName.getText().equals("") ||
                fnAuthor.getText().equals("") ||
                fnPrice.getText().equals("") ||
                !fnPrice.getText().chars().allMatch(Character::isDigit) ||
                fnLoc.getText().equals("") ||
                !fnLoc.getText().chars().allMatch(Character::isDigit))) {
            String date = fnDate.getText().equals("") ? "DEFAULT" : String.join("", "'", fnDate.getText(), "'");
            String edition = fnEdition.getText().equals("") ? "DEFAULT" : String.join("", "'" , fnEdition.getText(),  "'");
            String str = "INSERT INTO books(name, author, price, date_of_adding, status, edition, location_id) " +
                    "VALUES ('" + fnName.getText() + "', '" + fnAuthor.getText() + "', '" +
                    fnPrice.getText() + "' , " + date+ " , '" +
                    fnStatus.getSelectionModel().getSelectedItem().toString() + "' ," +
                    edition + ", '" + fnLoc.getText() + "')";
            dbHandler.sendRequest(str);
            System.out.println(str);
            reloadTable();
        }
    }

    public void reloadTable() {
        table.getItems().clear();
        addInfo();
    }

    public void addInfo() {
        ResultSet books = dbHandler.sendRequest("SELECT * FROM books");
        try {
            while (books.next()) {
                Book book = new Book(books.getInt(1),
                        books.getString(2),
                        books.getString(3),
                        books.getString(4),
                        books.getString(5),
                        books.getString(6),
                        books.getString(7),
                        books.getString(8));
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(book.getId()).contains(fnSearch.getText()) ||
                            book.getName().contains(fnSearch.getText()) ||
                            book.getAuthor().contains(fnSearch.getText()) ||
                            book.getDate_of_adding().contains(fnSearch.getText()) ||
                            book.getEdition().contains(fnSearch.getText()) ||
                            book.getStatus().contains(fnSearch.getText()) ||
                            book.getLocation_id().contains(fnSearch.getText()) ||
                            book.getPrice().contains(fnSearch.getText()))) {
                        continue;
                    }

                }
                data.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();

        fnStatus.getItems().removeAll(fnStatus.getItems());
        fnStatus.getItems().addAll("в библиотеке", "ожидание выдачи", "выдан", "испорчен");
        fnStatus.getSelectionModel().select("в библиотеке");

        table.setEditable(true);
        tableID.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableID.setEditable(false);
        tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Book, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Book, String> bookStringCellEditEvent) {
                Book book = bookStringCellEditEvent.getRowValue();
                book.setName(bookStringCellEditEvent.getNewValue());
                String str = "UPDATE books set name = '" +
                        book.getName() + "' WHERE id = " + book.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        tableAuthor.setCellFactory(TextFieldTableCell.forTableColumn());
        tableAuthor.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Book, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Book, String> bookStringCellEditEvent) {
                Book book = bookStringCellEditEvent.getRowValue();
                book.setAuthor(bookStringCellEditEvent.getNewValue());
                String str = "UPDATE books set author = '" +
                        book.getAuthor() + "' WHERE id = " + book.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
        tableEdition.setCellFactory(TextFieldTableCell.forTableColumn());
        tableEdition.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Book, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Book, String> bookStringCellEditEvent) {
                Book book = bookStringCellEditEvent.getRowValue();
                book.setEdition(bookStringCellEditEvent.getNewValue());
                String str = "UPDATE books set edition = '" +
                        book.getEdition() + "' WHERE id = " + book.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tablePrice.setCellFactory(TextFieldTableCell.forTableColumn());
        tablePrice.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Book, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Book, String> bookStringCellEditEvent) {
                Book book = bookStringCellEditEvent.getRowValue();
                book.setPrice(bookStringCellEditEvent.getNewValue());
                String str = "UPDATE books set price = '" +
                        book.getPrice() + "' WHERE id = " + book.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableStatus.setCellFactory(TextFieldTableCell.forTableColumn());
        tableStatus.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Book, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Book, String> bookStringCellEditEvent) {
                Book book = bookStringCellEditEvent.getRowValue();
                book.setStatus(bookStringCellEditEvent.getNewValue());
                String str = "UPDATE books set status = '" +
                        book.getStatus() + "' WHERE id = " + book.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableDate.setCellValueFactory(new PropertyValueFactory<>("date_of_adding"));
        tableDate.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Book, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Book, String> bookStringCellEditEvent) {
                Book book = bookStringCellEditEvent.getRowValue();
                book.setDate_of_adding(bookStringCellEditEvent.getNewValue());
                String str = "UPDATE books set date_of_adding = '" +
                        book.getDate_of_adding() + "' WHERE id = " + book.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLoc.setCellValueFactory(new PropertyValueFactory<>("location_id"));
        tableLoc.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLoc.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Book, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Book, String> bookStringCellEditEvent) {
                Book book = bookStringCellEditEvent.getRowValue();
                book.setLocation_id(bookStringCellEditEvent.getNewValue());
                String str = "UPDATE books set location_id = '" +
                        book.getLocation_id() + "' WHERE id = " + book.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Book book = table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM books WHERE id = " + book.getId());
                reloadTable();
            }
        });
        table.setItems(data);
    }

}
