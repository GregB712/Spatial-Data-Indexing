package com.company;

import java.util.*;
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
//            System.out.println(root.getMbr()[0][0]);
//            System.out.println(root.getMbr()[0][1]);
//            System.out.println(root.getMbr()[1][0]);
//            System.out.println(root.getMbr()[1][1]);
        sc.close();  // Closes the scanner

    }

    // Function to insert a new entry into the R*Tree
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

    // Function to choose the appropriate insertion path and reach the node where we will insert a new entry.
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


    // Function to perform a node split
    private void Split(Node overNode, Record extraEntry){

        int axis;
        List<List<Record>> bestDistribution = new ArrayList<>();

        //Invoke ChooseSplitAxis to determine the axis of the performed split
        axis = ChooseSplitAxis(overNode, extraEntry);

        //Invoke ChooseSplitIndex to determine the best distribution into 2 groups along the axis selected
        bestDistribution = ChooseSplitIndex(overNode, extraEntry, axis);

        // TODO Destribute the entries into two groups
    }

    // Function to determine the axis, perpendicular to which the split is performed
    private int ChooseSplitAxis(Node overNode, Record extraEntry){
        double [] S = new double[dim];

        for (int axis=0;axis<dim;axis++){
            List<Record> sortedEntries = new ArrayList<>();

            sortedEntries.add(extraEntry);
            for (int j=0;j<M;j++){
                sortedEntries.add(overNode.getRecords().get(j));
            }

            int axisUsed = axis;

            sortedEntries.sort(new Comparator<Record>() {
                @Override
                public int compare(Record o1, Record o2) {
                    return Double.compare(o1.getInfo().get(axisUsed), o2.getInfo().get(axisUsed));
                }
            });

            S[axis] = Calculate_S(sortedEntries);
        }

        double min = S[0];
        int minAxis = 0;
        for (int i=0; i<dim; i++){
            //System.out.println(S[i]);
            if (S[i]<min){
                min=S[i];
                minAxis=i;
            }
        }
        return minAxis;
    }

    private List<List<Record>> ChooseSplitIndex(Node overNode, Record extraEntry, int axis) {

        List<Record> sortedEntries = new ArrayList<>();
        List<List<Record>> bestDistribution = new ArrayList<>();

        sortedEntries.add(extraEntry);
        for (int j = 0; j < M; j++) {
            sortedEntries.add(overNode.getRecords().get(j));
        }

        int axisUsed = axis;

        sortedEntries.sort(new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return Double.compare(o1.getInfo().get(axisUsed), o2.getInfo().get(axisUsed));
            }
        });

        bestDistribution = Find_Best_Distribution(sortedEntries);
        return bestDistribution;
    }

    private List<List<Record>> Find_Best_Distribution(List<Record> sortedEntries){
        double [] overlap = new double[M-2*m+2];
        double [] area = new double[M-2*m+2];

        List<Record> group1 = new ArrayList<>();
        List<Record> group2 = new ArrayList<>();

        List<List<Record>> bestDistributionOverlap = new ArrayList<>();
        List<List<Record>> bestDistributionArea = new ArrayList<>();

        double[][] mbr1;
        double[][] mbr2;
        double minOverlap = Double.MAX_VALUE ;
        double minArea = Double.MAX_VALUE ;

        for (int k=1;k<=M-2*m+2;k++){
            for (int i=0;i<m-1+k;i++){
                group1.add(sortedEntries.get(i));
            }
            for (int i=m-1+k;i<sortedEntries.size();i++){
                group2.add(sortedEntries.get(i));
            }

            mbr1 = Calculate_Mbr(group1);
            mbr2 = Calculate_Mbr(group2);

            overlap[k-1] = Calculate_OverlapValue(mbr1,mbr2);
            area[k-1] = Calculate_Area(mbr1) + Calculate_Area(mbr2);

            if(area[k-1] < minArea){
                minArea = area[k-1];
                bestDistributionArea.clear();
                bestDistributionArea.add(new ArrayList<>(group1));
                bestDistributionArea.add(new ArrayList<>(group2));
            }

            if (overlap[k-1] < minOverlap){
                minOverlap = overlap[k-1];
                bestDistributionOverlap.clear();
                bestDistributionOverlap.add(new ArrayList<>(group1));
                bestDistributionOverlap.add(new ArrayList<>(group2));
            }
            group1.clear();
            group2.clear();
        }

        if(minOverlap==0){
            return bestDistributionArea;
        }else{
            return bestDistributionOverlap;
        }
    }

    private double Calculate_Area(double[][] mbr){
        double area = 1;
        for(int i=0;i<dim;i++){
            area = area * Math.abs(mbr[i][0]-mbr[i][1]);
        }
        return area;
    }

    private double Calculate_OverlapValue(double[][] mbr1, double[][] mbr2){
        double overlap = 1;
        for(int i=0;i<dim;i++){
            overlap = overlap * Math.max(0,Math.min(mbr1[i][1],mbr2[i][1])-Math.max(mbr1[i][0],mbr2[i][0]));
        }

        return overlap;
    }

    // Function to calculate the sum of all the margin-values of the different distributions
    private double Calculate_S(List<Record> sortedEntries){
        double sum = 0;
        List<Record> group1 = new ArrayList<>();
        List<Record> group2 = new ArrayList<>();
        double[][] mbr1;
        double[][] mbr2;

        for (int k=1;k<=M-2*m+2;k++){
            for (int i=0;i<m-1+k;i++){
                group1.add(sortedEntries.get(i));
            }
            for (int i=m-1+k;i<sortedEntries.size();i++){
                group2.add(sortedEntries.get(i));
            }

            mbr1 = Calculate_Mbr(group1);
            mbr2 = Calculate_Mbr(group2);

            sum+=Calculate_MarginValue(mbr1);
            //System.out.println(sum);
            sum+=Calculate_MarginValue(mbr2);
            //System.out.println(sum);
            group1.clear();
            group2.clear();
        }
        return sum;
    }

    // Function to calculate the mbr of each of the two groups during a node-split
    private double[][] Calculate_Mbr(List<Record> group){
        double[][] mbr = new double[dim][2];

        for(int i=0;i<dim;i++){
            for (int j=0;j<group.size();j++){
                double entryCoord = group.get(j).getInfo().get(i);

                // In every dimension axis check if we need to adjust any of the upper or lower bounds of the mbr.
                if(entryCoord<mbr[i][0] || mbr[i][0]==0){         //Check lower bound of mbr
                    mbr[i][0] = entryCoord;
                }
                if(entryCoord>mbr[i][1] || mbr[i][1]==0){         //Check upper bound of mbr
                    mbr[i][1] = entryCoord;
                }
            }
        }
        return mbr;
    }

    // Function to calculate the margin-value of a given mbr.
    private double Calculate_MarginValue(double[][] mbr){
        double sum=0;
        for(int i=0;i<dim;i++){
            sum+=Math.abs(mbr[i][0]-mbr[i][1]);
        }
        sum = sum * Math.pow(2,(dim-1));    // formula to calculate a bounding rectangle's margin-value in k-dimensions
        //System.out.println(sum);
        return sum;
    }

}


