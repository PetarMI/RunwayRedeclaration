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

        //recalculate values for strip 1
        recalculatedValuesStrip1 = calculateTakeOff(originalValues, position);
        recalculatedValuesStrip1.setLda(calculateLanding(originalValues, position));

        originalValues = this.runway.getStrip2().getOrigVal();
        position = originalValues.getTora() - runway.getObstaclePosition();

        recalculatedValuesStrip2 = calculateTakeOff(originalValues, position);
        recalculatedValuesStrip2.setLda(calculateLanding(originalValues, position));

        return new Pair<Values, Values>(recalculatedValuesStrip1, recalculatedValuesStrip2);
    }

    private Values calculateTakeOff(Values stripValues, int position){
        Values takeOffAway = this.takeOffAway(stripValues, position);
        Values takeOffTowards = this.takeOffTowards(stripValues, position);
        if (takeOffAway.getTora() > takeOffTowards.getTora()){
            return takeOffAway;
        }
        else {
            return takeOffTowards;
        }
    }

    private Values takeOffAway(Values stripValues, int position){
        int tempTora = stripValues.getTora() - Math.max(BLAST_PROTECTION_VALUE, RESA + STRIPEND_THRESHOLD)
                - (stripValues.getThreshold() - position );
        int tempAsda = stripValues.getTora() + stripValues.getAsda() - stripValues.getTora();
        int tempToda = stripValues.getTora() + stripValues.getToda() - stripValues.getTora();

        return new Values(tempTora, tempAsda, tempToda, 0);
    }

    private Values takeOffTowards(Values stripValues, int position){
        int tempTora = position - Math.max(this.obstacleHeight*50, RESA) - STRIPEND_THRESHOLD;

        //toda and asda are equal to the tora as the obstacle blocks the clearway and stopway
        return new Values(tempTora, tempTora, tempTora, 0);
    }

    private int calculateLanding(Values stripValues, int position) {
        return Math.max(landOver(stripValues, position), landTowards(stripValues, position));
    }

    private int landOver(Values stripValues, int position) {
        return (stripValues.getLda() - position - STRIPEND_THRESHOLD -
                Math.max(Math.max(RESA, BLAST_PROTECTION_VALUE), this.obstacleHeight*50));
    }

    private int landTowards(Values stripValues, int position) {
        return (stripValues.getLda() - position - STRIPEND_THRESHOLD - RESA);
    }
}
