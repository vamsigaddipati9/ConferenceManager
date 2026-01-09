package edu.ncsu.csc216.wolf_proceedings.view.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.wolf_proceedings.model.conference.Conference;
import edu.ncsu.csc216.wolf_proceedings.model.io.ConferenceReader;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.AcceptedItem;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.LightningTalk;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.Panel;
import edu.ncsu.csc216.wolf_proceedings.model.proceedings.Paper;
import edu.ncsu.csc216.wolf_proceedings.model.session.Session;


/**
 * Container for the Conference Proceedings project that provides the user the ability
 * to interact with sessions and accepted items
 * 
 * @author Dr. Sarah Heckman
 */
public class WolfProceedingsGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "WolfProceedings";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the New menu item. */
	private static final String NEW_TITLE = "New Conference";
	/** Text for the Load menu item. */
	private static final String LOAD_TITLE = "Load Conference";
	/** Text for the Save menu item. */
	private static final String SAVE_TITLE = "Save Conference";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Value for Paper */
    private static final String PAPER = "Paper";
    /** Value for Panel */
    private static final String PANEL = "Panel";
    /** Value for Lightning Talk */
    private static final String LIGHTNING_TALK = "LightningTalk";
	
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for creating a new conference. */
	private JMenuItem itemNew;
	/** Menu item for loading a conference file. */
	private JMenuItem itemLoad;
	/** Menu item for saving a conference to a file. */
	private JMenuItem itemSave;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;

	/** JPanel for sessions */
	private SessionsPanel pnlSession;
	
	/** JPanel for AcceptedItems */
	private AcceptedItemsPanel pnlAcceptedItems;
	
	/** Current conference - null if no conference created. */
	private Conference conference;
	
	/**
	 * Constructs a WolfProceedingsGUI object that will contain a JMenuBar and a
	 * JPanel that will hold different possible views of the data in
	 * the WolfProceedings.
	 */
	public WolfProceedingsGUI() {
		super();
		
		//Set up general GUI info
		setSize(1200, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Add panel to the container
		Container c = getContentPane();
		c.setLayout(new GridBagLayout());
		
		pnlSession = new SessionsPanel();
		Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder sessionBorder = BorderFactory.createTitledBorder(lowerEtched, "Sessions");
		pnlSession.setBorder(sessionBorder);
		pnlSession.setToolTipText("Sessions");
		
		pnlAcceptedItems = new AcceptedItemsPanel();
		lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Accepted Items");
		pnlAcceptedItems.setBorder(border);
		pnlAcceptedItems.setToolTipText("Accepted Items");
		
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = .5;
		constraints.weighty = 1;
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		c.add(pnlSession, constraints);
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		c.add(pnlAcceptedItems, constraints);
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options working with a file
	 * containing service conferences and incidents or for quitting the application.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemNew = new JMenuItem(NEW_TITLE);
		itemLoad = new JMenuItem(LOAD_TITLE);
		itemSave = new JMenuItem(SAVE_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemNew.addActionListener(this);
		itemLoad.addActionListener(this);
		itemSave.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSave.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemNew);
		menu.add(itemLoad);
		menu.add(itemSave);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}

	/**
	 * Performs actions depending on which button is clicked.
	 * @param e action event encapsulating the action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == itemNew) {
			if (conference != null && conference.isChanged()) {
				int select = JOptionPane.showConfirmDialog(null, "Current Conference is unsaved. Would you like to save before creating a new Conference?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (select == 1) {
					promptForConferenceName();
				} 
			} else {
				promptForConferenceName();
			}
			pnlSession.updateConference();
		} else if (e.getSource() == itemLoad) {
			try {
				if (conference != null && conference.isChanged()) {
					int select = JOptionPane.showConfirmDialog(null, "Current Conference is unsaved. Would you like to save before creating a new Conference?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (select == 0) {
						conference.saveConference(new File(getFileName(false)));
					}
				} 
				conference = ConferenceReader.readConferenceFile(new File(getFileName(true)));
				pnlSession.updateSessions();
				pnlSession.updateAcceptedItemsInSession(0);
				pnlAcceptedItems.updateAcceptedItems();
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(this, iae.getMessage());
			} catch (IllegalStateException ise) {
				//ignore the exception
			}
		} else if (e.getSource() == itemSave) { 
			try {
				conference.saveConference(new File(getFileName(false)));
			} catch (IllegalArgumentException iae) {
				JOptionPane.showMessageDialog(this, iae.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			if (conference != null && conference.isChanged()) {
				try {
					conference.saveConference(new File(getFileName(false)));
					System.exit(0);  //Ignore SpotBugs warning here - this is the only place to quit the program!
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(this, iae.getMessage());
				} catch (IllegalStateException exp) {
					//Don't do anything - user canceled (or error)
					System.exit(0);
				}
			} else {
				System.exit(0);
			}
		}
		
		itemSave.setEnabled(conference != null && conference.isChanged());
		pnlSession.enableSessionButtons();
		pnlSession.enableSessionMovementButtons(false);
		pnlAcceptedItems.updateAcceptedItems();
		repaint();
		validate();
		
	}
	
	/**
	 * Prompts the user for the conference name.
	 */
	private void promptForConferenceName() {
		String conferenceName = (String) JOptionPane.showInputDialog(this, "Conference Name?");
		if (conferenceName == null) {
			return; //no need to do anything
		}
		conference = new Conference(conferenceName);
	}
	
	/**
	 * Returns a file name generated through interactions with a JFileChooser
	 * object.
	 * @param load true if loading a file, false if saving
	 * @return the file name selected through JFileChooser
	 * @throws IllegalStateException if no file name provided
	 */
	private String getFileName(boolean load) {
		//Open JFileChooser to current working directory
		JFileChooser fc = new JFileChooser("./");  
		int returnVal = Integer.MIN_VALUE;
		if (load) {
			returnVal = fc.showOpenDialog(this);
		} else {
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File gameFile = fc.getSelectedFile();
		return gameFile.getAbsolutePath();
	}
	
	/**
	 * Prompts the user for session information
	 * @param parentComponent the component over which the dialog is displayed
	 * @param isAdd true if adding, false if editing
	 * @return Session from information
	 */
	public Session addEditSessionDialog(Component parentComponent, boolean isAdd) {
		JTextField sessionName = new JTextField(30);
		JTextField duration = new JTextField(30);
		
		JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
		
		panel.add(new JLabel("Session Name: "));
		panel.add(sessionName);
		
		panel.add(new JLabel("Session Duration: "));
		panel.add(duration);
		
		String title = "";
		if (isAdd) {
			title = "Add Session";
		} else {
			title = "Edit Session";
		}
		
		int result = JOptionPane.showConfirmDialog(parentComponent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (result == JOptionPane.OK_OPTION) {
			try {
				int dur = Integer.parseInt(duration.getText().trim());
				return new Session(sessionName.getText().trim(), dur);
			} catch (NumberFormatException e) {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Prompts the user for accepted item information
	 * @param parentComponent the component over which the dialog is displayed
	 * @param isAdd true if adding, false if editing
	 * @return accepted item from information
	 */
	public AcceptedItem addEditAcceptedItemDialog(Component parentComponent, boolean isAdd) {
		JTextField itemAuthors = new JTextField(30);
		JTextField itemTitle = new JTextField(30);
		JTextField duration = new JTextField(30);
		
		JPanel panel = new JPanel(new GridLayout(3, 3, 5, 5));
		
		panel.add(new JLabel("Authors: "));
		panel.add(itemAuthors);
		
		panel.add(new JLabel("Title: "));
		panel.add(itemTitle);
		
		panel.add(new JLabel("Duration: "));
		panel.add(duration);
		
		String title = "";
		if (isAdd) {
			title = "Add Accepted Item";
		} else {
			title = "Edit Accepted Item";
		}
		
		int result = JOptionPane.showConfirmDialog(parentComponent, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		
		if (result == JOptionPane.OK_OPTION) {
//			try {
//				int dur = Integer.parseInt(duration.getText().trim());
//				return new Session(sessionName.getText().trim(), dur);
//			} catch (NumberFormatException e) {
//				return null;
//			}
		} else {
			return null;
		}
		return null;
	}
	
	/**
	 * Starts the GUI for the WolfTickets application.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new WolfProceedingsGUI();
	}
	
	/**
	 * JPanel for Sessions
	 */
	private class SessionsPanel extends JPanel implements ActionListener {
		
		/** ID number use for object serialization */
		private static final long serialVersionUID = 1L;
		
		/** Label for conference name */
		private JLabel lblConferenceName;
		
		/** JTable for displaying list of sessions */
		private JTable tableSessions;
		/** TableModel for list of session */
		private SessionsTableModel sessionsTableModel;
		
		/** Button to add a session */
		private JButton btnAddSession;
		/** Button to edit the selected session */
		private JButton btnEditSession;
		/** Button to remove the selected session */
		private JButton btnRemoveSession;
		
		/** Label for Session name */
		private JLabel lblSessionName;
		/** Label for session duration */
		private JLabel lblSessionDuration;
		/** Label for time remaining in session */
		private JLabel lblSessionAvailability;
		
		/** JTable for displaying the list of accepted item in the selected session */
		private JTable tableItemsInSession;
		/** Table model for accepted items in the selected session */
		private ItemsInSessionTableModel itemsInSessionsTableModel;
		
		/** Button - move up */
		private JButton btnMoveUp;
		/** Button - move down */
		private JButton btnMoveDown;
		/** Button - move to front */
		private JButton btnMoveToFront;
		/** Button - move to back */
		private JButton btnMoveToBack;
		
		/**
		 * Creates the session list and accepted items per session along with associated
		 * controls.
		 */
		public SessionsPanel() {
			super(new GridBagLayout());
			
			lblConferenceName = new JLabel("Conference Name: ");
			lblSessionName = new JLabel("Session Name: ");
			lblSessionDuration = new JLabel("Duration (min): ");
			lblSessionAvailability = new JLabel("Availability (min): ");
			
			btnAddSession = new JButton("Add Session");
			btnEditSession = new JButton("Edit Session");
			btnRemoveSession = new JButton("Remove Session");
			
			btnAddSession.addActionListener(this);
			btnEditSession.addActionListener(this);
			btnRemoveSession.addActionListener(this);
			
			btnMoveUp = new JButton("Move Up");
			btnMoveDown = new JButton("Move Down");
			btnMoveToFront = new JButton("Move to Front");
			btnMoveToBack = new JButton("Move to Back");
		
			btnMoveUp.addActionListener(this);
			btnMoveDown.addActionListener(this);
			btnMoveToFront.addActionListener(this);
			btnMoveToBack.addActionListener(this);
			
			sessionsTableModel = new SessionsTableModel();
			tableSessions = new JTable(sessionsTableModel);
			tableSessions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableSessions.setPreferredScrollableViewportSize(new Dimension(500, 500));
			tableSessions.setFillsViewportHeight(true);
			tableSessions.getColumnModel().getColumn(0).setPreferredWidth(250);
			tableSessions.getColumnModel().getColumn(1).setPreferredWidth(30);
			tableSessions.getColumnModel().getColumn(2).setPreferredWidth(30);
			
			tableSessions.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				/**
				 * When the list selection is changed, the accepted item for the session 
				 * are updated.
				 * @param e selection event
				 */
				@Override
				public void valueChanged(ListSelectionEvent e) {
					int idx = tableSessions.getSelectedRow();
					updateAcceptedItemsInSession(idx);
				}
				
			});
			
			JScrollPane sessionsListScrollPane = new JScrollPane(tableSessions);
			
			itemsInSessionsTableModel = new ItemsInSessionTableModel();
			tableItemsInSession = new JTable(itemsInSessionsTableModel);
			tableItemsInSession.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableItemsInSession.setPreferredScrollableViewportSize(new Dimension(500, 500));
			tableItemsInSession.setFillsViewportHeight(true);			
			tableItemsInSession.getColumnModel().getColumn(0).setPreferredWidth(25);
			tableItemsInSession.getColumnModel().getColumn(1).setPreferredWidth(100);
			tableItemsInSession.getColumnModel().getColumn(2).setPreferredWidth(100);
			tableItemsInSession.getColumnModel().getColumn(3).setPreferredWidth(5);
						
			JScrollPane itemsInSessionsListScrollPane = new JScrollPane(tableItemsInSession);
			
			JPanel pnlSessionButtons = new JPanel();
			pnlSessionButtons.setLayout(new GridLayout(1, 3));
			pnlSessionButtons.add(btnAddSession);
			pnlSessionButtons.add(btnEditSession);
			pnlSessionButtons.add(btnRemoveSession);
			
			JPanel pnlAcceptedItemsInSession = new JPanel();
			pnlAcceptedItemsInSession.setLayout(new GridLayout(2, 2));
			pnlAcceptedItemsInSession.add(btnMoveUp);
			pnlAcceptedItemsInSession.add(btnMoveDown);
			pnlAcceptedItemsInSession.add(btnMoveToFront);
			pnlAcceptedItemsInSession.add(btnMoveToBack);
			
			JPanel pnlSessionDuration = new JPanel();
			pnlSessionDuration.setLayout(new GridLayout(1, 2));
			pnlSessionDuration.add(lblSessionDuration);
			pnlSessionDuration.add(lblSessionAvailability);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblConferenceName, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 30;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(sessionsListScrollPane, c);
			
			c.gridx = 0;
			c.gridy = 31;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlSessionButtons, c);
			
			c.gridx = 0;
			c.gridy = 34;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(lblSessionName, c);
			
			c.gridx = 0;
			c.gridy = 35;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlSessionDuration, c);
			
			c.gridx = 0;
			c.gridy = 36;
			c.weightx = 1;
			c.weighty = 30;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(itemsInSessionsListScrollPane, c);
			
			c.gridx = 0;
			c.gridy = 66;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlAcceptedItemsInSession, c);
			
			enableSessionButtons();
			enableSessionMovementButtons(false);
		}
		
		/**
		 * Returns the index of the selected session.
		 * @return the index of the selected session
		 */
		public int getSelectedSessionIdx() {
			return tableSessions.getSelectedRow();
		}
		
		/**
		 * Returns the index of the selected item in the selected session.
		 * @return the index of the selected item in the selected session
		 */
		public int getSelectedItemInSessionIdx() {
			return tableItemsInSession.getSelectedRow();
		}
		
		/**
		 * Update conference information
		 */
		public void updateConference() {
			if (conference == null) {
				lblConferenceName.setText("Conference Name: ");
			} else {
				lblConferenceName.setText("Conference Name: " + conference.getConferenceName());
			}
		}
		
		/**
		 * Update session information.
		 */
		public void updateSessions() {
			if (conference == null) {
				btnAddSession.setEnabled(false);
				btnEditSession.setEnabled(false);
				btnRemoveSession.setEnabled(false);
			} else {
				btnAddSession.setEnabled(true);
				btnEditSession.setEnabled(true);
				btnRemoveSession.setEnabled(true);
				
				itemSave.setEnabled(conference != null && conference.isChanged());
				sessionsTableModel.updateData();
				tableSessions.getColumnModel().getColumn(0).setPreferredWidth(250);
				tableSessions.getColumnModel().getColumn(1).setPreferredWidth(30);
				tableSessions.getColumnModel().getColumn(2).setPreferredWidth(30);
				
				lblConferenceName.setText("Conference Name: " + conference.getConferenceName());
			}
		}
		
		/**
		 * Update accepted items in session information.
		 * @param idx index of selected session
		 */
		public void updateAcceptedItemsInSession(int idx) {
			if (conference == null || idx < 0) {
				btnMoveUp.setEnabled(false);
				btnMoveDown.setEnabled(false);
				btnMoveToFront.setEnabled(false);
				btnMoveToBack.setEnabled(false);
				
				lblSessionName.setText("Session Name: ");
				lblSessionDuration.setText("Duration (min): ");
				lblSessionAvailability.setText("Availability (min): ");
			} else {
				btnMoveUp.setEnabled(true);
				btnMoveDown.setEnabled(true);
				btnMoveToFront.setEnabled(true);
				btnMoveToBack.setEnabled(true);
				
				tableSessions.setRowSelectionInterval(idx, idx);
				
				itemsInSessionsTableModel.updateData(idx);
				tableItemsInSession.getColumnModel().getColumn(0).setPreferredWidth(25);
				tableItemsInSession.getColumnModel().getColumn(1).setPreferredWidth(100);
				tableItemsInSession.getColumnModel().getColumn(2).setPreferredWidth(100);
				tableItemsInSession.getColumnModel().getColumn(3).setPreferredWidth(5);
				
				lblSessionName.setText("Session Name: " + conference.getSessionsAsArray()[idx][0]);
				lblSessionDuration.setText("Duration (min): " + conference.getSessionsAsArray()[idx][1]);
				lblSessionAvailability.setText("Availability (min): " + conference.getSessionsAsArray()[idx][2]);
			}
			
		}
		
		/**
		 * Enable or disable buttons to add/edit/remove a session.
		 */
		public void enableSessionButtons() {
			boolean enable = conference != null;
			
			btnAddSession.setEnabled(enable);
			btnEditSession.setEnabled(enable);
			btnRemoveSession.setEnabled(enable);
		}
		
		/**
		 * Enable or disable session buttons 
		 * @param enable true if enable, false if disabled
		 */
		public void enableSessionMovementButtons(boolean enable) {
			boolean whatToDo = enable;
			if (conference == null) {
				whatToDo = false;
			}
			
			btnMoveUp.setEnabled(whatToDo);
			btnMoveDown.setEnabled(whatToDo);
			btnMoveToFront.setEnabled(whatToDo);
			btnMoveToBack.setEnabled(whatToDo);	
		}

		/**
		 * Performs actions depending on which button is clicked.
		 * @param e action event encapsulating the action
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			int idxActiveSession = -1;
			
			if (e.getSource() == btnAddSession) {
				idxActiveSession = createEditSession(true, idxActiveSession);
			} else if (e.getSource() == btnEditSession) {
				int sessionRowIdx = tableSessions.getSelectedRow();
				
				idxActiveSession = createEditSession(false, sessionRowIdx);
			} else if (e.getSource() == btnRemoveSession) {
				int sessionRowIdx = tableSessions.getSelectedRow();
				
				if (sessionRowIdx < 0) {
					JOptionPane.showMessageDialog(SessionsPanel.this, 
							"No session selected.", 
							"No Session Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				conference.removeSession(sessionRowIdx);
				idxActiveSession = -1;
			} else if (e.getSource() == btnMoveUp) {
				idxActiveSession = tableSessions.getSelectedRow();
				int itemInSessionRowIdx = tableItemsInSession.getSelectedRow();
				
				if (itemInSessionRowIdx < 0) {
					JOptionPane.showMessageDialog(SessionsPanel.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				try {
					conference.getSession(idxActiveSession).getItemList().moveUp(itemInSessionRowIdx);
				} catch (IllegalArgumentException | IndexOutOfBoundsException iae) {
					JOptionPane.showMessageDialog(SessionsPanel.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
				}
			} else if (e.getSource() == btnMoveDown) {
				idxActiveSession = tableSessions.getSelectedRow();
				int itemInSessionRowIdx = tableItemsInSession.getSelectedRow();
				
				if (itemInSessionRowIdx < 0) {
					JOptionPane.showMessageDialog(SessionsPanel.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				try {
					conference.getSession(idxActiveSession).getItemList().moveDown(itemInSessionRowIdx);
				} catch (IllegalArgumentException | IndexOutOfBoundsException iae) {
					JOptionPane.showMessageDialog(SessionsPanel.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
				} 
			} else if (e.getSource() == btnMoveToFront) {
				idxActiveSession = tableSessions.getSelectedRow();
				int itemInSessionRowIdx = tableItemsInSession.getSelectedRow();
				
				if (itemInSessionRowIdx < 0) {
					JOptionPane.showMessageDialog(SessionsPanel.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				try {
					conference.getSession(idxActiveSession).getItemList().moveToFront(itemInSessionRowIdx);
				} catch (IllegalArgumentException | IndexOutOfBoundsException iae) {
					JOptionPane.showMessageDialog(SessionsPanel.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
				}
			} else if (e.getSource() == btnMoveToBack) {
				idxActiveSession = tableSessions.getSelectedRow();
				int itemInSessionRowIdx = tableItemsInSession.getSelectedRow();
				
				if (itemInSessionRowIdx < 0) {
					JOptionPane.showMessageDialog(SessionsPanel.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				try {
					conference.getSession(idxActiveSession).getItemList().moveToBack(itemInSessionRowIdx);
				} catch (IllegalArgumentException | IndexOutOfBoundsException iae) {
					JOptionPane.showMessageDialog(SessionsPanel.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
				}
			}
			
			updateSessions();
			updateAcceptedItemsInSession(idxActiveSession);
						
			WolfProceedingsGUI.this.repaint();
			WolfProceedingsGUI.this.validate();
		}
		
		/**
		 * Creates or edits a session.
		 * @param toAdd true if adding, false if editing
		 * @param selectedIdx selected index to edit
		 * @return index of added/edited session
		 */
		private int createEditSession(boolean toAdd, int selectedIdx) {
			SessionInputDialog dialog = null;
			if (toAdd) {
				dialog = new SessionInputDialog(WolfProceedingsGUI.this);
			} else {
				if (selectedIdx < 0) {
					JOptionPane.showMessageDialog(SessionsPanel.this, 
							"No session selected.", 
							"No Session Selected", 
							JOptionPane.WARNING_MESSAGE);
					return -1;
				}
				
				String sessionNameToEdit = sessionsTableModel.getValueAt(selectedIdx, 0).toString();
				String durationToEdit = sessionsTableModel.getValueAt(selectedIdx, 1).toString();
				
				dialog = new SessionInputDialog(WolfProceedingsGUI.this, sessionNameToEdit, durationToEdit);
			}
			dialog.setVisible(true);
			
			int idxActiveSession = -1;
			if (dialog.isConfirmed()) {
				String sessionName = dialog.getSessionName();
				int duration = dialog.getSessionDuration();
				
				try {
					if (toAdd) {
						idxActiveSession = conference.addSession(new Session(sessionName, duration));
						JOptionPane.showMessageDialog(WolfProceedingsGUI.this, "Session created.", "Success", JOptionPane.INFORMATION_MESSAGE);
					} else {
						idxActiveSession = conference.editSession(selectedIdx, new Session(sessionName, duration));
						JOptionPane.showMessageDialog(WolfProceedingsGUI.this, "Session edited.", "Success", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfProceedingsGUI.this, iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			
			return idxActiveSession;
		}
		
		/**
		 * SessionsTableModel is the object underlying the JTable object that displays
		 * the list of Sessions to the user.
		 */
		private class SessionsTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Session Name", "Duration (min)", "Available Time (min)"};
			/** Data stored in the table */
			private Object [][] data;
			
			
			/**
			 * Constructs the SessionsTableModel by requesting the latest information
			 * from the SessionsTableModel.
			 */
			public SessionsTableModel() {				
				updateData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			@Override
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			@Override
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @param col the column index
			 * @return the column name at the given column.
			 */
			@Override
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @param row the row index
			 * @param col the column index
			 * @return the data at the given location.
			 */
			@Override
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row the row index
			 * @param col the column index
			 */
			@Override
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with Session information
			 */
			private void updateData() {
				if (conference != null) {
					data = conference.getSessionsAsArray();
					fireTableStructureChanged();
				}
			}
		}
		
		/**
		 * ItemsInSessionTableModel is the object underlying the JTable object that displays
		 * the list of accepted items in the session to the user.
		 */
		private class ItemsInSessionTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Type", "Author(s)", "Title", "Duration (min)"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the ItemsInSessionTableModel by requesting the latest information
			 * from the ItemsInSessionTableModel.
			 */
			public ItemsInSessionTableModel() {
				updateData(-1);
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			@Override
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			@Override
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @param col the column index
			 * @return the column name at the given column.
			 */
			@Override
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @param row the row index
			 * @param col the column index
			 * @return the data at the given location.
			 */
			@Override
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row the row index
			 * @param col the column index
			 */
			@Override
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with Session information
			 * @param idx of which session to use
			 */
			private void updateData(int idx) {
				if (conference != null && idx > -1) {
					data = conference.getAcceptedItemsInSessionAsArray(idx);
					fireTableStructureChanged();
				}
			}
		}
	}
	
	/**
	 * Panel for listing accepted items and buttons to work with accepted items.
	 */
	private class AcceptedItemsPanel extends JPanel implements ActionListener {
		
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		
		/** JTable for displaying the list of accepted items */
		private JTable tableAcceptedItems;
		/** TableModel for accepted items */
		private AcceptedItemsTableModel tableModel;
		
		/** Button to add an accepted item */
		private JButton btnAddAcceptedItem;
		/** Button to edit an accepted item */
		private JButton btnEditAcceptedItem;
		/** Button to remove an accepted item */
		private JButton btnRemoveAcceptedItem;
		
		/** Button to add item to session */
		private JButton btnAddAcceptedItemToSession;
		/** Button to remove item from session */
		private JButton btnRemoveAcceptedItemFromSession;
		
		/**
		 * Constructs the AcceptedItemsPanel
		 */
		public AcceptedItemsPanel() {
			super(new GridBagLayout());
			
			btnAddAcceptedItem = new JButton("Add Item");
			btnEditAcceptedItem = new JButton("Edit Item");
			btnRemoveAcceptedItem = new JButton("Remove Item");
			
			btnAddAcceptedItem.addActionListener(this);
			btnEditAcceptedItem.addActionListener(this);
			btnRemoveAcceptedItem.addActionListener(this);
			
			btnAddAcceptedItemToSession = new JButton("< Add Item to Session");
			btnRemoveAcceptedItemFromSession = new JButton("> Remove Item from Session");
			
			btnAddAcceptedItemToSession.addActionListener(this);
			btnRemoveAcceptedItemFromSession.addActionListener(this);
			
			tableModel = new AcceptedItemsTableModel();
			tableAcceptedItems = new JTable(tableModel);
			tableAcceptedItems.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableAcceptedItems.setPreferredScrollableViewportSize(new Dimension(500, 500));
			tableAcceptedItems.setFillsViewportHeight(true);
			tableAcceptedItems.getColumnModel().getColumn(0).setPreferredWidth(25);
			tableAcceptedItems.getColumnModel().getColumn(1).setPreferredWidth(100);
			tableAcceptedItems.getColumnModel().getColumn(2).setPreferredWidth(100);
			tableAcceptedItems.getColumnModel().getColumn(3).setPreferredWidth(5);
			tableAcceptedItems.getColumnModel().getColumn(4).setPreferredWidth(100);
			
			JScrollPane listScrollPane = new JScrollPane(tableAcceptedItems);
			
			JPanel pnlAcceptedItemActions = new JPanel();
			pnlAcceptedItemActions.setLayout(new GridLayout(1, 3));
			pnlAcceptedItemActions.add(btnAddAcceptedItem);
			pnlAcceptedItemActions.add(btnEditAcceptedItem);
			pnlAcceptedItemActions.add(btnRemoveAcceptedItem);
			
			JPanel pnlAcceptedItemsAndSessions = new JPanel();
			pnlAcceptedItemsAndSessions.setLayout(new GridLayout(1, 2));
			pnlAcceptedItemsAndSessions.add(btnAddAcceptedItemToSession);
			pnlAcceptedItemsAndSessions.add(btnRemoveAcceptedItemFromSession);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 50;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(listScrollPane, c);
			
			c.gridx = 0;
			c.gridy = 51;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlAcceptedItemActions, c);
			
			c.gridx = 0;
			c.gridy = 52;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.HORIZONTAL;
			add(pnlAcceptedItemsAndSessions, c);
			
			updateAcceptedItems();
		}
		
		/**
		 * Updated the accepted items table and buttons.
		 */
		public void updateAcceptedItems() {
			if (conference == null) {
				btnAddAcceptedItem.setEnabled(false);
				btnEditAcceptedItem.setEnabled(false);
				btnRemoveAcceptedItem.setEnabled(false);
				btnAddAcceptedItemToSession.setEnabled(false);
				btnRemoveAcceptedItemFromSession.setEnabled(false);
			} else {
				btnAddAcceptedItem.setEnabled(true);
				btnEditAcceptedItem.setEnabled(true);
				btnRemoveAcceptedItem.setEnabled(true);
				btnAddAcceptedItemToSession.setEnabled(true);
				btnRemoveAcceptedItemFromSession.setEnabled(true);
				
				tableModel.updateData();
				tableAcceptedItems.getColumnModel().getColumn(0).setPreferredWidth(25);
				tableAcceptedItems.getColumnModel().getColumn(1).setPreferredWidth(100);
				tableAcceptedItems.getColumnModel().getColumn(2).setPreferredWidth(100);
				tableAcceptedItems.getColumnModel().getColumn(3).setPreferredWidth(5);
				tableAcceptedItems.getColumnModel().getColumn(4).setPreferredWidth(100);
			}
		}
		
		/**
		 * Performs actions depending on which button is clicked.
		 * @param e action event encapsulating the action
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnAddAcceptedItem) {
				createEditAcceptedItem(true, -1);
			} else if (e.getSource() == btnEditAcceptedItem) {
				int selectedIndex = tableAcceptedItems.getSelectedRow();
				createEditAcceptedItem(false, selectedIndex);
			} else if (e.getSource() == btnRemoveAcceptedItem) {
				int selectedIndex = tableAcceptedItems.getSelectedRow();
				
				if (selectedIndex < 0) {
					JOptionPane.showMessageDialog(WolfProceedingsGUI.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				conference.removeAcceptedItem(selectedIndex);
			} else if (e.getSource() == btnAddAcceptedItemToSession) {
				int selectedSessionIndex = pnlSession.getSelectedSessionIdx();
				int selectedItemIndex = tableAcceptedItems.getSelectedRow();

				if (selectedSessionIndex < 0) {
					JOptionPane.showMessageDialog(WolfProceedingsGUI.this, 
							"No session selected.", 
							"No Session Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				if (selectedItemIndex < 0) {
					JOptionPane.showMessageDialog(WolfProceedingsGUI.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {		
					conference.addItemToSession(selectedSessionIndex, selectedItemIndex);
					pnlSession.updateSessions();
					pnlSession.updateAcceptedItemsInSession(selectedSessionIndex);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfProceedingsGUI.this, 
							iae.getMessage(), 
							iae.getMessage(), 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else if (e.getSource() == btnRemoveAcceptedItemFromSession) {
				int selectedSessionIndex = pnlSession.getSelectedSessionIdx();
				int selectedItemInSession = pnlSession.getSelectedItemInSessionIdx();

				if (selectedSessionIndex < 0) {
					JOptionPane.showMessageDialog(WolfProceedingsGUI.this, 
							"No session selected.", 
							"No Session Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				if (selectedItemInSession < 0) {
					JOptionPane.showMessageDialog(WolfProceedingsGUI.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				conference.removeItemFromSession(selectedSessionIndex, selectedItemInSession);
				pnlSession.updateSessions();
				pnlSession.updateAcceptedItemsInSession(selectedSessionIndex);
			}
			
			updateAcceptedItems();
			
			WolfProceedingsGUI.this.repaint();
			WolfProceedingsGUI.this.validate();
		}
		
		/**
		 * Helper method to create an accepted item from the dialog.
		 * @param toAdd true if adding, false if editing
		 * @param selectedIdx index of item to edit or -1 if adding
		 */
		private void createEditAcceptedItem(boolean toAdd, int selectedIdx) {
			AcceptedItemInputDialog dialog = null;
			if (toAdd) {
				dialog = new AcceptedItemInputDialog(WolfProceedingsGUI.this);
			} else {				
				if (selectedIdx < 0) {
					JOptionPane.showMessageDialog(AcceptedItemsPanel.this, 
							"No item selected.", 
							"No Item Selected", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				String itemType = tableModel.getValueAt(selectedIdx, 0).toString();
				String itemAuthor = tableModel.getValueAt(selectedIdx, 1).toString();
				String itemTitle = tableModel.getValueAt(selectedIdx, 2).toString();
				String itemDuration = tableModel.getValueAt(selectedIdx, 3).toString();
				
				dialog = new AcceptedItemInputDialog(WolfProceedingsGUI.this, itemType, itemAuthor, itemTitle, itemDuration);
			}
			
			dialog.setVisible(true);
			
			if (dialog.isConfirmed()) {
				String type = dialog.getItemType();
				String authors = dialog.getAuthors();
				String title = dialog.getItemTitle();
				int duration = dialog.getDuration();
				
				try {
					AcceptedItem item = null;
					switch (type) {
						case WolfProceedingsGUI.PAPER:
							if (duration > 0) {
								item = new Paper(authors, title, duration);
							} else {
								item = new Paper(authors, title);
							}
							break;
						case WolfProceedingsGUI.PANEL:
							item = new Panel(authors, title);
							break;
						case WolfProceedingsGUI.LIGHTNING_TALK:
							item = new LightningTalk(authors, title);
							break;
						default:
							JOptionPane.showMessageDialog(WolfProceedingsGUI.this, "Invalid accepted item.", "Error", JOptionPane.ERROR_MESSAGE);
					}
					if (item != null) {
						if (toAdd) {
							conference.addAcceptedItem(item);
						} else {
							conference.editAcceptedItem(selectedIdx, item);
						}
					} else {
						JOptionPane.showMessageDialog(WolfProceedingsGUI.this, "Invalid accepted item.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(WolfProceedingsGUI.this, iae.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		
		/**
		 * AcceptedItemsTableModel is the object underlying the JTable object that displays
		 * the list of accepted items.
		 */
		private class AcceptedItemsTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"Type", "Author(s)", "Title", "Duration", "Session"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the IncidentTableModel by requesting the latest information
			 * from the IncidentTableModel.
			 */
			public AcceptedItemsTableModel() {
				updateData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			@Override
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			@Override
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @param col the column index
			 * @return the column name at the given column.
			 */
			@Override
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @param row the row index
			 * @param col the column index
			 * @return the data at the given location.
			 */
			@Override
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row the row index
			 * @param col the column index
			 */
			@Override
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with AcceptedItems information for the proceedings.
			 */
			public void updateData() {
				if (conference != null) {
					data = conference.getAcceptedItemsAsArray();
					fireTableStructureChanged();
				}
			}
		}
		
	}
	
	/**
	 * Creates a dialog for entering session information.
	 */
	public class SessionInputDialog extends JDialog {
	    /** Serial Version UID */
		private static final long serialVersionUID = 1L;
		/** Text field for session name */
		private JTextField txtSessionName;
		/** Text field for session duration */
	    private JTextField txtSessionDuration;
	    /** True if the session is confirmed */
	    private boolean confirmed = false; 

	    /**
	     * Constructor for the SessionInputDialog.
	     * @param owner The parent frame of this dialog.
	     * @param initialSessionName name of session for editing
	     * @param initialDuration duration of session for editing
	     */
	    public SessionInputDialog(JFrame owner, String initialSessionName, String initialDuration) {
	        super(owner, "Enter Session Details", true); 
	        
	        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
	        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

	        JPanel pnlForm = new JPanel(new GridLayout(2, 2, 5, 5)); 

	        pnlForm.add(new JLabel("Session Name: "));
	        txtSessionName = new JTextField(20);
	        pnlForm.add(txtSessionName);

	        pnlForm.add(new JLabel("Duration (minutes):"));
	        txtSessionDuration = new JTextField(10); 
	        pnlForm.add(txtSessionDuration);
	        
	        pnlMain.add(pnlForm, BorderLayout.CENTER);

	        JPanel pnlButtons = new JPanel(new GridLayout(1, 2));

	        // OK Button
	        JButton btnOk = new JButton("OK");
	        btnOk.addActionListener(new ActionListener() {
	        	/**
	    		 * Performs actions depending on which button is clicked.
	    		 * @param e action event encapsulating the action
	    		 */
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                confirmed = true;
	                dispose(); 
	            }
	        });

	        // Cancel Button
	        JButton btnCancel = new JButton("Cancel");
	        btnCancel.addActionListener(new ActionListener() {
	        	/**
	    		 * Performs actions depending on which button is clicked.
	    		 * @param e action event encapsulating the action
	    		 */
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                confirmed = false;
	                dispose();
	            }
	        });

	        pnlButtons.add(btnOk);
	        pnlButtons.add(btnCancel);
	        
	        pnlMain.add(pnlButtons, BorderLayout.SOUTH);

	        add(pnlMain);
	        
	        txtSessionName.setText(initialSessionName);
	        txtSessionDuration.setText(initialDuration);
	        
	        pack(); 
	        setLocationRelativeTo(owner); 
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE); 
	    }
	    
	    /**
	     * Default constructor for a new session.
	     * @param owner The parent frame of this dialog. 
	     */
	    public SessionInputDialog(JFrame owner) {
	    	this(owner, "", "");
	    }

	    /**
	     * Returns true if the user clicks ok.
	     * @return true if the user clicks ok
	     */
	    public boolean isConfirmed() {
	        return confirmed;
	    }

	    /**
	     * Returns the session name.
	     * @return the session name
	     */
	    public String getSessionName() {
	        return txtSessionName.getText();
	    }

	    /**
	     * Attempts to parse the duration input as an integer.
	     * @return The duration in minutes, or -1 if the input is invalid.
	     */
	    public int getSessionDuration() {
	        try {
	            return Integer.parseInt(txtSessionDuration.getText().trim());
	        } catch (NumberFormatException e) {
	            return -1; 
	        }
	    }
	}
	
	/**
	 * Creates a dialog for entering accepted item information.
	 */
	public class AcceptedItemInputDialog extends JDialog {
		/** Serial Version UID */
		private static final long serialVersionUID = 1L;
		/** Combo box for accepted item types. */
		private JComboBox<String> comboItemTypes;
		/** Field for authors */
	    private JTextField txtAuthors;
	    /** Field for title */
	    private JTextField txtTitle;
	    /** Field for optional paper duration */
	    private JTextField txtDuration;
	    /** Label for optional paper duration */
	    private JLabel lblDuration;
	    
	    /** True if item is confirmed */
	    private boolean confirmed = false;
	    
	    /**
	     * Constructor for the AcceptedItemInputDialog.
	     * @param owner The parent frame of this dialog.
	     * @param type type of accepted item
	     * @param itemAuthor author information for accepted item
	     * @param itemTitle title information for accepted item
	     * @param itemDuration duration information for accepted item
	     */
	    public AcceptedItemInputDialog(JFrame owner, String type, String itemAuthor, String itemTitle, String itemDuration) {
	        super(owner, "Create New Accepted Item", true);
	        
	        JPanel pnlMain = new JPanel(new BorderLayout(10, 10));
	        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        
	        JPanel pnlForm = new JPanel(new GridLayout(5, 2, 5, 5));
	        
	        pnlForm.add(new JLabel("Item Type:"));
	        String[] types = {PAPER, PANEL, LIGHTNING_TALK};
	        comboItemTypes = new JComboBox<>(types);
	        
	        comboItemTypes.addActionListener(new ActionListener() {
	        	/**
	    		 * Performs actions depending on which button is clicked.
	    		 * @param e action event encapsulating the action
	    		 */
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                updateDurationVisibility();
	            }
	        });
	        pnlForm.add(comboItemTypes);
	        
	        pnlForm.add(new JLabel("Authors:"));
	        txtAuthors = new JTextField(30);
	        pnlForm.add(txtAuthors);
	        
	        pnlForm.add(new JLabel("Title:"));
	        txtTitle = new JTextField(30);
	        pnlForm.add(txtTitle);
	        
	        lblDuration = new JLabel("Duration (mins):"); 
	        pnlForm.add(lblDuration);
	        
	        txtDuration = new JTextField(5); 
	        pnlForm.add(txtDuration);

	        pnlMain.add(pnlForm, BorderLayout.CENTER);
	        
	        JPanel pnlButtons = new JPanel(new GridLayout(1, 2));
	        
	        // OK Button
	        JButton btnOk = new JButton("OK");
	        btnOk.addActionListener(new ActionListener() {
	        	/**
	    		 * Performs actions depending on which button is clicked.
	    		 * @param e action event encapsulating the action
	    		 */
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                confirmed = true;
	                dispose(); 
	            }
	        });

	        // Cancel Button
	        JButton btnCancel = new JButton("Cancel");
	        btnCancel.addActionListener(new ActionListener() {
	        	/**
	    		 * Performs actions depending on which button is clicked.
	    		 * @param e action event encapsulating the action
	    		 */
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                confirmed = false;
	                dispose();
	            }
	        });
	        
	        pnlButtons.add(btnOk);
	        pnlButtons.add(btnCancel);
	        
	        pnlMain.add(pnlButtons, BorderLayout.SOUTH);
	        add(pnlMain);
	        
	        comboItemTypes.setSelectedItem(type);
	        txtAuthors.setText(itemAuthor);
	        txtTitle.setText(itemTitle);
	        txtDuration.setText(itemDuration);
	        
	        pack();
	        setLocationRelativeTo(owner);
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	        
	        updateDurationVisibility();
	    }
	    
	    /**
	     * Constructor for the AcceptedItemInputDialog.
	     * @param owner The parent frame of this dialog.
	     */
	    public AcceptedItemInputDialog(JFrame owner) {
	    	this(owner, "", "", "", "");
	    }
	    
	    /**
	     * Toggles the visibility of the duration components based on the selected item type.
	     */
	    private void updateDurationVisibility() {
	        boolean isPaper = PAPER.equals(comboItemTypes.getSelectedItem());
	        
	        lblDuration.setVisible(isPaper);
	        txtDuration.setVisible(isPaper);
	        
	        pack(); 
	    }
	    
	    /**
	     * Returns true if the user clicks ok.
	     * @return true if the user clicks ok
	     */
	    public boolean isConfirmed() {
	        return confirmed;
	    }

	    /**
	     * Returns the accepted item type.
	     * @return accepted item type
	     */
	    public String getItemType() {
	        return (String) comboItemTypes.getSelectedItem();
	    }

	    /**
	     * Returns the authors
	     * @return authors
	     */
	    public String getAuthors() {
	        return txtAuthors.getText().trim();
	    }

	    /**
	     * Returns the title
	     * @return title
	     */
	    public String getItemTitle() {
	        return txtTitle.getText().trim();
	    }

	    /**
	     * Returns the duration. Returns -1 if accepted item is not a Paper or input is empty.
	     * @return the duration or -1 if invalid
	     */
	    public int getDuration() {
	        if (!PAPER.equals(getItemType()) || txtDuration.getText().trim().isEmpty()) {
	            return -1;
	        }
	        try {
	            return Integer.parseInt(txtDuration.getText().trim());
	        } catch (NumberFormatException e) {
	        	return -1;
	        }
	    }
	}

}