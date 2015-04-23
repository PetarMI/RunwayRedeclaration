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
import java.util.List;

public class XMLHelper {

    public final static String OBSTACLE_FILE_NAME = "src/main/resources/obstacles.xml";
    public final static String OBSTACLE_NAME = "Name";
    public final static String OBSTACLE_HEIGHT = "Height";
    public final static String OBSTACLE_LENGTH = "Length";
    public final static String OBSTACLE_WIDTH = "Width";
    public final static String OBSTACLE_DESCRIPTION = "Description";
    public final static String OBSTACLE_LIST = "ObstacleList";
    public final static String OBSTACLE_ELEMENT = "Obstacle";

    public final static String STRIP = "Strip";
    public final static String AIRPORTS_DIRECTORY = "src/main/resources/Airports";
    public final static String AIRPORT = "Airport";
    public final static String AIRPORT_ATTR_NAME = "name";
    public final static String RUNWAY = "Runway";
    public final static String ID = "id";
    public final static String TORA = "TORA";
    public final static String ASDA = "ASDA";
    public final static String TODA = "TODA";
    public final static String LDA = "LDA";
    public final static String RUNWAY_ORIENTATION = "Orientation";
    public final static String RUNWAY_POSITION = "Position";
    public final static String RUNWAY_THRESHOLD = "Threshold";

    public XMLHelper() {
    }

    public static void main(String[] args) {
        new XMLHelper();
    }

    public boolean addObstacleXML(Obstacle obs) {
        List<Obstacle> obstacles = this.readObstacles();
        obstacles.add(obs);
        return createObstacleXML(obstacles);
    }

