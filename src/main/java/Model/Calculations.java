package Model;

import java.util.*;

/**
 * Store the calculations for each strip
 */
public class Calculations
{
    //guys I really don't like putting quotes everywhere, don't judge
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String TIMES = "*";
    public static final String TORA = "Tora";
    private static final String NEW_TORA = "New Tora";
    public static final String TODA = "Toda";
    public static final String ASDA = "Asda";
    public static final String LDA = "Lda";
    private static final String MAX = "MAX";
    private static final String LB = "(";
    private static final String RB = ")";
    private static final String THRESHOLD_DISTANCE = "ThresholdDistance";
    private static final String THRESHOLD = "Threshold";
    private static final String RESA = "RESA";
    private static final String STRIPEND_THRESHOLD = "StripendThreshold";
    private static final String OBSTACLEHEIGHT = "ObstacleHeight";
    private static final String BLAST_PROTECTION = "BlastProtection";
    private static final String COMMA = ",";

    private static final String EQUALS = "=";
    private static final String NEWLINE = "\n";

    //to know which elements to substitude
    private Set<String> values;

    private List<ArrayList<String>> toraCalcs;
    private List<ArrayList<String>> todaCalcs;
    private List<ArrayList<String>> asdaCalcs;
    private List<ArrayList<String>> ldaCalcs;

    public Calculations()
    {
        this.toraCalcs = new ArrayList<ArrayList<String>>();
        this.todaCalcs = new ArrayList<ArrayList<String>>();
        this.asdaCalcs = new ArrayList<ArrayList<String>>();
        this.ldaCalcs = new ArrayList<ArrayList<String>>();

        this.values = new HashSet<String>();
        this.values.add(TORA);
        this.values.add(NEW_TORA);
        this.values.add(TODA);
        this.values.add(ASDA);
        this.values.add(LDA);
        this.values.add(THRESHOLD_DISTANCE);
        this.values.add(THRESHOLD);
        this.values.add(RESA);
        this.values.add(STRIPEND_THRESHOLD);
        this.values.add(OBSTACLEHEIGHT);
        this.values.add(BLAST_PROTECTION);
    }

    public void startTakeOffCalcs(String type)
    {
        this.startToraCalcs(type);
        this.startTodaCalcs(type);
        this.startAsdaCalcs(type);
    }


    public void startLandingCalcs(String type)
    {
        if (type.equals(MathHandler.LAND_OVER))
        {
            this.ldaCalcs.add(new ArrayList<String>(Arrays.asList(LDA, MINUS, THRESHOLD_DISTANCE,
                    MINUS, STRIPEND_THRESHOLD, MINUS, MAX, LB, MAX, LB, RESA, COMMA, BLAST_PROTECTION, RB,
                    COMMA, OBSTACLEHEIGHT, TIMES, "50", RB)));
        }
        else
        {
            this.ldaCalcs.add(new ArrayList<String>(Arrays.asList(THRESHOLD_DISTANCE, MINUS,
                    STRIPEND_THRESHOLD, MINUS, RESA)));
        }
    }

    private void startToraCalcs(String type)
    {
        if (type.equals(MathHandler.TAKEOFF_AWAY))
        {
            this.toraCalcs.add(new ArrayList<String>(Arrays.asList(TORA, MINUS, MAX, LB, BLAST_PROTECTION,
                    COMMA, RESA, PLUS, STRIPEND_THRESHOLD, RB, MINUS, THRESHOLD_DISTANCE, MINUS, THRESHOLD)));

        }
        else
        {
            this.toraCalcs.add(new ArrayList<String>(Arrays.asList(THRESHOLD_DISTANCE, PLUS,
                    THRESHOLD, MINUS, MAX, LB, OBSTACLEHEIGHT, TIMES, "50", COMMA, RESA, RB, MINUS, STRIPEND_THRESHOLD)));
        }
    }

