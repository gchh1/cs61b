package byow.DisjointSets;

/** The interface of disjointSets */

public interface DisjointSets<T> {
    /** Connect two object */
    void connect(T p, T q);
    /** Return true if two objects are connected */
    boolean isConnected(T p, T q);
    /** Add items into the ds */
    void add(T v);
}
