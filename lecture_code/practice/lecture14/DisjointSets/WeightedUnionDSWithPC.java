package DisjointSets;

/** WeightedUnion with path compression */

public class WeightedUnionDSWithPC implements DisjointSets {
    private int[] parent;

    public WeightedUnionDSWithPC(int n) {
        parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = -1;
        }
    }

    private int find(int p) {
        int res = p;
        while(parent[res] > 0) {
            res = parent[res];
        }
        return res;
    }

    private int findByPathCompression(int p) {
        int res = p;
        while(parent[res] > 0) {
            res = parent[res];
        }

        p = res;
        while(parent[p] > 0) {
            int tmp = parent[p];
            parent[p] = res;
            p = tmp;
        }

        return res;
    }

    @Override
    public void connect(int p, int q) {
        int i = findByPathCompression(p);
        int j = findByPathCompression(q);
        if (i == j) {
            return;
        }
        if (parent[i] >= parent[j]) {
            parent[j] += parent[i];
            parent[i] = j;
        } else {
            parent[i] += parent[j];
            parent[j] = i;
        }
    }

    @Override
    public boolean isConnected(int p, int q) {
        return findByPathCompression(p) == findByPathCompression(q);
    }


}
