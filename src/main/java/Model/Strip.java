package Model;

public class Strip
{
    private int orientation;
    private String position;
    private Values origVal, recVal;
    private final int displacedThreshold;
    private String stripId;

    public Strip(String stripId, int orientation, String position,Values origVal, int displacedThreshold) {
        this.stripId = stripId;
        this.position = position;
        this.orientation = orientation;
        this.origVal = origVal;
        this.recVal = origVal;
        this.displacedThreshold = displacedThreshold;
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
