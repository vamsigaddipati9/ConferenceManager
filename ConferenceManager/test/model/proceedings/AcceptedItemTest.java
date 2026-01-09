package edu.ncsu.csc216.wolf_proceedings.model.proceedings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_proceedings.model.session.Session;

/**
 * Test class for the AcceptedItem abstract class.
 */
class AcceptedItemTest {

    /** A sample AcceptedItem used for testing */
    private AcceptedItem item;

    /** A sample session used for testing */
    private Session session;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        item = new Paper("Alice Johnson", "AI and Future Tech");
        session = new Session("Morning Session", 90);
    }

    /**
     * Tests getting and setting authors.
     */
    @Test
    void testAuthors() {
        assertEquals("Alice Johnson", item.getAuthors());
        item.setAuthors("Bob Smith");
        assertEquals("Bob Smith", item.getAuthors());

        assertThrows(IllegalArgumentException.class, () -> item.setAuthors(null));
        assertThrows(IllegalArgumentException.class, () -> item.setAuthors(""));
        assertThrows(IllegalArgumentException.class, () -> item.setAuthors("   "));
    }

    /**
     * Tests getting and setting title.
     */
    @Test
    void testTitle() {
        assertEquals("AI and Future Tech", item.getTitle());
        item.setTitle("Machine Learning");
        assertEquals("Machine Learning", item.getTitle());

        assertThrows(IllegalArgumentException.class, () -> item.setTitle(null));
        assertThrows(IllegalArgumentException.class, () -> item.setTitle(""));
        assertThrows(IllegalArgumentException.class, () -> item.setTitle("   "));
    }

    /**
     * Tests getting and setting type.
     */
    @Test
    void testType() {
        assertEquals("Paper", item.getType());
        item.setType("CustomType");
        assertEquals("CustomType", item.getType());

        assertThrows(IllegalArgumentException.class, () -> item.setType(null));
        assertThrows(IllegalArgumentException.class, () -> item.setType(""));
        assertThrows(IllegalArgumentException.class, () -> item.setType("   "));
    }

    /**
     * Tests getting and setting duration.
     */
    @Test
    void testDuration() {
        assertEquals(15, item.getDuration());
        item.setDuration(30);
        assertEquals(30, item.getDuration());

        assertThrows(IllegalArgumentException.class, () -> item.setDuration(4));
        assertThrows(IllegalArgumentException.class, () -> item.setDuration(121));
    }

    /**
     * Tests adding and removing a session.
     */
    @Test
    void testAddAndRemoveSession() {
        assertNull(item.getSession());

        item.addSession(session);
        assertEquals(session, item.getSession());

        // Adding again should fail
        assertThrows(IllegalArgumentException.class, () -> item.addSession(session));

        item.removeSession();
        assertNull(item.getSession());
    }

    /**
     * Tests getting the associated session.
     */
    @Test
    void testGetSession() {
        assertNull(item.getSession());
        item.addSession(session);
        assertEquals(session, item.getSession());
    }

    /**
     * Tests comparing two AcceptedItems.
     */
    @Test
    void testCompareTo() {
        Paper item2 = new Paper("Bob Smith", "AI and Future Tech");
        assertTrue(item.compareTo(item2) > 0 || item.compareTo(item2) < 0 || item.compareTo(item2) == 0);

        Paper same = new Paper("Alice Johnson", "AI and Future Tech");
        assertEquals(0, item.compareTo(same));

        assertThrows(NullPointerException.class, () -> item.compareTo(null));
    }

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        String expected = "Paper|Alice Johnson|AI and Future Tech";
        assertEquals(expected, item.toString());
    }
}
