package Model;

import java.util.ArrayList;

public class Airport {

    private ArrayList<Runway> runways;
    private String name;

    public Airport(ArrayList<Runway> runways, String name) {
        this.runways = runways;
        this.name = name;
    }

    public ArrayList<Runway> getRunways() {
        return runways;
    }

    public String getName() {
        return name;
    }
}
