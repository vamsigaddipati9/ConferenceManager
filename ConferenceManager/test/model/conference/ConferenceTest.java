package edu.ncsu.csc216.wolf_proceedings.model.conference;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_proceedings.model.proceedings.AcceptedItem;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.Paper;
import edu.ncsu.csc216.wolf_proceedings.model.session.Session;

/**
 * Unit tests for the Conference class. Tests adding, editing, and removing
 * sessions and accepted items, assigning items to sessions, and saving to a
 * file.
 * 
 * @author Vamsi
 */
public class ConferenceTest {

	/** The conference object used in the tests */
	private Conference conf;

	/** A session used in the tests */
	private Session session1;

	/** An accepted item used in the tests */
	private AcceptedItem paper1;

	/**
	 * Sets up test objects before each test. Creates a conference, a session, and
	 * an accepted item.
	 */
	@BeforeEach
	void setUp() {
		conf = new Conference("Test Conference");
		session1 = new Session("Session1", 120);
		paper1 = new Paper("Author A", "Title A", 60);
	}

	/**
	 * Tests adding a session and retrieving it by index.
	 */
	@Test
	void testAddAndGetSession() {
		int idx = conf.addSession(session1);
		assertEquals(0, idx);
		assertEquals(session1, conf.getSession(idx));
	}

	/**
	 * Tests editing an existing session in the conference.
	 */
	@Test
	void testEditSession() {
		conf.addSession(session1);
		Session session2 = new Session("Session2", 90);
		int newIdx = conf.editSession(0, session2);
		assertEquals(0, newIdx);
		assertEquals("Session2", conf.getSession(newIdx).getName());
	}

	/**
	 * Tests removing a session from the conference.
	 */
	@Test
	void testRemoveSession() {
		conf.addSession(session1);
		conf.removeSession(0);
		assertEquals(0, conf.getSessionsAsArray().length);
	}

	/**
	 * Tests adding and editing an accepted item in the conference proceedings.
	 */
	@Test
	void testAddAndEditAcceptedItem() {
		conf.addAcceptedItem(paper1);
		assertEquals(1, conf.getAcceptedItemsAsArray().length);

		AcceptedItem paper2 = new Paper("Author B", "Title B", 45);
		conf.editAcceptedItem(0, paper2);
		assertEquals("Title B", conf.getAcceptedItemsAsArray()[0][2]);
	}

	/**
	 * Tests removing an accepted item from the conference proceedings.
	 */
	@Test
	void testRemoveAcceptedItem() {
		conf.addAcceptedItem(paper1);
		conf.removeAcceptedItem(0);
		assertEquals(0, conf.getAcceptedItemsAsArray().length);
	}

	/**
	 * Tests adding an accepted item to a session.
	 */
	@Test
	void testAddItemToSession() {
		conf.addSession(session1);
		conf.addAcceptedItem(paper1);
		conf.addItemToSession(0, 0);

		String[][] itemsInSession = conf.getAcceptedItemsInSessionAsArray(0);
		assertEquals(1, itemsInSession.length);
		assertEquals("Title A", itemsInSession[0][2]);
	}

	/**
	 * Tests removing an accepted item from a session.
	 */
	@Test
	void testRemoveItemFromSession() {
		conf.addSession(session1);
		conf.addAcceptedItem(paper1);
		conf.addItemToSession(0, 0);
		conf.removeItemFromSession(0, 0);

		String[][] itemsInSession = conf.getAcceptedItemsInSessionAsArray(0);
		assertEquals(0, itemsInSession.length);
	}

	/**
	 * Tests the isChanged flag of the conference.
	 */
	@Test
	void testIsChangedFlag() {
		assertTrue(conf.isChanged());
		conf.setChanged(false);
		assertFalse(conf.isChanged());
	}

	/**
	 * Tests saving the conference to a temporary file. Ensures that the isChanged
	 * flag is reset after saving.
	 * 
	 * @throws IOException if the temporary file cannot be created
	 */
	@Test
	void testSaveConference() throws IOException {
		conf.addSession(session1);
		conf.addAcceptedItem(paper1);

		File tempFile = File.createTempFile("testConference", ".txt");
		tempFile.deleteOnExit();

		conf.saveConference(tempFile);
		assertFalse(conf.isChanged());
	}

