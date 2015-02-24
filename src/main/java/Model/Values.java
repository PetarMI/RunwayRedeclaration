package Model;

public class Values {
    private int tora;
    private int asda;
    private int toda;
    private int lda;
    private int threshold;

    public Values(int tora, int asda, int toda, int lda) {
        this.tora = tora;
        this.asda = asda;
        this.toda = toda;
        this.lda = lda;
        this.threshold = this.tora - this. lda;
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

    public void setLda (int lda) { this.lda = lda; }

    public int getThreshold() { return this.threshold; }

    @Override
    public String toString() {
        return "Values{" +
                "tora=" + tora +
                ", asda=" + asda +
                ", toda=" + toda +
                ", lda=" + lda +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Values values = (Values) o;

        if (asda != values.asda) return false;
        if (lda != values.lda) return false;
        if (toda != values.toda) return false;
        if (tora != values.tora) return false;

        return true;
    }
}
