package hust.cybersec.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Screen extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        final String SCREEN_FXML = "/View/Screen.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(SCREEN_FXML));
        ScreenController controller = new ScreenController();
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Project 1");
        primaryStage.show();

    }

    public static void main() {
        launch();
    }

}
