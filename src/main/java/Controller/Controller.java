package Controller;

import Model.Airport;
import Model.Runway;
import Model.XMLHelper;
import View.BeginFrame;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

/**
 * Author(s) Petar, Metin, Tom
 * Controller class for handling interactions between Model and View
 */
public class Controller {

    private Airport airport;
    private List<String> availableAirports;
    private XMLHelper xmlHelper;
    private Runway runway;
    private static BeginFrame currentFrame;

    public Controller() {
        xmlHelper = new XMLHelper();
        loadAirports();
    }

    public void setAirport(String airportName){
        try {
            this.airport = this.xmlHelper.readAirport(airportName);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void loadAirports(){
        try {
            this.availableAirports = xmlHelper.readAllAirports();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
