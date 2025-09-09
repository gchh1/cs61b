package byow.DisjointSets;

public class DisjointSetsTest {
    public static void main(String[] args) {
        DisjointSets<String> ds = new WeightedUnionDS<>();
        ds.add("asdf");
        ds.add("bfd");
        ds.add("cds");
        ds.add("dfd");
        ds.add("fjl");
        ds.add("gjk");
        ds.connect("asdf", "bfd");
        boolean b = ds.isConnected("asdf", "bfd");
    }
}
