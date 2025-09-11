package byow.Core.map;

import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/** Generate paths that connect each rooms */

public class Path {

    /** Generate a path from room1 to room2 */
    public void genPath(Room r1, Room r2, WorldPrivilege wp) {
        // Return if r1 and r2 is the same room
        if (r1 == r2) {
            return;
        }

        genLHallway(r1.getCenter(), r2.getCenter(), wp);
    }

    /** Generate a floor surrounded by walls */
    private void genFloor(Point p, WorldPrivilege wp) {
        if (p == null) return;

        if (p.tile != Tileset.FLOOR) {
            p.tile = Tileset.FLOOR;
        }

        int[][] dir = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1},
                {-1, -1}, {-1, 1}, {1, 1}, {1, -1}
        };

        for (int[] i : dir) {
            Point temp = wp.getPoint(p.x + i[0], p.y + i[1]);
            if (temp != null && temp.tile == Tileset.NOTHING) {
                temp.tile = Tileset.WALL;
            }
        }
    }

    /** Generate "L" shape hallway from p1 to p2 */
    private void genLHallway(Point p1, Point p2, WorldPrivilege wp) {
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
            LL2UR(p1, p2, wp);
        } else {
            UL2LR(p1, p2, wp);
        }
    }

    /** genLHallway helper: lower left to upper right */
    private void LL2UR(Point LL, Point UR, WorldPrivilege wp) {
        boolean xFirst = wp.getRandInt(0, 2) == 0;

        if (xFirst) {
            for (int x = LL.x; x <= UR.x; x++) {
                genFloor(wp.getPoint(x, LL.y), wp);
            }
            for (int y = LL.y; y <= UR.y; y++) {
                genFloor(wp.getPoint(UR.x, y), wp);
            }
        } else {
            for (int y = LL.y; y <= UR.y; y++) {
                genFloor(wp.getPoint(LL.x, y), wp);
            }
            for (int x = LL.x; x <= UR.x; x++) {
                genFloor(wp.getPoint(x, UR.y), wp);
            }
        }
    }

    /** genLHallway helper: upper left to lower right */
    private void UL2LR(Point UL, Point LR, WorldPrivilege wp) {
        boolean xFirst = wp.getRandInt(0, 2) == 0;

        if (xFirst) {
            for (int x = UL.x; x <= LR.x; x++) {
                genFloor(wp.getPoint(x, UL.y), wp);
            }
            for (int y = UL.y; y >= LR.y; y--) {
                genFloor(wp.getPoint(LR.x, y), wp);
            }
        } else {
            for (int y = UL.y; y >= LR.y; y--) {
                genFloor(wp.getPoint(UL.x, y), wp);
            }
            for (int x = UL.x; x <= LR.x; x++) {
                genFloor(wp.getPoint(x, LR.y), wp);
            }
        }
    }
}
