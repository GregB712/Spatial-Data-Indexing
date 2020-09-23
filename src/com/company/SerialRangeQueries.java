package com.company;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SerialRangeQueries {

    private final int dim;
    private final List<Double> givenCoor;
    private final String csvfile;
    private final List<Records> inRange;

    SerialRangeQueries(int dim, List<Double> givenCoor, String csvfile){
        this.dim = dim;
        this.givenCoor = new ArrayList<>(givenCoor);
        this.csvfile = csvfile;
        inRange = new ArrayList<>();
    }

    public void RangeQuery() throws Exception{
        String string;
        String[] parts;
        List<Double> dimensions = new ArrayList<>();
        Scanner sc = new Scanner(new File(csvfile));

        while(sc.hasNextLine()){
            string = sc.nextLine();

            parts = string.split("\\s+");

            for (int i = 0; i < dim; i++) {
                dimensions.add(Double.valueOf(parts[i+1]));
            }

            int counter = 0;

            for (int i = 0; i < givenCoor.size()/dim; i++) {

                if (dimensions.get(i)>=Math.min(givenCoor.get(i), givenCoor.get(i+dim))
                        && dimensions.get(i)<=Math.max(givenCoor.get(i), givenCoor.get(i+dim))){
                    counter++;
                }
            }
            if(counter==dim){
                inRange.add(new Records(parts[0], new ArrayList<>(dimensions)));
            }
            dimensions.clear();
        }
        sc.close();  //closes the scanner

        for (Records record: inRange) {
            record.showRecord();
        }
    }
}
