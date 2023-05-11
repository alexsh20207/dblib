package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class SelectTablesController {
    @FXML
    public Button buttonBack;
    @FXML
    public Button buttonReaders;
    @FXML
    public Button buttonStudents;
    @FXML
    public Button buttonApplicants;
    @FXML
    public Button buttonAssistantsAndOthers;
    @FXML
    public Button buttonFPK;
    @FXML
    public Button buttonGraduates;
    @FXML
    public Button buttonPO;
    @FXML
    public Button buttonTrainees;
    @FXML
    public Button buttonTeachers;
    @FXML
    public Button buttonBooks;
    @FXML
    public Button buttonCategories;
    @FXML
    public Button buttonBooCat;
    @FXML
    public Button buttonLibCards;
    @FXML
    public Button buttonOperations;
    @FXML
    public Button buttonRuleBreakers;
    @FXML
    public Button buttonBelongings;
    @FXML
    public Button buttonFines;
    @FXML
    public Button buttonLocations;
    @FXML
    public Button buttonChangeLoc;
    @FXML
    public Button buttonRepays;

    public void setRoot(String fxml) {
        try {
            App.setRoot(fxml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void returnBack() {
        setRoot("selectButtons");
    }
    public void showReaders() {setRoot("tableReaders");};
    public void showStudents() {setRoot("tableStudents");};
    public void showApplicants() {setRoot("tableApplicants");};
    public void showAssistantsAndOthers() {setRoot("tableAssistants");};
    public void showFPK() {setRoot("tableFPK");};
    public void showGraduates() {setRoot("tableGraduates");};
    public void showPO() {setRoot("tablePO");};
    public void showTeachers() {setRoot("tableTeachers");};
    public void showTrainees() {setRoot("tableTrainees");};
    public void showBooks() {setRoot("tableBooks");};
    public void showCategories() {setRoot("tableCategories");};
    public void showBooCat() {setRoot("tableBooksInCategories");};
    public void showLibCards() {setRoot("tableLibCards");};
    public void showOperations() {setRoot("tableOperations");};
    public void showRuleBreakers() {setRoot("tableRuleBreakers");};
    public void showFines() {setRoot("tableFines");};
    public void showBelongings() {setRoot("tableBelongings");};
    public void showLocations() {setRoot("tableLocations");};
    public void showChangeLoc() {setRoot("tableLocationChangings");};
    public void showRepays() {setRoot("tableRepays");};
}
