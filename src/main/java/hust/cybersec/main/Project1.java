package hust.cybersec.main;

import hust.cybersec.model.AtomicRedTeam;
import hust.cybersec.model.MitreAttackFramework;
import org.apache.poi.ss.formula.functions.Mirr;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Project1 {

    public static void main(String[] args) throws URISyntaxException {
        AtomicRedTeam art = new AtomicRedTeam();
        MitreAttackFramework maf = new MitreAttackFramework();

        try{
            art.download();
            maf.download();
        }catch (URISyntaxException e) {
            e.getMessage();
        }


        Scanner inp = new Scanner(System.in);
        int input;
        mainScreen();
        input = inp.nextInt();
        switch (input){
            case 1:
                try{
                    art.download();
                    maf.download();
                }catch (URISyntaxException e) {
                    e.getMessage();
                }
                mainScreen();
                break;
            case 2:
                mainScreen();
                break;
            case 3:
                mainScreen();
                break;
            case 4:
                inp.close();
                return;
            default:
                System.out.println("Invalid input! Please try again");
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
