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

        Map<String, newNode> data;
        OSMDomParser parser = new OSMDomParser("osm.txt");
        parser.parsingOSM();
        data = parser.getData();
        parser.writeDataFile();

        // Initialise R-Tree and build it
        RTree rtree = new RTree(2,"outfile.csv");
        rtree.BuildRTree();

    	/*List<Double> givenCoor = new ArrayList<>();
        givenCoor.add(0.0);
        givenCoor.add(0.0);

        SerialKNeighbors skn = new SerialKNeighbors(2,3, givenCoor, "outfile.csv");
        skn.Calculate_K_Neighbors();*/
	}
}
