package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppMenu {

    public void invokeMenu() throws Exception {
        // create a scanner so we can read the command-line input
        Scanner scanner = new Scanner(System.in);

        System.out.println("1) Build R* Tree from csv file");
        System.out.println("2) Load R* Tree from index file");
        System.out.println("3) Exit");

        String option;
        String csvfile = "";
        int dim = 0;
        do {
            System.out.print("Choose one of the above: ");

            option = scanner.next();

            switch (option) {
                case "1":
                    System.out.print("How many dimensions: ");
                    dim = scanner.nextInt();
                    System.out.print("Name of csv file: ");
                    csvfile = scanner.next();
                    break;
                case "2":
                    System.out.print("Name of index file: ");
                    String indexfile = scanner.next();
                    //dim = something from index file
                    break;
                case "3":
                    System.out.println("Bye Bye!");
                    return;
            }
        } while (!option.equals("1") && !option.equals("2"));

        // Build R* Tree
        System.out.println();
        System.out.println("R* Tree is ready!");
        System.out.println();

        String innerOption;
        List<Double> givenCoor = new ArrayList<Double>();

        do {
            System.out.println("What do you want to do?");
            System.out.println("1) Insert point");
            System.out.println("2) Search point");
            System.out.println("3) Find k nearest neighbors in a point");
            System.out.println("4) Find all the points inside an area");
            System.out.println("5) Exit");
            System.out.print("Choose one of the above: ");

            innerOption = scanner.next();

            switch (innerOption) {
                case "1":
                    System.out.println("---Insert---");
                    this.typeCoordinates(dim, scanner, givenCoor);
                    //Insert Record
                    break;
                case "2":
                    System.out.println("---Search---");
                    this.typeCoordinates(dim, scanner, givenCoor);
                    //Search Record
                    //If found return true else return false
                    break;
                case "3":
                    System.out.println("---KNN---");
                    this.typeCoordinates(dim, scanner, givenCoor);
                    System.out.print("Give number of nearest neighbors: ");
                    int k_neighbors = scanner.nextInt();
                    // Call KNN
                    SerialActions serialActions1 = new SerialActions(dim, givenCoor, csvfile, k_neighbors);
                    serialActions1.Knn();
                    givenCoor.clear();
                    break;
                case "4":
                    System.out.println("---Range Query---");
                    System.out.print("Give 1st Coordinates: ");
                    for (int i = 0; i < dim; i++) {
                        givenCoor.add(scanner.nextDouble());
                    }
                    System.out.print("Give 2nd Coordinates: ");
                    for (int i = 0; i < dim; i++) {
                        givenCoor.add(scanner.nextDouble());
                    }
                    //Call RQ
                    SerialActions serialActions2 = new SerialActions(dim, givenCoor, csvfile);
                    serialActions2.RQ();
                    givenCoor.clear();
                    break;
                case "5":
                    System.out.println("Bye Bye!");
                    return;
            }
            System.out.println("_________________________________________________");
        } while (true);

    }

    private void typeCoordinates(int dim, Scanner scanner, List<Double> givenCoor){
        System.out.print("Give Coordinates: ");
        for (int i = 0; i < dim; i++) {
            givenCoor.add(scanner.nextDouble());
        }
    }

}
