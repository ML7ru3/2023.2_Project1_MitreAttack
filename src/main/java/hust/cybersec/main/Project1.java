package hust.cybersec.main;

import hust.cybersec.controller.ScreenController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Project1 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ScreenController.showScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
