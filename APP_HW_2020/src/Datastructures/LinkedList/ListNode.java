package Datastructures.LinkedList;

public class ListNode <T> {
    private ListNode <T> next;
    private ListNode <T> prev;

    private T value;

    public ListNode(ListNode<T> next, ListNode<T> prev, T value) {
        this.next = next;
        this.prev = prev;
        this.value = value;
    }

    public ListNode() {}

    public ListNode(T value) {
        this.value = value;
    }

    public ListNode<T> getNext() {
        return next;
    }

    public void setNext(ListNode<T> next) {
        this.next = next;
    }

    public ListNode<T> getPrev() {
        return prev;
    }

    public void setPrev(ListNode<T> prev) {
        this.prev = prev;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /**
     * String representation of single node in linked list
     * @return String concatenation of node values
     */
    @Override
    public String toString() {
        String result = "(";

        result += (getPrev() != null)
                ? getPrev().getValue() + " <-- "
                : "       ";

        result += "(" + value + ")";

        result += (getNext() != null)
                ? " --> " + getNext().getValue() +""
                : "       ";

        result += ")";
        return result;
    }
}
