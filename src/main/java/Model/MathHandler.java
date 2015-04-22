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
    public static final String NO_INSTRUCTION = "N/A";

    private Runway runway;
    private Integer obstacleHeight;
    private Integer obstacleLengthHalf;
    private Integer obstacleWidthHalf;
    private int aircraftBlastProtection;
    private Pair<Values, Calculations> valuesStrip1;
    private Pair<Values, Calculations> valuesStrip2;
    private int calculations;

    public MathHandler(Runway runway) {
        this.runway = runway;
        this.obstacleHeight = null;
        this.obstacleLengthHalf = null;
        this.obstacleWidthHalf = null;
        this.valuesStrip1 = null;
        this.valuesStrip2 = null;
        this.calculations = 1;
    };

    //TODO for now return original values if there is no obstacle
    public Pair<Values, Values> recalculateValues(int obstHeight, int obstLength,
                int obstWidth, int blastProtection) {

        this.obstacleHeight = obstHeight;
        this.obstacleLengthHalf = (int)Math.ceil(obstLength/2.0) * calculations;
        this.obstacleWidthHalf = (int)Math.ceil(obstWidth/2.0) * calculations;

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
        this.valuesStrip1 = calculateTakeOff(originalValues, positionFromLeft,
                runway.getStrip1().getDisplacedThreshold());
        Values recalculatedValuesStrip1 = this.valuesStrip1.getValue1();
        //get the LDA and way of landing
        Pair<Pair<Integer, String>, Calculations> landValues = calculateLanding
                (originalValues, recalculatedValuesStrip1, positionFromLeft);
        Pair<Integer, String> landing = landValues.getValue1();
        recalculatedValuesStrip1.setLda(landing.getValue1());
        recalculatedValuesStrip1.setLanding(landing.getValue2());
        //set the breakdown calculations
        this.runway.setBreakdownCalculations(1, valuesStrip1.getValue2().mergeCalculations(landValues.getValue2()));

        originalValues = this.runway.getStrip2().getOrigVal();

        //recalculate TORA, TODA, ASDA for strip 2
        this.valuesStrip2 = calculateTakeOff(originalValues, positionFromRight,
                runway.getStrip2().getDisplacedThreshold());
        Values recalculatedValuesStrip2 = this.valuesStrip2.getValue1();
        //get new LDA and way of landing
        landValues = calculateLanding(originalValues, recalculatedValuesStrip2, positionFromRight);
        landing = landValues.getValue1();
        recalculatedValuesStrip2.setLda(landing.getValue1());
        recalculatedValuesStrip2.setLanding(landing.getValue2());
        //set the breakdown calculations
        this.runway.setBreakdownCalculations(2, valuesStrip2.getValue2().mergeCalculations(landValues.getValue2()));

        return new Pair<Values, Values>(recalculatedValuesStrip1, recalculatedValuesStrip2);
    }

    private Pair<Values, Calculations> calculateTakeOff(Values stripValues, int distance, int threshold)
    {
        Pair<Values, Calculations> takingOffAway = this.takeOffAway(stripValues, distance, threshold);
        Values takeOffAway = takingOffAway.getValue1();
        Pair<Values, Calculations> takingOffTowards = this.takeOffTowards(distance, threshold);
        Values takeOffTowards = takingOffTowards.getValue1();
        if (takeOffAway.getTora() > takeOffTowards.getTora()){
            takeOffAway.setTakeoff(TAKEOFF_AWAY);
            return new Pair<Values, Calculations>(takeOffAway, takingOffAway.getValue2());
        }
        else {
            takeOffTowards.setTakeoff(TAKEOFF_TOWARDS);
            return new Pair<Values, Calculations>(takeOffTowards, takingOffTowards.getValue2());
        }
    }

    private Pair<Values, Calculations> takeOffAway(Values stripValues, int distance, int threshold)
    {
        int distanceFromObstacle = Math.max(this.aircraftBlastProtection, RESA + STRIPEND_THRESHOLD) - obstacleLengthHalf;
        //initialize calculations
        Calculations calculations = new Calculations();
        calculations.startTakeOffCalcs(MathHandler.TAKEOFF_AWAY);
        int[] initialValuesTora = {stripValues.getTora(), this.aircraftBlastProtection, RESA, STRIPEND_THRESHOLD,
                    distance, threshold};
        calculations.makeSubstitutions(Calculations.TORA, initialValuesTora);
        //create calculation units
        int safeDistanceResa = RESA + STRIPEND_THRESHOLD;
        calculations.makeCalculation(Calculations.TORA, safeDistanceResa);
        int safeDistance = Math.max(this.aircraftBlastProtection, safeDistanceResa);
        calculations.makeCalculation(Calculations.TORA, safeDistance);
        //make calculations
        int tempTora = stripValues.getTora() - safeDistance - distance - threshold - obstacleLengthHalf;
        calculations.finishCalculations(Calculations.TORA, tempTora);
        //more breakdown
        int[] initialValuesToda = {tempTora, stripValues.getToda(), stripValues.getTora()};
        calculations.makeSubstitutions(Calculations.TODA, initialValuesToda);
        int[] initialValuesAsda = {tempTora, stripValues.getAsda(), stripValues.getTora()};
        calculations.makeSubstitutions(Calculations.ASDA, initialValuesAsda);
        //actual calculations
        int tempAsda = tempTora + stripValues.getAsda() - stripValues.getTora();
        calculations.finishCalculations(Calculations.ASDA, tempAsda);
        int tempToda = tempTora + stripValues.getToda() - stripValues.getTora();
        calculations.finishCalculations(Calculations.TODA, tempToda);
        //save values
        Values values = new Values(tempTora, tempAsda, tempToda, 0);
        //used for visualization
        values.setTakeoffDistanceFormObject(distanceFromObstacle);
        return new Pair<Values, Calculations>(values, calculations);
    }

    private Pair<Values, Calculations> takeOffTowards(int distance, int threshold)
    {
        //initialize calculations
        Calculations calculations = new Calculations();
        calculations.startTakeOffCalcs(MathHandler.TAKEOFF_TOWARDS);
        int[] initialValuesTora = {distance, threshold, this.obstacleHeight, RESA,
                    STRIPEND_THRESHOLD};
        calculations.makeSubstitutions(Calculations.TORA, initialValuesTora);
        //create calculation units
        int slope = this.obstacleHeight*50;
        calculations.makeCalculation(Calculations.TORA, slope);
        int safeDistance = Math.max(slope, RESA);
        calculations.makeCalculation(Calculations.TORA, safeDistance);
        //calculations
        int tempTora = distance + threshold - safeDistance - STRIPEND_THRESHOLD - obstacleLengthHalf;
        calculations.finishCalculations(Calculations.TORA, tempTora);

        calculations.finishCalculations(Calculations.TODA, tempTora);
        calculations.finishCalculations(Calculations.ASDA, tempTora);
        //toda and asda are equal to the tora as the obstacle blocks the clearway and stopway
        return new Pair<Values, Calculations>(new Values(tempTora, tempTora, tempTora, 0), calculations);
    }

    private Pair<Pair<Integer, String>, Calculations> calculateLanding(Values stripValues, Values recValues,  int distance)
    {
        //land over
        Pair<Pair<Integer, Integer>, Calculations> landOverCalcs = landOver(stripValues, distance);
        Pair<Integer, Integer> landingOver = landOverCalcs.getValue1();
        int landOver = landingOver.getValue1();
        //land towards
        Pair<Integer, Calculations> landingTowards = landTowards(distance);
        int landTowards = landingTowards.getValue1();

        if (landOver >= landTowards){
            recValues.setLandingDistanceFormObject(landingOver.getValue2());
           return new Pair(new Pair<Integer, String>(landOver, LAND_OVER), landOverCalcs.getValue2());
        }
        else {
            return new Pair(new Pair<Integer, String>(landTowards, LAND_TOWARDS), landingTowards.getValue2());
        }
    }

    private Pair<Pair<Integer, Integer>, Calculations> landOver(Values originalStripValues, int distance)
    {
        //initialize calculations
        Calculations calculations = new Calculations();
        calculations.startLandingCalcs(MathHandler.LAND_OVER);
        int[] initialValuesLda = {originalStripValues.getLda(), distance, STRIPEND_THRESHOLD, RESA,
                this.aircraftBlastProtection, obstacleHeight};
        calculations.makeSubstitutions(Calculations.LDA, initialValuesLda);
        int distanceFromObstacle = Math.max(Math.max(RESA, this.aircraftBlastProtection), this.obstacleHeight*50) - STRIPEND_THRESHOLD
                - this.obstacleLengthHalf;
        //create calculation units
        int slope = this.obstacleHeight*50;
        calculations.makeCalculation(Calculations.LDA, slope);
        int safeDistanceResa = Math.max(RESA, this.aircraftBlastProtection);
        calculations.makeCalculation(Calculations.LDA, safeDistanceResa);
        int safeDistance = Math.max(safeDistanceResa, slope);
        calculations.makeCalculation(Calculations.LDA, safeDistance);
        //actual calculations
        int lda = (originalStripValues.getLda() - distance - STRIPEND_THRESHOLD - safeDistance - obstacleLengthHalf);
        calculations.finishCalculations(Calculations.LDA, lda);
        return new Pair<Pair<Integer, Integer>, Calculations>
                (new Pair<Integer, Integer>((Math.min(originalStripValues.getLda(), lda)), distanceFromObstacle), calculations);
    }

    private Pair<Integer, Calculations> landTowards(int distance)
    {
        //initialize calculations
        Calculations calculations = new Calculations();
        calculations.startLandingCalcs(MathHandler.LAND_TOWARDS);
        int[] initialValuesLda = {distance, STRIPEND_THRESHOLD, RESA};
        calculations.makeSubstitutions(Calculations.LDA, initialValuesLda);
        //create calculation units
        int lda = distance - STRIPEND_THRESHOLD - RESA - obstacleLengthHalf;
        calculations.finishCalculations(Calculations.LDA, lda);
        return new Pair<Integer, Calculations>(lda, calculations);
    }

    public void setSimpleCalculations()
    {
        this.calculations = 0;
    };

    public void setComplexCalculations()
    {
        this.calculations = 1;
    };
}
