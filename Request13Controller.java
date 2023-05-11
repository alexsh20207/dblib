package org.example;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Request13Controller implements Initializable
{
    @FXML public Button buttonReturnBack;
    @FXML public Button buttonReload;
    @FXML public TextField fnPropVal;
    @FXML public Text text;

    DBHandler dbHandler = new DBHandler();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addInfo();
    }

    public void returnBack() {
        try {
            App.setRoot("selectRequests");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void reloadTable() {
        addInfo();
    }

    private void addInfo() {
        if (fnPropVal.getText().equals("")) {
            text.setText("Введите ID");
            return;
        }
        String where = "\nWHERE lib_cards.id = " + fnPropVal.getText();
        String str = "SELECT * FROM lib_cards";
        ResultSet res = dbHandler.sendRequest(str + where);
        try {
            if (!res.next()) {
                text.setText("Нет читателя с таким ID");
                return;
            }
            text.setText("LibCardID - " + res.getString(1));
            str = "SELECT * FROM readers inner join lib_cards" +
                    " on readers.id = lib_cards.reader_id";
            res = dbHandler.sendRequest(str + where);
            res.next();
            String type = "";
            switch (res.getString(5)) {
                case "student": {
                    type = "студент";
                    break;
                }
                case "teacher" : {
                    type = "преподаватель";
                    break;
                }
                case "trainee" : {
                    type = "стажёр";
                    break;
                }
                case "listener PO": {
                    type = "слушатель ПО";
                    break;
                }
                case "graduate" : {
                    type = "выпускник";
                    break;
                }
                case "fpk" : {
                    type = "ФПК";
                    break;
                }
                case "assistand and others": {
                    type = "ассистент";
                    break;
                }
                case "applicant" : {
                    type = "абитуриент";
                    break;
                }
            }
            text.setText(text.getText() + "\nИмя - " + res.getString(1) +
                    "\nФамилия - " + res.getString(2) +
                    "\nТип читателя - " + type);
            str = "SELECT belongings.libcard_id, b.name \n" +
                    "from belongings inner join books b on b.id = belongings.book_id\n" +
                    "inner join lib_cards on belongings.libcard_id = lib_cards.id";
            res = dbHandler.sendRequest(str + where);
            int bookNum = 0;
            text.setText(text.getText() + "\nКниги на руках: ");
            String books = "";
            while (res.next()) {
                bookNum++;
                books = String.join("", books, "\t" ,res.getString(2) + "\n");
            }
            text.setText(text.getText() + bookNum + "\n");
            if (bookNum != 0) {
                text.setText(text.getText() + books);
            }
            str = "SELECT price FROM fines inner join lib_cards on lib_cards.id = fines.libcard_id";
            res = dbHandler.sendRequest(str + where);
            text.setText(text.getText() + "\nШтрафы: ");
            String fines = "";
            int fineNum = 0;
            while (res.next()) {
                fineNum++;
                fines = String.join("", fines, "\t" ,res.getString(1) + "\n");
            }
            text.setText(text.getText() + fineNum + "\n");
            if (fineNum != 0) {
                text.setText(text.getText() + fines);
            }
            str = "SELECT l1.address, l2.address, date_of_changing FROM location_changings inner join lib_cards\n" +
                    "    on location_changings.lib_card_id = lib_cards.id\n" +
                    "inner join locations l1 on old_location_id = l1.id\n" +
                    "inner join locations l2 on new_location_id = l2.id";
            res = dbHandler.sendRequest(str + where);
            text.setText(text.getText() + "\nСколько раз менял место выдачи: ");
            String locs = "";
            int locNum = 0;
            while (res.next()) {
                locNum++;
                locs = String.join("", locs, "\t" , res.getString(1) +
                        " - " +  res.getString(2) + " - "+
                        res.getString(3) + "\n");
            }
            text.setText(text.getText() + locNum + "\n");
            if (locNum != 0) {
                text.setText(text.getText() + locs);
            }
            str = "SELECT locations.address \n" +
                    "from lib_cards inner join locations on locations.id = lib_cards.location_id";
            res = dbHandler.sendRequest(str + where);
            res.next();
            text.setText(text.getText() + "Зарегестрирован по месту выдачи" + res.getString(1) + "\n");

            str = "SELECT last_registation from lib_cards";
            res = dbHandler.sendRequest(str + where);
            res.next();
            text.setText(text.getText() + "Дата регистрации на месте - " + res.getString(1) + "\n");

            str = "SELECT start_date from lib_cards";
            res = dbHandler.sendRequest(str + where);
            res.next();
            text.setText(text.getText() + "Дата регистрации в бибилиотеке - " + res.getString(1) + "\n");

            str = "SELECT books.name, type_of_repay, end_of_date from repays inner join books on repays.book_id = books.id\n" +
                    "inner join lib_cards on repays.lib_card_id = lib_cards.id";
            res = dbHandler.sendRequest(str + where);
            text.setText(text.getText() + "\nСколько нужно возместить ущерба за книги: ");
            String bookRepays = "";
            int brNum = 0;
            while (res.next()) {
                brNum++;
                bookRepays = String.join("", bookRepays, "\t" , res.getString(1) +
                        " - " +  res.getString(2) + " - "+
                        res.getString(3) + "\n");
            }
            text.setText(text.getText() + brNum + "\n");
            if (brNum != 0) {
                text.setText(text.getText() + bookRepays);
            }

            str = "SELECT reason, end_date FROM rule_breakers inner join lib_cards on lib_cards.id = rule_breakers.lib_card_id";
            res = dbHandler.sendRequest(str + where);
            text.setText(text.getText() + "\nСколько нарушений правил: ");
            String rules = "";
            int ruleNum = 0;
            while (res.next()) {
                ruleNum++;
                rules = String.join("", rules, "\t" , res.getString(1) +
                        " - " +  res.getString(2) + "\n");
            }
            text.setText(text.getText() + ruleNum + "\n");
            if (ruleNum != 0) {
                text.setText(text.getText() + rules);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
