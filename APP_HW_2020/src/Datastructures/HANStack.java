package Datastructures;

import Datastructures.LinkedList.DoublyLinkedList;
import Datastructures.LinkedList.ListNode;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HANStack<T> {
    /**
     * Internally used LinkedList for data storage
     */
    private DoublyLinkedList<T> list;

    /**
     * Constructor for new stack
     */
    public HANStack() {
        list = new DoublyLinkedList<>();
    }

    /**
     * Private iterator class
     * used to iterate over all all linked list elements.
     * Works with foreach loops.
     */
    private class MyIterator implements Iterator<T> {
        private DoublyLinkedList<T> list;
        private int current;

        MyIterator(DoublyLinkedList<T> list) {
            this.list = list;
            this.current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < list.size();
        }

        @Override
        public T next() {
            if(!hasNext()) throw new NoSuchElementException();
            return list.get(current++).getValue();
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    public Iterator<T> iterator() {
        return new MyIterator( list );
    }

    /**
     * Remove the first element from list
     * and return element
     * @return first element in list
     */
    public T pop() {
        ListNode<T> removed = list.removeLast();
        return (removed == null) ? null : removed.getValue();
    }

    /**
     * Array representation of list
     * @return new array
     */
    public T[] toArray() {
        return list.toArray(new Object[list.size()]);
    }

    /**
     * Put a new element in the list
     * at the beginning of the list
     * @param element new value to add to list
     */
    public void push(T element) {
        list.add(element);
    }

    /**
     * See the value of first item without
     * removing it
     * @return first element
     */
    public T top() {
        return list.get( list.size()-1 ).getValue();
    }

    /**
     * Check if stack contains no elements
     * @return 1 stack is empty
     *         0 stack is not empty
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Return a string representation of list
     * values
     * @return string representation
     */
    @Override
    public String toString() {
        return list.toString();
    }
}
