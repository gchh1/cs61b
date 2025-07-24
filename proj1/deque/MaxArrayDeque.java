package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.comparator = c;
    }

    public T max() {
        return max(this.comparator);
    }


    public T max(Comparator<T> c) {
        if (size() == 0) {
            return null;
        }
        T maxItem = get(0);
        for (int i = 1; i < size(); i++) {
            T cur = get(i);
            if (c.compare(cur, maxItem) > 0) {
                maxItem = cur;
            }
        }
        return maxItem;
    }
}
