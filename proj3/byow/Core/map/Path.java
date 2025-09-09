package byow.Core.map;

import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/** Generate paths that connect each rooms */

public class Path {
    /** The world where to generate Path */
    World world;


    /** Constructor */
    public Path(World world) {
        this.world = world;
    }

    /** Return the list of the neighbor of the Point */
    private List<Point> getNeighbors(Point p, int dis) {
        int[][] dirs = {{-dis, 0}, {dis, 0}, {0, -dis}, {0, dis}};
        List<Point> neighbors = new ArrayList<>();

        for (int[] d : dirs) {
            Point tmp = world.getPoint(p.x + d[0], p.y + d[1]);
            if (!(tmp == null) && tmp.checkPoint()) {
                neighbors.add(tmp);
            }
        }

        return neighbors;
    }

    /** Return the random valid neighbor */
    private Point randNeighbor(List<Point> neighbors) {
        return neighbors.get(world.getRandInt(0, neighbors.size()));
    }

    /** Generate maze to fill the map */
    public void genMaze() {

        // Initialize the map with WALL except room area
        for (int i = 0; i < World.WIDTH; i++) {
            for (int j = 0; j < World.HEIGHT; j++) {
                Point p = world.getPoint(i, j);
                p.tile = p.tile == Tileset.NOTHING ? Tileset.WALL : p.tile;
            }
        }

        Stack<Point> stack = new Stack<>();
        stack.add(world.getPoint(0, 0));

        while (!stack.isEmpty()) {
            Point curr = stack.peek();
            curr.marked = true;

            // Fill the curr point with path
            curr.tile = Tileset.FLOOR;

            // Get all neighbors that are not marked
            List<Point> neighbors = new ArrayList<>();
            for (Point p : getNeighbors(curr, 2)) {
                if (!p.marked && !p.inRoom && p.tile == Tileset.WALL) {
                    neighbors.add(p);
                }
            }

            if (neighbors.isEmpty()) {
                stack.pop();
            } else {
                Point next = randNeighbor(neighbors);
                int midX = (curr.x + next.x) / 2;
                int midY = (curr.y + next.y) / 2;

                Point mid = world.getPoint(midX, midY);
                if (!mid.inRoom && mid.tile == Tileset.WALL) {
                    mid.tile = Tileset.FLOOR;
                }
                stack.add(next);
            }
        }
    }


    /** Generate a path from room1 to room2 */
    public void genPath(Room r1, Room r2) {
        // Return if r1 and r2 is the same room
        if (r1 == r2) {
            return;
        }

        genLHallway(r1.getCenter(), r2.getCenter());
    }

    /** Generate a floor surrounded by walls */
    private void genFloor(Point p) {
        if (p == null) return; // 额外的 null 检查

        if (p.tile != Tileset.FLOOR) {
            p.tile = Tileset.FLOOR;
        }

        int[][] dir = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, 1}, {1, -1}
        };

        for (int[] i : dir) {
            Point temp = world.getPoint(p.x + i[0], p.y + i[1]);
            if (temp != null && temp.tile == Tileset.NOTHING) {
                temp.tile = Tileset.WALL;
            }
        }
    }

    /** Generate "L" shape hallway from p1 to p2 */
    private void genLHallway(Point p1, Point p2) {
        if (p1 == null || p2 == null) {
            return;
        }
        // Make sure p1 is the left point
        if (p1.x > p2.x) {
            Point tmp = p1;
            p1 = p2;
            p2 = tmp;
        }

        if (p1.y < p2.y) {
            LL2UR(p1, p2);
        } else {
            UL2LR(p1, p2);
        }
    }

    /** genLHallway helper: lower left to upper right */
    private void LL2UR(Point LL, Point UR) {
        boolean xFirst = world.getRandInt(0, 2) == 0;

        if (xFirst) {
            for (int x = LL.x; x <= UR.x; x++) {
                genFloor(world.getPoint(x, LL.y));
            }
            for (int y = LL.y; y <= UR.y; y++) {
                genFloor(world.getPoint(UR.x, y));
            }
        } else {
            for (int y = LL.y; y <= UR.y; y++) {
                genFloor(world.getPoint(LL.x, y));
            }
            for (int x = LL.x; x <= UR.x; x++) {
                genFloor(world.getPoint(x, UR.y));
            }
        }
    }

    /** genLHallway helper: upper left to lower right */
    private void UL2LR(Point UL, Point LR) {
        boolean xFirst = world.getRandInt(0, 2) == 0;

        if (xFirst) {
            for (int x = UL.x; x <= LR.x; x++) {
                genFloor(world.getPoint(x, UL.y));
            }
            for (int y = UL.y; y >= LR.y; y--) {
                genFloor(world.getPoint(LR.x, y));
            }
        } else {
            for (int y = UL.y; y >= LR.y; y--) {
                genFloor(world.getPoint(UL.x, y));
            }
            for (int x = UL.x; x <= LR.x; x++) {
                genFloor(world.getPoint(x, LR.y));
            }
        }
    }
}
