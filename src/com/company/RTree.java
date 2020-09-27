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
        this.root = new Node(dim,null);
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
        Node selected = ChooseSubtree(currNode, entry);

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
        Split_Leaf(overNode,extraEntry);
    }

    // Function to choose the appropriate insertion path and reach the node where we will insert a new entry.
    private Node ChooseSubtree(Node currNode, Record entry){

        // If N is leaf, return N
        if (currNode.getChildren().size()==0) {
            return currNode;
        }

        // Else if the childpointers of N point to leaves, choose the child whose rectangle needs least overlap enlargement
        else if(currNode.getChildren().get(0).isLeaf()){
            int numOfChildren = currNode.getChildren().size();
            double [] Overlap = new double[numOfChildren];
            for (int i=0;i<numOfChildren;i++){
                for (int j=0;j<numOfChildren;j++){
                    if(i!=j){
                        Overlap[i]-=Calculate_OverlapValue(currNode.getChildren().get(i).getMbr(),currNode.getChildren().get(j).getMbr());
                    }
                }
                Node temp = new Node(dim, null);
                temp.adjustMbr(entry);
                temp.adjustMbr(currNode.getChildren().get(i));
                for (int j=0;j<numOfChildren;j++){
                    if(i!=j){
                        Overlap[i]+=Calculate_OverlapValue(temp.getMbr(),currNode.getChildren().get(j).getMbr());
                    }
                }
            }
            int minNode=0;
            double minOverlap = Overlap[0];
            for (int i=1;i<numOfChildren;i++){
                if (Overlap[i]<minOverlap){
                    minOverlap=Overlap[i];
                    minNode=i;
                }
            }

            return currNode.getChildren().get(minNode);
        }

        // Else if the childpointers of N point to non-leaves, choose child whose rectangle needs least area enlargement
        else{
            int numOfChildren = currNode.getChildren().size();
            double [] Area = new double[numOfChildren];
            for (int i=0;i<numOfChildren;i++){
                Area[i]-=Calculate_Area(currNode.getChildren().get(i).getMbr());
                Node temp = new Node(dim, null);
                temp.adjustMbr(entry);
                temp.adjustMbr(currNode.getChildren().get(i));
                Area[i]+=Calculate_Area(temp.getMbr());
            }
            int minNode=0;
            double minArea = Area[0];
            for (int i=1;i<numOfChildren;i++){
                if (Area[i]<minArea){
                    minArea=Area[i];
                    minNode=i;
                }
            }
            return currNode.getChildren().get(minNode);
        }
    }

    // Function to perform a node split ( NON-LEAF )
    private void Split_NonLeaf(Node parent, Node extraNode){  
        int axis;
        List<List<Node>> bestDistribution;

        //Invoke ChooseSplitAxis_NonLeaf to determine the axis of the performed split
        axis = ChooseSplitAxis_NonLeaf(parent, extraNode);

        //Invoke ChooseSplitIndex_NonLeaf to determine the best distribution into 2 groups along the axis selected
        bestDistribution = ChooseSplitIndex_NonLeaf(parent, extraNode, axis);

        //If we are at root node, then create 2 new nodes
        if (parent.getParent()==null) {
            parent.adjustMbr(extraNode);
            Node child1 = new Node(dim, parent);
            Node child2 = new Node(dim, parent);
            for (int i = 0; i < bestDistribution.get(0).size(); i++) {          //Node of group no.1
                child1.getChildren().add(bestDistribution.get(0).get(i));
                child1.adjustMbr(bestDistribution.get(0).get(i));
            }
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                child2.getChildren().add(bestDistribution.get(1).get(i));
                child2.adjustMbr(bestDistribution.get(1).get(i));
            }
            parent.getChildren().clear();
            parent.getChildren().add(child1);
            parent.getChildren().add(child2);
        }
        //Else if parent node has M children, then split him too and go upwards
        else if(parent.getParent().getChildren().size()==M){
            Node child = new Node(dim,null);
            for (int i=0;i<bestDistribution.get(0).size();i++){                 //Node of group no.1
                child.getChildren().add(bestDistribution.get(0).get(i));
                child.adjustMbr(bestDistribution.get(0).get(i));
            }
            parent.getChildren().clear();
            parent.clearMbr();
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                parent.getChildren().add(bestDistribution.get(1).get(i));
                parent.adjustMbr(bestDistribution.get(1).get(i));
            }

            // RECURSION!
            // Now the parent node has more than M children so we have to split them.
            Split_NonLeaf(parent.getParent(),child);
        }
        //Else if parent node has less than M children, then create only 1 new node;
        else if(parent.getParent().getChildren().size()<M){
            parent.getParent().adjustMbr(extraNode);
            Node child = new Node(dim,parent.getParent());
            for (int i=0;i<bestDistribution.get(0).size();i++){                 //Node of group no.1
                child.getChildren().add(bestDistribution.get(0).get(i));
                child.adjustMbr(bestDistribution.get(0).get(i));
            }

            parent.getChildren().clear();
            parent.clearMbr();
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                parent.getChildren().add(bestDistribution.get(1).get(i));
                parent.adjustMbr(bestDistribution.get(1).get(i));
            }
            parent.getParent().getChildren().add(child);
        }
    }

    //Function to provide the new distributions so that we can create new nodes during a split ( NON-LEAF )
    private List<List<Node>> ChooseSplitIndex_NonLeaf(Node parent, Node extraNode, int axis) {
        List<Node> sortedNodes1 = new ArrayList<>();
        List<Node> sortedNodes2 = new ArrayList<>();
        List<List<Node>> bestDistribution = new ArrayList<>();

        sortedNodes1.add(extraNode);
        sortedNodes2.add(extraNode);
        for (int j = 0; j < M; j++) {
            sortedNodes1.add(parent.getChildren().get(j));
            sortedNodes2.add(parent.getChildren().get(j));
        }

        // Sort based on the lower value of the rectangles
        sortedNodes1.sort(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Double.compare(o1.getMbr()[axis][0], o2.getMbr()[axis][0]);
            }
        });

        // Sort based on the upper value of the rectangles
        sortedNodes2.sort(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Double.compare(o1.getMbr()[axis][1], o2.getMbr()[axis][1]);
            }
        });

        bestDistribution = Find_Best_Distribution_NonLeaf(sortedNodes1,sortedNodes2);
        return bestDistribution;
    }


    //Function to find the best possible distribution during a split ( NON-LEAF )
    private List<List<Node>> Find_Best_Distribution_NonLeaf(List<Node> sortedNodes1, List<Node> sortedNodes2){
        double overlap=0;
        double area=0;

        List<Node> group1 = new ArrayList<>();
        List<Node> group2 = new ArrayList<>();

        List<List<Node>> bestDistributionOverlap1 = new ArrayList<>();
        List<List<Node>> bestDistributionArea1 = new ArrayList<>();

        double[][] mbr1;
        double[][] mbr2;
        double minOverlap1 = Double.MAX_VALUE;
        double minArea1 = Double.MAX_VALUE;

        for (int k=1;k<=M-2*m+2;k++){
            for (int i=0;i<m-1+k;i++){
                group1.add(sortedNodes1.get(i));
            }
            for (int i=m-1+k;i<sortedNodes1.size();i++){
                group2.add(sortedNodes1.get(i));
            }

            mbr1 = Calculate_Mbr_NonLeaf(group1);
            mbr2 = Calculate_Mbr_NonLeaf(group2);

            overlap = Calculate_OverlapValue(mbr1,mbr2);
            area = Calculate_Area(mbr1) + Calculate_Area(mbr2);

            if(area < minArea1){
                minArea1 = area;
                bestDistributionArea1.clear();
                bestDistributionArea1.add(new ArrayList<>(group1));
                bestDistributionArea1.add(new ArrayList<>(group2));
            }

            if (overlap < minOverlap1){
                minOverlap1 = overlap;
                bestDistributionOverlap1.clear();
                bestDistributionOverlap1.add(new ArrayList<>(group1));
                bestDistributionOverlap1.add(new ArrayList<>(group2));
            }
            group1.clear();
            group2.clear();
        }

        List<List<Node>> bestDistributionOverlap2 = new ArrayList<>();
        List<List<Node>> bestDistributionArea2 = new ArrayList<>();

        double minOverlap2 = Double.MAX_VALUE;
        double minArea2 = Double.MAX_VALUE;

        for (int k=1;k<=M-2*m+2;k++){
            for (int i=0;i<m-1+k;i++){
                group1.add(sortedNodes2.get(i));
            }
            for (int i=m-1+k;i<sortedNodes2.size();i++){
                group2.add(sortedNodes2.get(i));
            }

            mbr1 = Calculate_Mbr_NonLeaf(group1);
            mbr2 = Calculate_Mbr_NonLeaf(group2);

            overlap = Calculate_OverlapValue(mbr1,mbr2);
            area = Calculate_Area(mbr1) + Calculate_Area(mbr2);

            if(area < minArea2){
                minArea2 = area;
                bestDistributionArea2.clear();
                bestDistributionArea2.add(new ArrayList<>(group1));
                bestDistributionArea2.add(new ArrayList<>(group2));
            }

            if (overlap < minOverlap2){
                minOverlap2 = overlap;
                bestDistributionOverlap2.clear();
                bestDistributionOverlap2.add(new ArrayList<>(group1));
                bestDistributionOverlap2.add(new ArrayList<>(group2));
            }
            group1.clear();
            group2.clear();
        }

        if(minOverlap1==0){
            if(minOverlap2!=0){
                return bestDistributionArea1;
            }else{
                if (minArea1<=minArea2){
                    return bestDistributionArea1;
                }else{
                    return bestDistributionArea2;
                }
            }
        }else{
            if(minOverlap2==0){
                return bestDistributionArea2;
            }else{
                if (minOverlap1<=minOverlap2){
                    return bestDistributionOverlap1;
                }else{
                    return bestDistributionOverlap2;
                }
            }
        }
    }

    // Function to determine the axis, perpendicular to which the split is performed ( NON-LEAF )
    private int ChooseSplitAxis_NonLeaf(Node parent, Node extraNode){
        double [] S = new double[dim];

        for (int axis=0;axis<dim;axis++){
            List<Node> sortedNodes = new ArrayList<>();

            sortedNodes.add(extraNode);
            for (int j=0;j<M;j++){
                sortedNodes.add(parent.getChildren().get(j));
            }

            int axisUsed = axis;
            for (int i=0;i<2;i++){      // Calculate S according to the upper and lower values of the rectangles
                int iUsed = i;
                sortedNodes.sort(new Comparator<Node>() {
                    @Override
                    public int compare(Node o1, Node o2) {
                        return Double.compare(o1.getMbr()[axisUsed][iUsed], o2.getMbr()[axisUsed][iUsed]);
                    }
                });
                S[axis] += Calculate_S_NonLeaf(sortedNodes);
            }
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

    // Function to calculate the sum of all the margin-values of the different distributions ( NON-LEAF )
    private double Calculate_S_NonLeaf(List<Node> sortedNodes){
        double sum = 0;
        List<Node> group1 = new ArrayList<>();
        List<Node> group2 = new ArrayList<>();
        double[][] mbr1;
        double[][] mbr2;

        for (int k=1;k<=M-2*m+2;k++) {
            for (int i = 0; i < m - 1 + k; i++) {
                group1.add(sortedNodes.get(i));
            }
            for (int i = m - 1 + k; i < sortedNodes.size(); i++) {
                group2.add(sortedNodes.get(i));
            }
            mbr1 = Calculate_Mbr_NonLeaf(group1);
            mbr2 = Calculate_Mbr_NonLeaf(group2);

            sum+=Calculate_MarginValue(mbr1);
            sum+=Calculate_MarginValue(mbr2);
            group1.clear();
            group2.clear();
        }
        return sum;
    }

    // Function to calculate the mbr of each of the two groups during a node-split ( NON-LEAF )
    private double[][] Calculate_Mbr_NonLeaf(List<Node> group){
        double[][] mbr = new double[dim][2];

        for(int i=0;i<dim;i++){
            for (int j=0;j<group.size();j++){
                double upper = group.get(j).getMbr()[i][1];
                double lower = group.get(j).getMbr()[i][0];

                // In every dimension axis check if we need to adjust any of the upper or lower bounds of the mbr.
                if(lower<mbr[i][0] || mbr[i][0]==0){         //Check lower bound of mbr
                    mbr[i][0] = lower;
                }
                if(upper>mbr[i][1] || mbr[i][1]==0){         //Check upper bound of mbr
                    mbr[i][1] = upper;
                }
            }
        }
        return mbr;
    }

    // Function to perform a node split ( LEAF )
    private void Split_Leaf(Node overNode, Record extraEntry){

        int axis;
        List<List<Record>> bestDistribution;

        //Invoke ChooseSplitAxis_Leaf to determine the axis of the performed split
        axis = ChooseSplitAxis_Leaf(overNode, extraEntry);

        //Invoke ChooseSplitIndex_Leaf to determine the best distribution into 2 groups along the axis selected
        bestDistribution = ChooseSplitIndex_Leaf(overNode, extraEntry, axis);

        //If we are at root node, then create 2 new nodes
        if (overNode.getParent()==null){
            overNode.adjustMbr(extraEntry);
            Node child1 = new Node(dim,overNode);
            Node child2 = new Node(dim,overNode);
            for (int i=0;i<bestDistribution.get(0).size();i++){                 //Node of group no.1
                child1.getRecords().add(bestDistribution.get(0).get(i));
                child1.adjustMbr(bestDistribution.get(0).get(i));
            }
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                child2.getRecords().add(bestDistribution.get(1).get(i));
                child2.adjustMbr(bestDistribution.get(1).get(i));
            }
            overNode.getChildren().add(child1);
            overNode.getChildren().add(child2);
            overNode.setLeaf(false);
        }
        //Else if parent node has M children, then split him too and go upwards
        else if(overNode.getParent().getChildren().size()==M){
            Node child = new Node(dim,null);
            for (int i=0;i<bestDistribution.get(0).size();i++){                 //Node of group no.1
                child.getRecords().add(bestDistribution.get(0).get(i));
                child.adjustMbr(bestDistribution.get(0).get(i));
            }
            overNode.getRecords().clear();
            overNode.clearMbr();
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                overNode.getRecords().add(bestDistribution.get(1).get(i));
                overNode.adjustMbr(bestDistribution.get(1).get(i));
            }

            // Now the parent node has more than M children so we have to split them.
            Split_NonLeaf(overNode.getParent(),child);
        }
        //Else if parent node has less than M children, then create only 1 new node;
        else if(overNode.getParent().getChildren().size()<M){
            overNode.getParent().adjustMbr(extraEntry);
            Node child = new Node(dim,overNode.getParent());
            for (int i=0;i<bestDistribution.get(0).size();i++){                 //Node of group no.1
                child.getRecords().add(bestDistribution.get(0).get(i));
                child.adjustMbr(bestDistribution.get(0).get(i));
            }

            overNode.getRecords().clear();
            overNode.clearMbr();
            for (int i=0;i<bestDistribution.get(1).size();i++){                 //Node of group no.2
                overNode.getRecords().add(bestDistribution.get(1).get(i));
                overNode.adjustMbr(bestDistribution.get(1).get(i));
            }
            overNode.getParent().getChildren().add(child);
            overNode.getParent().setLeaf(false);                    //Might be unnecessary
        }
    }

    // Function to determine the axis, perpendicular to which the split is performed ( LEAF )
    private int ChooseSplitAxis_Leaf(Node overNode, Record extraEntry){
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

            S[axis] = Calculate_S_Leaf(sortedEntries);
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


    //Function to provide the new distributions so that we can create new nodes during a split ( LEAF )
    private List<List<Record>> ChooseSplitIndex_Leaf(Node overNode, Record extraEntry, int axis) {

        List<Record> sortedEntries = new ArrayList<>();
        List<List<Record>> bestDistribution = new ArrayList<>();

        sortedEntries.add(extraEntry);
        for (int j = 0; j < M; j++) {
            sortedEntries.add(overNode.getRecords().get(j));
        }

        sortedEntries.sort(new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return Double.compare(o1.getInfo().get(axis), o2.getInfo().get(axis));
            }
        });

        bestDistribution = Find_Best_Distribution_Leaf(sortedEntries);
        return bestDistribution;
    }


    //Function to find the best possible distribution during a split ( LEAF )
    private List<List<Record>> Find_Best_Distribution_Leaf(List<Record> sortedEntries){
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

            mbr1 = Calculate_Mbr_Leaf(group1);
            mbr2 = Calculate_Mbr_Leaf(group2);

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

    // Function to calculate the area-value of a given mbr.
    private double Calculate_Area(double[][] mbr){
        double area = 1;
        for(int i=0;i<dim;i++){
            area = area * Math.abs(mbr[i][0]-mbr[i][1]);
        }
        return area;
    }

    // Function to calculate the overlap-value of 2 given mbrs.
    private double Calculate_OverlapValue(double[][] mbr1, double[][] mbr2){
        double overlap = 1;
        for(int i=0;i<dim;i++){
            overlap = overlap * Math.max(0,Math.min(mbr1[i][1],mbr2[i][1])-Math.max(mbr1[i][0],mbr2[i][0]));
        }

        return overlap;
    }

    // Function to calculate the sum of all the margin-values of the different distributions ( LEAF )
    private double Calculate_S_Leaf(List<Record> sortedEntries){
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

            mbr1 = Calculate_Mbr_Leaf(group1);
            mbr2 = Calculate_Mbr_Leaf(group2);

            sum+=Calculate_MarginValue(mbr1);
            sum+=Calculate_MarginValue(mbr2);
            group1.clear();
            group2.clear();
        }
        return sum;
    }

    // Function to calculate the mbr of each of the two groups during a node-split ( LEAF )
    private double[][] Calculate_Mbr_Leaf(List<Record> group){
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


