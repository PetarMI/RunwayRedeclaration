package Model;

public class Strip
{
    private String designator;
    private int tora;
    private int asda;
    private int toda;
    private int lda;
    private int rectora;
    private int recasda;
    private int rectoda;
    private int reclda;
    private int displacedThreshold;     //The displaced portion can be used for take-off but not for landing

    public Strip(String designator, int tora, int asda, int toda, int lda, int displacedThreshold){
        this.designator = designator;
        this.tora = tora;
        this.asda = asda;
        this.toda = toda;
        this.lda = lda;
        this.displacedThreshold = displacedThreshold;
    }

    public String getDesignator() {
        return designator;
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

    public int getRectora() {
        return rectora;
    }

    public int getRecasda() {
        return recasda;
    }

    public int getRectoda() {
        return rectoda;
    }

    public int getReclda() {
        return reclda;
    }

    public int getDisplacedThreshold() {return displacedThreshold; }

    //changing values based on array returned by the maths module
    public void recalculateValues(Integer[] newValues){
        this.rectora = newValues[0];
        this.recasda = newValues[1];
        this.rectoda = newValues[2];
        this.reclda = newValues[3];
    }
}
