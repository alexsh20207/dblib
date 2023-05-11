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

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class TableLTeachersController implements Initializable {
    @FXML
    public Button buttonBack;
    @FXML
    public TableView<Teacher> table;
    @FXML
    public TableColumn<Teacher, String > tableID;
    @FXML
    public TableColumn<Teacher, String> tableFN;
    @FXML
    public TableColumn<Teacher, String> tableLN;
    @FXML
    public TableColumn<Teacher, String> tableFromUni;
    @FXML
    public TableColumn<Teacher, String> tableCafedra;
    @FXML
    public TableColumn<Teacher, String> tableRank;
    @FXML
    public TableColumn<Teacher,String> tableDegree;
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
    @FXML
    public TextField fnRank;
    @FXML
    public TextField fnDegree;

    private final ObservableList<Teacher> data = FXCollections.observableArrayList();
    public TextField fnCafedra;
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
                fnCafedra.getText().equals("") ||
                fnDegree.getText().equals("") ||
                fnRank.getText().equals(""))) {
            String str = "INSERT INTO " +
                    "teachers (first_name, last_name, from_uni, department_name, " +
                    "degree, rank, id) VALUES ('" +
                    fnFN.getText() + "', '" + fnLN.getText() + "', '" +
                    fnFromUni.getSelectionModel().getSelectedItem().toString() + "', '" +
                    fnCafedra.getText() + "', '" + fnDegree.getText() +
                    "', '" + fnRank.getText() + "', " +
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
        ResultSet teachers = dbHandler.sendRequest("SELECT * FROM teachers");
        try {
            while(teachers.next()) {
                Teacher teacher = new Teacher(
                        teachers.getInt(7),
                        teachers.getString(1),
                        teachers.getString(2),
                        teachers.getString(3),
                        "teacher",
                        teachers.getString(4),
                        teachers.getString(6),
                        teachers.getString(5)
                );
                if (!fnSearch.getText().equals("")) {
                    if (!(String.valueOf(teacher.getId()).contains(fnSearch.getText()) ||
                    teacher.getFirst_name().contains(fnSearch.getText()) ||
                    teacher.getLast_name().contains(fnSearch.getText()) ||
                    teacher.getFrom_uni().contains(fnSearch.getText()) ||
                    teacher.getCafedra().contains(fnSearch.getText()) ||
                    teacher.getRank().contains(fnSearch.getText()) ||
                    teacher.getDegree().contains(fnSearch.getText()))
                    ) {
                        continue;
                    }
                }
                data.add(teacher);
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
        tableFN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Teacher, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Teacher, String> teacherStringCellEditEvent) {
                Teacher teacher = teacherStringCellEditEvent.getRowValue();
                teacher.setFirst_name(teacherStringCellEditEvent.getNewValue());
                String str = "UPDATE teachers set first_name = '" +
                        teacher.getFirst_name() + "' WHERE id = " + teacher.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableLN.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        tableLN.setCellFactory(TextFieldTableCell.forTableColumn());
        tableLN.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Teacher, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Teacher, String> teacherStringCellEditEvent) {
                Teacher teacher = teacherStringCellEditEvent.getRowValue();
                teacher.setLast_name(teacherStringCellEditEvent.getNewValue());
                String str = "UPDATE teachers set last_name = '" +
                        teacher.getLast_name() + "' WHERE id = " + teacher.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableFromUni.setCellValueFactory(new PropertyValueFactory<>("from_uni"));
        tableFromUni.setCellFactory(TextFieldTableCell.forTableColumn());
        tableFromUni.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Teacher, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Teacher, String> teacherStringCellEditEvent) {
                Teacher teacher = teacherStringCellEditEvent.getRowValue();
                teacher.setFrom_uni(teacherStringCellEditEvent.getNewValue());
                if (teacher.getFrom_uni().equals("t") || teacher.getFrom_uni().equals("f")) {
                    String str = "UPDATE teachers set from_uni = '" +
                            teacher.getFrom_uni() + "' WHERE id = " + teacher.getId();
                    dbHandler.sendRequest(str);
                    reloadTable();
                }
            }
        });
        tableCafedra.setCellValueFactory(new PropertyValueFactory<>("cafedra"));
        tableCafedra.setCellFactory(TextFieldTableCell.forTableColumn());
        tableCafedra.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Teacher, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Teacher, String> teacherStringCellEditEvent) {
                Teacher teacher = teacherStringCellEditEvent.getRowValue();
                teacher.setCafedra(teacherStringCellEditEvent.getNewValue());
                String str = "UPDATE teachers set department_name = '" +
                        teacher.getCafedra() + "' WHERE id = " + teacher.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableRank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        tableRank.setCellFactory(TextFieldTableCell.forTableColumn());
        tableRank.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Teacher, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Teacher, String> teacherStringCellEditEvent) {
                Teacher teacher = teacherStringCellEditEvent.getRowValue();
                teacher.setRank(teacherStringCellEditEvent.getNewValue());
                String str = "UPDATE teachers set rank = '" +
                        teacher.getRank() + "' WHERE id = " + teacher.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        tableDegree.setCellValueFactory(new PropertyValueFactory<>("degree"));
        tableDegree.setCellFactory(TextFieldTableCell.forTableColumn());
        tableDegree.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Teacher, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Teacher, String> teacherStringCellEditEvent) {
                Teacher teacher = teacherStringCellEditEvent.getRowValue();
                teacher.setDegree(teacherStringCellEditEvent.getNewValue());
                String str = "UPDATE teachers set degree = '" +
                        teacher.getDegree() + "' WHERE id = " + teacher.getId();
                dbHandler.sendRequest(str);
                reloadTable();
            }
        });
        menuDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Teacher teacher = table.getSelectionModel().getSelectedItem();
                dbHandler.sendRequest("DELETE FROM teachers WHERE id = " + teacher.getId());
                reloadTable();
            }
        });
        table.setItems(data);
    }
}
