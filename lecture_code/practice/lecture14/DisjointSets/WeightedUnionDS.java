package DisjointSets;

public class WeightedUnionDS implements DisjointSets {
    private int[] parent;

    public WeightedUnionDS(int n) {
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

    @Override
    public void connect(int p, int q) {
        int i = find(p);
        int j = find(q);
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
        return find(p) == find(q);
    }


}
