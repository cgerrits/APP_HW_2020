package Datastructures.LinkedList;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HANLinkedList<T> {
    private HeaderNode<T> header;

    private int length;

    public HANLinkedList() {
        reset();
    }


    /**
     * Private iterator class
     * used to iterate over all all linked list elements.
     * Works with foreach loops.
     */
    private class MyIterator implements Iterator<T> {
        private HANLinkedList<T> list;
        private int current;

        MyIterator(HANLinkedList<T> list) {
            this.list = list;
            this.current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < list.length;
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
        return new MyIterator( this );
    }

    /**
     * Add a node at the beginning of the chain
     * @param element new element to add in a node wrap
     */
    public ListNode<T> add(T element) {
        ListNode<T> newNode = header.add(element);
        length++;
        return newNode;
    }

    public int size() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public ListNode<T> remove() {
        if(length == 0) return null;
        length--;
        return header.remove();
    }

    public void insert(int index, T element) {
        header.insert(index, element);
    }

    public void delete(int index) {
        length--;
        header.delete(index);
    }

    public void reset() {
        header = new HeaderNode<>();
        length = 0;
    }

    public ListNode<T> get(int index) {
        return header.get(index);
    }

    /**
     * Array representation of linkedList
     * @return list representation of linkedList
     */
    @SuppressWarnings("unchecked")
    public T[] toArray(Object[] a) {
        int i = 0;
        T[] result = (a.length < size())
                ? (T[]) new Object[size()]
                : (T[]) a;
        while(i < size()) {
            result[i] = get(i).getValue();
            i++;
        }
        return result;
    }

    /**
     * Return all elements in list as single String
     * @return string concatenation of list elements
     */
    @Override
    public String toString() {
        String result = "";

        for(int i = size()-1; i >= 0; i--) {
            if(get(i) == null) continue;
            result += "\n";
            result += get(i);
        }
        return (result.equals("")) ? "" : result.substring(0, result.length()-1);
    }
}
