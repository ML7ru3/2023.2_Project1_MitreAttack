package hust.cybersec.main;

import hust.cybersec.exportexcel.ExportExcel;
import hust.cybersec.model.AtomicRedTeam;
import hust.cybersec.model.MitreAttackFramework;

import java.net.URISyntaxException;
import java.util.Scanner;

public class Project1 {

    public static ExportExcel exportExcel = new ExportExcel();
    public static void main(String[] args) throws URISyntaxException {
        AtomicRedTeam art = new AtomicRedTeam();
        MitreAttackFramework maf = new MitreAttackFramework();

//        try{
//            art.download();
//            maf.download();
//        }catch (URISyntaxException e) {
//            e.getMessage();
//        }


        Scanner inp = new Scanner(System.in);
        int input;
        mainScreen();
        input = inp.nextInt();

        switch (input) {
            case 1:
                try {
                    art.download();
                    maf.download();
                } catch (URISyntaxException e) {
                    e.getMessage();
                }
                mainScreen();
                break;
            case 2:
                try {
                    exportExcel.export();
                } catch (Exception e) {
                    System.err.println("There's an error while exporting data to excel");
                    e.printStackTrace();
                }
                break;
            case 3:
                break;
            case 4:
                inp.close();
                return;
            default:
                System.err.println("Invalid input! Please try again");
                mainScreen();
        }

    }

    static void mainScreen(){
        System.out.println("Welcome to our Project I. What do you want to do?");
        System.out.println("1. Update data");
        System.out.println("2. Export excel");
        System.out.println("3. Get chart data");
        System.out.println("4. Exit");
    }
}
