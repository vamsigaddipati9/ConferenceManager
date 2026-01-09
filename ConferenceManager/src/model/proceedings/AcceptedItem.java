package edu.ncsu.csc216.wolf_proceedings.model.proceedings;

import edu.ncsu.csc216.wolf_proceedings.model.session.Session;

/**
 * Abstract base class representing an accepted item in the WolfProceedings system.
 * 
 * @author 
 */
public abstract class AcceptedItem implements Comparable<AcceptedItem> {

    /** The author(s) of the item. */
    private String authors;

    /** The title of the item. */
    private String title;

    /** The type of the item (e.g., "Paper", "Panel", "Lightning Talk"). */
    private String type;

    /** The current session that is selected */
    private Session selectedSession;
    
    /** The duration of the item in minutes. */ 
    private int duration;

    /** The minimum allowed duration of the item. */
    public static final int MIN_DURATION = 5;

    /** The maximum allowed duration of the item. */
    public static final int MAX_DURATION = 120;

    /**
     * Constructs an AcceptedItem with the given parameters.
     * 
     * @param type The type of the item (e.g., "Paper").
     * @param title The title of the item.
     * @param authors The authors of the item.
     * @param duration The duration of the item in minutes.
     */
    public AcceptedItem(String type, String authors, String title, int duration) {
    	setType(type);
        setAuthors(authors);
        setTitle(title);
        setDuration(duration);
        this.selectedSession = null;
    }

    /**
     * Retrieves the item's authors.
     * @return The authors of the item.
     */
    public String getAuthors() {
        return authors;
    }
    
    /**
     * Sets the list of authors for the item.
     * @param authors The new authors.
     */
    public void setAuthors(String authors) {
    	if (authors == null || authors.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid accepted item.");
        }
        this.authors = authors;
    }

    /**
     * Retrieves the item's title.
     * @return The title of the item.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title of the item.
     * @param title The new title.
     */
    public void setTitle(String title) {
    	if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid accepted item.");
        }
        this.title = title;
    }
    
    /**
     * Retrieves the item's type string.
     * @return The type string of the item.
     */
    public String getType() {
        return type;
    }
    
    /**
     * Sets the type of the item.
     * @param type The new type.
     */
    public void setType(String type) {
    	if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid accepted item.");
        }
        this.type = type;
    }

    /**
     * Retrieves the item's duration.
     * @return The duration of the item in minutes.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the item.
     * @param duration The new duration in minutes.
     */
    public void setDuration(int duration) {
    	if (duration < MIN_DURATION || duration > MAX_DURATION) {
            throw new IllegalArgumentException("Invalid accepted item.");
        }
        this.duration = duration;
    }

    /**
     * Adds a session to this accepted item.
     * @param session the session to add
     */
    public void addSession(Session session) {
        if (session == null || this.selectedSession != null) {
            throw new IllegalArgumentException("Cannot add item.");
        } 
        if (this.duration > session.getRemainingCapacity()) {
            throw new IllegalArgumentException("Cannot add item.");
        }
        this.selectedSession = session;
    }

    /**
     * Removes the session associated with this accepted item.
     */
    public void removeSession() {
        if (this.selectedSession != null) {
            this.selectedSession = null;
        }
    }

    /**
     * Returns the session associated with this accepted item.
     * @return the session associated with this accepted item, or null if none
     */
    public Session getSession() {
        return selectedSession;
    }
    
    /**
     * Compares this AcceptedItem with another AcceptedItem.
     * @param other The other item to compare against.
     * @return A negative integer, zero, or a positive integer as this object 
     *         is less than, equal to, or greater than the specified object.
     */
    @Override
    public int compareTo(AcceptedItem other) {
    	if (other == null) {
            throw new NullPointerException();
        }

        int authorCompare = this.authors.compareToIgnoreCase(other.authors);
        if (authorCompare != 0) {
            return authorCompare;
        }
        return this.title.compareToIgnoreCase(other.title);
    }
    
    /**
     * Returns a string representation of the item.
     * @return A string representation of the item.
     */
    @Override
    public String toString() {
        return type + "|" + authors + "|" + title;
    }
}
