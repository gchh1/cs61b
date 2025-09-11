package byow.Core.map;

import byow.TileEngine.TERenderer;

/** Visualize the random world in order to test GenWorld class
 * @author yhc
 */

public class RandWorldVisualTest {

    public static void main(String[] args) {
        World world = new World(80, 30, 4928);
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);
        ter.renderFrame(world.getWorld());
    }

}
