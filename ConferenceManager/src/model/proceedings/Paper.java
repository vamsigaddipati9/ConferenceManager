package edu.ncsu.csc216.wolf_proceedings.model.proceedings;

/**
 * Represents a paper in the conference proceedings. A paper is a type of
 * AcceptedItem.
 * 
 * @author Vamsi Gaddipati
 */
public class Paper extends AcceptedItem {

    /** The default set length of a paper*/
    private static final int PAPER_DURATION = 15;
    
    /** The default set type of a paper*/
    private static final String ITEM_TYPE = "Paper";
    
    /** True or false depening on if paper has custom duration*/
    private boolean customDuration = false;

    /**
     * Constructs a Paper with the given title and author.
     * @param authors the authors of the paper
     * @param title the title or name of the paper
     */
    public Paper(String authors, String title) {
    	super(ITEM_TYPE, authors, title, PAPER_DURATION);
    	this.customDuration = false;
    }
    
    /**
     * Constructs a Paper with the given title, author, and duration.
     * @param authors the authors of the paper 
     * @param title the title or name of the paper
     * @param duration the duration of the paper
     */
    public Paper(String authors, String title, int duration) {
    	super(ITEM_TYPE, authors, title, duration);
    	this.customDuration = true;
    }
    
    /**
     * Returns a string representation of the item.
     * @return A string representation of the item.
     */
    @Override
    public String toString() {
    	if(this.customDuration) {
    		return ITEM_TYPE + "|" + getAuthors() + "|" + getTitle() + "|" + getDuration(); 
    	}
    		return ITEM_TYPE + "|" + getAuthors() + "|" + getTitle(); 
    }
}
