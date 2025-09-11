package byow.Core;

import byow.Core.map.Point;
import byow.Core.map.World;
import byow.Core.player.Player;
import byow.TileEngine.TETile;

import java.io.Serializable;
import java.util.List;

/** Store the state of the game */

public class GameState implements Serializable {
    /** The basic world */
    private TETile[][] world;
    /** The list form of world */
    private List<Point> listWorld;
    /** The player */
    private Player player;
    /** The world seed */
    private long seed;

    /** Constructor */
    public GameState(TETile[][] world, Player player, long seed) {
        this.world = world;
        this.player = player;
        this.seed = seed;
    }

    public GameState() {
        this.world = null;
        this.player = null;
    }

    /** Reset the world */
    public void resetState(World world, Player player, long seed) {
        listWorld = world.getListWorld();
        this.world = world.getWorld();
        this.player = player;
        this.seed = seed;
    }

    /** Get the world */
    public TETile[][] getWorld() {
        return world;
    }

    /** Get the player */
    public Player getPlayer() {
        return player;
    }

    /** Get the seed */
    public long getSeed() {
        return seed;
    }

    /** Get the list world */
    public List<Point> getListWord() {
        return listWorld;
    }
}
