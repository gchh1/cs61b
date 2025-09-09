package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};
    /** Failure phrase */
    private static final String FAILURE = "Game Over! You made it to round: ";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        StringBuilder s = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            s.append(CHARACTERS[RandomUtils.uniform(rand, 0, 26)]);
        }
        return s.toString();
    }

    public void drawFrame(String s) {
        // Clear the screen
        StdDraw.clear();

        // Draw background
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.filledRectangle(width / 2.0, height / 2.0, width / 2.0, height / 2.0);

        // Set text font
        StdDraw.setFont(new Font("Jokerman", Font.PLAIN, 23));
        StdDraw.setPenColor(StdDraw.WHITE);

        // Draw helpful ui
        StdDraw.text(4, height - 1, "Round: " + round);
        String tips = round % 2 == 1 ? "Watch!" : "Type!";
        StdDraw.text(width / 2.0, height - 1, tips);
        String e = ENCOURAGEMENT[RandomUtils.uniform(rand, 0, ENCOURAGEMENT.length)];
        StdDraw.text(width - e.length() / 2.0 + 1, height - 1, e);
        StdDraw.line(0, height - 2, width, height - 2);

        // Drw text
        StdDraw.setFont(new Font("Jokerman", Font.BOLD, 30));
        StdDraw.text(width / 2.0, height / 2.0, s);
        StdDraw.show();

    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);

            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        StringBuilder res = new StringBuilder();
        while (res.toString().length() != n) {
            if (StdDraw.hasNextKeyTyped()) {
                res.append(StdDraw.nextKeyTyped());
            }
        }
        return res.toString();
    }

    public void startGame() {
        round = 1;
        String expect;
        String input;
        do {
//            drawFrame("Round: " + round);
//            StdDraw.pause(500);
            expect = generateRandomString(round);
            flashSequence(expect);
            input = solicitNCharsInput(round);
//            drawFrame(input);
//            StdDraw.pause(500);
            round++;
        } while (input.equals(expect));

        drawFrame(FAILURE + round);
    }

}
