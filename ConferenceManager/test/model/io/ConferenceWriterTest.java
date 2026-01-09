package edu.ncsu.csc216.wolf_proceedings.model.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_proceedings.model.conference.Conference;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.Paper;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.Panel;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.LightningTalk;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.AcceptedItem;
import edu.ncsu.csc216.wolf_proceedings.model.session.Session;
import edu.ncsu.csc216.wolf_proceedings.model.util.ISortedList;
import edu.ncsu.csc216.wolf_proceedings.model.util.SortedList;

/**
 * JUnit tests for the ConferenceWriter class.
 * Tests writing conference data, including sessions and accepted items,
 * to a file and handling of various edge cases.
 * 
 * @author Vamsi Gaddipati
 */
class ConferenceWriterTest {

    /** The test output file name */
    private static final String TEST_FILE = "test_conference.txt";

    /** A sample conference used for testing */
    private Conference conf;

    /** A sample session used for testing */
    private Session session1;

    /** Sample accepted item paper for testing */
    private AcceptedItem paper;
    /** Sample accepted item panel for testing */
    private AcceptedItem panel;
    /** Temp accepted item lightning talk for testing */
    private AcceptedItem lt;

    /**
     * Sets up the test environment before each test method.
     * Initializes a sample conference, session, and accepted items.
     */
    @BeforeEach
    void setUp() {
        conf = new Conference("Test Conference");
        session1 = new Session("Morning Session", 90);

        paper = new Paper("Bob Brown", "Cybersecurity Trends", 15);
        panel = new Panel("Eve Adams", "Future of Education");
        lt = new LightningTalk("Alice Johnson", "Future of AI");

        session1.addAcceptedItem(paper);
        session1.addAcceptedItem(panel);
        conf.addSession(session1);

        conf.addAcceptedItem(lt); // unassigned
    }

    /**
     * Cleans up after each test by deleting the test file if it exists.
     */
    @AfterEach
    void tearDown() {
        File f = new File(TEST_FILE);
        if (f.exists()) {
            f.delete();
        }
    }

    /**
     * Tests writing a conference file with valid sessions and accepted items.
     * Verifies that the file exists and contains expected conference name, session info,
     * and unassigned accepted items.
     * 
     * @throws IOException if reading the file fails
     */
    @Test
    void testWriteConferenceFileValid() throws IOException {
        ISortedList<Session> sessions = new SortedList<>();
        sessions.add(session1);

        ISortedList<AcceptedItem> proceedings = new SortedList<>();
        proceedings.add(lt);

        File f = new File(TEST_FILE);
        ConferenceWriter.writeConferenceFile(f, conf.getConferenceName(), sessions, proceedings);

        assertTrue(f.exists(), "File should be created");

        String content = Files.readString(f.toPath());
        assertTrue(content.contains("Test Conference"), "File should contain conference name");
        assertTrue(content.contains("Morning Session,90"), "File should contain session info");

        // Items in the session
        assertTrue(content.contains("* Paper|Bob Brown|Cybersecurity Trends"), "File should contain paper in session");
        assertTrue(content.contains("* Panel|Eve Adams|Future of Education"), "File should contain panel in session");

        // Unassigned item
        assertTrue(content.contains("* LightningTalk|Alice Johnson|Future of AI"), "File should contain unassigned item");
    }

    /**
     * Tests writing a conference file when session and accepted item lists are empty.
     * Verifies that the file is still created and contains the conference name.
     * 
     * @throws IOException if reading the file fails
     */
    @Test
    void testWriteConferenceFileEmptyLists() throws IOException {
        ISortedList<Session> emptySessions = new SortedList<>();
        ISortedList<AcceptedItem> emptyItems = new SortedList<>();

        File f = new File(TEST_FILE);
        ConferenceWriter.writeConferenceFile(f, conf.getConferenceName(), emptySessions, emptyItems);

        assertTrue(f.exists(), "File should be created even with empty lists");

        String content = Files.readString(f.toPath());
        assertTrue(content.contains("Test Conference"), "File should contain conference name");
    }

    /**
     * Tests writing a conference file to an invalid file path.
     * Verifies that an IllegalArgumentException is thrown with the expected message.
     */
    @Test
    void testWriteConferenceFileInvalidPath() {
        String invalidPath = "/invalid/path/test.txt";
        ISortedList<Session> sessions = new SortedList<>();
        ISortedList<AcceptedItem> proceedings = new SortedList<>();

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> ConferenceWriter.writeConferenceFile(new File(invalidPath), conf.getConferenceName(), sessions, proceedings));
        assertEquals("Unable to save file.", e.getMessage());
    }
}
