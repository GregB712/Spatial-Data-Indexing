package com.company;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Main class of the project.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        //Create Datafile from csv
        CSVParser csvParser = new CSVParser(2, "outfile.csv");
        csvParser.CSVParsing();
        csvParser.writeDataFile();
        // Initialise R-Tree and build it
        //RTree rtree = new RTree(2,"test.csv");
        //rtree.BuildRTree();
        //rtree.WriteIndexFile();

        /** Queries for R-tree  **/
        //rtree.InsertNewEntry(2);          // (DONE!) Inserts new entry into the tree

        //rtree.RangeQuery(2);              // (DONE!) Prints the old_datafile line for every entry that belongs to the query

        //rtree.kNNQuery(2,3);              // (NOT DONE!) TODO


        //AppMenu appMenu = new AppMenu();
        //appMenu.invokeMenu();



	}

}
