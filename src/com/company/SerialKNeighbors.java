package com.company;

import java.io.*;
import java.util.*;

public class SerialKNeighbors {

    private final int dim;
    private final int k_neighbors;
    private final List<Double> givenCoor;
    private final String csvfile;
    private PriorityQueue<Records> pq;

    SerialKNeighbors(int dim, int k_neighbors, List<Double> givenCoor, String csvfile){
        this.dim = dim;
        this.k_neighbors = k_neighbors;
        this.givenCoor = new ArrayList<>(givenCoor);
        this.csvfile = csvfile;
    }

    public void Calculate_K_Neighbors() throws Exception{
        pq = new PriorityQueue<>(k_neighbors, Comparator.comparing(Records::getDistance).reversed());
        String string;
        String[] parts;
        double dist = 0;
        List<Double> dimensions = new ArrayList<>();

        Scanner sc = new Scanner(new File(csvfile));

        while(sc.hasNextLine()){
            string = sc.nextLine();

            parts = string.split("\\s+");

            for (int i = 0; i < dim; i++) {
                dimensions.add(Double.valueOf(parts[i+1]));
            }
            for (int i = 0; i < dim; i++) {
                dist += Math.pow(2, dimensions.get(i)-givenCoor.get(i));
            }
            dist = Math.sqrt(dist);

            if (pq.size() >= k_neighbors){
                pq.add(new Records(parts[0], new ArrayList<>(dimensions), dist));
                pq.poll();
            } else {
                pq.add(new Records(parts[0], new ArrayList<>(dimensions), dist));
            }
            dist = 0;
            dimensions.clear();
        }

        sc.close();  //closes the scanner

        for (int i = 0; i < k_neighbors; i++) {
            Objects.requireNonNull(pq.poll()).showRecord();
        }
    }
}
