package hust.cybersec.controller;

import hust.cybersec.conversion.DataProcessing;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ChartController extends ScreenController implements Initializable {
    private static DataProcessing data;

    @FXML
    private StackedBarChart stackedChart;

    @FXML
    private ChoiceBox<String> choiceBox;

    public ChartController(DataProcessing data) {
        this.data = data;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            data = new DataProcessing();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //all header analyse
        choiceBox.getItems().add("Domains");
        choiceBox.getItems().add("Platforms");
        choiceBox.getItems().add("Tactics");


        choiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            switch (newValue) {
                case "Domains":
                    showDomainChart();
                    break;
                case "Platforms":
                    showPlatformChart();
                    break;
                case "Tactics":
                    showTacticsChart();
                    break;

            }
        });
        choiceBox.setValue("Domains");


        stackedChart.setAnimated(false);
        stackedChart.setTitle("Converage Analystic of Atomic RT to MITRE ATT&CK");


    }


    public void showChart() throws IOException {
        final String SCREEN_FXML_FILE_PATH = "/View/Chart.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();

        URL location = ChartController.class.getResource(SCREEN_FXML_FILE_PATH);
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

    void showPlatformChart() {
        stackedChart.getData().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();

        series1.setName("Covered");
        series2.setName("Uncovered");

        Iterator<String> coveredKeyIterator = data.getCoveredPlatforms().keySet().iterator();
        Iterator<String> keyIterator = data.getPlatforms().keySet().iterator();

        while (coveredKeyIterator.hasNext()){
            String key = coveredKeyIterator.next();
            series1.getData().add(new XYChart.Data<>(key, data.getCoveredPlatforms().get(key)));
        }

        while (keyIterator.hasNext()){
            String key = keyIterator.next();
            series2.getData().add(new XYChart.Data<>(key, data.getPlatforms().get(key) - data.getCoveredPlatforms().get(key)));
        }

        stackedChart.getData().addAll(series1, series2);
        addToolTip(series1, series2);
    }

    void showDomainChart() {
        stackedChart.getData().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();

        Number test = (Number) data.getAllCoveredTest().size();
        Number uncovered = (Number) (data.getListEnterprises().size() - data.getAllCoveredTest().size());

        series1.setName("Covered");
        series1.getData().add(new XYChart.Data<>("Enterprise Attack", test));
        series1.getData().add(new XYChart.Data<>("Mobile Attack", 0));
        series1.getData().add(new XYChart.Data<>("ICS Attack", 0));


        series2.setName("Uncovered");
        series2.getData().add(new XYChart.Data<>("Enterprise Attack", uncovered));
        series2.getData().add(new XYChart.Data<>("Mobile Attack", data.getListMobiles().size()));
        series2.getData().add(new XYChart.Data<>("ICS Attack", data.getListICSs().size()));


        stackedChart.getData().addAll(series1, series2);
        addToolTip(series1, series2);
    }

    void showTacticsChart(){
        stackedChart.getData().clear();

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();

        series1.setName("Covered");
        series2.setName("Uncovered");
        Iterator<String> coveredKeyIterator = data.getCoveredTactics().keySet().iterator();
        Iterator<String> keyIterator = data.getTactics().keySet().iterator();

        while (coveredKeyIterator.hasNext()){
            String key = coveredKeyIterator.next();
            series1.getData().add(new XYChart.Data<>(key, data.getCoveredTactics().get(key)));
        }

        while (keyIterator.hasNext()){
            String key = keyIterator.next();
            if (data.getCoveredTactics().get(key) == null) series2.getData().add(new XYChart.Data<>(key, data.getTactics().get(key)));
            else series2.getData().add(new XYChart.Data<>(key, data.getTactics().get(key) - data.getCoveredTactics().get(key)));
        }


        stackedChart.getData().addAll(series1, series2);
        addToolTip(series1, series2);
    }

    private void addToolTip(XYChart.Series<String, Number> series1, XYChart.Series<String, Number> series2) {
        for (XYChart.Data<String, Number> dataChart : series1.getData()) {
            int total = 0;
            if (data.getPlatforms().containsKey(dataChart.getXValue())) {
                total = data.getPlatforms().get(dataChart.getXValue());
            } else if (data.getTactics().containsKey(dataChart.getXValue())) {
                total = data.getTactics().get(dataChart.getXValue());
            } else if (dataChart.getXValue().equals("Enterprise Attack")) {
                total = data.getListEnterprises().size();
            }
            else total = dataChart.getYValue().intValue();

            double coverage = dataChart.getYValue().doubleValue() / total * 100; // Convert to double here
            Tooltip tooltip = new Tooltip("Value: " + dataChart.getYValue() + "\nCOVERAGE RATIO: " + String.format("%.2f", coverage) + "%");
            tooltip.setShowDelay(Duration.millis(5));
            Tooltip.install(dataChart.getNode(), tooltip);
        }

        for (XYChart.Data<String, Number> dataChart : series2.getData()) {
            int total = 0;
            if (data.getPlatforms().containsKey(dataChart.getXValue())) {
                total = data.getPlatforms().get(dataChart.getXValue());
            } else if (data.getTactics().containsKey(dataChart.getXValue())) {
                total = data.getTactics().get(dataChart.getXValue());
            } else if (dataChart.getXValue().equals("Enterprise Attack")) {
                total = data.getListEnterprises().size();
            }
            else total = dataChart.getYValue().intValue();

            double coverage = dataChart.getYValue().doubleValue() / total * 100; // Convert to double here
            Tooltip tooltip = new Tooltip("Value: " + dataChart.getYValue() + "\nCOVERAGE RATIO: " + String.format("%.2f", coverage) + "%");
            tooltip.setShowDelay(Duration.millis(5));
            Tooltip.install(dataChart.getNode(), tooltip);
        }
    }


}
