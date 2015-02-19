package Model;

/**
 * Represents an obstacle that can be placed on a runway
 * @author Thomas, Metin
 */

import java.util.ArrayList;
import java.util.List;

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

    //TODO review necessity of method
    public ArrayList<Runway> getRunways() {
        return this.runways;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getRunwayIds() {
        List<String> runways = new ArrayList<String>();
        for (Runway r : this.runways){
            runways.add(r.getRunwayId());
        }

        return runways;
    }

    //not possible to pass an inexistent runway ID
    //user picks from combobox populated with getRunwayIds()
    public Runway getRunway(String runwayId){
        for (Runway r : this.runways) {
            if (r.getRunwayId().equals(runwayId)) {
                return r;
            }
        }

        return null;
    }
}
