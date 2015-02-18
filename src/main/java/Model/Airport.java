package Model;

/**
 * Represents an obstacle that can be placed on a runway
 * @author Thomas, Metin
 */

import java.util.ArrayList;

public class Airport {

    private ArrayList<Runway> runways;
    private String name;

    public Airport(ArrayList<Runway> runways, String name) {
        this.runways = runways;
        this.name = name;
    }

    public void addRunway (Runway runway) {
        runways.add(runway);
    }

    public void removeRunway (Runway runway) {
        runways.remove(runway);
    }

    public ArrayList<Runway> getRunways() {
        return this.runways;
    }

    public String getName() {
        return this.name;
    }
}
