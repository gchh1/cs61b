package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author yhc
 */
public class MyHashMap<K, V> implements Map61B<K, V> {


    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size;
    private double loadFactor;

    /** Constructors */
    public MyHashMap() {
        this(8, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.size = 0;
        this.loadFactor = maxLoad;
        buckets = new Collection[initialSize];
        for (int i = 0; i < initialSize; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new HashSet<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return null;
    }


    @Override
    public void clear() {
        buckets = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    @Override
    public V get(K key) {
        return getNode(key) == null ? null : getNode(key).value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        // Resize if reach the load factor
        if ((double) size / buckets.length >= loadFactor) {
            resize(2 * buckets.length);
        }

        // Update the same key
        if (this.containsKey(key)) {
            getNode(key).value = value;
        } else {
            assign(key, value);
            size++;
        }

    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Collection<Node> bucket : buckets) {
            for (Node item : bucket) {
                keySet.add(item.key);
            }
        }
        return keySet;
    }

    @Override
    public V remove(K key) {
        V res = null;
        if (containsKey(key)) {
            Node node = getNode(key);
            int bucketNum = Math.floorMod(key.hashCode(), buckets.length);
            buckets[bucketNum].remove(node);
            res = node.value;
            size--;
        }
        return res;
    }

    @Override
    public V remove(K key, V value) {
        V res = null;
        if (containsKey(key)) {
            Node node = getNode(key);
            int bucketNum = Math.floorMod(key.hashCode(), buckets.length);
            if (node.value.equals(value)) {
                buckets[bucketNum].remove(node);
                size--;
                res = node.value;
            }
        }
        return res;
    }

    @Override
    public Iterator<K> iterator() {
        return this.keySet().iterator();
    }

    /** Resize the buckets whenever the load factor reach the maximum */
    private void resize(int capacity) {
        Collection<Node>[] newBuckets = new Collection[capacity];
        for (int i = 0; i < capacity; i++) {
            newBuckets[i] = createBucket();
        }

        // Reassign the element
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                int bucketNum = Math.floorMod(node.key.hashCode(), capacity);
                newBuckets[bucketNum].add(createNode(node.key, node.value));
            }
        }
        buckets = newBuckets;
    }

    /** Assign one item into the table */
    private void assign(K key, V value) {
        int bucketNum = Math.floorMod(key.hashCode(), buckets.length);
        buckets[bucketNum].add(createNode(key, value));
    }

    /** Get the node through the key */
    private Node getNode(K key) {
        if (size != 0) {
            int bucketNum = Math.floorMod(key.hashCode(), buckets.length);
            for (Node item : buckets[bucketNum]) {
                if (item.key.equals(key)) {
                    return item;
                }
            }
        }
        return null;
    }
}
