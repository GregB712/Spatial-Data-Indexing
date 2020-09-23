package com.company;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private double[] mbr = {0,0,0,0};

    private boolean leaf;
    private List<Record> records = new ArrayList<>();
    private List<Node> children = new ArrayList<>();

    Node(){
        this.leaf=true;
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

    public double[] getMbr() {
        return mbr;
    }
}
