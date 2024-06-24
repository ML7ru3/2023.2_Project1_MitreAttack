package hust.cybersec.controller;


import hust.cybersec.conversion.DataProcessing;
import hust.cybersec.exportexcel.ExportExcel;
import hust.cybersec.model.AtomicRedTeam;
import hust.cybersec.model.MitreAttackFramework;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;


public class ScreenController implements Initializable {
    AtomicRedTeam art = new AtomicRedTeam();
    MitreAttackFramework maf = new MitreAttackFramework();


    DataProcessing data;
    ExportExcel excelExporter;

    @FXML
    void updateData(ActionEvent event) {
        downloadData();
    }

    @FXML
    void exportExcell(ActionEvent event) throws IOException {
        excelExporter.export();
    }

    @FXML
    void viewChart(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            try {
                ChartScreen.launchScreen();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void exitPressed(ActionEvent event) {

    }


    private void downloadData(){
        try {
            art.download();
            maf.download();
        } catch (URISyntaxException e) {
            e.getMessage();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        downloadData();
        try {
            data = new DataProcessing();
        } catch (IOException e) {
            System.err.println("An error occurred while reading the data file");
        }
        excelExporter = new ExportExcel(data);


    }
}
