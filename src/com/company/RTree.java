package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;


public class RTree {

    final int M = 4;  // MAX bound of entries in a node
    final int m = 2;  // min bound of entries in a node

    private final int dim;
    private final String csvfile;
    private Node root;

    RTree(int dim, String csvfile) throws FileNotFoundException {
        this.dim = dim;
        this.csvfile = csvfile;

        // Initialise root
        this.root = new Node(dim);
    }


    // Construct the IndexFile based on the R-Tree made from function BuildRTree.

    private String WriteIndexFile(){
        return "";
    }

    // Build R-Tree

    public void BuildRTree() throws FileNotFoundException {

            // Parsing a CSV file into Scanner class constructor
            Scanner sc = new Scanner(new File(csvfile));

            // Loop through the CSVFile lines to get id and coordinates based on dimension parameter
            // We need the following csv format:
            // 1st element = id
            // Rest dimension elements (e.g. 2) = coordinates
            while (sc.hasNext()){
                String string = sc.nextLine();
                String[] parts = string.split("\\s+");

                String id = parts[0];
                List<Double> coordinates = new ArrayList<>();

                for (int i = 0; i < dim; i++) {
                    coordinates.add(Double.valueOf(parts[i+1]));
                }

                Record entry = new Record(id,coordinates);

                Insert(entry, root);
            }
            System.out.println(root.getMbr()[0][0]);
            System.out.println(root.getMbr()[0][1]);
            System.out.println(root.getMbr()[1][0]);
            System.out.println(root.getMbr()[1][1]);
            sc.close();  // Closes the scanner

    }

    private void Insert(Record entry, Node currNode){

        // Choose in which node we will try to insert the new entry
        Node selected = ChooseSubtree(currNode);

        // If node has less than M entries, then insert the entry into this node.
        if (selected.getRecords().size()<M){
            selected.getRecords().add(entry);
            selected.adjustMbr(entry);
        }

        // Else if node has M entries, then invoke OverflowTreatment
        else{
            OverflowTreatment( selected, entry);
            // TODO OverflowTreatment( Node selected, Record entry )
        }

    }

    // According to the papers about the R*tree, the OverflowTreatment function should try to first Remove p elements
    // from the overflown node, and then ReInsert them to the tree again to sometimes prevent splits.
    // However, since we were asked to not implement a Remove/Delete function, we are also not able to implement the
    // ReInsert function as well.

    // As a result, the OverflowTreatment function will only invoke the Split function.
    private void OverflowTreatment( Node overNode, Record extraEntry ){
        Split(overNode,extraEntry);
    }

    private Node ChooseSubtree(Node currNode){

        // If N is leaf, return N
        if (currNode.getChildren().size()==0) {
            return currNode;
        }

        // Else if the childpointers of N point to leaves, choose child whose rectangle needs least overlap enlargement
        else if(currNode.getChildren().get(0).isLeaf()){

            return currNode; // TODO RECURSION
        }

        // Else if the childpointers of N point to non-leaves, choose child whose rectangle needs least area enlargement
        else{

            return currNode; // TODO RECURSION
        }
    }

    private void Split(Node overNode, Record extraEntry){

        int axis;

        //Invoke ChooseSplitAxis to determine the axis of the performed split
        axis = ChooseSplitAxis();

        //Invoke ChooseSplitIndex to determine the best distribution into 2 groups along the axis selected
        ChooseSplitIndex(axis);

        // TODO Destribute the entries into two groups
    }

    private int ChooseSplitAxis(){
        return 0;  // TODO
    }

    private void ChooseSplitIndex(int axis){
        // TODO
    }

    private void ReInsert(){

    }
}


