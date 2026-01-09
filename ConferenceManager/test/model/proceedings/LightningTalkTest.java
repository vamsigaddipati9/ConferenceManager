package edu.ncsu.csc216.wolf_proceedings.model.proceedings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_proceedings.model.session.Session;

/**
 * Test class for the LightningTalk class.
 * Provides tests for constructor, getters, setters, session behavior,
 * and comparison methods for LightningTalk objects.
 *  
 * @author Vamsi Gaddipati
 */
class LightningTalkTest {

    /** A sample lightning talk used for testing */
    private LightningTalk lightningTalk;

    /**
     * Sets up the test environment before each test method.
     * Initializes a sample LightningTalk object.
     */
    @BeforeEach
    void setUp() {
        lightningTalk = new LightningTalk("Jane Doe", "Lightning Talk on AI");
    }

    /**
     * Tests the constructor and getters for LightningTalk.
     */
    @Test
    void testConstructorAndGetters() {
        assertEquals("Jane Doe", lightningTalk.getAuthors());
        assertEquals("Lightning Talk on AI", lightningTalk.getTitle());
        assertEquals("LightningTalk", lightningTalk.getType());
        assertEquals(5, lightningTalk.getDuration());
    }

    /**
     * Tests setting and getting the authors.
     */
    @Test
    void testSetAndGetAuthors() {
        lightningTalk.setAuthors("John Smith");
        assertEquals("John Smith", lightningTalk.getAuthors());
    }

    /**
     * Tests setting and getting the title.
     */
    @Test
    void testSetAndGetTitle() {
        lightningTalk.setTitle("New Title");
        assertEquals("New Title", lightningTalk.getTitle());
    }

    /**
     * Tests getting the type of the LightningTalk.
     */
    @Test
    void testGetType() {
        assertEquals("LightningTalk", lightningTalk.getType());
    }

    /**
     * Tests getting the duration of the LightningTalk.
     */
    @Test
    void testGetDuration() {
        assertEquals(5, lightningTalk.getDuration());
    }

    /**
     * Tests adding and removing a session to/from the LightningTalk.
     */
    @Test
    void testAddAndRemoveSession() {
        Session s = new Session("AI Session", 60);

        lightningTalk.addSession(s);
        assertEquals(s, lightningTalk.getSession());

        lightningTalk.removeSession();
        assertNull(lightningTalk.getSession());
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    void testCompareTo() {
        LightningTalk lt2 = new LightningTalk("ZZZ Author", "A Title");
        LightningTalk lt3 = new LightningTalk("Jane Doe", "Lightning Talk on AI");

        assertTrue(lightningTalk.compareTo(lt2) < 0);
        assertTrue(lt2.compareTo(lightningTalk) > 0);
        assertEquals(0, lightningTalk.compareTo(lt3));
    }

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        String out = lightningTalk.toString();

        assertTrue(out.contains("LightningTalk"));
        assertTrue(out.contains("Jane Doe"));
        assertTrue(out.contains("Lightning Talk on AI"));
    }
}
