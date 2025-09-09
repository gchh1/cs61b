package byow.Core.map;

import byow.DisjointSets.WeightedUnionDS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Weighted union disjoint sets that contain room */

public class RoomSet extends WeightedUnionDS<Room> {
    /** The world instance */
    World world;
    /** The rooms list */
    List<Room> rooms;



    /** Add a room list */
    public RoomSet(List<Room> rooms, World w) {
        super();
        world = w;
        this.rooms = rooms;
        for (Room item : rooms) {
            add(item);
        }

    }

    /** Connect all rooms */
    public void connectRooms() {
        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < rooms.size(); i++) {
            for (int j = i + 1; j < rooms.size(); j++) {
                Room r1 = rooms.get(i);
                Room r2 = rooms.get(j);
                int dist = manhattan(r1, r2);
                edges.add(new Edge(r1, r2, dist));
            }
        }

        // Sort the edges
        Collections.sort(edges);

        // Draw the edge
        for (Edge e : edges) {
            if (!isConnected(e.r1, e.r2)) {
                connect(e.r1, e.r2);
                genPath(e.r1, e.r2);
            }
        }
    }

    /** Generate a path from a room to the other room */
    private void genPath(Room r1, Room r2) {
        Path path = new Path(world);
        path.genPath(r1, r2);
    }

    /** Calculate the Manhattan distance of two rooms */
    private int manhattan(Room r1, Room r2) {
        return Math.abs(r1.getCenter().x - r2.getCenter().x) + Math.abs(r1.getCenter().y - r2.getCenter().y);
    }
}
