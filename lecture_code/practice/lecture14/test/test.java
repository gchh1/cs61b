package test;

import DisjointSets.DisjointSets;
import DisjointSets.QuickFindDS;

public class test {
    public static void main(String[] args) {
        QuickFindDS ds = new QuickFindDS(7);
        ds.connect(0, 1);
        ds.connect(3, 1);
        ds.connect(6, 1);
        System.out.println(ds.isConnected(0, 6));
    }
}
