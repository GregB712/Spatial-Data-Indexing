/**
 * Main class of the project.
 */
package com.company;

public class Main {

    public static void main(String[] args) throws Exception {

        // Initialise R-Tree and build it
        RTree rtree = new RTree(2,"test.csv");
        rtree.BuildRTree();

//        AppMenu appMenu = new AppMenu();
//        appMenu.invokeMenu();

	}
}
