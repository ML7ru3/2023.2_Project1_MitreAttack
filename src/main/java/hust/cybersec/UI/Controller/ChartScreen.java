package hust.cybersec.UI.Controller;

import hust.cybersec.conversion.DataProcessing;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChartScreen extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final String SCREEN_FXML_FILE_PATH = "src/main/java/hust/cybersec/UI/view/MITRE_ATT&CK_chart.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(SCREEN_FXML_FILE_PATH));
        ChartController chartController = new ChartController(new DataProcessing());
        fxmlLoader.setController(chartController);
        Parent root = fxmlLoader.load();


        Scene scene = new Scene(root);
        primaryStage.setTitle("ATOMIC ANALYZER");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchScreen() {
        launch();
    }
}
