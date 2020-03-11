package Datastructures.LinkedList;

public class FooterNode<T> {
    private ListNode<T> node;

    public FooterNode() {
        node = new ListNode<>();
    }

    /**
     * Remove the element closest to footer
     * @return the removed element
     */
    public ListNode<T> remove() {
        ListNode<T> removed = node;
        if(node.getPrev() != null) {
            node.getPrev().setNext(null);
            node = node.getPrev();
        } else {
            node = null;
        }
        return removed;
    }

    /**
     * Add an element at the end of the linked list
     * @param element value of new node
     * @return the added node
     */
    public ListNode<T> add(T element) {
        ListNode<T> newNode = new ListNode<T>( element );
        node.setNext(newNode);
        newNode.setPrev(node);
        node = newNode;
        return newNode;
    }

    /**
     * Set a reference to the last node in the chain.
     * Only happens on first add
     * @param node last node in chain
     */
    public void set(ListNode<T> node) {
        this.node = node;
    }


    /**
     * Reset the reference to the last node in the chain.
     * Only happens when list is empty after removing
     */
    public void reset() {
        this.node = null;
    }

    public ListNode<T> get(int index){
        ListNode<T> current = node;

        while(index-- > 0) {
            current = (current == null) ? null : current.getPrev();
        }
        return current;
    }

    public void insert(int index, T element) {
        ListNode<T> pos = get(index);
        ListNode<T> newNode = new ListNode<T>( pos, pos.getPrev(), element);

        pos.getPrev().setNext(newNode);
        pos.setPrev(newNode);
    }

    public ListNode<T> delete(int index) {
        ListNode<T> deleted = get(index);

        if(deleted.getPrev() != null)
            deleted.getPrev().setNext(deleted.getNext());

        if(deleted.getNext() != null)
            deleted.getNext().setPrev(deleted.getPrev());

        return deleted;
    }
}