package com.company;

import java.util.List;

public class Record {

    private String id;
    private List<Double> info;

    Record(String id, List<Double> info){
        this.id = id;
        this.info = info;
    }

    public List<Double> getInfo() {
        return info;
    }

    public String getId() {
        return id;
    }
}


