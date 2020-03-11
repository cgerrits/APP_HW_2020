package Datastructures.LinkedList;

public class HeaderNode<T>{

    private ListNode<T> node;

    public HeaderNode() {
        node = new ListNode<>();
    }

    public ListNode<T> add(T element) {
        ListNode<T> newNode = new ListNode<>(node, null, element);
        node.setPrev(newNode);
        node = newNode;
        return node;
    }

    public ListNode<T> remove() {
        ListNode<T> removed = node;
        node = node.getNext();
        node.setPrev(null);
        return removed;
    }

    public ListNode<T> get(int index){
        ListNode<T> current = node;
        while(index-- > 0) {
            current = (current == null) ? null : current.getNext();
        }
        return current;
    }

    public void insert(int index, T element) {
        ListNode<T> pos = get(index);
        ListNode<T> newNode = new ListNode<T>( pos, pos.getPrev(), element);
        pos.getPrev().setNext(newNode);
        pos.getNext().setPrev(newNode);
    }

    public ListNode<T> delete(int index) {
        ListNode<T> deleted = get(index);
        deleted.getPrev().setNext(deleted.getNext());
        deleted.getNext().setPrev(deleted.getPrev());
        return deleted;
    }
}
