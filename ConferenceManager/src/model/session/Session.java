package edu.ncsu.csc216.wolf_proceedings.model.session;

import edu.ncsu.csc216.wolf_proceedings.model.proceedings.AcceptedItem;
import edu.ncsu.csc216.wolf_proceedings.model.util.ISwapList;
import edu.ncsu.csc216.wolf_proceedings.model.util.SwapList;

/**
 * Represents a session in the WolfProceedings system. A session contains
 * multiple accepted items and has a name and duration.
 * 
 * @author Vamsi Gaddipati
 */
public class Session implements Comparable<Session> {

    /** The name of the session */
    private String name;

    /** The duration of the session in minutes */
    private int duration;

    /** The minimum allowed duration for a session */
    private static final int MIN_DURATION = 5;

    /** The maximum allowed duration for a session */
    private static final int MAX_DURATION = 120;

    /** The list of accepted items in this session */
    private ISwapList<AcceptedItem> itemList;

    /**
     * Constructs a Session with the given name and duration.
     * 
     * @param name the name of the session
     * @param duration the duration of the session in minutes
     */
    public Session(String name, int duration) {
    	setName(name);
        setDuration(duration);
        itemList = new SwapList<>();
    }

    /**
     * Returns the name of the session.
     * 
     * @return the session name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the session.
     * 
     * @param name the new session name
     */
    public void setName(String name) {
    	if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid session.");
        }
        this.name = name;
    }

    /**
     * Returns the duration of the session.
     * 
     * @return the duration in minutes
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the session.
     * 
     * @param duration the new duration in minutes
     */
    public void setDuration(int duration) {
    	if (duration < MIN_DURATION || duration > MAX_DURATION) {
            throw new IllegalArgumentException("Invalid session.");
        }
        this.duration = duration;
    }

    /**
     * Returns the list of accepted items in this session.
     * 
     * @return the list of accepted items
     */
    public ISwapList<AcceptedItem> getItemList() {
        return itemList;
    }

    /**
     * Adds an accepted item to the session.
     * 
     * @param item the AcceptedItem to add
     */
    public void addAcceptedItem(AcceptedItem item) {
        if (item == null || getRemainingCapacity() < item.getDuration()) {
            throw new IllegalArgumentException("Cannot add item.");
        }
        try {
            item.addSession(this);
            itemList.add(item); 
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Cannot add item.");
        }
    }

    /**
     * Removes the accepted item at the specified index from the session.
     * 
     * @param idx the index of the AcceptedItem to remove
     */
    public void removeAcceptedItem(int idx) {
    	AcceptedItem item = itemList.get(idx);
        itemList.remove(idx);
        item.removeSession();
    }

    /**
     * Returns the remaining capacity (time) in the session.
     * 
     * @return the remaining duration in minutes
     */
    public int getRemainingCapacity() {
    	int used = 0;
        for (int i = 0; i < itemList.size(); i++) {
            used += itemList.get(i).getDuration();
        }
        return duration - used;
    }

    /**
     * Compares this session with another session, typically by name.
     * 
     * @param other the other Session to compare against
     * @return a negative integer, zero, or a positive integer as this session
     *         is less than, equal to, or greater than the other session
     */
    public int compareTo(Session other) {
        return this.name.compareToIgnoreCase(other.getName());
    }

    /**
     * Returns a string representation of the session.
     * 
     * @return a string describing the session
     */
    @Override
    public String toString() {
        String result = name + ";" + duration;
        for (AcceptedItem item : itemList) {
            result += "\n" + item.toString();
        }
        return result;
    }
}
