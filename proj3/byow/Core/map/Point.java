package byow.Core.map;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;

/** Represent a point in the world */

public class Point implements Serializable {
    /** The x-coordinate of the Point */
    public int x;
    /** The y-coordinate of the Point */
    public int y;
    /** The Tile type of the Point */
    protected TETile tile = Tileset.NOTHING;


    /** Constructor */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, TETile tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

    /** Set the tile type of the Point */
    public void setTile(TETile t) {
        tile = t;
    }

    /** Return whether the point is walkable */
    public boolean isWalkable() {
        return tile.description().equals(Tileset.FLOOR.description());
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
