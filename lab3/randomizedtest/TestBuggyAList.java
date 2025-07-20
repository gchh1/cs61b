package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> trueList = new AListNoResizing<>();
        BuggyAList<Integer> buggyAList = new BuggyAList<>();

        /* test add */
        // Add first item
        trueList.addLast(4);
        buggyAList.addLast(4);
        assertEquals(trueList.getLast(), buggyAList.getLast());

        // Add second item
        trueList.addLast(5);
        buggyAList.addLast(5);
        assertEquals(trueList.getLast(), buggyAList.getLast());

        // Add third item
        trueList.addLast(6);
        buggyAList.addLast(6);
        assertEquals(trueList.getLast(), buggyAList.getLast());

        /* test remove */
        assertEquals(trueList.removeLast(), buggyAList.removeLast());
        assertEquals(trueList.removeLast(), buggyAList.removeLast());
        assertEquals(trueList.removeLast(), buggyAList.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> B = new BuggyAList<>();

        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                assertEquals(size, B.size());
                System.out.println("size: " + size);
            }

            if (L.size() == 0) {
                continue;
            }

            if (operationNumber == 2) {
                // getLast
                int lastVal = L.getLast();
                assertEquals(L.getLast(), B.getLast());
                System.out.println("getLast(" + lastVal + ")");
            } else if (operationNumber == 3) {
                // removeLast
                int lastVal = L.removeLast();
                assertEquals(lastVal, (int)B.removeLast());
                System.out.println("removeLast(" + lastVal + ")");
            }
        }
    }
}
