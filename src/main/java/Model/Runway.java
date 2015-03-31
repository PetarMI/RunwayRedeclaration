package Model;

public class Runway
{
    public static final String OBSTACLE_ABOVE = "Above";
    public static final String OBSTACLE_BELOW = "Below";

    private String runwayId;
    private Strip strip1;
    private Strip strip2;
    private Obstacle obstacle;
    private Integer positionFromLeftDT;
    private Integer positionFromRightDT;
    private Integer distanceFromCentreline;
    private String positionFromCentreline;
    private MathHandler mathHandler;

    public Runway(String runwayId, Strip strip1, Strip strip2) {
        this.runwayId = runwayId;
        this.strip1 = strip1;
        this.strip2 = strip2;
        this.obstacle = null;
        this.positionFromLeftDT = null;
        this.positionFromRightDT = null;
        this.distanceFromCentreline = null;
        this.mathHandler = new MathHandler(this);
    }

    public String getRunwayId() {
        return runwayId;
    }

    public Strip getStrip1() { return strip1; }

    public Strip getStrip2() { return strip2; }

    public void addObstacle(Obstacle obstacle, int obstaclePositionFromLeft,
            int obstaclePositionFromRight, int distanceFromCentreline, String position){
        this.obstacle = obstacle;
        this.positionFromLeftDT = obstaclePositionFromLeft;
        this.positionFromRightDT = obstaclePositionFromRight;
        this.distanceFromCentreline = distanceFromCentreline;
        this.positionFromCentreline = position;
    }

    //pass blast allowance specific to the landing aircraft
    public void recalculateValues(int aircraftBlastProtection) {
        Pair<Values, Values> recVals = this.mathHandler.recalculateValues(this.obstacle.getHeight(),
                this.obstacle.getLength(), this.obstacle.getWidth(), aircraftBlastProtection);
        this.strip1.setRecVal(recVals.getValue1());
        this.strip2.setRecVal(recVals.getValue2());
    }

    public int getCompassHeading()
    {
        int heading = Integer.parseInt(this.runwayId.substring(0, 2))*10;
        return (90 - heading);
    }

    public int getDistanceFromCentrelineFor3D()
    {
        if (this.positionFromCentreline.equals(OBSTACLE_ABOVE))
        {
            return this.distanceFromCentreline;
        }
        else
        {
            return (0 - this.distanceFromCentreline);
        }
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public int getPositionFromLeftDT(){
        return this.positionFromLeftDT;
    }

    public int getPositionFromRightDT(){
        return this.positionFromRightDT;
    }

    public int getObstacleDistanceFromCentreline(){
        return this.distanceFromCentreline;
    }

    public void setSimpleCalculations()
    {
        this.mathHandler.setSimpleCalculations();
    }

    public void setComplexCalculations()
    {
        this.mathHandler.setComplexCalculations();
    }

    public void setBreakdownCalculations(Integer strip, Calculations calcs)
    {
        if (strip == 1)
        {
            this.strip1.setCalculationBreakdown(calcs);
        }
        else
        {
            this.strip2.setCalculationBreakdown(calcs);
        }
    }

    @Override
    public String toString() {
        return "Runway{" +
                "runwayId='" + runwayId + '\'' +
                ", strip1=" + strip1 +
                ", strip2=" + strip2 +
                '}';
    }
}