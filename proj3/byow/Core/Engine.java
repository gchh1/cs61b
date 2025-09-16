package byow.Core;

import byow.Core.HUD.HUD;
import byow.Core.map.World;
import byow.Core.player.Player;
import byow.Networking.BYOWClient;
import byow.Networking.BYOWServer;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.IOException;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    public void interactWithKeyboard() {
        GameState gs = new GameState();
        World world = null;
        Player player = null;
        long seed = 0;
        boolean enteringSeed = false;
        boolean gameRunning = false;
        boolean firstRunGame = true;
        boolean awaitingQuit = false;

        HUD.drawWelcome(800, 600);


        while (true) {
            // ---- 处理输入 ----
            if (StdDraw.hasNextKeyTyped()) {
                char input = Character.toLowerCase(StdDraw.nextKeyTyped());

                if (!gameRunning) {
                    // 菜单逻辑
                    if (!enteringSeed && input == 'l') {
                        // Load
                        gs = Utils.loadGame();
                        world = new World(gs.getWorld(), gs.getSeed());
                        player = gs.getPlayer();
                        player.setPlayer(world);
                        gameRunning = true;

                    } else if (!enteringSeed && input == 'n') {
                        // New game
                        enteringSeed = true;

                    } else if (enteringSeed && Character.isDigit(input)) {
                        // 输入种子
                        seed = seed * 10 + (input - '0');

                    } else if (enteringSeed && input == 's') {
                        // 完成种子输入
                        enteringSeed = false;
                        gameRunning = true;
                        world = new World(WIDTH, HEIGHT, seed);
                        gs.resetState(world, new Player(world), seed);
                        player = gs.getPlayer();
                        player.setPlayer(world);
                    }

                }
                if (gameRunning) {
                    if (firstRunGame) {
                        StdDraw.clear();
                        ter.initialize(WIDTH, HEIGHT + 2); // +2 留HUD空间
                        ter.renderFrame(world.getWorld());
                        HUD.drawGameHUB(world.getWorld());
                        StdDraw.show();
                        firstRunGame = false;
                    }
                    // ---- 游戏运行逻辑 ----
                    if (input == ':') {
                        awaitingQuit = true;
                    } else if (awaitingQuit && input == 'q') {
                        Utils.saveGame(gs);
                        break; // 退出
                    } else {
                        awaitingQuit = false;
                        // 玩家移动
                        if (Utils.inMove(String.valueOf(input))) {
                            player.move(String.valueOf(input), world);
                        }
                    }
                }
            }

            // ---- 渲染 ----
            if (world != null) {
                ter.renderFrame(world.getWorld());
                HUD.drawGameHUB(world.getWorld()); // HUD 同时显示鼠标tile + 玩家信息
                StdDraw.show();
                StdDraw.pause(20);
            }
        }

        System.exit(0);
    }

    /** Check if a character is numerate */
    private boolean isNum(char c) {
        return c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9';
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        GameState gs = new GameState();
        World world;

        input = Utils.splitInput(input);
        if (input.contains("l")) {
            // Load game
            gs = Utils.loadGame();
            world = new World(gs.getWorld(), gs.getSeed());
            input = input.substring(1);
        } else {
            // Create a new game
            int end = input.indexOf('s');
            long seed = Long.parseLong(input.substring(0, end));
            world = new World(WIDTH, HEIGHT, seed);
            gs.resetState(world, new Player(world), seed);

            input = input.substring(end + 1);
        }


//        ter.initialize(WIDTH, HEIGHT);
        Player player = gs.getPlayer();
        player.setPlayer(world);
        player.move(input, world);

        if (input.contains(":")) {
            Utils.saveGame(gs);
        }
//        ter.renderFrame(world.getWorld());
        return gs.getWorld();
    }

    /** Network part */
    public void interactWithRemoteClient(String portString) throws IOException {
        int port = Integer.parseInt(portString);
        BYOWServer server = new BYOWServer(port);

        GameState gs = new GameState();
        World world = null;
        Player player = null;
        long seed = 0;
        boolean enteringSeed = false;
        boolean gameRunning = false;
        boolean awaitingQuit = false;

        HUD.drawWelcome(800, 600);
        server.sendCanvasConfig(800, 600);
        server.sendCanvas();

        ter.initialize(WIDTH, HEIGHT + 2);
        server.sendCanvasConfig(WIDTH * 16, (HEIGHT + 2) * 16);

        while (true) {
            // ---- 输入 ----
            if (server.clientHasKeyTyped()) {
                char input = Character.toLowerCase(server.clientNextKeyTyped());

                if (!gameRunning) {
                    // 菜单逻辑
                    if (!enteringSeed && input == 'l') {
                        gs = Utils.loadGame();
                        world = new World(gs.getWorld(), gs.getSeed());
                        player = gs.getPlayer();
                        player.setPlayer(world);
                        gameRunning = true;

                    } else if (!enteringSeed && input == 'n') {
                        enteringSeed = true;

                    } else if (enteringSeed && Character.isDigit(input)) {
                        seed = seed * 10 + (input - '0');

                    } else if (enteringSeed && input == 's') {
                        enteringSeed = false;
                        gameRunning = true;
                        world = new World(WIDTH, HEIGHT, seed);
                        gs.resetState(world, new Player(world), seed);
                        player = gs.getPlayer();
                        player.setPlayer(world);
                    }

                } else {
                    // 游戏逻辑
                    if (input == ':') {
                        awaitingQuit = true;
                    } else if (awaitingQuit && input == 'q') {
                        Utils.saveGame(gs);
                        break; // 退出循环
                    } else {
                        awaitingQuit = false;
                        if (Utils.inMove(String.valueOf(input))) {
                            player.move(String.valueOf(input), world);
                        }
                    }
                }
            }

            // ---- 渲染 ----
            if (world != null) {
                ter.renderFrame(world.getWorld());
                HUD.drawGameHUB(world.getWorld());
                StdDraw.show();
                server.sendCanvas(); // ⚠️ 每次渲染都要同步到客户端
            }

            StdDraw.pause(20);
        }

        // ---- 退出前清理 ----
        server.stopConnection();
    }
}
