package Model;

public class Strip
{
    private int orientation;
    private String position;
    private Values origVal, recVal;
    private final int displacedThreshold;
    private String stripId;
    private Calculations breakdown;

    public Strip(String stripId, int orientation, String position,Values origVal, int displacedThreshold) {
        this.stripId = stripId;
        this.position = position;
        this.orientation = orientation;
        this.origVal = origVal;
        this.recVal = origVal;
        this.displacedThreshold = displacedThreshold;
        this.breakdown = null;
    }

    public int getOrientation(){
        return orientation;
    }

    public String getPosition(){
        return position;
    }

    public String getDesignator(){
        return Integer.toString(orientation) + position;
    }

    public void setRecVal(Values recVal) {
        this.recVal = recVal;
    }

    public Values getOrigVal() {
        return origVal;
    }

    public int getDisplacedThreshold() { return this.displacedThreshold; }

    public Values getRecVal() {
        return recVal;
    }

    public String getStripId() {
        return stripId;
    }

    public void setCalculationBreakdown(Calculations calc)
    {
        this.breakdown = calc;
    }

    public String viewCalculationBreakdown()
    {
        if (this.breakdown != null) {
            return this.breakdown.toString();
        }
        else {
            return "No calculations";
        }
    }

    @Override
    public String toString() {
        return "Strip{" +
                "orientation=" + orientation +
                ", position='" + position + '\'' +
                ", origVal=" + origVal +
                ", recVal=" + recVal +
                ", stripId='" + stripId + '\'' +
                '}';
    }
}
