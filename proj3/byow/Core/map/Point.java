package byow.Core.map;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

/** Represent a point in the world */

public class Point {
    /** The x-coordinate of the Point */
    protected int x;
    /** The y-coordinate of the Point */
    protected int y;
    /** The Tile type of the Point */
    protected TETile tile = Tileset.NOTHING;
    /** Whether the Point is marked */
    protected boolean marked = false;
    /** Whether the Point is in a room */
    protected boolean inRoom = false;


    /** Constructor */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /** Check if the point is valid */
    public boolean checkPoint() {
        return x >= 0 && x < World.WIDTH && y >= 0 && y < World.HEIGHT;
    }

    /** Set the tile type of the Point */
    public void setTile(TETile t) {
        tile = t;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        Point o = (Point) obj;
        return o.x == this.x && o.y == this.y;
    }
}