    private void startTodaCalcs(String type)
    {
        if (type.equals(MathHandler.TAKEOFF_AWAY))
        {
            this.todaCalcs.add(new ArrayList<String>(Arrays.asList(NEW_TORA, PLUS, TODA, MINUS, TORA)));
        }
        else
        {
            this.todaCalcs.add(new ArrayList<String>(Arrays.asList(NEW_TORA)));
        }
    }

    private void startAsdaCalcs(String type)
    {
        if (type.equals(MathHandler.TAKEOFF_AWAY))
        {
            this.asdaCalcs.add(new ArrayList<String>(Arrays.asList(NEW_TORA, PLUS, ASDA, MINUS, TORA)));
        }
        else
        {
            this.asdaCalcs.add(new ArrayList<String>(Arrays.asList(NEW_TORA)));
        }
    }

    public void makeCalculation(String segment, int value)
    {
        List<String> currentRowCalculation = this.findLastCalculation(segment);
        Pair<Integer, Integer> portionToSimplify = this.findSimplification(currentRowCalculation);
        List<ArrayList<String>> currentCalculation = this.findSegmentCalculation(segment);
        currentCalculation.add(this.createNewRow(currentRowCalculation, portionToSimplify, Integer.toString(value)));
    }

    private Pair<Integer, Integer> findSimplification(List<String> row)
    {
        int inBrackets = 0;
        /*System.out.println("Simplifying:");
        for (String str : row)
        {
            System.out.print(str + " ");
        }
        System.out.println();*/

        int simplifyStart = 0, simplifyEnd = 0;
        for (int pos = 0; pos < row.size(); pos++)
        {
            String current = row.get(pos);
            if (current.equals(LB))
            {
                //System.out.println("Found ( at " + pos);
                inBrackets++;
                simplifyStart = pos - 1;
            }
            if ((inBrackets > 0) && (current.equals(TIMES) || current.equals(PLUS)))
            {
                //System.out.println("Brackets " + current + " " + inBrackets + (pos - 1) + " " + (pos + 1));
                return new Pair(pos - 1, pos + 1);
            }
            if (current.equals(RB))
            {
                //System.out.println("Found ) at " + pos);
                inBrackets--;
                if (simplifyEnd == 0)
                {
                    simplifyEnd = pos;
                }
                if (inBrackets == 0)
                {
                    //System.out.println(simplifyStart + " " + simplifyEnd);
                    return new Pair<Integer, Integer>(simplifyStart, simplifyEnd);
                }
            }
        }
        //System.out.println(simplifyStart + " " + simplifyEnd + " ");
        return new Pair<Integer, Integer>(simplifyStart, simplifyEnd);
    }

    public void makeSubstitutions(String segment, int[] values)
    {
        int noValue = 0;
        List<String> currentRowCalculation = this.findLastCalculation(segment);
        ArrayList<String> newRow = new ArrayList<String>();
        List<ArrayList<String>> currentCalculation = this.findSegmentCalculation(segment);
        for(int pos = 0; pos < currentRowCalculation.size(); pos++)
        {
            String currentToken = currentRowCalculation.get(pos);
            if (this.values.contains(currentToken))
            {
                int value = values[noValue];
                if (value < 0)
                {
                    newRow.add(LB);
                    newRow.add(Integer.toString(values[noValue]));
                    newRow.add(RB);
                }
                else
                {
                    newRow.add(Integer.toString(values[noValue]));
                }
                noValue++;
            }
            else
            {
                newRow.add(currentToken);
            }
        }
        currentCalculation.add(newRow);
        ArrayList<String> simplified = doubleNegative(newRow);
        if(simplified != null) {
            currentCalculation.add(simplified);
        }
    }

