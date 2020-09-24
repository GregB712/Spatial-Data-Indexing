package com.company;

import java.util.ArrayList;
import java.util.List;

public class SerialActions {

    private final int dim;
    private final List<Double> givenCoor;
    private final String filename;
    private final int k_neighbors;

    SerialActions(int dim, List<Double> givenCoor, String filename, int k_neighbors){
        this.dim = dim;
        this.filename = filename;
        this.givenCoor = new ArrayList<>(givenCoor);
        this.k_neighbors = k_neighbors;
    }

    SerialActions(int dim, List<Double> givenCoor, String filename){
        this.dim = dim;
        this.filename = filename;
        this.givenCoor = new ArrayList<>(givenCoor);
        this.k_neighbors = 0;
    }

    // K-nn Neighbors Query doing Serial-Searching on the csv datafile. (For comparing purposes)
    public void Knn() throws Exception {

        SerialKNeighbors skn = new SerialKNeighbors(dim, k_neighbors, givenCoor, filename);

        long startTime = System.nanoTime();
        skn.Calculate_K_Neighbors();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("SerialKNeighbors: " + duration/1000000 + "ms");

    }

    public void RQ() throws Exception {

        SerialRangeQueries srq = new SerialRangeQueries(dim, givenCoor, filename);

        long startTime = System.nanoTime();
        srq.RangeQuery();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("SerialRangeQueries: " + duration/1000000 + "ms");
    }
}
