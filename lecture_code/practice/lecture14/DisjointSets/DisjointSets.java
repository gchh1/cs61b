package DisjointSets;

public interface DisjointSets {
    /** Connect two items P and Q */
    void connect(int p, int q);

    /** Return true if p and q are connected */
    boolean isConnected(int p, int q);
}
