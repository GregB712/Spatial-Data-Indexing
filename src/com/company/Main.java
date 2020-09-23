/**
 * Main class of the project.
 */
package com.company;

import java.io.FileNotFoundException;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Map<String, newNode> data;
	    OSMDomParser parser = new OSMDomParser("osm.txt");
	    parser.parsingOSM();
	    data = parser.getData();
	    parser.writeDataFile();

		// Initialise R-Tree and build it
		RTree rtree = new RTree(2,"outfile.csv");
		rtree.BuildRTree();

	}
}
