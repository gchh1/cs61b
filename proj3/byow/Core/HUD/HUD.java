package byow.Core.HUD;

import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;

/** Draw HUD to the world */

public class HUD {
    /** the welcome page */
    public static void drawWelcome(int width, int height) {
        // Set the width and height of the page
        StdDraw.setCanvasSize(width, height);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // Tips
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new Font("Cascadia Code", Font.BOLD, 30));
        StdDraw.text(width / 2.0, height * 2 / 5.0, "[ L ]  LOAD");
        StdDraw.text(width / 2.0, height * 2 / 5.0 * 2 / 3, "[ N***S ]  NEW");
        StdDraw.text(width / 2.0, height * 2 / 5.0 / 3.0, "[ :Q ]  QUIT");

        StdDraw.show();
    }

    /** In game hub */
    public static void drawGameHUB(TETile[][] world) {
        StdDraw.setPenColor(StdDraw.WHITE);
//        StdDraw.setFont(new Font("Cascadia Code", Font.BOLD, 14));

        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();

        if (mouseX >= 0 && mouseX < world.length && mouseY >= 0 && mouseY < world[0].length) {
            TETile tile = world[mouseX][mouseY];
            String desc = tile.description();
            StdDraw.textLeft(1, world[0].length - 10, desc);
        }
    }
}
