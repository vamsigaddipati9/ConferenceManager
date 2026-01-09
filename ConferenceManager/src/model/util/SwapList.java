package edu.ncsu.csc216.wolf_proceedings.model.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Provides a list of elements that can be moved up, down, or to the front/back.
 * Implements the ISwapList interface using an array-based structure.
 *
 * @param <E> the type of elements stored in the list
 */
public class SwapList<E> implements ISwapList<E> {

    /** Array to store the elements of the list */
    private E[] list;

    /** Current number of elements in the list */
    private int size;

    /** Initial capacity of the array */
    private static final int INITIAL_CAPACITY = 10;

    /**
     * Constructs a new SwapList with an initial capacity.
     * Initializes the internal array and sets size to 0.
     */
    @SuppressWarnings("unchecked")
    public SwapList() {
        list = (E[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Adds the specified element to the end of the list.
     *
     * @param element the element to add
     * @throws NullPointerException if the element is null
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new NullPointerException("Cannot add null element.");
        }
        checkCapacity(size + 1);
        list[size] = element;
        size++;
    }

    /**
     * Ensures that the internal array has enough capacity to hold the specified size.
     * If the array is too small, it is resized to a larger capacity.
     *
     * @param newSize the required minimum capacity
     */
    @SuppressWarnings("unchecked")
    private void checkCapacity(int newSize) {
        if (newSize > list.length) {
            int newCapacity = list.length * 2;
            E[] newList = (E[]) new Object[newCapacity];
            for (int i = 0; i < size; i++) {
                newList[i] = list[i];
            }
            list = newList;
        }
    }

    /**
     * Removes and returns the element at the specified index.
     * Shifts subsequent elements to fill the gap.
     *
     * @param idx the index of the element to remove
     * @return the removed element
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    @Override
    public E remove(int idx) {
        checkIndex(idx);
        E removed = list[idx];
        for (int i = idx; i < size - 1; i++) {
            list[i] = list[i + 1];
        }
        list[size - 1] = null;
        size--;
        return removed;
    }

    /**
     * Checks whether the specified index is valid for accessing elements.
     *
     * @param idx the index to check
     * @throws IndexOutOfBoundsException if the index is less than 0 or greater than or equal to size
     */
    private void checkIndex(int idx) {
        if (idx < 0 || idx >= size) {
            throw new IndexOutOfBoundsException("Invalid index.");
        }
    }

    /**
     * Moves the element at the specified index up by one position.
     * If the element is already at the front, the list is not changed.
     *
     * @param idx the index of the element to move up
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    @Override
    public void moveUp(int idx) {
        checkIndex(idx);
        if (idx > 0) {
            E temp = list[idx];
            list[idx] = list[idx - 1];
            list[idx - 1] = temp;
        }
    }

    /**
     * Moves the element at the specified index down by one position.
     * If the element is already at the end, the list is not changed.
     *
     * @param idx the index of the element to move down
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    @Override
    public void moveDown(int idx) {
        checkIndex(idx);
        if (idx < size - 1) {
            E temp = list[idx];
            list[idx] = list[idx + 1];
            list[idx + 1] = temp;
        }
    }

    /**
     * Moves the element at the specified index to the front of the list.
     * If the element is already at the front, the list is not changed.
     *
     * @param idx the index of the element to move to the front
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    @Override
    public void moveToFront(int idx) {
        checkIndex(idx);
        if (idx > 0) {
            E element = list[idx];
            for (int i = idx; i > 0; i--) {
                list[i] = list[i - 1];
            }
            list[0] = element;
        }
    }

    /**
     * Moves the element at the specified index to the back of the list.
     * If the element is already at the back, the list is not changed.
     *
     * @param idx the index of the element to move to the back
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    @Override
    public void moveToBack(int idx) {
        checkIndex(idx);
        if (idx < size - 1) {
            E element = list[idx];
            for (int i = idx; i < size - 1; i++) {
                list[i] = list[i + 1];
            }
            list[size - 1] = element;
        }
    }

    /**
     * Returns the element at the specified index without removing it.
     *
     * @param idx the index of the element to retrieve
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    @Override
    public E get(int idx) {
        checkIndex(idx);
        return list[idx];
    }

    /**
     * Returns the number of elements currently in the list.
     *
     * @return the size of the list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns an iterator over the elements in the list in proper sequence.
     *
     * @return an iterator over the elements in this list
     */
    @Override
    public Iterator<E> iterator() {
        return new SwapListIterator();
    }

    /**
     * Iterator implementation for SwapList.
     * Iterates over elements in the list in proper sequence.
     */
    private class SwapListIterator implements Iterator<E> {

        /** Index of the next element to return */
        private int current;

        /** Index of the last element returned by next(), -1 if none */
        private int lastReturnedIndex = -1;

        /**
         * Constructs a new SwapListIterator starting at the beginning of the list.
         */
        public SwapListIterator() {
            current = 0;
        }

        /**
         * Returns true if the iteration has more elements.
         *
         * @return true if there are more elements to iterate over
         */
        @Override
        public boolean hasNext() {
            return current < size;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element
         * @throws NoSuchElementException if no more elements exist
         */
        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturnedIndex = current;
            return list[current++];
        }

        /**
         * Removes from the list the last element returned by next().
         * Can only be called once per call to next().
         *
         * @throws IllegalStateException if next() has not been called
         *                               or remove() has already been called after the last next()
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove not supported.");
        }
    }
}
