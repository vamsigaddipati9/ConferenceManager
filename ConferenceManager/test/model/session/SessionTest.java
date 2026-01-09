package edu.ncsu.csc216.wolf_proceedings.model.session;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_proceedings.model.proceedings.Paper;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.AcceptedItem;

/**
 * Test class for the Session class.
 * 
 * @author Vamsi Gaddipati
 */
class SessionTest {

    /** Sample session used for testing */
    private Session session;

    /** Sample accepted item used for testing */
    private AcceptedItem item;

    /**
     * Sets up the test environment before each test method.
     */
    @BeforeEach
    void setUp() {
        session = new Session("Morning Session", 60);
        item = new Paper("Alice Johnson", "AI and Future Tech"); // 15 minutes
    }

    /**
     * Tests getting and setting the session name.
     */
    @Test
    void testGetAndSetName() {
        assertEquals("Morning Session", session.getName());
        session.setName("Afternoon Session");
        assertEquals("Afternoon Session", session.getName());

        assertThrows(IllegalArgumentException.class, () -> session.setName(null));
        assertThrows(IllegalArgumentException.class, () -> session.setName(""));
        assertThrows(IllegalArgumentException.class, () -> session.setName("   "));
    }

    /**
     * Tests getting and setting the session duration.
     */
    @Test
    void testGetAndSetDuration() {
        assertEquals(60, session.getDuration());
        session.setDuration(90);
        assertEquals(90, session.getDuration());

        assertThrows(IllegalArgumentException.class, () -> session.setDuration(4));
        assertThrows(IllegalArgumentException.class, () -> session.setDuration(121));
    }

    /**
     * Tests retrieving the list of accepted items in the session.
     */
    @Test
    void testGetItemList() {
        assertNotNull(session.getItemList());
        assertEquals(0, session.getItemList().size());
        session.addAcceptedItem(item);
        assertEquals(1, session.getItemList().size());
        assertEquals(item, session.getItemList().get(0));
    }

    /**
     * Tests adding an accepted item to the session.
     */
    @Test
    void testAddAcceptedItem() {
        session.addAcceptedItem(item);
        assertEquals(1, session.getItemList().size());
        assertEquals(item, session.getItemList().get(0));
        assertEquals(session, item.getSession());

        // Adding null should fail
        assertThrows(IllegalArgumentException.class, () -> session.addAcceptedItem(null));

        // Adding item exceeding remaining capacity
        Paper longPaper = new Paper("Bob Smith", "Long Paper", 100);
        assertThrows(IllegalArgumentException.class, () -> session.addAcceptedItem(longPaper));

        // Adding same item again should fail
        assertThrows(IllegalArgumentException.class, () -> session.addAcceptedItem(item));
    }

    /**
     * Tests removing an accepted item from the session.
     */
    @Test
    void testRemoveAcceptedItem() {
        session.addAcceptedItem(item);
        assertEquals(1, session.getItemList().size());
        session.removeAcceptedItem(0);
        assertEquals(0, session.getItemList().size());
        assertNull(item.getSession());

        // Removing invalid index should throw
        assertThrows(IndexOutOfBoundsException.class, () -> session.removeAcceptedItem(0));
    }

    /**
     * Tests getting the remaining capacity of the session.
     */
    @Test
    void testGetRemainingCapacity() {
        assertEquals(60, session.getRemainingCapacity());
        session.addAcceptedItem(item); // 15 minutes
        assertEquals(45, session.getRemainingCapacity());

        Paper paper2 = new Paper("Bob Smith", "ML Paper", 20);
        session.addAcceptedItem(paper2);
        assertEquals(25, session.getRemainingCapacity());
    }

    /**
     * Tests the compareTo method.
     */
    @Test
    void testCompareTo() {
        Session session2 = new Session("Afternoon Session", 90);
        assertTrue(session.compareTo(session2) > 0 || session.compareTo(session2) < 0);

        Session session3 = new Session("Morning Session", 60);
        assertEquals(0, session.compareTo(session3));
    }

    /**
     * Tests the toString method.
     */
    @Test
    void testToString() {
        String expected = "Morning Session;60";
        assertEquals(expected, session.toString());

        session.addAcceptedItem(item);
        expected += "\nPaper|Alice Johnson|AI and Future Tech";
        assertEquals(expected, session.toString());
    }
}
