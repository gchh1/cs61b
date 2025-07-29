public class SLList<typename> {
    private class ListNode {
        public typename item;
        public ListNode next;

        /** constructor for ListNode */
        public ListNode(typename i, ListNode n) {
            item = i;
            next = n;
        }


    }

    /** 哨兵节点 */
    private ListNode sentinel;
    private int size;

    /** constructor for SLList */
    public SLList() {
        sentinel = new ListNode(null, null); 
        size = 0;
    }

    public SLList(typename x) {
        sentinel = new ListNode(null, null);
        sentinel.next = new ListNode(x, null); 
        size = 1;
    }
    
    /** add x to the first of SLList */
    public void addFirst(typename x) {
        sentinel.next = new ListNode(x, sentinel.next);
        size += 1;
    }

    /** get the first item of SLList */
    public typename getFirst() {
        return sentinel.next.item;
    }

    /** add x to the last of SLList */
    public void addLast(typename x) {
        ListNode p = sentinel;

        while (p.next != null) {
            p = p.next;
        }
        
        size += 1;
        p.next = new ListNode(x, null);
    }

    /** get the last item of SLList */
    public typename getLast() {
        ListNode last = sentinel;
        while (last.next != null) {
            last = last.next;
        }

        return last.item;
    }

    /** return the size of the list that starts at node p */
    private int size(ListNode p) {
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
        // or SLList<String> List = new SLList<>();
        SLList<String> list = new SLList<String>();
        list.addLast("hello");
        System.out.println(list.getLast());
    }
}       
