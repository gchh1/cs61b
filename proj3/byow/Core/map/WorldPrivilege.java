package byow.Core.map;

/** The interface of random source */

public interface WorldPrivilege {
    /** Return the width of the world */
    int getWidth();
    /** Return the height of the world */
    int getHeight();
    /** Return a random integer */
    int getRandInt(int lb, int ub);
    /** Get the point in the world */
    Point getPoint(int x, int y);
    /** Get the player Point */
    Point getPlayerPoint();
}
