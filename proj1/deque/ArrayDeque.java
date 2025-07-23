package deque;

import java.util.Iterator;
import java.util.StringJoiner;

public class ArrayDeque<Type> implements Deque<Type>, Iterable<Type> {
    private Type[] items;
    private int size;
    private int capacity;
    private int nextFirst;
    private int nextLast;

    public ArrayDeque() {
        size = 0;
        capacity = 8;
        items = (Type[]) new Object[capacity];
        nextFirst = 0;
        nextLast = 1;
    }

    private void resize(int capacity) {
        Type[] temp = (Type[]) new Object[capacity];

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

    private int getValidIndex(int index) {
        if (index < 0) {
            return Math.abs(capacity + index);
        }
        if (index >= capacity) {
            return Math.abs(capacity - index);
        }
         return index;
    }

    @Override
    public void addFirst(Type item) {
        if (size == items.length) {
            this.resize(2 * this.capacity);
        }
        items[nextFirst] = item;
        nextFirst = getValidIndex(nextFirst - 1);
        size += 1;
    }

    @Override
    public void addLast(Type item) {
        if (size == items.length) {
            this.resize(2 * this.capacity);
        }
        items[nextLast] = item;
        nextLast = getValidIndex(nextLast + 1);
        size += 1;
    }

    @Override
    public Type removeFirst() {
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
    public Type removeLast() {
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
    public Type get(int index) {
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

    public Iterator<Type> iterator() {
        return new Iterator<Type>() {
            private int index = getValidIndex(nextFirst + 1);

            @Override
            public boolean hasNext() {
                return size != 0;
            }

            @Override
            public Type next() {
                Type val = items[index];
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
