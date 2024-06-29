package hust.cybersec.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DownloadingController extends ScreenController implements Initializable {

    @FXML
    private Label label;

    public Label getLabel() {
        return label;
    }

    public void setLabel(String text) {
        this.label.setText(this.label.getText() + "\n" + text);
    }


}
