package edu.ncsu.csc216.wolf_proceedings.model.conference;

import java.io.File;

import edu.ncsu.csc216.wolf_proceedings.model.io.ConferenceWriter;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.AcceptedItem;
import edu.ncsu.csc216.wolf_proceedings.model.session.Session;
import edu.ncsu.csc216.wolf_proceedings.model.util.ISortedList;
import edu.ncsu.csc216.wolf_proceedings.model.util.ISwapList;
import edu.ncsu.csc216.wolf_proceedings.model.util.SortedList;

/**
 * Represents a conference in the WolfProceedings system.
 * A conference contains sessions and accepted items (proceedings)
 * and provides operations to manage them.
 * 
 * @author Vamsi Gaddipati
 */
public class Conference {

    /** The name of the conference */
    private String conferenceName;

    /** Flag indicating if the conference has unsaved changes */
    private boolean isChanged;

    /** The sorted list of sessions in the conference */
    private ISortedList<Session> sessions;

    /** The sorted list of accepted items in the conference */
    private ISortedList<AcceptedItem> proceedings;

    /**
     * Constructs a Conference with the given name.
     * 
     * @param conferenceName the name of the conference
     */
    public Conference(String conferenceName) {
    	setConferenceName(conferenceName);
        sessions = new SortedList<Session>();
        proceedings = new SortedList<AcceptedItem>();
        isChanged = true;
    }

    /**
     * Saves the conference to the given file.
     * 
     * @param conferenceFile the file to save the conference data to
     */
    public void saveConference(File conferenceFile) {
        if (conferenceFile == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }
        
        // Use ConferenceWriter to write the file
        ConferenceWriter.writeConferenceFile(
            conferenceFile,         // File to write to
            this.getConferenceName(), // Conference name
            this.sessions,          // ISortedList of sessions
            this.proceedings        // ISortedList of accepted items
        );
        
        // Mark conference as saved
        this.isChanged = false;
    }

    /**
     * Returns the name of the conference.
     * 
     * @return the conference name
     */
    public String getConferenceName() {
    	return conferenceName;
    }

    /**
     * Sets the name of the conference.
     * 
     * @param conferenceName the new conference name
     */
    private void setConferenceName(String conferenceName) {
    	if (conferenceName == null || conferenceName.isEmpty()) {
            throw new IllegalArgumentException("Invalid name.");
        }
        this.conferenceName = conferenceName;
    }

    /**
     * Checks if the conference has unsaved changes.
     * 
     * @return true if there are unsaved changes, false otherwise
     */
    public boolean isChanged() {
        return isChanged;
    }

    /**
     * Sets the flag indicating whether the conference has unsaved changes.
     * 
     * @param changed true if the conference has unsaved changes, false otherwise
     */
    public void setChanged(boolean changed) {
        this.isChanged = changed;
    }

    /**
     * Adds a session to the conference.
     * 
     * @param toAdd the session to add
     * @return the index at which the session was added
     */
    public int addSession(Session toAdd) {
    	 if (toAdd == null) {
    	        throw new NullPointerException(); 
    	    }

    	 for (int i = 0; i < sessions.size(); i++) {
    	        if (sessions.get(i).getName().equals(toAdd.getName())) {
    	            throw new IllegalArgumentException("Cannot add item."); 
    	        }
    	    }
    	 
    	try {
            sessions.add(toAdd);
            isChanged = true;
            return sessions.indexOf(toAdd);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot add item.");
        }
    }

    /**
     * Edits an existing session in the conference.
     * 
     * @param idx    the index of the session to edit
     * @param toEdit the updated session
     * @return the index of the edited session
     */
    public int editSession(int idx, Session toEdit) {
    	if (idx < 0 || idx >= sessions.size()) {
            throw new IndexOutOfBoundsException();
        }
    	if (toEdit == null) {
            throw new NullPointerException();
        }
    	
    	for (int i = 0; i < sessions.size(); i++) {
            if (i == idx) continue; // skip the original session
            if (sessions.get(i).getName().equals(toEdit.getName())) {
                throw new IllegalArgumentException("Cannot add item."); // duplicate detected
            }
        }

        Session original = sessions.get(idx);
        sessions.remove(idx);
        try {
            sessions.add(toEdit);
            isChanged = true;
            return sessions.indexOf(toEdit);
        } catch (IllegalArgumentException e) {
            sessions.add(original); // restore original
            throw new IllegalArgumentException("Cannot add item.");
        }
    }

    /**
     * Removes the session at the specified index from the conference.
     * 
     * @param idx the index of the session to remove
     */
    public void removeSession(int idx) {
    	 if (idx < 0 || idx >= sessions.size()) {
             throw new IndexOutOfBoundsException();
         }
         sessions.remove(idx);
         isChanged = true;
    }

    /**
     * Retrieves the session at the specified index.
     * 
     * @param idx the index of the session to retrieve
     * @return the session at the specified index
     */
    public Session getSession(int idx) {
    	if (idx < 0 || idx >= sessions.size()) {
            throw new IndexOutOfBoundsException();
        }
        return sessions.get(idx);
    }

