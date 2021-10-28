package constants;

public class PairTriple<K, V, T> {

    private final K a;
    private final V b;
    private final T c;

    public PairTriple(K a, V b, T c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public K getA() {
        return a;
    }

    public V getB() {
        return b;
    }

    public T getC() {
        return c;
    }
}
