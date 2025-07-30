
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
    public boolean connected(int p, int q) {
        return findByPathCompression(p) == findByPathCompression(q);
    }

    public static void main(String[] args) {
        WeightedUnionDSWithPC ds = new WeightedUnionDSWithPC(16);
        
        ds.connect(0, 1);
        ds.connect(0, 2);
        ds.connect(0, 3);
        ds.connect(0, 4);
        ds.connect(1, 5);
        ds.connect(1, 6);
        ds.connect(1, 7);
        ds.connect(2, 8);
        ds.connect(2, 9);
        ds.connect(3, 10);
        ds.connect(5, 11);
        ds.connect(5, 12);
        ds.connect(6, 13);
        ds.connect(8, 14);
        ds.connect(11, 15);
        System.out.println(ds.connected(15, 10));
    }
}
