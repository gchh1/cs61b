package deque;

import java.util.Iterator;

public interface Deque<T> {
    /* Add an item of T T to the front of the deque */
    public void addFirst(T item);

    /* Add an item of T T to the back of the deque */
    public void addLast(T item);

    /* Return true if deque if empty, false otherwise */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /* Return the number of items in the deque */
    public int size();

    /* Print the item in the deque from first to last, separated by a space */
    public void printDeque();

    /* Remove and return the item at the front of the deque. Return null when no such item */
    public T removeFirst();

    /* Remove and return the item at the back of the deque. Return null when no such item */
    public T removeLast();

    /* Get the item at the given index. Return null when no such item */
    public T get(int index);
}
