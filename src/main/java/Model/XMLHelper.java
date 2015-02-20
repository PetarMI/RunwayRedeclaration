package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class XMLHelper {

    public final static String OBSTACLE_FILE_NAME = "src/main/resources/obstacles.xml";
    public final static String OBSTACLE_NAME = "Name";
    public final static String OBSTACLE_HEIGHT = "Height";
    public final static String OBSTACLE_LENGTH = "Length";
    public final static String OBSTACLE_WIDTH = "Width";
    public final static String OBSTACLE_DESCRIPTION = "Description";
    public final static String OBSTACLE_LIST = "ObstacleList";
    public final static String OBSTACLE_ELEMENT = "Obstacle";


    public final static String AIRPORTS_DIRECTORY = "src/main/resources/Airports";
    public final static String AIRPORT = "Airport";
    public final static String AIRPORT_ATTR_NAME = "name";
    public final static String RUNWAY = "Runway";
    public final static String RUNWAY_ID = "id";
    public final static String TORA = "TORA";
    public final static String ASDA ="ASDA";
    public final static String TODA = "TODA";
    public final static String LDA = "LDA";
    public final static String RUNWAY_ORIENTATION = "Orientation";
    public final static String RUNWAY_POSITION = "Position";


    public XMLHelper() {
    }

    public static void main (String []args){
        new XMLHelper();
    }

    public boolean createObstacleXML(ArrayList<Obstacle> obs) {

        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;

        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElement(OBSTACLE_LIST);
            doc.appendChild(mainRootElement);

            for(Obstacle o : obs){
                mainRootElement.appendChild(getObstacle(doc, o.getName(), o.getWidth(), o.getHeight(), o.getLength(), o.getDescription()));
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult myFile = new StreamResult(new FileOutputStream(new File(OBSTACLE_FILE_NAME)));
            transformer.transform(source, myFile);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    private Node getObstacle(Document doc, String name, int width, int height, int length, String description) {
        Element obstacle = doc.createElement(OBSTACLE_ELEMENT);
        obstacle.appendChild(getElements(doc, OBSTACLE_NAME, String.valueOf(name)));
        obstacle.appendChild(getElements(doc, OBSTACLE_LENGTH, String.valueOf(length)));
        obstacle.appendChild(getElements(doc, OBSTACLE_WIDTH, String.valueOf(width)));
        obstacle.appendChild(getElements(doc, OBSTACLE_HEIGHT, String.valueOf(height)));
        obstacle.appendChild(getElements(doc, OBSTACLE_DESCRIPTION, String.valueOf(description)));
        return obstacle;
    }
    //
//    // utility method to create text node
    private Node getElements(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    public ArrayList<Obstacle> readObstacles(){
        try {
            ArrayList<Obstacle> obsList = new ArrayList<Obstacle>();
            File file = new File(OBSTACLE_FILE_NAME);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName(OBSTACLE_ELEMENT);

            for (int s = 0; s < nodeList.getLength(); s++) {

                Node currentNode = nodeList.item(s);

                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) currentNode;
                    String obsName = element.getElementsByTagName(OBSTACLE_NAME).item(0).getTextContent();
                    int obsWidth =  Integer.valueOf(element.getElementsByTagName(OBSTACLE_WIDTH).item(0).getTextContent());
                    int obsHeight = Integer.valueOf(element.getElementsByTagName(OBSTACLE_HEIGHT).item(0).getTextContent());
                    int obsLength =  Integer.valueOf(element.getElementsByTagName(OBSTACLE_LENGTH).item(0).getTextContent());
                    String obsDescr = element.getElementsByTagName(OBSTACLE_DESCRIPTION).item(0).getTextContent();

                    obsList.add(new Obstacle(obsName, obsWidth, obsHeight, obsLength, obsDescr));
                }
            }

            return obsList;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //TODO change ArrayList to just a List. Because we don't need to specify its an ArrayList.
    //TODO read only the Airport names and thus return a List of Strings
    public ArrayList<String> readAllAirports() throws IOException, SAXException, ParserConfigurationException {
        ArrayList<String> airportsNames = new ArrayList<String>();
        File airportDirectory = new File(AIRPORTS_DIRECTORY);

        if(airportDirectory.exists() && airportDirectory.isDirectory()){

            //select only files that finish in .xml
            File[] xmlFiles = airportDirectory.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".xml");
                }});

            for(File f : xmlFiles){
                airportsNames.add(f.getName().split(".xml")[0]);
            }

            return airportsNames;
        }
        else {
            airportDirectory.mkdirs();
            return null;
        }
    }

    //TODO take an airport name as a string an return an airport  object - SORRY ANDREI
    public Airport readAirport(String fileName) throws ParserConfigurationException, IOException, SAXException {

        File file = new File(AIRPORTS_DIRECTORY + File.separator + fileName);
        ArrayList<Runway> runways = new ArrayList<Runway>();
        System.out.println(fileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        String airportName = ((Element) doc.getElementsByTagName(AIRPORT).item(0)).getAttribute(AIRPORT_ATTR_NAME);
        NodeList nodeList = doc.getElementsByTagName(RUNWAY);


        for (int s = 0; s < nodeList.getLength(); s++) {
            Node currentNode = nodeList.item(s);

            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) currentNode;
                String runwayId = element.getAttribute(RUNWAY_ID);
                int tora1 = Integer.valueOf(element.getElementsByTagName(TORA).item(0).getTextContent());
                int tora2 = Integer.valueOf(element.getElementsByTagName(TORA).item(1).getTextContent());
                int asda1 = Integer.valueOf(element.getElementsByTagName(ASDA).item(0).getTextContent());
                int asda2 = Integer.valueOf(element.getElementsByTagName(ASDA).item(1).getTextContent());
                int toda1 = Integer.valueOf(element.getElementsByTagName(TODA).item(0).getTextContent());
                int toda2 = Integer.valueOf(element.getElementsByTagName(TODA).item(1).getTextContent());
                int lda1 = Integer.valueOf(element.getElementsByTagName(LDA).item(0).getTextContent());
                int lda2 = Integer.valueOf(element.getElementsByTagName(LDA).item(1).getTextContent());
                String position = element.getElementsByTagName(RUNWAY_POSITION).item(0).getTextContent();
                int orientation = Integer.valueOf(element.getElementsByTagName(RUNWAY_ORIENTATION).item(0).getTextContent());
                //TODO: On import create two strips, then create runway using these two strips
                //runways.add(new Runway(runwayId, tora, asda, toda, lda, orientation, position));
                Strip strip1 = new Strip(orientation, position, tora1, asda1, toda1, lda1);
                if (position.equals("L")) {
                    position = "R";
                } else {
                    position = "L";
                }
                Strip strip2 = new Strip(orientation + 18, position, tora2, asda2, toda2, lda2);
                runways.add(new Runway(strip1, strip2));
            }
        }

        return new Airport(runways, airportName);

    }
}