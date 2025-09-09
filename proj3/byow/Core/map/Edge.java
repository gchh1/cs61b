package byow.Core.map;

public class Edge implements Comparable<Edge> {
    Room r1;
    Room r2;
    int weight;

    public Edge(Room r1, Room r2, int weight) {
        this.r1 = r1;
        this.r2 = r2;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge other) {
        return this.weight - other.weight;
    }
}
