package com.company;

import java.util.List;

public class Record {

    private String id;
    private List<Double> info;
    private int line;

    Record(String id, List<Double> info, int line){
        this.id = id;
        this.info = info;
        this.line = line;
    }

    public List<Double> getInfo() {
        return info;
    }

    public String getId() {
        return id;
    }

    public int getLine() {
        return line;
    }
}


