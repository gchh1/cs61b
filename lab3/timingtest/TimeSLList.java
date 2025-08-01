package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        int n = 1000;
        int m = 10000;

        for (int i = 0; i < 8; i++) {
            // Step1: create a SLList
            SLList<Integer> test = new SLList<>();
            // Step2: add n items
            for (int j = 0; j < n; j++) {
                test.addLast(j);
            }
            // Step3: start the timer
            Stopwatch sw = new Stopwatch();
            // Step4: add m items
            for (int k = 0; k < m; k++) {
                test.addLast(k);
            }
            // Step5: check timer
            double time = sw.elapsedTime();
            times.addLast(time);

            Ns.addLast(n);
            opCounts.addLast(m);

            n *= 2;
        }
        printTimingTable(Ns, times, opCounts);
    }

}
