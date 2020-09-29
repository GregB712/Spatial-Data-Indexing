/**
 * Main class of the project.
 */
package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) throws Exception {

        // Initialise R-Tree and build it
        RTree rtree = new RTree(2,"outfile.csv");
        rtree.BuildRTree();
        rtree.WriteIndexFile();
//        AppMenu appMenu = new AppMenu();
//        appMenu.invokeMenu();

	}
}
