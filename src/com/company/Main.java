package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the project.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        //Create Datafile from csv
        CSVParser csvParser = new CSVParser(2, "outfile.csv");
        csvParser.CSVParsing();
        csvParser.writeDataFile();

        /*List<Double> l = new ArrayList<>();
        l.add(26.5291294);
        l.add(41.5163899);
        l.add(26.5323722);
        l.add(41.5031784);
        SerialActions serialActions =  new SerialActions(2, l);
        serialActions.RQ();*/

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
