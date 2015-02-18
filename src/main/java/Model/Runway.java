package Model;
import java.awt.geom.Point2D;

public class Runway {


    private String runwayId;
    // Unsure whether we need to store these in runway, could be held in the lower strip and called from there
    private int degree;
    private String position;
    private Strip lower; // Maybe rename these -> maybe rename then strip1 and strip2, makes it straight forward.
    private Strip higher;


    // Any way to give this less paramters? Maybe Include a strip as a parameter?
    public Runway(String runwayId, int degree, String position, int ltora, int lasda, int ltoda, int llda, int rtora, int rasda, int rtoda, int rlda, int displacedThreshold) {
        this.runwayId = runwayId;
        this.degree = degree;
        this.position = position;
        this.lower = new Strip(Integer.toString(degree).concat(position), ltora, lasda, ltoda, llda, displacedThreshold);
        String altPosition = position;
        if(position.equals("L"))
            altPosition = "R";  //what if we have 3 parallel runways then we also need a 'C'
        this.higher = new Strip(Integer.toString(degree+18).concat(altPosition), rtora, rasda, rtoda, rlda, displacedThreshold);
    }

    // Constructor that does not need a position
    public Runway(String runwayId, int degree, int ltora, int lasda, int ltoda, int llda, int rtora, int rasda, int rtoda, int rlda, int displacedThreshold) {
        this.runwayId = runwayId;
        this.degree = degree;
        this.lower = new Strip(Integer.toString(degree), ltora, lasda, ltoda, llda, displacedThreshold);
        this.higher = new Strip(Integer.toString(degree+18), rtora, rasda, rtoda, rlda, displacedThreshold);
    }

    public String getRunwayId() {
        return runwayId;
    }

    public Strip getLower() { return lower; }

    public Strip getHigher() { return higher; }

    public int getOrientation() {
        return degree;
    }

    public String getPosition() {
        return position;
    }
}
