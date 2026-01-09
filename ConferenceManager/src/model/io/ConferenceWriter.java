package edu.ncsu.csc216.wolf_proceedings.model.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import edu.ncsu.csc216.wolf_proceedings.model.proceedings.AcceptedItem;
import edu.ncsu.csc216.wolf_proceedings.model.session.Session;
import edu.ncsu.csc216.wolf_proceedings.model.util.ISortedList;

/**
 * This class provides a method to write the conference name, sessions,
 * and accepted items to a specified file in a format suitable for reading
 * back with ConferenceReader.
 */
public class ConferenceWriter {

    /**
     * Writes the conference information to the specified file.
     * The file will contain the conference name, sessions, and accepted items.
     * 
     * @param conferenceFile the file to write the conference data to
     * @param conferenceName the name of the conference
     * @param sessions the sorted list of sessions in the conference
     * @param proceedings the sorted list of accepted items in the conference
     */
    public static void writeConferenceFile(File conferenceFile, String conferenceName, 
                                           ISortedList<Session> sessions, 
                                           ISortedList<AcceptedItem> proceedings) {
        try (PrintWriter writer = new PrintWriter(conferenceFile)) {
            // Write conference name
            writer.println(conferenceName);

            // Write sessions and their accepted items
            for (int i = 0; i < sessions.size(); i++) {
                Session session = sessions.get(i);
                writer.println("# " + session.getName() + "," + session.getDuration());

                for (AcceptedItem item : session.getItemList()) {
                    writer.println("* " + item.toString());
                }
            }

            // Write accepted items not assigned to any session
            writer.println("# +++");
            for (int i = 0; i < proceedings.size(); i++) {
                AcceptedItem item = proceedings.get(i);
                if (item.getSession() == null) {
                    writer.println("* " + item.toString());
                }
            }

        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Unable to save file.");
        }
    }
}
