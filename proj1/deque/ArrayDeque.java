package deque;

import java.util.Iterator;
import java.util.StringJoiner;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    protected T[] items;
    protected int size;
    protected int capacity;
    protected int nextFirst;
    protected int nextLast;

    public ArrayDeque() {
        size = 0;
        capacity = 8;
        items = (T[]) new Object[capacity];
        nextFirst = 0;
        nextLast = 1;
    }

    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];

        int index = getValidIndex(nextFirst + 1);
        int i = 0;
        for (; i < size; i++) {
            temp[i] = items[index];
            index = getValidIndex(index + 1);
        }

        this.capacity = capacity;
        items = temp;
        nextFirst = capacity - 1;
        nextLast = i;
    }

    protected int getValidIndex(int index) {
        if (index < 0) {
            return Math.abs(capacity + index);
        }
        if (index >= capacity) {
            return Math.abs(capacity - index);
        }
         return index;
    }

    @Override
    public void addFirst(T item) {
        if (size == items.length) {
            this.resize(2 * this.capacity);
        }
        items[nextFirst] = item;
        nextFirst = getValidIndex(nextFirst - 1);
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (size == items.length) {
            this.resize(2 * this.capacity);
        }
        items[nextLast] = item;
        nextLast = getValidIndex(nextLast + 1);
        size += 1;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        if (capacity >= 16 && (double) size / capacity < 0.25) {
            resize((int) (capacity / 4));
        }
        nextFirst = getValidIndex(nextFirst + 1);
        size -= 1;
        return items[nextFirst];
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        if (capacity >= 16 && (double) size / capacity < 0.25) {
            resize((int) (capacity / 4));
        }
        nextLast = getValidIndex(nextLast - 1);
        size -= 1;
        return items[nextLast];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T get(int index) {
        if (size == 0 || index >= size) {
            return null;
        }
        return items[getValidIndex(nextFirst + 1 + index)];
    }

    @Override
    public void printDeque() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < size; i++) {
            output.append(String.valueOf(this.get(i)));
            if (i != size - 1) {
                output.append(" ");
            }
        }
        output.append(" ");
        System.out.println(output);
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index = getValidIndex(nextFirst + 1);

            @Override
            public boolean hasNext() {
                return size != 0;
            }

            @Override
            public T next() {
                T val = items[index];
                index = getValidIndex(index + 1);
                return val;
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deque<?>)) return false;
        if (size != ((Deque<?>)o).size()) return false;

        boolean flag = true;
        for (int i = 0; i < size; i++) {
            if (this.get(i) != ((Deque<?>)o).get(i)) {
                flag = false;
            }
        }
        return flag;
    }
}
