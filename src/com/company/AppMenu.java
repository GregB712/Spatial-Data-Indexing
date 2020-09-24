package com.company;

import java.util.Scanner;

public class AppMenu
{

    public static void main (String[] args) {
        // create a scanner so we can read the command-line input
        Scanner scanner = new Scanner(System.in);

        System.out.println("1) Build R* Tree from csv file");
        System.out.println("2) Load R* Tree from index file");
        System.out.println("3) Exit");

        String option;
        do {
            System.out.print("Choose one of the above: ");

            option = scanner.next();

            switch (option) {
                case "1":
                    System.out.print("How many dimensions: ");
                    int dim = scanner.nextInt();
                    System.out.print("Name of csv file: ");
                    String csvfile = scanner.next();
                    break;
                case "2":
                    System.out.print("Name of index file: ");
                    String indexfile = scanner.next();
                    break;
                case "3":
                    System.out.println("Bye Bye!");
                    return;
            }
        } while (!option.equals("1") && !option.equals("2"));

        // Build R* Tree
        System.out.println();
        System.out.println("R* Tree is ready!");
        System.out.println("What do you want to do?");
        System.out.println("1) Insert point");
        System.out.println("2) Search point");
        System.out.println("3) Find k nearest neighbors in a point");
        System.out.println("4) Find all the points inside an area");
        System.out.println("5) Exit");

        System.out.print("Choose one of the above: ");

        String innerOption;

        do {
            System.out.print("Choose one of the above: ");

            innerOption = scanner.next();

            switch (innerOption) {
                case "1":
                    System.out.print("Insert");
                    break;
                case "2":
                    System.out.print("Search");
                    break;
                case "3":
                    System.out.println("KNN");
                    break;
                case "4":
                    System.out.println("Range Query");
                    break;
                case "5":
                    System.out.println("Bye Bye!");
                    return;
            }
        } while (!innerOption.equals("1") && !innerOption.equals("2") && !innerOption.equals("3")
                && !innerOption.equals("4"));

    }

}
