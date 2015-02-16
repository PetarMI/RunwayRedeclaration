package Model;

public class Runway {


    private String runwayId;
    private int tora;
    private int asda;
    private int toda;
    private int lda;
    private String orientation;
    private int position;

    public Runway(String runwayId, int tora, int asda, int toda, int lda, String orientation, int position) {
        this.runwayId = runwayId;
        this.tora = tora;
        this.asda = asda;
        this.toda = toda;
        this.lda = lda;
        this.orientation = orientation;
        this.position = position;
    }

    public String getRunwayId() {
        return runwayId;
    }

    public int getTora() {
        return tora;
    }

    public int getAsda() {
        return asda;
    }

    public int getToda() {
        return toda;
    }

    public int getLda() {
        return lda;
    }

    public String getOrientation() {
        return orientation;
    }

    public int getPosition() {
        return position;
    }
}
