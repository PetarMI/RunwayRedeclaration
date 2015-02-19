package Model;

public class Runway
{

    private String runwayId;
    private Strip strip1;
    private Strip strip2;

    public Runway(Strip strip1, Strip strip2) {
        this.strip1 = strip1;
        this.strip2 = strip2;
        this.runwayId = strip1.getDesignator() + "/" + strip2.getDesignator();
    }

    public String getRunwayId() {
        return runwayId;
    }

    public Strip getStrip1() { return strip1; }

    public Strip getStrip2() { return strip2; }
}
