package edu.ncsu.csc216.wolf_proceedings.model.io;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.wolf_proceedings.model.conference.Conference;

/**
 * JUnit tests for the ConferenceReader class.
 * 
 * @author Vamsi Gaddipati
 */
class ConferenceReaderTest {

    /** Temporary test file used in each test */
    private File testFile;

    /**
     * Sets up a temporary conference file before each test.
     * The file contains a conference name, multiple sessions with assigned
     * items, and unassigned items.
     * 
     * @throws IOException if there is a problem creating the temporary file
     */
    @BeforeEach
    void setUp() throws IOException {
        testFile = File.createTempFile("tempConference", ".txt");
        testFile.deleteOnExit();

        try (FileWriter writer = new FileWriter(testFile)) {
            // Conference name
            writer.write("SIGCSE Technical Symposium 2020\n");

            // Morning Session
            writer.write("#Morning Session,100\n");
            writer.write("*Paper|Alice Johnson|AI and Future Tech|15\n");
            writer.write("*Panel|John Smith|Panel on Future Tech\n");

            // Afternoon Session
            writer.write("#Afternoon Session,90\n");
            writer.write("*LightningTalk|Jane Doe|Quantum Computing\n");
            writer.write("*Paper|Bob Brown|Cybersecurity Trends|85\n");

            // Unassigned items
            writer.write("+++\n");
            writer.write("*Paper|Charlie Davis|Future of AI|15\n");
            writer.write("*Panel|Eve Adams|Future of Education\n");
        }
    }

    /**
     * Tests that ConferenceReader correctly reads a valid conference file.
     * Verifies sessions, assigned accepted items, unassigned items,
     * and remaining capacities.
     */
    @Test
    void testReadConferenceFileValid() {
        Conference conf = ConferenceReader.readConferenceFile(testFile);
        assertNotNull(conf);
        assertEquals("SIGCSE Technical Symposium 2020", conf.getConferenceName());

        // Check sessions (sorted order)
        String[][] sessions = conf.getSessionsAsArray();
        assertEquals(2, sessions.length, "Should have 2 sessions");

        // Afternoon Session (sorted first)
        assertEquals("Afternoon Session", sessions[0][0]);
        assertEquals("90", sessions[0][1]);
        assertEquals("0", sessions[0][2], "Remaining capacity should be 0 (90 - 5 - 85)");

        // Morning Session
        assertEquals("Morning Session", sessions[1][0]);
        assertEquals("100", sessions[1][1]);
        assertEquals("10", sessions[1][2], "Remaining capacity should account for Paper/Panel");

        // All accepted items (assigned + unassigned)
        String[][] allItems = conf.getAcceptedItemsAsArray();
        assertEquals(6, allItems.length, "There should be 6 accepted items total");

        // Check unassigned items (Charlie + Eve)
        int unassignedCount = 0;
        for (String[] item : allItems) {
            if (item[4] == null || item[4].isEmpty()) {
                unassignedCount++;
            }
        }
        assertEquals(2, unassignedCount, "There should be 2 unassigned items: Charlie + Eve");
    }

    /**
     * Tests that reading a non-existent conference file
     * throws IllegalArgumentException.
     */
    @Test
    void testReadConferenceFileNonExistent() {
        File f = new File("nonexistent_file.txt");
        assertThrows(IllegalArgumentException.class, () -> ConferenceReader.readConferenceFile(f));
    }

    /**
     * Tests that reading a null conference file
     * throws IllegalArgumentException.
     */
    @Test
    void testReadConferenceFileNull() {
        assertThrows(IllegalArgumentException.class, () -> ConferenceReader.readConferenceFile(null));
    }

    /**
     * Tests reading a conference file with unassigned items.
     * Verifies that unknown item types are ignored and valid items
     * are correctly added as unassigned.
     * 
     * @throws Exception if file creation fails
     */
    @Test
    void testProcessAcceptedItemThroughFile() throws Exception {
        File tempFile = File.createTempFile("testConference", ".txt");
        tempFile.deleteOnExit();

        try (java.io.PrintWriter writer = new java.io.PrintWriter(tempFile)) {
            writer.println("My Conference");
            writer.println("# +++"); // unassigned items
            writer.println("*LightningTalk|Alice Johnson|Future of AI");
            writer.println("*UnknownType|Bob Brown|Cybersecurity"); // ignored
        }

        Conference conf = ConferenceReader.readConferenceFile(tempFile);

        String[][] allItems = conf.getAcceptedItemsAsArray();
        assertEquals(1, allItems.length, "Only valid LightningTalk should be added");
        assertEquals("LightningTalk", allItems[0][0]);
        assertEquals("Alice Johnson", allItems[0][1]);
        assertEquals("Future of AI", allItems[0][2]);
        assertEquals("5", allItems[0][3]); // duration 0 for LightningTalk
        assertEquals("", allItems[0][4]); // unassigned
    }

