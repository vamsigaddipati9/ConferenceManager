package edu.ncsu.csc216.wolf_proceedings.model.io;

import java.io.File;
import java.util.Scanner;

import edu.ncsu.csc216.wolf_proceedings.model.conference.Conference;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.AcceptedItem;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.LightningTalk;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.Panel;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.Paper;
import edu.ncsu.csc216.wolf_proceedings.model.session.Session;

/**
 * Reads a conference file and converts it into a Conference object.
 * 
 * @author Vamsi Gaddipati
 */
public class ConferenceReader {

    /**
     * Reads a conference file and returns a Conference object.
     * 
     * @param conferenceFile the file to read
     * @return a Conference object with sessions and accepted items
     * @throws IllegalArgumentException if the file cannot be loaded
     */
    public static Conference readConferenceFile(File conferenceFile) {
        if (conferenceFile == null || !conferenceFile.exists()) {
            throw new IllegalArgumentException("Unable to load file.");
        }

        StringBuilder sb = new StringBuilder();
        try (Scanner scanner = new Scanner(conferenceFile)) {
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to load file.");
        }

        String[] lines = sb.toString().split("\n");
        if (lines.length < 1) {
            throw new IllegalArgumentException("Invalid file format.");
        }

        // First line: conference name
        Conference conf = new Conference(lines[0].trim());

        Session currentSession = null;
        boolean unassignedSection = false;

        for (int i = 1; i < lines.length; i++) {        	
            String line = lines[i].trim();
            if (line.isEmpty()) continue;

            if ("# +++".equals(line)) {
                unassignedSection = true;
                currentSession = null;
            } else if (line.startsWith("#")) {
            	currentSession = null;
                unassignedSection = false;
                currentSession = parseSession(line.substring(1).trim(), conf);
            } else if (line.startsWith("*")) {
                parseAcceptedItem(line.substring(1).trim(), currentSession, conf, unassignedSection);
            }
        }

        conf.setChanged(false);
        return conf;
    }

    /** Parses a session line and adds it to the conference 
     * 
     * @param line The line that is the session
     * @param conf The confererence the session will be added to
     * @return return the session that was created
     * */
    private static Session parseSession(String line, Conference conf) {
        String[] parts = line.split(",");
        if (parts.length != 2) return null;
        if (parts[0].isEmpty() || parts[1].isEmpty()) return null;

        String name = parts[0].trim();
        int duration;
        
        try {
            duration = Integer.parseInt(parts[1].trim());
            if (duration < 5 || duration > 120) return null;
        } catch (NumberFormatException e) {
            return null;
        }

        Session s = new Session(name, duration);
        try {
            conf.addSession(s);
        } catch (IllegalArgumentException e) {
            return null; // duplicate session name
        }
        return s;
    }

    /** Parses an accepted item line and adds it to the conference and session if assigned 
     * 
     * @param line The line in the file being checked
     * @param session The session the line is in
     * @param conf The conference the session and line are in
     * @param unassigned Decides whether item is assigned to a session
     */
    private static void parseAcceptedItem(String line, Session session, Conference conf, boolean unassigned) {
        String[] parts = line.split("\\|");
        if (parts.length < 3) return;

        String type = parts[0].trim().toLowerCase();
        String authors = parts[1].trim();
        String title = parts[2].trim();

        AcceptedItem item;

        try {
            switch (type) {
                case "paper":
                    if (parts.length > 4) return; // too many fields
                    int duration = 15;
                    if (parts.length == 4) {
                        try {
                            duration = Integer.parseInt(parts[3].trim());
                        } catch (NumberFormatException e) {
                            return; // invalid duration
                        }
                    }
                    item = new Paper(authors, title, duration);
                    break;

                case "panel":
                    if (parts.length != 3) return;
                    item = new Panel(authors, title);
                    break;

                case "lightningtalk":
                case "lightning talk":
                    if (parts.length != 3) return;
                    item = new LightningTalk(authors, title);
                    break;

                default:
                    return; // unknown type
            }
            
            if(unassigned && session == null) {
                conf.addAcceptedItem(item);
            }

            if (!unassigned && session != null) {
                conf.addAcceptedItem(item);
                session.addAcceptedItem(item);
            }
            
        } catch (IllegalArgumentException e) {
        }
    }
}
