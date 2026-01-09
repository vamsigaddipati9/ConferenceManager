package edu.ncsu.csc216.wolf_proceedings.model.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the SortedList class.
 * Tests adding, removing, retrieving, and searching
 * elements in a sorted list.
 * 
 * @author Vamsi Gaddipati
 */
class SortedListTest {

    /** The list used in the tests */
    private SortedList<String> list;

    /**
     * Sets up a new SortedList before each test.
     */
    @BeforeEach
    void setUp() {
        list = new SortedList<>();
    }

    /**
     * Tests adding elements and verifying sorted order.
     */
    @Test
    void testAdd() {
        list.add("Charlie");
        list.add("Alice");
        list.add("Bob");

        assertEquals("Alice", list.get(0));
        assertEquals("Bob", list.get(1));
        assertEquals("Charlie", list.get(2));
    }

    /**
     * Tests removing elements by index.
     */
    @Test
    void testRemove() {
        list.add("Alice");
        list.add("Bob");
        list.add("Charlie");

        String removed = list.remove(1);
        assertEquals("Bob", removed);
        assertEquals(2, list.size());
        assertEquals("Alice", list.get(0));
        assertEquals("Charlie", list.get(1));

        removed = list.remove(0);
        assertEquals("Alice", removed);
        assertEquals("Charlie", list.get(0));
        assertEquals(1, list.size());
    }

    /**
     * Tests retrieving elements by index.
     */
    @Test
    void testGet() {
        list.add("Alice");
        list.add("Bob");

        assertEquals("Alice", list.get(0));
        assertEquals("Bob", list.get(1));
    }

    /**
     * Tests checking whether the list contains elements.
     */
    @Test
    void testContains() {
        list.add("Alice");
        list.add("Bob");

        assertTrue(list.contains("Alice"));
        assertTrue(list.contains("Bob"));
        assertFalse(list.contains("Charlie"));
        assertFalse(list.contains(null));
    }

    /**
     * Tests retrieving the size of the list.
     */
    @Test
    void testSize() {
        assertEquals(0, list.size());
        list.add("Alice");
        list.add("Bob");
        assertEquals(2, list.size());
        list.remove(0);
        assertEquals(1, list.size());
    }

    /**
     * Tests retrieving the index of an element.
     */
    @Test
    void testIndexOf() {
        list.add("Alice");
        list.add("Bob");
        list.add("Charlie");

        assertEquals(0, list.indexOf("Alice"));
        assertEquals(1, list.indexOf("Bob"));
        assertEquals(2, list.indexOf("Charlie"));
        assertEquals(-1, list.indexOf("David"));
        assertEquals(-1, list.indexOf(null));
    }

    /**
     * Tests adding null and duplicate elements.
     */
    @Test
    void testAddExceptions() {
        assertThrows(NullPointerException.class, () -> list.add(null));
        list.add("Alice");
        assertThrows(IllegalArgumentException.class, () -> list.add("Alice"));
    }

    /**
     * Tests that get() and remove() throw IndexOutOfBoundsException.
     */
    @Test
    void testIndexOutOfBounds() {
        list.add("Alice");
        list.add("Bob");

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(2));

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(2));
    }
}
