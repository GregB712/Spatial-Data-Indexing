/**
 * Main class of the project.
 */
package com.company;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        Map<String, newNode> data;
	    OSMDomParser parser = new OSMDomParser("osm.txt");
	    parser.parsingOSM();
	    data = parser.getData();
        data.forEach((k, v) -> System.out.println("id: "+k+" name:"+v.getName()+ " lat:"+v.getLatitude()+ " lon:"+v.getLongitude()));
    }
}
