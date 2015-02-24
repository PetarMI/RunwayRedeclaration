package Model;

public class Runway
{

    private String runwayId;
    private Strip strip1;
    private Strip strip2;
    private Obstacle obstacle;
    private Integer obstaclePosition;
    private Integer distanceFromCentreline;
    private MathHandler mathHandler;

    public Runway(String runwayId, Strip strip1, Strip strip2) {
        this.runwayId = runwayId;
        this.strip1 = strip1;
        this.strip2 = strip2;
        this.obstacle = null;
        this.obstaclePosition = null;
        this.distanceFromCentreline = null;
        this.mathHandler = new MathHandler(this);
    }

    public String getRunwayId() {
        return runwayId;
    }

    public Strip getStrip1() { return strip1; }

    public Strip getStrip2() { return strip2; }

    public void addObstacle(Obstacle obstacle, int obstaclePosition, int distanceFromCentreline){
        this.obstacle = obstacle;
        this.obstaclePosition = obstaclePosition;
        this.distanceFromCentreline = distanceFromCentreline;
    }

    //TODO pass blast allowance
    public void recalculateValues() {
        Pair<Values, Values> recVals = this.mathHandler.recalculateValues(this.obstacle.getHeight());
        this.strip1.setRecVal(recVals.getValue1());
        this.strip2.setRecVal(recVals.getValue2());
    }

    public int getObstaclePosition(){
        return this.obstaclePosition;
    }

    public int getObstacleDistanceFromCentreline(){
        return this.distanceFromCentreline;
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
