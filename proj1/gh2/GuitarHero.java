package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

/**
 * A client that uses the synthesizer package to replicate a plucked guitar string sound
 */
public class GuitarHero {
    public static final double[] CONCERT = new double[37];
    public static final double CONCERT_A = 440.0;
    public static final double CONCERT_C = CONCERT_A * Math.pow(2, 3.0 / 12.0);

    private static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static GuitarString[] strings;

    private static void initConcert() {
        for (int i = 0; i < 37; i++) {
            CONCERT[i] = 440 * Math.pow(2, (i - 24) / 12.0);
        }
    }

    private static void initGuitarStrings() {
        strings = new GuitarString[37];
        for (int i = 0; i < 37; i++) {
            strings[i] = new GuitarString(CONCERT[i]);
        }
    }

    public static void main(String[] args) {
        initConcert();
        initGuitarStrings();

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (keyboard.indexOf(key) != -1) {
                    strings[keyboard.indexOf(key)].pluck();
                }
            }

            /* compute the superposition of samples */
            double sample = 0;
            for (int i = 0; i < 37; i++) {
                sample += strings[i].sample();
            }

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            for (int i = 0; i < 37; i++) {
                strings[i].tic();
            }
        }
    }
}