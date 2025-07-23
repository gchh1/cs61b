package deque;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class listNode {
        public T item;
        public listNode prev;
        public listNode next;

        public listNode() {
            item = null;
            prev = next = null;
        }

        public listNode(T i, listNode p, listNode n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private listNode sentinel;
    private int size;

    /* constructor */
    public LinkedListDeque() {
        sentinel = new listNode();
        sentinel.prev = sentinel.next = sentinel;
    }

    @Override
    public void addFirst(T item) {
        listNode p = new listNode(item, sentinel, sentinel.next);
        sentinel.next.prev = p;
        sentinel.next = p;

        size += 1;
    }

    @Override
    public void addLast(T item) {
        listNode p = new listNode(item, sentinel.prev, sentinel);
        sentinel.prev.next = p;
        sentinel.prev = p;

        size += 1;
    }

    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        listNode target = sentinel.next;
        sentinel.next = target.next;
        target.next.prev = sentinel;

        size -= 1;

        return target.item;
    }

    @Override
    public T removeLast() {
        if (sentinel.next == sentinel) {
            return null;
        }
        listNode target = sentinel.prev;
        sentinel.prev = target.prev;
        target.prev.next = sentinel;

        size -= 1;

        return target.item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        listNode target = sentinel.next;
        while (index > 0) {
            target = target.next;
            index -= 1;
        }
        return target.item;
    }

    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursiveHelper(sentinel.next, index);
    }

    public T getRecursiveHelper(listNode p, int i) {
        if (i == 0) {
            return p.item;
        }
        return getRecursiveHelper(p.next, i - 1);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        listNode target = sentinel.next;
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < size; i++) {
            output.append((String.valueOf(target.item)));
            if (i != size - 1) {
                output.append(" ");
            }
            target = target.next;
        }
        System.out.println(output.append("\n"));
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private listNode cur = sentinel.next;

            @Override
            public boolean hasNext() {
                return cur != sentinel;
            }

            @Override
            public T next() {
                T val = cur.item;
                cur = cur.next;
                return val;
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deque<?>)) return false;
        if (size != ((Deque<?>)o).size()) return false;

        int index = size - 1;
        boolean flag = true;

        while (index >= 0) {
            if (this.get(index) != ((Deque<?>)o).get(index)) {
                flag = false;
            }
            index -= 1;
        }
        return flag;
    }
}
