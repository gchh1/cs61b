package byow.Core.map;

import byow.TileEngine.Tileset;

/** The room class that support matrix rooms
 * @author yhc
 */

public class Room {
    /** The start point of a room */
    private Point start;
    /** The width of the room */
    private int width;
    /** The height of the room */
    private int height;
    /** The world to generate rooms in */
    private World world;


    /** Constructor */
    public Room(World world, int x, int y, int w, int h) {
        start = world.getPoint(x, y);
        this.world = world;
        this.width = w;
        this.height = h;
    }

    /** Generate a room in the world.
     * if confliction exists, room generation will fail */
    public boolean genRoom() {
        // Return false if confliction exists
        if (detectConflict()) {
            return false;
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Point p = world.getPoint(start.x + i, start.y + j);
                if (i == 0 || j == 0 || i == width - 1 || j == height - 1) {
                    p.setTile(Tileset.WALL);
                } else {
                    p.setTile(Tileset.FLOOR);
                }
            }
        }
        return true;
    }

    /** Detect the confliction */
    public boolean detectConflict() {

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Point p = world.getPoint(start.x + i, start.y + j);
                if (p == null || p.tile != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }


    /** Return the center of the room */
    public Point getCenter() {
        return world.getPoint(start.x + width / 2, start.y + height / 2);
    }
}