	/**
	 * Test getting session as array with a valid capacity.
	 */
	@Test
	public void testGetSessionsAsArrayCorrectCapacity() {
		// Create a conference
		Conference confer = new Conference("TestConf");

		// Create sessions
		Session s1 = new Session("Morning Session", 120);
		Session s2 = new Session("Afternoon Session", 90);

		// Add sessions to the conference
		confer.addSession(s1);
		confer.addSession(s2);

		// Convert to array
		String[][] sessionArray = confer.getSessionsAsArray();

		// Check array dimensions
		assertEquals(2, sessionArray.length);
		assertEquals(3, sessionArray[0].length);

		// Check session 1 values
		assertEquals("Morning Session", sessionArray[1][0]);
		assertEquals("120", sessionArray[1][1]);
		assertEquals("120", sessionArray[1][2]); // no items added, so remaining = duration

		// Check session 2 values
		assertEquals("Afternoon Session", sessionArray[0][0]);
		assertEquals("90", sessionArray[0][1]);
		assertEquals("90", sessionArray[0][2]); // no items added
	}
	
    /** Tests editing an accepted item with a null item */
    @Test
    void testEditAcceptedItemNull() {
        conf.addAcceptedItem(paper1);
        assertThrows(NullPointerException.class, () -> {
            conf.editAcceptedItem(0, null);
        });
    }

    /** Tests editing an accepted item using an invalid (negative) index */
    @Test
    void testEditAcceptedItemNegativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            conf.editAcceptedItem(-1, paper1);
        });
    }

    /** Tests editing an accepted item using an index that is too large */
    @Test
    void testEditAcceptedItemIndexTooLarge() {
        conf.addAcceptedItem(paper1);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            conf.editAcceptedItem(5, paper1);
        });
    }

    /** Tests editing an item so it becomes a duplicate */
    @Test
    void testEditAcceptedItemCreatesDuplicate() {
        conf.addAcceptedItem(new Paper("Author A", "Title A", 60));
        conf.addAcceptedItem(new Paper("Author B", "Title B", 45));

        // Attempt to edit item 0 to match item 1 → should fail
        assertThrows(IllegalArgumentException.class, () -> {
            conf.editAcceptedItem(0, new Paper("Author B", "Title B", 45));
        });
    }

    /** Tests editing an item to itself (same title/authors), which IS allowed */
    @Test
    void testEditAcceptedItemToItself() {
        conf.addAcceptedItem(paper1);

        // Should NOT throw: method skips same index (i == idx)
        assertDoesNotThrow(() -> {
            conf.editAcceptedItem(0, paper1);
        });
    }
    
    /** Tests editing a session with a null session */
    @Test
    void testEditSessionNull() {
        conf.addSession(session1);
        assertThrows(NullPointerException.class, () -> {
            conf.editSession(0, null);
        });
    }

    /** Tests editing a session using a negative index */
    @Test
    void testEditSessionNegativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            conf.editSession(-1, session1);
        });
    }

    /** Tests editing a session using an index that is too large */
    @Test
    void testEditSessionIndexTooLarge() {
        conf.addSession(session1);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            conf.editSession(5, session1);
        });
    }

    /** Tests editing a session to create a duplicate session name */
    @Test
    void testEditSessionCreatesDuplicate() {
        Session session2 = new Session("Session2", 90);
        conf.addSession(session1);
        conf.addSession(session2);

        // Attempt to edit session1 to have the same name as session2 → should fail
        Session duplicate = new Session("Session2", 120);
        assertThrows(IllegalArgumentException.class, () -> {
            conf.editSession(0, duplicate);
        });
    }

    /** Tests editing a session to itself (same name), which is allowed */
    @Test
    void testEditSessionToItself() {
        conf.addSession(session1);

        // Should NOT throw: method skips same index when checking duplicates
        assertDoesNotThrow(() -> {
            conf.editSession(0, session1);
        });
    }

    /** Tests editing a session successfully with a new name */
    @Test
    void testEditSessionSuccess() {
        conf.addSession(session1);
        Session newSession = new Session("NewSession", 100);
        
        int idx = conf.editSession(0, newSession);
        
        assertEquals(0, idx);
        assertEquals("NewSession", conf.getSession(idx).getName());
        assertTrue(conf.isChanged());
    }


}
