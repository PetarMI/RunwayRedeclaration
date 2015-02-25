package Model;

/**

 */
public class MathHandler
{
    private final static int CENTRELINE_THRESHOLD = 75;
    private final static int STRIPEND_THRESHOLD = 60;
    //TODO unhardcode
    private final static int BLAST_PROTECTION_VALUE = 300;
    private final static int RESA = 240;

    private static final String LAND_OVER = "Land over";
    private static final String LAND_TOWARDS = "Land towards";
    private static final String TAKEOFF_AWAY = "TakeOff Away";
    private static final String TAKEOFF_TOWARDS = "TakeOff Towards";

    private Runway runway;
    private Integer obstacleHeight;
    private Values recalculatedValuesStrip1;
    private Values recalculatedValuesStrip2;

    public MathHandler(Runway runway) {
        this.runway = runway;
        this.obstacleHeight = null;
        this.recalculatedValuesStrip1 = null;
        this.recalculatedValuesStrip2 = null;
    };

    //TODO consider if no exception
    public Pair<Values, Values> recalculateValues(int obstHeight){
        if (runway.getObstacleDistanceFromCentreline() > CENTRELINE_THRESHOLD &&
                runway.getObstaclePosition() > STRIPEND_THRESHOLD){
            //TODO maybe throw custom exception saying we don't have an obstacle and values are the same
            return null;
        }

        this.obstacleHeight = obstHeight;

        Values originalValues = this.runway.getStrip1().getOrigVal();
        int position = runway.getObstaclePosition();

        //recalculate TORA, TODA, ASDA for strip 1
        recalculatedValuesStrip1 = calculateTakeOff(originalValues, position);
        //get the LDA and way of landing
        Pair<Integer, String> landing = calculateLanding(originalValues, position);
        recalculatedValuesStrip1.setLda(landing.getValue1());
        recalculatedValuesStrip1.setLanding(landing.getValue2());

        originalValues = this.runway.getStrip2().getOrigVal();
        position = originalValues.getTora() - runway.getObstaclePosition();

        //recalculate TORA, TODA, ASDA for strip 12
        recalculatedValuesStrip2 = calculateTakeOff(originalValues, position);
        //get new LDA and way of landing
        landing = calculateLanding(originalValues, position);
        recalculatedValuesStrip2.setLda(landing.getValue1());
        recalculatedValuesStrip2.setLanding(landing.getValue2());

        return new Pair<Values, Values>(recalculatedValuesStrip1, recalculatedValuesStrip2);
    }

    private Values calculateTakeOff(Values stripValues, int position){
        Values takeOffAway = this.takeOffAway(stripValues, position);
        Values takeOffTowards = this.takeOffTowards(stripValues, position);
        if (takeOffAway.getTora() > takeOffTowards.getTora()){
            takeOffAway.setTakeoff(TAKEOFF_AWAY);
            return takeOffAway;
        }
        else {
            takeOffTowards.setTakeoff(TAKEOFF_TOWARDS);
            return takeOffTowards;
        }
    }

    private Values takeOffAway(Values stripValues, int position){
        int tempTora = stripValues.getTora() - Math.max(BLAST_PROTECTION_VALUE, RESA + STRIPEND_THRESHOLD) - position;
        int tempAsda = tempTora + stripValues.getAsda() - stripValues.getTora();
        int tempToda = tempTora + stripValues.getToda() - stripValues.getTora();

        return new Values(tempTora, tempAsda, tempToda, 0);
    }

    private Values takeOffTowards(Values stripValues, int position){
        int tempTora = position - Math.max(this.obstacleHeight*50, RESA) - STRIPEND_THRESHOLD;

        //toda and asda are equal to the tora as the obstacle blocks the clearway and stopway
        return new Values(tempTora, tempTora, tempTora, 0);
    }

    private Pair<Integer, String> calculateLanding(Values stripValues, int position) {
        int landOver = landOver(stripValues, position);
        int landTowards = landTowards(stripValues, position);

        if (landOver >= landTowards){
           return new Pair<Integer, String>(landOver, LAND_OVER);
        }
        else {
            return new Pair<Integer, String>(landTowards, LAND_TOWARDS);
        }
    }

    private int landOver(Values originalStripValues, int position) {
        int lda = (originalStripValues.getTora() - position - STRIPEND_THRESHOLD -
                Math.max(Math.max(RESA, BLAST_PROTECTION_VALUE), this.obstacleHeight*50));
        return Math.min(originalStripValues.getLda(), lda);
    }

    private int landTowards(Values originalStripValues, int position) {
        return (position - STRIPEND_THRESHOLD - RESA - originalStripValues.getThreshold());
    }
}