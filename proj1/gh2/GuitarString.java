package gh2;

 import deque.ArrayDeque;
 import deque.Deque;

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
     private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) Math.round(SR / frequency);
        buffer = new ArrayDeque<>();
        for (int i = 0; i < capacity; i++) {
            buffer.addFirst((double) 0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        int length = buffer.size();
        Deque<Double> temp = new ArrayDeque<>();
        for (int i = 0; i < length; i++) {
            double r = Math.random() - 0.5;
            temp.addLast(r);
        }
        buffer = temp;
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double temp = buffer.removeFirst();
        buffer.addLast(DECAY * (temp + buffer.get(0)) / 2);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
}
