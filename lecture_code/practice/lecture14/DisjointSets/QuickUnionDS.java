package DisjointSets;

public class QuickUnionDS implements DisjointSets {
    private int[] parent;

    public QuickUnionDS(int n) {
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
        parent[p] = j;
    }

    @Override
    public boolean isConnected(int p, int q) {
        return find(p) == find(q);
    }


}