    public boolean createObstacleXML(List<Obstacle> obs) {

        DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;

        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element mainRootElement = doc.createElement(OBSTACLE_LIST);
            doc.appendChild(mainRootElement);

            for (Obstacle o : obs) {
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

    private Node makeStripElement(Document doc, Strip str) {
        Element strip = doc.createElement(STRIP);
        strip.setAttribute(ID, str.getStripId());
        strip.appendChild(getElements(doc, TORA, String.valueOf(str.getOrigVal().getTora())));
        strip.appendChild(getElements(doc, ASDA, String.valueOf(str.getOrigVal().getAsda())));
        strip.appendChild(getElements(doc, TODA, String.valueOf(str.getOrigVal().getToda())));
        strip.appendChild(getElements(doc, LDA, String.valueOf(str.getOrigVal().getLda())));
        strip.appendChild(getElements(doc, RUNWAY_THRESHOLD, String.valueOf(str.getDisplacedThreshold())));
        strip.appendChild(getElements(doc, RUNWAY_ORIENTATION, String.valueOf(str.getOrientation())));
        strip.appendChild(getElements(doc, RUNWAY_POSITION, String.valueOf(str.getPosition())));

        return strip;
    }


    private Element makeRunwayElement(Document doc, Runway runway) {
        Element run = doc.createElement(RUNWAY);
        run.setAttribute(ID, runway.getRunwayId());
        run.appendChild(makeStripElement(doc, runway.getStrip1()));
        run.appendChild(makeStripElement(doc, runway.getStrip2()));

        return run;
    }

    //
//    // utility method to create text node
    private Node getElements(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    public List<Obstacle> readObstacles() {
        try {
            ArrayList<Obstacle> obsList = new ArrayList<Obstacle>();
            File file = new File(OBSTACLE_FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
                this.createObstacleXML(new ArrayList<Obstacle>());
                return new ArrayList<Obstacle>();
            }
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
                    int obsWidth = Integer.valueOf(element.getElementsByTagName(OBSTACLE_WIDTH).item(0).getTextContent());
                    int obsHeight = Integer.valueOf(element.getElementsByTagName(OBSTACLE_HEIGHT).item(0).getTextContent());
                    int obsLength = Integer.valueOf(element.getElementsByTagName(OBSTACLE_LENGTH).item(0).getTextContent());
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

    public ArrayList<String> readAllAirports() throws IOException, SAXException, ParserConfigurationException {
        ArrayList<String> airportsNames = new ArrayList<String>();
        File airportDirectory = new File(AIRPORTS_DIRECTORY);

        if (airportDirectory.exists() && airportDirectory.isDirectory()) {

            //select only files that finish in .xml
            File[] xmlFiles = airportDirectory.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(".xml");
                }
            });

            for (File f : xmlFiles) {
                airportsNames.add(f.getName().split(".xml")[0]);
            }

            return airportsNames;
        } else {
            airportDirectory.mkdirs();
            return null;
        }
    }

    public Airport readAirport(String fileName) throws ParserConfigurationException, IOException, SAXException {

        File file = new File(AIRPORTS_DIRECTORY + File.separator + fileName);
        ArrayList<Runway> runways = new ArrayList<Runway>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        String airportName = ((Element) doc.getElementsByTagName(AIRPORT).item(0)).getAttribute(AIRPORT_ATTR_NAME);
        NodeList nodeList = doc.getElementsByTagName(RUNWAY);

        for (int s = 0; s < nodeList.getLength(); s++) {
            Node currentNode = nodeList.item(s);

            if (currentNode.getNodeType() == Node.ELEMENT_NODE) {

                Element element = ((Element) currentNode);
                String runwayId = element.getAttribute(ID);
                NodeList stripList = element.getElementsByTagName(STRIP);
                //Two strips for each runway in XML
                runways.add(new Runway(runwayId, this.getStrip(stripList.item(0)), this.getStrip(stripList.item(1))));
            }
        }
        return new Airport(runways, airportName);
    }

    public void addRunway(String airport, Runway runway) throws ParserConfigurationException, IOException, SAXException
    {
        File file = new File(AIRPORTS_DIRECTORY + File.separator + airport + ".xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        doc.getFirstChild().appendChild(makeRunwayElement(doc, runway));

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult myFile = new StreamResult(new FileOutputStream(file));
            transformer.transform(source, myFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeRunway(String airport, String runwayID) throws ParserConfigurationException, IOException, SAXException
    {
        File file = new File(AIRPORTS_DIRECTORY + File.separator + airport + ".xml");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();

        Node airportNode = doc.getElementsByTagName(AIRPORT).item(0);
        NodeList runwayList = ((Element)airportNode).getElementsByTagName(RUNWAY);

        for (int i = 0; i < runwayList.getLength(); i++)
        {
            Node node = runwayList.item(i);
            if (node.getAttributes().getNamedItem(ID).getNodeValue().equals(runwayID))
            {
                node.getParentNode().removeChild(node);
                break;
            }
        }

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult myFile = new StreamResult(new FileOutputStream(file));
            transformer.transform(source, myFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Strip getStrip(Node stripNode){

        if (stripNode.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) stripNode;
            String stripId = element.getAttribute(ID);
            int tora = Integer.valueOf(element.getElementsByTagName(TORA).item(0).getTextContent());
            int asda = Integer.valueOf(element.getElementsByTagName(ASDA).item(0).getTextContent());
            int toda = Integer.valueOf(element.getElementsByTagName(TODA).item(0).getTextContent());
            int lda = Integer.valueOf(element.getElementsByTagName(LDA).item(0).getTextContent());
            Values values = new Values(tora, asda, toda, lda);
            String position = element.getElementsByTagName(RUNWAY_POSITION).item(0).getTextContent();
            int threshold = Integer.valueOf(element.getElementsByTagName(RUNWAY_THRESHOLD).item(0).getTextContent());
            int orientation = Integer.valueOf(element.getElementsByTagName(RUNWAY_ORIENTATION).item(0).getTextContent());
            Strip strip = new Strip(stripId, orientation, position, values, threshold);
            return strip;
        }
        else return null;
    }
}