package DisjointSets;

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
    public boolean isConnected(int p, int q) {
        return id[p] == id[q];
    }


}
