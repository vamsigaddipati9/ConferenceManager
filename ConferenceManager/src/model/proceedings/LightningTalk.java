package edu.ncsu.csc216.wolf_proceedings.model.proceedings;

/**
 * Represents a lightning talk in the conference proceedings. A lightning talk is a type of
 * AcceptedItem.
 * 
 * @author Vamsi Gaddipati
 */
public class LightningTalk extends AcceptedItem {

    /** The default set length of a lightning talk */
    private static final int PRESENTATION_LENGTH = 5;
    
    /** The default set type of a lightning talk*/
    private static final String ITEM_TYPE = "LightningTalk";

    /**
     * Constructs a lightning talk with the given title and author.
     * @param authors the authors of the lightning talk
     * @param title the title or name of the lightning talk
     */
    public LightningTalk(String authors, String title) {
    	super(ITEM_TYPE, authors, title, PRESENTATION_LENGTH);
    }
}
