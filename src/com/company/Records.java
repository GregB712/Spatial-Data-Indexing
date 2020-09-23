package com.company;

import java.util.List;

public class Records {

    private final String id;
    private final List<Double> coordinates;
    private final double distance;

    Records(String id, List<Double> coordinates, double distance){
        this.id = id;
        this.coordinates = coordinates;
        this.distance = distance;
    }

    public Object showRecord(){
        System.out.println(id + " " + coordinates + " " + distance);
        return null;
    }

    public String getId() {
        return id;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public double getDistance() {
        return distance;
    }
}
