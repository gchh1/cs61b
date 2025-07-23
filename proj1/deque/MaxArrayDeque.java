package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        this.size = 0;
        this.comparator = c;
        this.capacity = 8;
        this.items = (T[]) new Object[capacity];
        this.nextFirst = 0;
        this.nextLast = 1;
    }

    public T max() {
        if (size == 0) {
            return null;
        }
        T maxItem = this.get(0);
        for (int i = 1; i < size; i++) {
            if (comparator.compare(this.get(i), maxItem) > 0) {
                maxItem = this.get(i);
            }
        }
        return maxItem;
    }

    public T max(Comparator<T> c) {
        if (size == 0) {
            return null;
        }
        T maxItem = this.get(0);
        for (int i = 1; i < size; i++) {
            if (c.compare(this.get(i), maxItem) > 0) {
                maxItem = this.get(i);
            }
        }
        return maxItem;
    }
}
