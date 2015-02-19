package Controller;

import Model.Airport;
import Model.Runway;
import Model.XMLHelper;

import java.util.List;

/**
 * Author(s) Petar, Metin, Tom
 * Controller class for handling interactions between Model and View
 */
public class Controller
{
    public Controller() {
        andrei = new XMLHelper();
        loadAirports();
    };

    private Airport airport;
    private List<String> availableAirports;
    private XMLHelper andrei;
    private Runway runway;

    public void setAirport(String airportName){
        //this.airport = this.andrei.readAirport(airportName);
    }

    private void loadAirports(){
        //this.availableAirports = andrei.readAllAirports();
    }
}
