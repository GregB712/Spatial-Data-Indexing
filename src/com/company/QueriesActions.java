package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QueriesActions {

    private static int dim;
    private static IndexNode temp;
    private static IndexNode root;
    private static String index;

    public static void main(String[] args) throws FileNotFoundException {
        index = "test_index";

        dim = 2;

        List<Double> givenCoor = new ArrayList<>();
        givenCoor.add(1.0);
        givenCoor.add(2.0);

        searchRecord(givenCoor, 0);
    }

    //Search tree from index file and create path given coordinates (if they exist) using recursion
    private static void searchRecord(List<Double> givenCoor, int lineNumber)
            throws FileNotFoundException {
        boolean check = true;
        Scanner scanner = new Scanner(new File(index));
        //find correct line of file (skip lines)
        if(lineNumber!=0){
            System.out.println("SKIP "+ (5*lineNumber-5) +" lines");
            for (int i = 0; i < (5*lineNumber-4)-1; i++) {
                //System.out.println(scanner.nextLine());
                scanner.nextLine();
            }
        } else {
            System.out.println("Skip none");
        }
        root = createIndexNode(scanner); //first is the root (in each recursion root changes)
        if (checkMBR(root, givenCoor) && root.getChilds().get(0)!=0){ //Continue if given point is inside root and also root is not a leaf
            //find correct line of file (skip lines)
            if(root.getId()!=1){
                System.out.println("SKIP "+ 5*(root.getChilds().get(0)-root.getId()-1) +" lines");
                for (int i = 0; i < 5*(root.getChilds().get(0)-root.getId()-1); i++) {
                    scanner.nextLine();
                }
            } else {
                System.out.println("Skip none");
            }
            int i = 0;
            while((i < root.getChilds().size()) && check){ //loop through all child nodes of the root
                temp = createIndexNode(scanner);
                i++;
                if(checkMBR(temp, givenCoor)){ //TODO don't stop when you find the first node with suitable MBR
                    check = false;
                }
            }
            if(!check) {
                scanner.close();
                searchRecord(givenCoor, temp.getId()); //Recursion, goes deeper in the tree
            }
        } else if (!checkMBR(root, givenCoor)){
            System.out.println("Out of Bounds!");
        } else if (root.getChilds().get(0)==0){ //Point is found
            System.out.println("MBR: "+ root.getMbr());
            System.out.println("BlockID: "+ root.getBlockID());
        }
        System.out.println("END");
    }

    //Checking if an IndexNode is inside a MBR
    private static boolean checkMBR(IndexNode indexNode, List<Double> coor){
        int counter=0;
        for (int i = 0; i < dim; i++) {
            if(indexNode.getMbr().get(i)<= coor.get(i) ){
                counter++;
            }
            if(indexNode.getMbr().get(i+dim)>= coor.get(i)){
                counter++;
            }
        }
        //System.out.println("counter:" + counter);
        return counter == 2 * dim;
    }

    //function to read from index file and creating objects IndexNode
    private static IndexNode createIndexNode(Scanner sc){
        int id = Integer.parseInt(sc.nextLine());

        String line = sc.nextLine();

        String[] parts;

        List<Double> mbr = new ArrayList<>();

        parts = line.split("\\s+");

        for (String part : parts) {
            mbr.add(Double.valueOf(part));
        }

        line = sc.nextLine();

        List<Integer> childs =  new ArrayList<>();

        parts = line.split("\\s+");

        for (String part : parts) {
            childs.add(Integer.valueOf(part));
        }

        String blockID = sc.nextLine();
        sc.nextLine();

        return new IndexNode(dim, id, mbr, childs, blockID);
    }
}
