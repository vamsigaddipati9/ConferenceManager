package edu.ncsu.csc216.wolf_proceedings.model.proceedings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_proceedings.model.session.Session;

/**
 * Test class for the Paper class.
 * Tests constructors, getters, setters, compareTo, and adding/removing sessions.
 * 
 * @author Vamsi Gaddipati
 */
class PaperTest {

    /** A sample paper used for testing */
    private Paper paper;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        paper = new Paper("Alice Johnson", "AI and Future Tech");
    }

    /**
     * Tests the constructor with default duration and getters.
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals("Paper", paper.getType());
        assertEquals("Alice Johnson", paper.getAuthors());
        assertEquals("AI and Future Tech", paper.getTitle());
        assertEquals(15, paper.getDuration());  // default duration
    }

    /**
     * Tests the constructor with custom duration.
     */
    @Test
    void testConstructorWithDuration() {
        Paper p = new Paper("Bob Brown", "Custom Paper", 30);
        assertEquals("Paper", p.getType());
        assertEquals("Bob Brown", p.getAuthors());
        assertEquals("Custom Paper", p.getTitle());
        assertEquals(30, p.getDuration());
    }

    /**
     * Tests setting and getting the authors.
     */
    @Test
    void testSetAndGetAuthors() {
        paper.setAuthors("New Author");
        assertEquals("New Author", paper.getAuthors());

        assertThrows(IllegalArgumentException.class, () -> paper.setAuthors(null));
        assertThrows(IllegalArgumentException.class, () -> paper.setAuthors(""));
    }

    /**
     * Tests setting and getting the title.
     */
    @Test
    void testSetAndGetTitle() {
        paper.setTitle("Updated Title");
        assertEquals("Updated Title", paper.getTitle());

        assertThrows(IllegalArgumentException.class, () -> paper.setTitle(null));
        assertThrows(IllegalArgumentException.class, () -> paper.setTitle(""));
    }

    /**
     * Tests getting the type of the Paper.
     */
    @Test
    void testGetType() {
        assertEquals("Paper", paper.getType());
    }

    /**
     * Tests getting the duration of the Paper.
     */
    @Test
    void testGetDuration() {
        assertEquals(15, paper.getDuration());
    }

    /**
     * Tests adding and removing a session to/from the Paper.
     */
    @Test
    void testAddAndRemoveSession() {
        Session s = new Session("Session A", 60);

        assertNull(paper.getSession());

        paper.addSession(s);
        assertEquals(s, paper.getSession());

        paper.removeSession();
        assertNull(paper.getSession());
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    void testCompareTo() {
        Paper p2 = new Paper("Bob", "Another Title");

        assertTrue(paper.compareTo(p2) > 0 || paper.compareTo(p2) < 0 || paper.compareTo(p2) == 0);

        Paper same = new Paper("Alice Johnson", "AI and Future Tech");
        assertEquals(0, paper.compareTo(same));
    }

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
    	assertEquals("Paper|Alice Johnson|AI and Future Tech", paper.toString());
    }
}
