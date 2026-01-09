package edu.ncsu.csc216.wolf_proceedings.model.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SwapList class.
 * Tests adding, removing, retrieving, and moving elements,
 * as well as iterator behavior and exception cases.
 * 
 * 
 * @author Vamsi Gaddipati
 */
class SwapListTest {

    /** The SwapList used in the tests */
    private SwapList<String> list;

    /**
     * Sets up a new empty SwapList before each test.
     */
    @BeforeEach
    void setUp() {
        list = new SwapList<>();
    }

    /**
     * Tests adding elements to the list.
     */
    @Test
    void testAdd() {
        list.add("A");
        list.add("B");
        list.add("C");

        assertEquals(3, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(2));
    }

    /**
     * Tests removing elements from the list.
     */
    @Test
    void testRemove() {
        list.add("A");
        list.add("B");
        list.add("C");

        String removed = list.remove(1);
        assertEquals("B", removed);

        assertEquals(2, list.size());
        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
    }

    /**
     * Tests getting elements by index.
     */
    @Test
    void testGet() {
        list.add("A");
        list.add("B");

        assertEquals("A", list.get(0));
        assertEquals("B", list.get(1));
    }

    /**
     * Tests the size of the list after operations.
     */
    @Test
    void testSize() {
        assertEquals(0, list.size());

        list.add("A");
        list.add("B");

        assertEquals(2, list.size());

        list.remove(0);
        assertEquals(1, list.size());
    }

    /**
     * Tests moving elements up by one position.
     */
    @Test
    void testMoveUp() {
        list.add("A");
        list.add("B");
        list.add("C");

        list.moveUp(2); // move "C" up: A, C, B
        assertEquals("C", list.get(1));
        assertEquals("B", list.get(2));

        list.moveUp(0); // no change
        assertEquals("A", list.get(0));
    }

    /**
     * Tests moving elements down by one position.
     */
    @Test
    void testMoveDown() {
        list.add("A");
        list.add("B");
        list.add("C");

        list.moveDown(0); // move A down: B, A, C
        assertEquals("B", list.get(0));
        assertEquals("A", list.get(1));

        list.moveDown(2); // no change
        assertEquals("C", list.get(2));
    }

    /**
     * Tests moving elements to the front of the list.
     */
    @Test
    void testMoveToFront() {
        list.add("A");
        list.add("B");
        list.add("C");

        list.moveToFront(2); // C → front: C, A, B
        assertEquals("C", list.get(0));

        list.moveToFront(0); // should do nothing
        assertEquals("C", list.get(0));
    }

    /**
     * Tests moving elements to the back of the list.
     */
    @Test
    void testMoveToBack() {
        list.add("A");
        list.add("B");
        list.add("C");

        list.moveToBack(0); // A → back: B, C, A
        assertEquals("A", list.get(2));

        list.moveToBack(2); // should do nothing
        assertEquals("A", list.get(2));
    }

    /**
     * Tests iterator behavior including hasNext, next, and exception cases.
     */
    @Test
    void testIterator() {
        list.add("A");
        list.add("B");
        list.add("C");

        Iterator<String> it = list.iterator();

        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertEquals("B", it.next());
        assertEquals("C", it.next());
        assertFalse(it.hasNext());

        assertThrows(NoSuchElementException.class, () -> it.next());
        assertThrows(UnsupportedOperationException.class, () -> it.remove());
    }

    /**
     * Tests that add() throws NullPointerException when adding null elements.
     */
    @Test
    void testAddNullException() {
        assertThrows(NullPointerException.class, () -> list.add(null));
    }

    /**
     * Tests that get() and remove() throw IndexOutOfBoundsException for invalid indexes.
     */
    @Test
    void testIndexOutOfBounds() {
        list.add("A");

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));

        assertThrows(IndexOutOfBoundsException.class, () -> list.moveUp(2));
        assertThrows(IndexOutOfBoundsException.class, () -> list.moveDown(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.moveToFront(5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.moveToBack(-2));
    }
}
