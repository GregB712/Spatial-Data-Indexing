package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the project.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        AppMenu appMenu = new AppMenu();
        appMenu.invokeMenu();

        /** Queries for R-tree  **/         // TODO: WE NEED TO RUN THOSE SEPERATELY DUE TO STREAM PROBLEMS
        //rtree.InsertNewEntry(2);       // (DONE!) Inserts new entry into the tree    //CSV AND DATAFILE SHOULD HAVE NO BLANK LINE AT THE END

        //rtree.RangeQuery(2);              // (DONE!) Prints the old_datafile line for every entry that belongs to the query

        //rtree.kNNQuery(2,5);              // (DONE!) Prints the info of the knn query

	}

}
