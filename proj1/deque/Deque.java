package deque;

import java.util.Iterator;

public interface Deque<Type> {
    /* Add an item of type Type to the front of the deque */
    public void addFirst(Type item);

    /* Add an item of type Type to the back of the deque */
    public void addLast(Type item);

    /* Return true if deque if empty, false otherwise */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /* Return the number of items in the deque */
    public int size();

    /* Print the item in the deque from first to last, separated by a space */
    public void printDeque();

    /* Remove and return the item at the front of the deque. Return null when no such item */
    public Type removeFirst();

    /* Remove and return the item at the back of the deque. Return null when no such item */
    public Type removeLast();

    /* Get the item at the given index. Return null when no such item */
    public Type get(int index);
}
