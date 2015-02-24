package Model;

/**
 *java doesnt have a stupid pair class!!!!
 */
public class Pair<K, V> {

    private final K value1;
    private final V value2;

    public Pair(K element0, V element1) {
        this.value1 = element0;
        this.value2 = element1;
    }

    public K getValue1() {
        return this.value1;
    }

    public V getValue2() {
        return this.value2;
    }

}
