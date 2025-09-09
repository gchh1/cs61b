package byow.Core.map;

import byow.Core.RandomUtils;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Generate a random world with the given seed */

public class World {
    /** The width of the world */
    protected static final int WIDTH = 100;
    /** The height of the world */
    protected static final int HEIGHT = 60;
    /** Random generator */
    private Random random;
    /** The List that represent a world */
    private final List<Point> world = new ArrayList<>();


    /** Generate and render the world */
    public void genWorld(long seed) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        initWorld();

        genRandWorld(seed);

        ter.renderFrame(getWorld());
    }

    /** Generate random world */
    private void genRandWorld(long seed) {
        // Initialize the random through the seed
        random = new Random(seed);

        RoomSet rs = new RoomSet(genRandRoom(), this);
        rs.connectRooms();

//        genPath();
    }


    /** Randomly generate rooms in the world */
    private List<Room> genRandRoom() {
        // Set the number of total rooms
        int roomNum = RandomUtils.uniform(random, 3, 25);
        // Create a List that store all rooms
        List<Room> rooms = new ArrayList<>();

        int count = 0;

        while (count < roomNum) {
            // Randomly initialize the room
            int width = RandomUtils.uniform(random, 5, 15);
            int height = RandomUtils.uniform(random, 5, 15);
            int x = RandomUtils.uniform(random, 0, WIDTH);
            int y = RandomUtils.uniform(random, 0, HEIGHT);
            Room room = new Room(this, x, y, width, height);
            // Add the room to the list
            if (room.genRoom()) {
                rooms.add(room);
                count++;
            }
        }

        return rooms;
    }

    /** Generate path between each room */
    private void genPath() {
        Path path = new Path(this);
//        path.genMaze(world.get(15));
//        path.genPath();
    }

    /** Initial the world with List type */
    private void initWorld() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Point p = new Point(i, j);
                world.add(p);
            }
        }
    }

    /** Return the world with 2-dimensional array */
    private TETile[][] getWorld() {
        TETile[][] res = new TETile[WIDTH][HEIGHT];

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                res[i][j] = getPoint(i, j).tile;
            }
        }

        return res;
    }

    /** Return the Point with given x and y coordinate */
    public Point getPoint(int x, int y) {
        if (x < 0 || y < 0 || x >= World.WIDTH || y >= World.HEIGHT) {
            return null;
        }
        return world.get(x * HEIGHT + y);
    }

    /** Return a random integer by uniform
     * @param lb lower bound
     * @param up upper bound */
    public int getRandInt(int lb, int up) {
        return RandomUtils.uniform(random, lb, up);
    }
}