    /**
     * Tests that only valid accepted item types (Paper, Panel, LightningTalk)
     * are added from the file. Verifies that unknown types are ignored.
     * Also checks default durations for Panel and LightningTalk.
     * 
     * @throws Exception if file creation fails
     */
    @Test
    void testProcessAcceptedItemTypes() throws Exception {
        File tempFile = File.createTempFile("itemTypesTest", ".txt");
        tempFile.deleteOnExit();

        try (java.io.PrintWriter writer = new java.io.PrintWriter(tempFile)) {
            writer.println("Item Types Conference");
            writer.println("# +++"); // unassigned
            writer.println("*Paper|Alice Smith|AI Research|20");
            writer.println("*Panel|John Doe|Tech Panel");
            writer.println("*LightningTalk|Jane Roe|Quantum Computing");
            writer.println("*UnknownType|Bob Brown|Cybersecurity"); // ignored
        }

        Conference conf = ConferenceReader.readConferenceFile(tempFile);

        String[][] allItems = conf.getAcceptedItemsAsArray();
        assertEquals(3, allItems.length, "Only Paper, Panel, and LightningTalk should be added");

        // Paper
        assertEquals("Paper", allItems[0][0]);
        assertEquals("Alice Smith", allItems[0][1]);
        assertEquals("AI Research", allItems[0][2]);
        assertEquals("20", allItems[0][3]);

        // Panel
        assertEquals("Panel", allItems[2][0]);
        assertEquals("John Doe", allItems[2][1]);
        assertEquals("Tech Panel", allItems[2][2]);
        assertEquals("75", allItems[2][3], "Panels use default duration");

        // LightningTalk
        assertEquals("LightningTalk", allItems[1][0]);
        assertEquals("Jane Roe", allItems[1][1]);
        assertEquals("Quantum Computing", allItems[1][2]);
        assertEquals("5", allItems[1][3], "LightningTalk default duration");

        // Ensure unknown type ignored
        for (String[] item : allItems) {
            assertNotEquals("UnknownType", item[0]);
        }
    }
    
    /** Test for conference1.txt file*/
    @Test
    public void testConference1() {
        File file = new File("test-files/conference1.txt");
        Conference conf = ConferenceReader.readConferenceFile(file);

        // ---------- Check Title ----------
        assertEquals("SIGCSE Technical Symposium 2020", conf.getConferenceName());

        // ---------- Check Sessions ----------
        String[][] sessions = conf.getSessionsAsArray();

        assertEquals(5, sessions.length);

        assertEquals("Accessibility", sessions[0][0]);
        assertEquals("75", sessions[0][1]);   
	    assertEquals("25", sessions[0][2]);    

        assertEquals("Computing Education Research", sessions[1][0]);
        assertEquals("75", sessions[1][1]);
        assertEquals("0", sessions[1][2]);
        
        assertEquals("Problem Solving", sessions[2][0]);
        assertEquals("45", sessions[2][1]);
        assertEquals("45", sessions[2][2]);

        assertEquals("Security", sessions[3][0]);
        assertEquals("75", sessions[3][1]);
        assertEquals("30", sessions[3][2]);

        assertEquals("Student Experiences", sessions[4][0]);
        assertEquals("40", sessions[4][1]);
        assertEquals("5", sessions[4][2]);

       
        String[][] items = conf.getAcceptedItemsAsArray();

        assertEquals(15, items.length);   

        assertEquals("Paper", items[0][0]);
        assertEquals("Abe Leite; Saúl A. Blanco", items[0][1]);
        assertEquals("Effects of Human vs. Automatic Feedback on Students' Understanding of AI Concepts and Programming Style", items[0][2]);
        assertEquals("15", items[0][3]); 

        assertEquals("Paper", items[4][0]);
        assertEquals("Catherine M. Baker; Yasmine N. El-Glaly; Kristen Shinohara", items[4][1]);
        assertEquals("A Systematic Analysis of Accessibility in Computing Education Research", items[4][2]);
        assertEquals("15", items[4][3]); 
    }
    
    /** Test for conference11.txt file*/
    @Test
    public void testConference11() {
        File file = new File("test-files/conference11.txt");
        Conference conf = ConferenceReader.readConferenceFile(file);
        String[][] sessions = conf.getSessionsAsArray();

        assertEquals("10", sessions[4][2]);
    }
    
    /** Test for conference10.txt file*/
    @Test
    public void testConference10() {
        File file = new File("test-files/conference10.txt");
        Conference conf = ConferenceReader.readConferenceFile(file);
        String[][] items = conf.getAcceptedItemsAsArray();
        
        assertEquals(0, items.length);
    }
    
    /** Test for conference4.txt file*/
    @Test
    public void testConference4() {
        File file = new File("test-files/conference4.txt");
        Conference conf = ConferenceReader.readConferenceFile(file);
        String[][] items = conf.getAcceptedItemsAsArray();
        
        assertEquals(0, items.length);
    }
    
    /** Test for conference3.txt file*/
    @Test
    public void testConference3() {
        File file = new File("test-files/conference3.txt");
        Conference conf = ConferenceReader.readConferenceFile(file);
        String[][] sessions = conf.getSessionsAsArray();
        
        assertEquals(0, sessions.length);
    }
}
