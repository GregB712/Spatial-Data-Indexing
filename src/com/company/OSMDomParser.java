/**
 * Parser for the OSM file.
 */
package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OSMDomParser {

    private Map<String, newNode> data;
    private final String textFile;

    //Constructor of the class OSMDomParser.
    OSMDomParser(String textFile){
        this.textFile = textFile;
    }

    //Function of parsing OSM file and saves the results to a hash map.
    public void parsingOSM() {
        data = new HashMap<>();
        String id;
        String name;
        double lat;
        double lon;
        try {
            File inputFile = new File(textFile);
            //Get Document Builder.
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            //Build Document.
            Document doc = dBuilder.parse(inputFile);

            //Normalize the XML Structure; It's just too important !!
            doc.getDocumentElement().normalize();

            //System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("node");
            //System.out.println("----------------------------");
            //System.out.println(nList.getLength());

            //For loop to check all the nodes in the OSM file.
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    //Checking the infos of each node.
                    //Node id, name and coordinates (latitude, longitude) are required.
                    if (eElement.hasChildNodes()) {
                        int j = 1;
                        boolean check = true;
                        try {
                            //Check for name.
                            while (check) {
                                j = j + 2; //j variable increase by two because of the data representation in OSM file.
                                if(eElement.getChildNodes().item(j).getAttributes().item(0).getTextContent().equals("name")){
                                    //System.out.println("FOUND IT!");
                                    check=false;
                                }
                            }
                            id = eElement.getAttribute("id");
                            name = eElement.getChildNodes().item(j).getAttributes().item(1).getTextContent();
                            lat = Double.parseDouble(eElement.getAttribute("lat"));
                            lon = Double.parseDouble(eElement.getAttribute("lon"));
                            /*System.out.println("Node ID : " + id);
                            System.out.println("Name : " + name);
                            System.out.println("Latitude : " + lat);
                            System.out.println("Longitude : " + lon);
                            System.out.println("----------------------------");*/
                            data.put(id, new newNode(name, lat, lon)); //Saving parsing data.

                        } catch (NullPointerException e) {
                            //System.out.println("Caught the NullPointerException");
                            //System.out.println("----------------------------");
                        }
                    } else {
                        id = eElement.getAttribute("id");
                        name = "NaN";
                        lat = Double.parseDouble(eElement.getAttribute("lat"));
                        lon = Double.parseDouble(eElement.getAttribute("lon"));
                        data.put(id, new newNode(name, lat, lon)); //Saving parsing data.
                    }
                }
            }
            //data.forEach((k,v) -> System.out.println("id: "+k+" name:"+v.getName()+ " lat:"+v.getLatitude()+ " lon:"+v.getLatitude()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeDataFile(){
        //New file object.
        File file = new File("datafile");

        BufferedWriter bf = null;

        try{

            //Create new BufferedWriter for the output file.
            bf = new BufferedWriter( new FileWriter(file) );
            BufferedWriter finalBf = bf;

            //Iterate map entries.
            data.forEach((k, v) -> {
                try {
                    finalBf.write("id: "+k+" name:"+v.getName()+ " lat:"+v.getLatitude()+ " lon:"+v.getLongitude());
                    finalBf.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            bf.flush();

        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                //Close the writer.
                assert bf != null;
                bf.close();
            }catch(Exception ignored){}
        }
    }

    //Getter for the hash map (data).
    public Map<String, newNode> getData() {
        return data;
    }
}
