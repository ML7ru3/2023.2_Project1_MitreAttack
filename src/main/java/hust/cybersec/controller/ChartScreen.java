package hust.cybersec.controller;

import hust.cybersec.conversion.DataProcessing;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ChartScreen {

    static DataProcessing data;

    public static void launchScreen() throws IOException {
        data = new DataProcessing();
        final String SCREEN_FXML_FILE_PATH = "/View/Chart.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();

        URL location = ChartScreen.class.getResource(SCREEN_FXML_FILE_PATH);
        fxmlLoader.setLocation(location);

        ChartController chartController = new ChartController(data);
        fxmlLoader.setController(chartController);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("ATOMIC ANALYZER");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
