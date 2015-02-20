package Model;

public class Strip
{
    private int orientation;
    private String position;
    private int tora;
    private int asda;
    private int toda;
    private int lda;
    private int rectora;
    private int recasda;
    private int rectoda;
    private int reclda;


    public Strip(int orientation, String position, int tora, int asda, int toda, int lda) {
        this.position = position;
        this.orientation = orientation;
        this.tora = tora;
        this.asda = asda;
        this.toda = toda;
        this.lda = lda;
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

    //TODO: Maths module inside of this? Rather than pass values into it to assign
    public void recalculateValues(int[] newValues){
        this.rectora = newValues[0];
        this.recasda = newValues[1];
        this.rectoda = newValues[2];
        this.reclda = newValues[3];
    }
}