    private ArrayList<String> doubleNegative(List<String> row)
    {
        ArrayList<String> simplified = new ArrayList<String>();
        String previousToken = "";
        boolean change = false;

        for (int pos = 0; pos < row.size(); pos++)
        {
            String currentToken = row.get(pos);
            if (!currentToken.equals(LB) || previousToken.equals(MAX))
            {
                previousToken = currentToken;
                simplified.add(currentToken);
            }
            else
            {
                change = true;
                if(previousToken.equals(PLUS)) {
                    simplified.set(simplified.size() - 1, MINUS);
                }
                else if (previousToken.equals(MINUS)) {
                    simplified.set(simplified.size() - 1, PLUS);
                }
                else {
                    simplified.add(MINUS);
                }
                int tokenValue = Integer.valueOf(row.get(pos + 1));
                tokenValue = 0 - tokenValue;
                simplified.add(String.valueOf(tokenValue));
                previousToken = "";
                pos = pos + 2;
            }
        }

        if (change) {
            return simplified;
        }else {
            return null;
        }
    }

    public void finishCalculations(String segment, int value)
    {
        ArrayList<String> lastRow = new ArrayList<String>(Arrays.asList(Integer.toString(value)));
        this.findSegmentCalculation(segment).add(lastRow);
    }

    public Calculations mergeCalculations(Calculations ldaCalcs)
    {
        this.ldaCalcs = ldaCalcs.getLdaCalculations();
        return this;
    }

    public List<ArrayList<String>> getLdaCalculations()
    {
        return this.ldaCalcs;
    }

    @Override
    public String toString()
    {
        StringBuilder breakdown = new StringBuilder();

        breakdown.append(TORA + " ");
        for (int i = 0; i < this.toraCalcs.size(); i++)
        {
            breakdown.append(EQUALS + " ");
            for (String token : toraCalcs.get(i))
            {
                breakdown.append(token + " ");
            }
            breakdown.append(NEWLINE);
        }

        breakdown.append(NEWLINE);

        breakdown.append(TODA + " ");
        for (int i = 0; i < this.todaCalcs.size(); i++)
        {
            breakdown.append(EQUALS + " ");
            for (String token : todaCalcs.get(i))
            {
                breakdown.append(token + " ");
            }
            breakdown.append(NEWLINE);
        }

        breakdown.append(NEWLINE);

        breakdown.append(ASDA + " ");
        for (int i = 0; i < this.asdaCalcs.size(); i++)
        {
            breakdown.append(EQUALS + " ");
            for (String token : asdaCalcs.get(i))
            {
                breakdown.append(token + " ");
            }
            breakdown.append(NEWLINE);
        }

        breakdown.append(NEWLINE);

        breakdown.append(LDA + " ");
        for (int i = 0; i < this.ldaCalcs.size(); i++)
        {
            breakdown.append(EQUALS + " ");
            for (String token : ldaCalcs.get(i))
            {
                breakdown.append(token + " ");
            }
            breakdown.append(NEWLINE);
        }

        return breakdown.toString();
    }

    private ArrayList<String> createNewRow(List<String> previousRow, Pair<Integer, Integer> portion, String value)
    {
        ArrayList<String> newRow = new ArrayList<String>();
        for(int i = 0; i < portion.getValue1(); i++)
        {
            newRow.add(previousRow.get(i));
        }

        newRow.add(value);

        for(int i = portion.getValue2() + 1; i < previousRow.size(); i++)
        {
            newRow.add(previousRow.get(i));
        }

        return newRow;
    }

    private List<ArrayList<String>> findSegmentCalculation(String segment)
    {
        if (segment.equals(TORA)) {
            return this.toraCalcs;
        }
        else if (segment.equals(TODA)) {
            return this.todaCalcs;
        }
        else if (segment.equals(ASDA)) {
            return this.asdaCalcs;
        }
        else {
            return this.ldaCalcs;
        }
    }

    private List<String> findLastCalculation(String segment)
    {
        if (segment.equals(TORA)) {
            return this.toraCalcs.get(toraCalcs.size() - 1);
        }
        else if (segment.equals(TODA)) {
            return this.todaCalcs.get(todaCalcs.size() - 1);
        }
        else if (segment.equals(ASDA)) {
            return this.asdaCalcs.get(asdaCalcs.size() - 1);
        }
        else {
            return this.ldaCalcs.get(ldaCalcs.size() - 1);
        }
    }
}
