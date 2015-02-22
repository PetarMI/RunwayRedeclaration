package Model;

/**
 *java doesnt have a stupid pair class!!!!
 */
public class Pair<K, V> {

    private final K strip1;
    private final V strip2;

    public Pair(K element0, V element1) {
        this.strip1 = element0;
        this.strip2 = element1;
    }

    public K getStrip1() {
        return this.strip1;
    }

    public V getStrip2() {
        return this.strip2;
    }

}
