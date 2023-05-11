package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class SelectRequestsController {
    @FXML
    public Button buttonBack;
    @FXML
    public Button buttonR1;
    @FXML
    public Button buttonR2;
    @FXML
    public Button buttonR3;
    @FXML
    public Button buttonR4;
    @FXML
    public Button buttonR5;
    @FXML
    public Button buttonR6;
    @FXML
    public Button buttonR7;
    @FXML
    public Button buttonR8;
    @FXML
    public Button buttonR9;
    @FXML
    public Button buttonR10;
    @FXML
    public Button buttonR11;
    @FXML
    public Button buttonR12;
    @FXML
    public Button buttonR13;

    public void setRoot(String fxml) {
        try {
            App.setRoot(fxml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void returnBack() {setRoot("selectButtons");}
    public void loadR1() {setRoot("request1");};
    public void loadR2() {setRoot("request2");};
    public void loadR3() {setRoot("request3");};
    public void loadR4() {setRoot("request4");};
    public void loadR5() {setRoot("request5");};
    public void loadR6() {setRoot("request6");};
    public void loadR7() {setRoot("request7");};
    public void loadR8() {setRoot("request8");};
    public void loadR9() {setRoot("request9");};
    public void loadR10() {setRoot("request10");};
    public void loadR11() {setRoot("request11");};
    public void loadR12() {setRoot("request12");};
    public void loadR13() {setRoot("request13");};

}
