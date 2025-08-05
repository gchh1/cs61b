package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable, V> implements Map61B<K, V> {

    /** Represents one basic node in the binary search tree
     *  that stores the key-value pairs
     */
    private class BSTNode {

        K key;
        V val;
        BSTNode left;
        BSTNode right;


        BSTNode(K k, V v, BSTNode l, BSTNode r) {
            key = k;
            val = v;
            left = l;
            right = r;
        }

        BSTNode get(K k) {
            int cmp = k.compareTo(key);
            if (k != null && cmp == 0) {
                return this;
            }
            if (cmp < 0) {
                if (this.left == null) {
                    return null;
                }
                return this.left.get(k);
            }

            if (this.right == null) {
                return null;
            }
            return this.right.get(k);
        }

    }

    private BSTNode BST;
    private int size;

    public BSTMap() {
        BST = null;
        size = 0;
    }

    // Override methods
    /** Remove all the mapping in the binary search tree */
    @Override
    public void clear() {
        size = 0;
        BST = null;
    }

    /** Returns true if this map contains a mapping for the specified key
     * @param key: specified key
     * @return bool: true if contain
     */
    @Override
    public boolean containsKey(K key) {
        if (BST == null) {
            return false;
        }
        return BST.get(key) != null;
    }

    @Override
    public V get(K key) {
        if (BST == null || BST.get(key) == null) {
            return null;
        }
        return BST.get(key).val;
    }

    @Override
    public int size() {
        return size;
    }

    /** Put the key-value pair into the map
     * @param key: represents the key
     * @param value: represents the value
     */
    @Override
    public void put(K key, V value) {
        BST = insert(BST, key, value);
    }

    @Override
    public Set<K> keySet() {
        Set<K> set = new LinkedHashSet<>();
        Iterator<K> it = this.iterator();
        while (it.hasNext()) {
            set.add(it.next());
        }
        return set;
    }

    /** Remove the pair that owns key in the map
     * @param key: target key
     * @return val: the value
     */
    @Override
    public V remove(K key) {
        if (!this.containsKey(key)) {
            return null;
        }
        size -= 1;
        V res = BST.get(key).val;
        BST = delete(BST, key);
        return res;
    }

    @Override
    public V remove(K key, V value) {
        if (!this.containsKey(key) || !value.equals(BST.get(key).val)) {
            return null;
        }
        size -= 1;
        V res = BST.get(key).val;
        BST = delete(BST, key);
        return res;
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {

            private Stack<BSTNode> stack = new Stack<>();

            {
                pushLeft(BST);
            }

            private void pushLeft(BSTNode node) {
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }

            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }

            @Override
            public K next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                BSTNode curr = stack.pop();
                pushLeft(curr.right);
                return curr.key;
            }
        };
    }

    // Specific methods
    public void printInOrder() {
        printInOrder(BST);
    }

    /** Helper function for print in increase */
    private void printInOrder(BSTNode n) {
        if (n == null) {
            return;
        }
        printInOrder(n.left);
        System.out.println(n.val);
        printInOrder(n.right);
    }


    /** Helper function for put method */
    private BSTNode insert(BSTNode n, K k, V v) {
        if (n == null) {
            size += 1;
            return new BSTNode(k, v, null, null);
        }
        int cmp = k.compareTo(n.key);
        if (cmp > 0) {
            n.right = insert(n.right, k, v);
        } else if (cmp < 0) {
            n.left = insert(n.left, k, v);
        } else {
            n.val = v;
            size -= 1;
        }

        return n;
    }

    /** Helper function for remove method */
    private BSTNode delete(BSTNode n, K k) {
        if (n == null) {
            return null;
        }

        int cmp = k.compareTo(n.key);
        if (cmp < 0) {
            n.left = delete(n.left, k);
        } else if (cmp > 0) {
            n.right = delete(n.right, k);
        } else {
            if (n.left == null) {
                return n.right;
            }
            if (n.right == null) {
                return n.left;
            }
            BSTNode successor = findMin(n.right);
            n.key = successor.key;
            n.val = successor.val;
            n.right = delete(n.right, successor.key);
        }
        return n;
    }

    /** Find the minimum item in the binary search tree */
    private BSTNode findMin(BSTNode n) {
        BSTNode res = n;
        while (res.left != null) {
            res = res.left;
        }
        return res;
    }
}
