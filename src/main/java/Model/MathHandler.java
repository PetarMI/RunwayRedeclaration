package Model;

/**

 */
public class MathHandler
{
    public final static int CENTRELINE_THRESHOLD = 75;
    public final static int STRIPEND_THRESHOLD = 60;

    public final static int RESA = 240;

    public static final String LAND_OVER = "Land over";
    public static final String LAND_TOWARDS = "Land towards";
    public static final String TAKEOFF_AWAY = "TakeOff Away";
    public static final String TAKEOFF_TOWARDS = "TakeOff Towards";

    private Runway runway;
    private Integer obstacleHeight;
    private Integer obstacleLengthHalf;
    private Integer obstacleWidthHalf;
    private int aircraftBlastProtection;
    private Values recalculatedValuesStrip1;
    private Values recalculatedValuesStrip2;

    public MathHandler(Runway runway) {
        this.runway = runway;
        this.obstacleHeight = null;
        this.obstacleLengthHalf = null;
        this.obstacleWidthHalf = null;
        this.recalculatedValuesStrip1 = null;
        this.recalculatedValuesStrip2 = null;
    };

    //TODO for now return original values if there is no obstacle
    public Pair<Values, Values> recalculateValues(int obstHeight, int obstLength,
                int obstWidth, int blastProtection) {

        this.obstacleHeight = obstHeight;
        this.obstacleLengthHalf = (int)Math.ceil(obstLength/2.0);
        this.obstacleWidthHalf = (int)Math.ceil(obstWidth/2.0);

        //decide if we have to recalculate the runway distances
        if ((runway.getObstacleDistanceFromCentreline() - obstacleWidthHalf) > CENTRELINE_THRESHOLD ||
                (((runway.getStrip1().getDisplacedThreshold() + STRIPEND_THRESHOLD
                        + runway.getPositionFromLeftDT() + obstacleLengthHalf) < 0) ||
                        ((runway.getStrip2().getDisplacedThreshold() + STRIPEND_THRESHOLD
                                + runway.getPositionFromRightDT() + obstacleLengthHalf) < 0))){
            //TODO maybe throw custom exception saying we don't have an obstacle and values are the same
            return new Pair<Values, Values>(this.runway.getStrip1().getOrigVal(), this.runway.getStrip2().getOrigVal());
        }

        this.aircraftBlastProtection = blastProtection;

        Values originalValues = this.runway.getStrip1().getOrigVal();
        int positionFromLeft = runway.getPositionFromLeftDT();
        int positionFromRight = runway.getPositionFromRightDT();

        //recalculate TORA, TODA, ASDA for strip 1
        recalculatedValuesStrip1 = calculateTakeOff(originalValues, positionFromLeft,
                runway.getStrip1().getDisplacedThreshold());
        //get the LDA and way of landing
        Pair<Integer, String> landing = calculateLanding(originalValues, recalculatedValuesStrip1, positionFromLeft);
        recalculatedValuesStrip1.setLda(landing.getValue1());
        recalculatedValuesStrip1.setLanding(landing.getValue2());

        originalValues = this.runway.getStrip2().getOrigVal();

        //recalculate TORA, TODA, ASDA for strip 2
        recalculatedValuesStrip2 = calculateTakeOff(originalValues, positionFromRight,
                runway.getStrip2().getDisplacedThreshold());
        //get new LDA and way of landing
        landing = calculateLanding(originalValues, recalculatedValuesStrip2, positionFromRight);
        recalculatedValuesStrip2.setLda(landing.getValue1());
        recalculatedValuesStrip2.setLanding(landing.getValue2());

        return new Pair<Values, Values>(recalculatedValuesStrip1, recalculatedValuesStrip2);
    }

    private Values calculateTakeOff(Values stripValues, int distance, int threshold){
        Values takeOffAway = this.takeOffAway(stripValues, distance, threshold);
        Values takeOffTowards = this.takeOffTowards(distance, threshold);
        if (takeOffAway.getTora() > takeOffTowards.getTora()){
            takeOffAway.setTakeoff(TAKEOFF_AWAY);
            return takeOffAway;
        }
        else {
            takeOffTowards.setTakeoff(TAKEOFF_TOWARDS);
            return takeOffTowards;
        }
    }

    private Values takeOffAway(Values stripValues, int distance, int threshold){
        int distanceFromObstacle = Math.max(this.aircraftBlastProtection, RESA + STRIPEND_THRESHOLD) - obstacleLengthHalf;
        int tempTora = stripValues.getTora() - Math.max(this.aircraftBlastProtection, RESA + STRIPEND_THRESHOLD)
                - distance - threshold - obstacleLengthHalf;
        int tempAsda = tempTora + stripValues.getAsda() - stripValues.getTora();
        int tempToda = tempTora + stripValues.getToda() - stripValues.getTora();
        Values values = new Values(tempTora, tempAsda, tempToda, 0);
        values.setTakeoffDistanceFormObject(distanceFromObstacle);
        return values;
    }

    private Values takeOffTowards(int distance, int threshold){
        int tempTora = distance - obstacleLengthHalf + threshold - Math.max(this.obstacleHeight*50, RESA)
                - STRIPEND_THRESHOLD;

        //toda and asda are equal to the tora as the obstacle blocks the clearway and stopway
        return new Values(tempTora, tempTora, tempTora, 0);
    }

    private Pair<Integer, String> calculateLanding(Values stripValues, Values recValues,  int distance) {
        Pair<Integer, Integer> landingOver = landOver(stripValues, distance);
        int landOver = landingOver.getValue1();
        int landTowards = landTowards(distance);

        if (landOver >= landTowards){
            recValues.setLandingDistanceFormObject(landingOver.getValue2());
           return new Pair<Integer, String>(landOver, LAND_OVER);
        }
        else {
            return new Pair<Integer, String>(landTowards, LAND_TOWARDS);
        }
    }

    private Pair<Integer, Integer> landOver(Values originalStripValues, int distance) {
        int distanceFromObstacle = Math.max(Math.max(RESA, this.aircraftBlastProtection), this.obstacleHeight*50) - STRIPEND_THRESHOLD
                - this.obstacleLengthHalf;
        int lda = (originalStripValues.getLda() - distance - STRIPEND_THRESHOLD - obstacleLengthHalf -
                Math.max(Math.max(RESA, this.aircraftBlastProtection), this.obstacleHeight*50));
        return new Pair<Integer, Integer>(Math.min(originalStripValues.getLda(), lda), distanceFromObstacle);
    }

    private int landTowards(int distance) {
        return (distance - obstacleLengthHalf - STRIPEND_THRESHOLD - RESA);
    }
}
