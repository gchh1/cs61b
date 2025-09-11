package byow.Core.map;

import byow.Core.RandomUtils;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Generate a random world with the given seed */

public class World implements WorldPrivilege {
    /** The width of the world */
    protected int WIDTH;
    /** The height of the world */
    protected int HEIGHT;
    /** Random generator */
    private final Random random;
    /** List of rooms */
    private List<Room> rooms;
    /** The List that represent a world */
    private List<Point> world;


    /** Constructor */
    public World(int w, int h, long seed) {
        WIDTH = w;
        HEIGHT = h;
        random = new Random(seed);
        world = new ArrayList<>();
        rooms = new ArrayList<>();
        initWorld();
        genRandWorld();
    }

    public World(TETile[][] w, long seed) {
        WIDTH = w.length;
        HEIGHT = w[0].length;
        random = new Random(seed);
        writeWorld(w);
    }

    /** Generate random world */
    private void genRandWorld() {
        rooms = genRandRoom();
        RoomSet rs = new RoomSet(rooms);
        rs.connectRooms(this);

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
    public TETile[][] getWorld() {
        TETile[][] res = new TETile[WIDTH][HEIGHT];

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                res[i][j] = getPoint(i, j).tile;
            }
        }

        return res;
    }

    /** Write the world */
    public void writeWorld(TETile[][] w) {
        world = new ArrayList<>();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Point p = new Point(i, j, w[i][j]);
                if (p.tile == Tileset.AVATAR) {
                    p.setTile(Tileset.FLOOR);
                }
                world.add(p);
            }
        }
    }

    /** Get the world list */
    public List<Point> getListWorld() {
        return world;
    }

    /** Return the Point with given x and y coordinate */
    @Override
    public Point getPoint(int x, int y) {
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
            return null;
        }
        return world.get(x * HEIGHT + y);
    }

    @Override
    public Point getPlayerPoint() {
        int roomNum = getRandInt(0, rooms.size());
        Room room = rooms.get(roomNum);
        return getPoint(room.getCenter().x, room.getCenter().y);

    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    /** Return a random integer by uniform
     * @param lb lower bound
     * @param up upper bound */
    @Override
    public int getRandInt(int lb, int up) {
        return RandomUtils.uniform(random, lb, up);
    }
}
