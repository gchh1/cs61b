package byow.DisjointSets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Basic Weighted union disjoint set */

public class WeightedUnionDS<T> implements DisjointSets<T> {
    /** Node class */
    protected class Node {
        private int parent;
        private T val;

        public Node(T v) {
            this.val = v;
            this.parent = -1;
        }

        public T getVal() {
            return val;
        }
    }

    /** Basic array */
    protected List<Node> nodes;
    /** Mapping T and index */
    private Map<T, Integer> indexMap;

    /** Constructor */
    public WeightedUnionDS() {
        nodes = new ArrayList<>();
        indexMap = new HashMap<>();
    }

    /** Find the index of root of a Node */
    private int findRootIndex(int i) {
        if (nodes.get(i).parent < 0) {
            return i;
        }
        nodes.get(i).parent = findRootIndex(nodes.get(i).parent);
        return nodes.get(i).parent;
    }


    @Override
    public void add(T v) {
        if (!indexMap.containsKey(v)) {
            nodes.add(new Node(v));
            indexMap.put(v, nodes.size() - 1);
        }
    }

    @Override
    public void connect(T p, T q) {
        int i = indexMap.get(p);
        int j = indexMap.get(q);
        int rootI = findRootIndex(i);
        int rootJ = findRootIndex(j);

        // Return if p and q is connected
        if (rootI == rootJ) {
            return;
        }

        if (nodes.get(rootI).parent < nodes.get(rootJ).parent) {
            nodes.get(rootI).parent += nodes.get(rootJ).parent;
            nodes.get(rootJ).parent = rootI;
        } else {
            nodes.get(rootJ).parent += nodes.get(rootI).parent;
            nodes.get(rootI).parent = rootJ;
        }
    }

    @Override
    public boolean isConnected(T p, T q) {
        return findRootIndex(indexMap.get(p)) == findRootIndex(indexMap.get(q));
    }
}
