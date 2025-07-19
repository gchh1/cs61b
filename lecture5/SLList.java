public class SLList {
    private class ListNode {
        public int item;
        public ListNode next;

        /** constructor for ListNode */
        public ListNode(int i, ListNode n) {
            item = i;
            next = n;
        }


    }

    /** 哨兵节点 */
    private ListNode sentinel;
    private int size;

    /** constructor for SLList */
    public SLList() {
        sentinel = new ListNode(0, null); 
        size = 0;
    }

    public SLList(int x) {
        sentinel = new ListNode(0, null);
        sentinel.next = new ListNode(x, null); 
        size = 1;
    }
    
    /** add x to the first of SLList */
    public void addFirst(int x) {
        sentinel.next = new ListNode(x, sentinel.next);
        size += 1;
    }

    /** get the first item of SLList */
    public int getFirst() {
        return sentinel.next.item;
    }

    /** add x to the last of SLList */
    public void addLast(int x) {
        ListNode p = sentinel;

        while (p.next != null) {
            p = p.next;
        }
        
        size += 1;
        p.next = new ListNode(x, null);
    }

    /** get the last item of SLList */
    public int getLast() {
        ListNode last = sentinel;
        while (last.next != null) {
            last = last.next;
        }

        return last.item;
    }

    /** return the size of the list that starts at node p */
    private static int size(ListNode p) {
        if (p.next == null) {
            return 1;
        } 
        return 1 + size(p.next);
    }

    /** get the size of SLList */
    public int size() {
        return size;
    }

    public static void main(String[] args) {
        SLList list = new SLList();
        list.addLast(4);
        System.out.println(list.getLast());
    }
}
