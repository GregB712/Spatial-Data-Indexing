package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class OSMDomParser {

    public static void main(String[] args) {

        try {
            File inputFile = new File("osm.txt");
            //Get Document Builder
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            //Build Document
            Document doc = dBuilder.parse(inputFile);

            //Normalize the XML Structure; It's just too important !!
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("node");
            System.out.println("----------------------------");
            //System.out.println(nList.getLength());

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.hasChildNodes()) {
                        int j = 1;
                        boolean check = true;
                        try {
                            while (check) {
                                j = j + 2;
                                if(eElement.getChildNodes().item(j).getAttributes().item(0).getTextContent().equals("name")){
                                    System.out.println("FOUND IT!");
                                    check=false;
                                }
                            }
                            System.out.println("Node ID : "
                                    + eElement.getAttribute("id"));
                            System.out.println("Name : "
                                    + eElement.getChildNodes().item(j).getAttributes().item(1).getTextContent());
                            System.out.println("Latitude : "
                                    + eElement.getAttribute("lat"));
                            System.out.println("Longitude : "
                                    + eElement.getAttribute("lon"));
                            System.out.println("----------------------------");
                        } catch (NullPointerException e) {
                            System.out.println("Caught the NullPointerException");
                            System.out.println("----------------------------");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
