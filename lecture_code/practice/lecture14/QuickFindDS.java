
public class QuickFindDS implements DisjointSets {
    private int[] id;

    public QuickFindDS(int num) {
        id = new int[num];
        for (int i = 0; i < num; i++) {
            id[i] = i;
        }
    }

    @Override
    public void connect(int p, int q) {
        int pid = id[p];
        int qid = id[p];
        id[p] = id[q];
        for (int i = 0; i < id.length; i++) {
            if (id[i] == pid) {
                id[i] = qid;
            }
        }
    }

    @Override
    public boolean connected(int p, int q) {
        return id[p] == id[q];
    }

    public static void main(String[] args) {
        QuickFindDS ds = new QuickFindDS(7);
        ds.connect(0, 1);
        ds.connect(3, 1);
        ds.connect(6, 1);
        System.out.println(ds.connected(0, 6));
    }
}
