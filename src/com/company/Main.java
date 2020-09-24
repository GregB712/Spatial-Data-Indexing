/**
 * Main class of the project.
 */
package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {

        // Initialise R-Tree and build it
        RTree rtree = new RTree(2,"test.csv");
        rtree.BuildRTree();

        //Serial_Knn();
        //Serial_RQ();
	}

    // K-nn Neighbors Query doing Serial-Searching on the csv datafile. (For comparing purposes)
	public static void Serial_Knn() throws Exception {
        List<Double> givenCoor1 = new ArrayList<>();
        givenCoor1.add(0.0);
        givenCoor1.add(0.0);

        SerialKNeighbors skn = new SerialKNeighbors(2,3, givenCoor1, "outfile.csv");

        long startTime = System.nanoTime();
        skn.Calculate_K_Neighbors();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("SerialKNeighbors: " + duration/1000000 + "ms");

    }

    public static void Serial_RQ() throws Exception {
        List<Double> givenCoor2 = new ArrayList<>();
        givenCoor2.add(26.5209287);
        givenCoor2.add(41.5135626);
        givenCoor2.add(26.5220499);
        givenCoor2.add(41.5123335);

        SerialRangeQueries srq = new SerialRangeQueries(2, givenCoor2, "outfile.csv");

        long startTime = System.nanoTime();
        srq.RangeQuery();
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);  //divide by 1000000 to get milliseconds.
        System.out.println("SerialRangeQueries: " + duration/1000000 + "ms");
    }
}
