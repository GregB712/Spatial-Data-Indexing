/**
  The objects of the class represents nodes with required information.
 **/
package com.company;

public class newNode {
    private String name;
    private double latitude;
    private double longitude;

    //Constructor of the class newNode.
    newNode(String name, double latitude, double longitude){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Getters and Setters for the private fields.
    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
