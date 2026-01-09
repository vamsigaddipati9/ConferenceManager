package edu.ncsu.csc216.wolf_proceedings.model.proceedings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_proceedings.model.session.Session;

/**
 * Test class for the Panel class.
 * Tests constructors, getters, setters, compareTo, and adding/removing sessions.
 * 
 * @author Vamsi Gaddipati
 */
class PanelTest {

    /** A sample panel used for testing */
    private Panel panel;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        panel = new Panel("John Smith", "Panel on Future Tech");
    }

    /**
     * Tests the constructor and getters for Panel.
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals("Panel", panel.getType());
        assertEquals("John Smith", panel.getAuthors());
        assertEquals("Panel on Future Tech", panel.getTitle());
        assertEquals(75, panel.getDuration());  // default duration
    }

    /**
     * Tests setting and getting the authors.
     */
    @Test
    void testSetAndGetAuthors() {
        panel.setAuthors("Updated Author");
        assertEquals("Updated Author", panel.getAuthors());

        assertThrows(IllegalArgumentException.class, () -> panel.setAuthors(null));
        assertThrows(IllegalArgumentException.class, () -> panel.setAuthors(""));
    }

    /**
     * Tests setting and getting the title.
     */
    @Test
    void testSetAndGetTitle() {
        panel.setTitle("Updated Title");
        assertEquals("Updated Title", panel.getTitle());

        assertThrows(IllegalArgumentException.class, () -> panel.setTitle(null));
        assertThrows(IllegalArgumentException.class, () -> panel.setTitle(""));
    }

    /**
     * Tests getting the type of the Panel.
     */
    @Test
    void testGetType() {
        assertEquals("Panel", panel.getType());
    }

    /**
     * Tests getting the duration of the Panel.
     */
    @Test
    void testGetDuration() {
        assertEquals(75, panel.getDuration());
    }

    /**
     * Tests adding and removing a session to/from the Panel.
     */
    @Test
    void testAddAndRemoveSession() {
        Session s = new Session("Morning Session", 90);

        assertNull(panel.getSession());

        panel.addSession(s);
        assertEquals(s, panel.getSession());

        panel.removeSession();
        assertNull(panel.getSession());
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    void testCompareTo() {
        Panel p2 = new Panel("Zack", "Z Title");

        assertTrue(panel.compareTo(p2) != 0);

        Panel same = new Panel("John Smith", "Panel on Future Tech");
        assertEquals(0, panel.compareTo(same));
    }

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        String expected = "Panel|John Smith|Panel on Future Tech";
        assertEquals(expected, panel.toString());
    }
}
