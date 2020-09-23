package com.company;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private double[][] mbr;

    private boolean leaf;
    private int dim;
    private List<Record> records = new ArrayList<>();
    private List<Node> children = new ArrayList<>();

    Node(int dim){
        this.dim = dim;
        this.leaf=true;
        mbr = new double[dim][2];
    }

    public void adjustMbr( Record newEntry ){
        for(int i=0;i<dim;i++){

            double entryCoord = newEntry.getInfo().get(i);

            // In every dimension axis check if we need to adjust any of the upper or lower bounds of the mbr.
            if(entryCoord<mbr[i][0] || mbr[i][0]==0){         //Check lower bound of mbr
                mbr[i][0] = entryCoord;
            }
            if(entryCoord>mbr[i][1] || mbr[i][1]==0){         //Check upper bound of mbr
                mbr[i][1] = entryCoord;
            }
        }
    }

    public boolean isLeaf() {
        return leaf;
    }

    public List<Node> getChildren() {
        return children;
    }

    public List<Record> getRecords() {
        return records;
    }

    public double[][] getMbr() {
        return mbr;
    }
}
