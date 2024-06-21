package hust.cybersec.main;

import hust.cybersec.UI.Controller.ChartController;
import hust.cybersec.UI.Controller.ScreenController;
import hust.cybersec.conversion.DataProcessing;
import hust.cybersec.exportexcel.ExportExcel;
import hust.cybersec.model.AtomicRedTeam;
import hust.cybersec.model.MitreAttackFramework;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Project1 extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        final String SCREEN_FXML_FILE_PATH = "src/main/java/hust/cybersec/UI/view/MITRE_ATT&CK_chart.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(SCREEN_FXML_FILE_PATH));
        ChartController chartController = new ChartController(new DataProcessing());
        fxmlLoader.setController(chartController);
        Parent root = fxmlLoader.load();


        Scene scene = new Scene(root);
        stage.setTitle("Simulation of Forces");
        stage.setScene(scene);  
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


//    public static void main(String[] args) throws URISyntaxException, IOException {
//        DataProcessing data = new DataProcessing();
//        ExportExcel exportExcel = new ExportExcel(data);
//        ChartController chartController = new ChartController(data);
//
//        AtomicRedTeam art = new AtomicRedTeam();
//        MitreAttackFramework maf = new MitreAttackFramework();
//
////        try{
////            art.download();
////            maf.download();
////        }catch (URISyntaxException e) {
////            e.getMessage();
////        }
//
//
//        Scanner inp = new Scanner(System.in);
//        int input;
//        mainScreen();
//        input = inp.nextInt();
//
//        switch (input) {
//            case 1:
//                try {
//                    art.download();
//                    maf.download();
//                } catch (URISyntaxException e) {
//                    e.getMessage();
//                }
//                mainScreen();
//                break;
//            case 2:
//                try {
//                    exportExcel.export();
//                } catch (Exception e) {
//                    System.err.println("There's an error while exporting data to excel");
//                    e.printStackTrace();
//                }
//                break;
//            case 3:
//                chartController.showChart();
//                break;
//            case 4:
//                inp.close();
//                return;
//            default:
//                System.err.println("Invalid input! Please try again");
//                mainScreen();
//        }
//
//    }
//
//    static void mainScreen(){
//        System.out.println("Welcome to our Project I. What do you want to do?");
//        System.out.println("1. Update data");
//        System.out.println("2. Export excel");
//        System.out.println("3. Get chart data");
//        System.out.println("4. Exit");
//    }
}
