package Model;

public class Runway
{

    private String runwayId;
    private Strip strip1;
    private Strip strip2;

    public Runway(String runwayId, Strip strip1, Strip strip2) {
        this.runwayId = runwayId;
        this.strip1 = strip1;
        this.strip2 = strip2;
    }

    public String getRunwayId() {
        return runwayId;
    }

    public Strip getStrip1() { return strip1; }

    public Strip getStrip2() { return strip2; }

    @Override
    public String toString() {
        return "Runway{" +
                "runwayId='" + runwayId + '\'' +
                ", strip1=" + strip1 +
                ", strip2=" + strip2 +
                '}';
    }
}
