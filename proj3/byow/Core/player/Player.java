
package byow.Core.player;

import byow.Core.map.Point;
import byow.Core.map.WorldPrivilege;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;

/** Exactly a Point that represent the player.
 * Providing move methods
 * @author yhc
 */

public class Player implements Serializable {
    /** The player point */
    int x;
    int y;

    /** Constructor */
    public Player(WorldPrivilege wp) {
       Point p = wp.getPlayerPoint();
       x = p.x;
       y = p.y;
    }


    /** Set the player */
    public void setPlayer(WorldPrivilege wp) {
        wp.getPoint(x, y).setTile(Tileset.AVATAR);
    }

    /** Move the player, one step a time */
    public void move(int[] dir, WorldPrivilege wp) {
        int newX = x + dir[0];
        int newY = y + dir[1];

        // Check
        if (newX < 0 || newX >= wp.getWidth() || newY < 0 || newY >= wp.getHeight()) {
            return;
        }

        Point next = wp.getPoint(newX, newY);
        // Return if not walkable
        if (!next.isWalkable()) {
            return;
        }

        wp.getPoint(x, y).setTile(Tileset.FLOOR);
        next.setTile(Tileset.AVATAR);

        x = next.x;
        y = next.y;
    }

    /** Handle move string */
    public void move(String str, WorldPrivilege wp) {
        int[][] dirs = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
        for (int i = 0; i < str.length(); i++) {
            switch (str.charAt(i)) {
                case 'w':
                    move(dirs[0], wp);
                    break;
                case 's':
                    move(dirs[1], wp);
                    break;
                case 'a':
                    move(dirs[2], wp);
                    break;
                case 'd':
                    move(dirs[3], wp);
                    break;
            }
        }
    }


}