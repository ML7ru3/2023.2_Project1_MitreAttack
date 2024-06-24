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
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ChartController implements Initializable {
    private DataProcessing data;

    @FXML
    private StackedBarChart<String, Number> stackedChart;

    @FXML
    void analyzeValue(ActionEvent event) {

    }

    public ChartController(DataProcessing data) {
        this.data = data;
    }

    public DataProcessing getData() {
        return data;
    }

    public void setData(DataProcessing data) {
        this.data = data;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            data = new DataProcessing();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Defining the axes
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList
                ("Enterprise Attack", "Mobile Attack", "ICS Attack")));

        xAxis.setLabel("category");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Tests");

        //Creating the Bar chart
        StackedBarChart<String, Number> stackedBarChart =
                new StackedBarChart<>(xAxis, yAxis);


        stackedChart.setAnimated(true);
        stackedChart.setTitle("Converage Analystic of Atomic RT to MITRE ATT&CK");

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();

        Number test = (Number) data.getAllTest().size();
        Number uncovered = (Number) (data.getListEnterprises().size() - data.getAllTest().size()); ;
        System.out.println(test);

        series1.setName("Convered");
        series1.getData().add(new XYChart.Data<>("Enterprise Attack", test));
        series1.getData().add(new XYChart.Data<>("Mobile Attack", 0));
        series1.getData().add(new XYChart.Data<>("ICS Attack", 0));


        series2.setName("Uncovered");
        series2.getData().add(new XYChart.Data<>("Enterprise Attack", uncovered));
        series2.getData().add(new XYChart.Data<>("Mobile Attack", data.getListMobiles().size()));
        series2.getData().add(new XYChart.Data<>("ICS Attack", data.getListICSs().size()));


        stackedChart.getData().addAll(series1, series2);
    }


}