    /**
     * Adds an accepted item to the conference proceedings.
     * 
     * @param toAdd the accepted item to add
     */
    public void addAcceptedItem(AcceptedItem toAdd) {
    	if (toAdd == null) {
            throw new NullPointerException();
        }
    	
    	for (int i = 0; i < proceedings.size(); i++) {
    	    AcceptedItem a = proceedings.get(i);
    	    if (a.getTitle().equals(toAdd.getTitle()) && a.getAuthors().equals(toAdd.getAuthors())) {
    	        throw new IllegalArgumentException("Cannot add item.");
    	    }
    	}
    	
    	try {
            proceedings.add(toAdd);
            isChanged = true;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot add item.");
        }
    	
    }

    /**
     * Edits an existing accepted item in the conference proceedings.
     * 
     * @param idx    the index of the accepted item to edit
     * @param toEdit the updated accepted item
     */
    public void editAcceptedItem(int idx, AcceptedItem toEdit) {
    	if (idx < 0 || idx >= proceedings.size()) {
            throw new IndexOutOfBoundsException();
        }
    	if (toEdit == null) {
            throw new NullPointerException();
        }    	
    	
    	for (int i = 0; i < proceedings.size(); i++) {
            if (i == idx) continue; // skip the original item
            AcceptedItem a = proceedings.get(i);
            if (a.getTitle().equals(toEdit.getTitle()) && a.getAuthors().equals(toEdit.getAuthors())) {
                throw new IllegalArgumentException("Cannot add item."); // duplicate detected
            }
        }

        AcceptedItem original = proceedings.get(idx);
        proceedings.remove(idx);
             
        try {
            proceedings.add(toEdit);
            isChanged = true;
        } catch (IllegalArgumentException e) {
            proceedings.add(original); // restore original
            throw new IllegalArgumentException("Cannot add item.");
        }
    }

    /**
     * Removes the accepted item at the specified index from the conference proceedings.
     * 
     * @param idx the index of the accepted item to remove
     */
    public void removeAcceptedItem(int idx) {
    	if (idx < 0 || idx >= proceedings.size()) {
            throw new IndexOutOfBoundsException();
        }
        proceedings.remove(idx);
        isChanged = true;
    }
    
    /**
     * Adds an accepted item from the conference proceedings to a session.
     * 
     * @param sessionIdx the index of the session to add the item to
     * @param itemIdx the index of the accepted item in the proceedings to add
     */
    public void addItemToSession(int sessionIdx, int itemIdx) {
    	if (sessionIdx < 0 || sessionIdx >= sessions.size() ||
                itemIdx < 0 || itemIdx >= proceedings.size()) {
                throw new IndexOutOfBoundsException();
            }

            Session session = sessions.get(sessionIdx);
            AcceptedItem item = proceedings.get(itemIdx);

            try {
                session.addAcceptedItem(item);
                isChanged = true;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Cannot add item.");
            }
    }

    /**
     * Removes an accepted item from a session.
     * 
     * @param sessionIdx the index of the session to remove the item from
     * @param itemIdx the index of the accepted item within the session
     */
    public void removeItemFromSession(int sessionIdx, int itemIdx) {
    	if (sessionIdx < 0 || sessionIdx >= sessions.size()) {
            throw new IndexOutOfBoundsException();
        }

        Session session = sessions.get(sessionIdx);
        session.removeAcceptedItem((itemIdx));
        isChanged = true;    }

    /**
     * Returns all sessions in the conference as a 2D array of Strings.
     * 
     * @return a 2D array of Strings representing all sessions
     */
    public String[][] getSessionsAsArray() {
    	String[][] arr = new String[sessions.size()][3];
        for (int i = 0; i < sessions.size(); i++) {
            Session s = sessions.get(i);
            arr[i][0] = s.getName();
            arr[i][1] = String.valueOf(s.getDuration());
            arr[i][2] = String.valueOf(s.getRemainingCapacity());
        }
        return arr;
    }

    /**
     * Returns all accepted items within a specific session as a 2D array of Strings.
     * 
     * @param idx the index of the session in the conference
     * @return a 2D array of Strings representing all accepted items in the session
     */
    public String[][] getAcceptedItemsInSessionAsArray(int idx) {
    	if (idx < 0 || idx >= sessions.size()) {
            throw new IndexOutOfBoundsException();
        }
        Session s = sessions.get(idx);
        ISwapList<AcceptedItem> items = s.getItemList();
        String[][] arr = new String[items.size()][4];
        for (int i = 0; i < items.size(); i++) {
            AcceptedItem a = items.get(i);
            arr[i][0] = a.getType();
            arr[i][1] = a.getAuthors();
            arr[i][2] = a.getTitle();
            arr[i][3] = String.valueOf(a.getDuration());
        }
        return arr;
    }

    /**
     * Returns all accepted items in the conference (not assigned to sessions)
     * as a 2D array of Strings.
     * 
     * @return a 2D array of Strings representing all accepted items
     */
    public String[][] getAcceptedItemsAsArray() {
    	String[][] arr = new String[proceedings.size()][5];
        for (int i = 0; i < proceedings.size(); i++) {
            AcceptedItem a = proceedings.get(i);
            arr[i][0] = a.getType();
            arr[i][1] = a.getAuthors();
            arr[i][2] = a.getTitle();
            arr[i][3] = String.valueOf(a.getDuration());
            arr[i][4] = a.getSession() == null ? "" : a.getSession().getName();
        }
        return arr;    }    
}
