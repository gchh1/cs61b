package byow.lab12;
import jh61b.junit.In;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import ucb.gui.Widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    /** The width of the world */
    private static final int WIDTH = 50;
    /** The height of the world */
    private static final int HEIGHT = 50;
    /** Seed to generate rand num */
    private static final long SEED = 114514;
    /** Random generator */
    private static final Random RANDOM = new Random(SEED);


    /** Pair class that stores the x to y pair */
    private static class Pair {
        private int xPair;
        private int yPair;

        public Pair(int x, int y) {
            xPair = x;
            yPair = y;
        }
    }

    /** Return the x y pairs */
    private static List<Pair> getPair() {
        List<Pair> pairs = new ArrayList<>(19);

        for (int i = 0; i < 5; i++) {
            int num = 5 - Math.abs(2 - i);
            int start = 5 - num;
            for (int j = 0; j < num; j++) {
                pairs.add(new Pair(i, start + 2 * j));
            }
        }

        return pairs;
    }

    /** Generate the world with the given size */
    public static void genWorld(TETile[][] world, int size, int diffX, int diffY) {
        for (Pair p : getPair()) {
            addHexagon(size, world, getX(size, diffX).get(p.xPair), getY(size, diffY).get(p.yPair));
        }
        filBlank(world);
    }

    /** Return array that stores the X point */
    private static List<Integer> getX(int size, int diffX) {
        List<Integer> xPoints = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            xPoints.add((2 * size - 1) * i + diffX);
        }
        return xPoints;
    }

    /** Return array that stores the X point */
    private static List<Integer> getY(int size, int diffY) {
        List<Integer> yPoints = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            yPoints.add(i * size + diffY);
        }
        return yPoints;
    }


    /** Draw a single hexagon pattern to the given position of the world
     * @param size: the length of side for the hexagon */
    private static void addHexagon(int size, TETile[][] world, int startX, int startY) {
        TETile tile = randTile();
        for (int i = 0; i < 2 * size; i++) {
            int start;
            int num;
            if (i < size) {
                start = size - i - 1;
                num = size + 2 * i;
            } else {
                start = i - size;
                num = 5 * size - 2 * i - 2;
            }
            for (int j = start; j < start + num; j++) {
                world[startX + j][startY + i] = randColorTile(tile);
            }
        }
    }

    /** Return a tile with rand color */
    private static TETile randColorTile(TETile tile) {
        return TETile.colorVariant(tile, RANDOM.nextInt(50), RANDOM.nextInt(50), RANDOM.nextInt(50), RANDOM);
    }

    /** Return a rand tile */
    private static TETile randTile() {
        int randNum = RANDOM.nextInt(8);
        switch(randNum) {
            case 0:
                return Tileset.GRASS;
            case 1:
                return Tileset.WALL;
            case 2:
                return Tileset.FLOWER;
            case 3:
                return Tileset.MOUNTAIN;
            case 4:
                return Tileset.FLOOR;
            case 5:
                return Tileset.SAND;
            case 6:
                return Tileset.TREE;
            case 7:
                return Tileset.WATER;
            default:
                return Tileset.NOTHING;
        }
    }

    /** Replace the blank tile with NOTHING */
    private static void filBlank(TETile[][] world) {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (world[i][j] == null) {
                    world[i][j] = Tileset.NOTHING;
                }
            }
        }
    }


    /** Main method */
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];

        genWorld(world, 4, 5, 5);

        ter.renderFrame(world);
    }
}
