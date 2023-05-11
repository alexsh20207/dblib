package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class SelectButtonsController {
    @FXML
    public Button buttonTable;
    @FXML
    public Button buttonRequest;
    @FXML public Button buttonAdmin;
    @FXML public Button buttonUser;

    public void showTables(ActionEvent actionEvent) {
        try {
            App.setRoot("selectTables");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showRequests(ActionEvent actionEvent) {
        try {
            App.setRoot("selectRequests");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
     public void logAsUser(ActionEvent actionEvent) {
         try {
             App.setRoot("userPage");
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }
     public void logAsAdmin(ActionEvent actionEvent) {
         try {
             App.setRoot("adminPage");
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }
}
