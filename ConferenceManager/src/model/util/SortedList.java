package edu.ncsu.csc216.wolf_proceedings.model.util;

/**
 * The SortedList class provides an implementation of the
 * ISortedList interface that maintains elements in sorted order.
 * Elements are stored in ascending order according to their natural ordering.
 *
 * @author Vamsi Gaddipati
 * @param <E> the type of elements stored in this list; must implement Comparable
 */
public class SortedList<E extends Comparable<E>> implements ISortedList<E> {
    
    /** The number of elements currently in the list. */
    private int size;
    
    /** The first node of the linked list. */
    private ListNode front;

    /**
     * Constructs an empty SortedList object.
     */
    public SortedList() {
        size = 0;
        front = null;
    }

    /**
     * Adds the specified element to the list in sorted order.
     *
     * @param element the element to add
     * @throws NullPointerException if the specified element is null
     * @throws IllegalArgumentException if the element already exists in the list
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new NullPointerException("Cannot add null element.");
        }
        
        if (contains(element)) {
            throw new IllegalArgumentException("Cannot add duplicate element.");
        }
        
        if (front == null || element.compareTo(front.data) < 0) {
            front = new ListNode(element, front);
        } else {
            ListNode current = front;
            while (current.next != null && element.compareTo(current.next.data) > 0) {
                current = current.next;
            }
            current.next = new ListNode(element, current.next);
        }
        size++;
    }

    /**
     * Removes and returns the element at the specified index in the list.
     *
     * @param idx the index of the element to remove
     * @return the element previously at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public E remove(int idx) {
        checkIndex(idx);
        E removedData;
        
        if (idx == 0) {
            removedData = front.data;
            front = front.next;
        } else {
            ListNode current = front;
            for (int i = 0; i < idx - 1; i++) {
                current = current.next;
            }
            removedData = current.next.data;
            current.next = current.next.next;
        }
        size--;
        return removedData;
    }
    
    /**
     * Checks whether the given index is within the valid range of the list.
     *
     * @param idx the index to check
     * @throws IndexOutOfBoundsException if the index is less than 0 or
     *         greater than or equal to the size of the list
     */
    private void checkIndex(int idx) {
        if (idx < 0 || idx >= size) {
            throw new IndexOutOfBoundsException("Invalid index.");
        }
    }

    /**
     * Returns true if the list contains the specified element.
     *
     * @param element the element to check for
     * @return true if this list contains the element; false otherwise
     */
    @Override
    public boolean contains(E element) {
        if (element == null) {
            return false;
        }
        ListNode current = front;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param idx the index of the element to return
     * @return the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @Override
    public E get(int idx) {
        checkIndex(idx);
        ListNode current = front;
        for (int i = 0; i < idx; i++) {
            current = current.next;
        }
        return current.data;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns the index of the specified element in this list,
     * or -1 if this list does not contain the element.
     *
     * @param element the element to search for
     * @return the index of the specified element, or -1 if not found
     */
    @Override
    public int indexOf(E element) {
        if (element == null) {
            return -1;
        }
        ListNode current = front;
        int index = 0;
        while (current != null) {
            if (current.data.equals(element)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }
    
    /**
     * Private inner class representing a single node in the linked list.
     */
    private class ListNode {
        /** The data stored in this node. */
        private E data;
        /** Reference to the next node in the list. */
        private ListNode next;

        /**
         * Constructs a ListNode with the given data and next node reference.
         *
         * @param data the data element stored in this node
         * @param next reference to the next node in the list
         * @throws NullPointerException if data is null
         */
        public ListNode(E data, ListNode next) {
            if (data == null) {
                throw new NullPointerException("Cannot store null data in list.");
            }
            this.data = data;
            this.next = next;
        }
    }
}
