/**
 * Main class of the project.
 */
package com.company;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, newNode> data;
	    OSMDomParser parser = new OSMDomParser("osm.txt");
	    parser.parsingOSM();
	    data = parser.getData();
	    parser.writeDataFile();
        //data.forEach((k, v) -> System.out.println("id: "+k+" name:"+v.getName()+ " lat:"+v.getLatitude()+ " lon:"+v.getLongitude()));

        /*String fileName = "datafile";

        try (FileInputStream fis = new FileInputStream(fileName)) {

            int i;

            do {

                byte[] buf = new byte[32768];
                i = fis.read(buf);

                String value = new String(buf, StandardCharsets.UTF_8);
                System.out.println(value);
                System.out.println("--------------------------------------------------------------------------------");

            } while (i != -1);
        } catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
