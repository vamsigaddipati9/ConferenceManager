package edu.ncsu.csc216.wolf_proceedings.model.proceedings;

/**
 * Represents a panel in the conference proceedings. A panel is a type of
 * AcceptedItem.
 * 
 * @author Vamsi Gaddipati
 */
public class Panel extends AcceptedItem {

    /** The default set length of a panel*/
    private static final int PRESENTATION_LENGTH = 75;
    
    /** The default set type of a panel*/
    private static final String ITEM_TYPE = "Panel";

    /**
     * Constructs a Panel with the given title and author.
     * @param authors the authors of the panel
     * @param title the title or name of the panel
     */
    public Panel(String authors, String title) {
    	super(ITEM_TYPE, authors, title, PRESENTATION_LENGTH);
    }
}
