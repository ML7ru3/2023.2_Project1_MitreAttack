package hust.cybersec.controller;


import hust.cybersec.conversion.DataProcessing;
import hust.cybersec.excel.ExportExcel;
import hust.cybersec.model.AtomicRedTeam;
import hust.cybersec.model.MitreAttackFramework;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;


public class ScreenController implements Initializable {

    private DataProcessing data;
    ExportExcel excelExporter;
    DownloadingController downloadingController;
    ChartController chartController;

    MitreAttackFramework maf;
    AtomicRedTeam art;

    @FXML
    void updateData(ActionEvent event) throws IOException {
        final String DOWNLOADING_FXML = "/View/Downloading.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(DOWNLOADING_FXML));
        loader.setController(downloadingController);
        Parent root = loader.load();
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Downloading Data...");
        stage.show();

        Task<Void> downloadTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    art.download(downloadingController);
                    maf.download(downloadingController);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    // Update UI on success
                    stage.setTitle("Data Downloaded");
                    try {
                        showScreen();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    // Update UI on failure
                    stage.setTitle("Download Failed");
                });
            }
        };

        new Thread(downloadTask).start();

    }

    @FXML
    void exportExcell(ActionEvent event) throws IOException {
        excelExporter.export();
    }

    @FXML
    void viewChart(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
            chartController.showChart();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void exitPressed(ActionEvent event) {
        Platform.exit();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chartController = new ChartController(data);
        try {
            data = new DataProcessing();
        } catch (IOException e) {
            System.err.println("An error occurred while reading the data file");
        }
        excelExporter = new ExportExcel(data);
        downloadingController = new DownloadingController();
        art = new AtomicRedTeam();
        maf = new MitreAttackFramework();


    }


    public static void showScreen() throws IOException {
        final String SCREEN_FXML = "/View/Screen.fxml";
        FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource(SCREEN_FXML));
        ScreenController controller = new ScreenController();
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Project 1");
        primaryStage.show();

    }

}
