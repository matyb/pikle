package com.pk;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;

import com.pk.script.ui.ScriptPanel;

public class PrettyKid extends JFrame implements ActionListener, Constants {
	
	private static final long serialVersionUID = 1L;
	private JDialog preferencesJDialog = null;
	private Config config = null;
	private ScriptPanel scriptPanel = null;
	JDesktopPane desktop;
	Vector<Container> myInternalFrameList = null;
	Map<String, ConnectionInformation> connectionInfoByKey = null;
	Preferences propertiyFrame = null;

	MyInternalFrame frame;
	MyInternalFrame ji;

	String user;
	String password;
	String url;
	String driver;

	JMenu windowMenu = null;
	JMenuBar menuBar = null; // The menu bar
	JMenuItem nextWindowItem = null;
	JMenuItem newConItem = null;
	JMenuItem newWinItem = null;
	JMenuItem saveItem = null;
	JMenuItem exortXMLItem = null;
	JMenuItem exortHTMLItem = null;
	JMenuItem saveSQLItem = null;
	JMenuItem loadSQLItem = null;
	JMenuItem printItem = null;
	JMenuItem clearQueryItem = null;// Clear SQL item
	JMenuItem executeItem = null;
	JMenuItem historyItem = null;
	JMenuItem exitItem = null;// Exit item
	JMenuItem cutItem = null;// Cut item
	JMenuItem copyItem = null;// Copy item
	JMenuItem pasteItem = null;// Paste item
	JMenuItem previousSQLItem = null;// Previous SQL statements item
	JMenuItem commitItem = null;
	JMenuItem rollBackItem = null;
	JMenuItem objectTableItem = null; // Object Table item
	JMenuItem objectIndexItem = null; // Object Index item
	JMenuItem objectProcedureItem = null;
	JMenuItem objectPackageItem = null;
	JMenuItem planItem = null;
	JMenuItem aboutpklItem = null; // About item
	JMenuItem preferencesItem = null;
	JMenuItem scriptRunnerItem = null;
	JMenuItem undoItem = null;
	JMenuItem redoItem = null;

	Properties pkLiteProperties = null;

	int isSgaFrameOpened = 0;

	public PrettyKid() {

		super("pikle SQL Client"); // Call base constructor

		Set<AWTKeyStroke> tmpHashSet = new HashSet<AWTKeyStroke>();
		tmpHashSet.add(AWTKeyStroke.getAWTKeyStroke("TAB"));
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
			.setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, tmpHashSet);

		myInternalFrameList = new Vector<Container>();
		connectionInfoByKey = new Hashtable<String, ConnectionInformation>();
		try {

			// This code will load and create the new config object from a xml file
			ConfigDigester tmpConfigDigester = new ConfigDigester();
			tmpConfigDigester.setFileName("config.xml");
			config = tmpConfigDigester.createConfig();
			UIManager.setLookAndFeel(config.getSelectedLookAndFeel());
			SwingUtilities.updateComponentTreeUI(this);
			getpropertiyFrame().setConfig(config);
			getpropertiyFrame().initialize();

			List<ConnectionConfig> tmp = config.getConnections();
			String driveName = null;
			int size = tmp.size();
			for (int x = 0; x < size; x++) {
				driveName = ((ConnectionConfig) tmp.get(x)).getDriver();
				try {
					Class.forName(driveName);
				} catch (ClassNotFoundException e) {
					JOptionPane.showMessageDialog(this,
							"Could not find class for driver: " + driveName);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e1) {
			}
		}
		setBounds(50, 0, 100, 100); // Set window bounds
		menuBar = new JMenuBar(); // The menu bar
		newConItem = new JMenuItem("New Connection");
		newWinItem = new JMenuItem("New Window");
		saveItem = new JMenuItem("as Delimited File...");
		exortXMLItem = new JMenuItem("as XML...");
		exortHTMLItem = new JMenuItem("as HTML...");
		saveSQLItem = new JMenuItem("Save SQL...");
		loadSQLItem = new JMenuItem("Load SQL...");
		printItem = new JMenuItem("Print...");
		clearQueryItem = new JMenuItem("Clear query"); // Clear SQL item
		executeItem = new JMenuItem("Execute");
		historyItem = new JMenuItem("History");
		exitItem = new JMenuItem("Exit"); // Exit item
		cutItem = new JMenuItem("Cut"); // Cut item
		copyItem = new JMenuItem("Copy"); // Copy item
		pasteItem = new JMenuItem("Paste"); // Paste item
		previousSQLItem = new JMenuItem("Previous SQL"); // Previous SQL
															// statements item
		commitItem = new JMenuItem("Commit");
		rollBackItem = new JMenuItem("RollBack");
		objectTableItem = new JMenuItem("Table"); // Object Table item
		objectIndexItem = new JMenuItem("Index"); // Object Index item
		objectProcedureItem = new JMenuItem("Procedure");
		objectPackageItem = new JMenuItem("Package");
		preferencesItem = new JMenuItem("Preferences");
		scriptRunnerItem = new JMenuItem("Run Script");
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");

		planItem = new JMenuItem("Explain plan");
		aboutpklItem = new JMenuItem("About pikle"); // About item

		planItem = new JMenuItem("Explain plan");
		nextWindowItem = new JMenuItem("Next Window");
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height
				- inset * 2);

		addWindowListener(new WindowHandler()); // Listener for window close

		JPanel overAllPanel = new JPanel();
		overAllPanel.setLayout(new BorderLayout());
		overAllPanel.setPreferredSize(new java.awt.Dimension(screenSize.width
				- inset * 4, screenSize.height - inset * 4));
		setContentPane(overAllPanel);
		desktop = new JDesktopPane(); // a specialized layered pane
		overAllPanel.add(desktop, BorderLayout.CENTER);
		setJMenuBar(createMenuBar());

		JToolBar toolBar = new JToolBar();

		addButtons(toolBar);
		overAllPanel.add(toolBar, BorderLayout.NORTH);

		desktop.putClientProperty("JDesktopPane.dragMode", "outline");

		pack();

		setVisible(true);

		createFrame(); // Create first window

	}

	protected JMenuBar createMenuBar() {
		// Create the menubar from the menu items

		JMenu fileMenu = new JMenu("File"); // Create File menu
		fileMenu.setMnemonic('F'); // Create shortcut
		JMenu editMenu = new JMenu("Edit"); // Create Edit menu
		editMenu.setMnemonic('E'); // Create shortcut
		JMenu objectMenu = new JMenu("Schema"); // Create Monitor menu
		objectMenu.setMnemonic('S'); // Create shortcut

		JMenu windowMenu = new JMenu("Window");
		windowMenu.setMnemonic('W');
		JMenu helpMenu = new JMenu("Help"); // Create Help menu
		helpMenu.setMnemonic('H'); // Create shortcut

		printItem.addActionListener(this);
		newConItem.addActionListener(this); // Add Item Listener from HERE!!!
		newWinItem.addActionListener(this);
		saveItem.addActionListener(this);
		exortXMLItem.addActionListener(this);
		exortHTMLItem.addActionListener(this);
		saveSQLItem.addActionListener(this);
		loadSQLItem.addActionListener(this);
		clearQueryItem.addActionListener(this);
		executeItem.addActionListener(this);
		historyItem.addActionListener(this);
		exitItem.addActionListener(this);
		cutItem.addActionListener(this);
		copyItem.addActionListener(this);
		pasteItem.addActionListener(this);
		previousSQLItem.addActionListener(this);
		commitItem.addActionListener(this);
		rollBackItem.addActionListener(this);
		objectTableItem.addActionListener(this);
		objectIndexItem.addActionListener(this);
		objectProcedureItem.addActionListener(this);
		objectPackageItem.addActionListener(this);
		preferencesItem.addActionListener(this);
		scriptRunnerItem.addActionListener(this);
		undoItem.addActionListener(this);
		redoItem.addActionListener(this);
		nextWindowItem.addActionListener(this);
		nextWindowItem.setAccelerator(KeyStroke.getKeyStroke('G',
				Event.CTRL_MASK));
		// nextWindowItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_TAB,
		// java.awt.Event.CTRL_MASK, false));

		planItem.addActionListener(this);
		aboutpklItem.addActionListener(this);

		newConItem.setAccelerator(KeyStroke.getKeyStroke('N', Event.CTRL_MASK));
		newWinItem.setAccelerator(KeyStroke.getKeyStroke('W', Event.CTRL_MASK));
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
		clearQueryItem.setAccelerator(KeyStroke.getKeyStroke('D',
				Event.CTRL_MASK));
		executeItem
				.setAccelerator(KeyStroke.getKeyStroke('R', Event.CTRL_MASK));
		historyItem
				.setAccelerator(KeyStroke.getKeyStroke('I', Event.CTRL_MASK));
		cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));
		cutItem.getAccessibleContext().setAccessibleDescription("Cut");
		cutItem.setAction(new DefaultEditorKit.CutAction());
		cutItem.setText("Cut"); // need to do this because setAction () changes
								// the message
		copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK));
		copyItem.getAccessibleContext().setAccessibleDescription("Copy");
		copyItem.setAction(new DefaultEditorKit.CopyAction());
		copyItem.setText("Copy");
		pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				ActionEvent.CTRL_MASK));
		pasteItem.getAccessibleContext().setAccessibleDescription("Paste");
		pasteItem.setAction(new DefaultEditorKit.PasteAction());
		pasteItem.setText("Paste");
		previousSQLItem.setAccelerator(KeyStroke.getKeyStroke('P',
				Event.CTRL_MASK));
		planItem.setAccelerator(KeyStroke.getKeyStroke('E', Event.CTRL_MASK));

		JMenu tuningMenu = new JMenu("Tuning");
		tuningMenu.setMnemonic('T');
		fileMenu.add(newConItem); // Add file menu Item from HERE!!!
		fileMenu.add(newWinItem);
		fileMenu.add(executeItem);
		fileMenu.add(printItem);
		fileMenu.addSeparator();
		JMenu exportMenu = new JMenu("Export Results...");
		exportMenu.add(saveItem);
		exportMenu.add(exortXMLItem);
		exportMenu.add(exortHTMLItem);
		fileMenu.add(exportMenu);
		// fileMenu.add(saveItem);
		// fileMenu.add(exortXMLItem);
		// fileMenu.add(exortHTMLItem);
		fileMenu.add(saveSQLItem);
		fileMenu.add(loadSQLItem);
		// fileMenu.add(clearQueryItem); // to edit menu

		fileMenu.add(exitItem);
		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.add(undoItem);
		editMenu.add(redoItem);
		editMenu.addSeparator(); // Add separator
		editMenu.add(clearQueryItem);
		editMenu.add(previousSQLItem);
		editMenu.add(historyItem);
		editMenu.addSeparator();
		editMenu.add(commitItem);
		editMenu.add(rollBackItem);
		objectMenu.add(objectTableItem); // Add object menu Item from HERE!!!
		objectMenu.add(objectIndexItem);
		objectMenu.add(objectProcedureItem);
		objectMenu.add(objectPackageItem);

		tuningMenu.add(planItem);
		helpMenu.add(aboutpklItem); // Add help menu Item from HERE!!!

		windowMenu.addSeparator();
		windowMenu.add(nextWindowItem);
		windowMenu.addSeparator();
		windowMenu.add(scriptRunnerItem);
		windowMenu.add(preferencesItem);
		menuBar.add(fileMenu); // Add menu to the menubar
		menuBar.add(editMenu);
		menuBar.add(objectMenu);
		menuBar.add(tuningMenu);
		menuBar.add(windowMenu);
		menuBar.add(helpMenu);

		return menuBar;

	}

	public void createFrame() {
		frame = new MyInternalFrame(this);
		desktop.add(frame);
	}

	public void createMyNewWindow() {
		MyInternalFrame tmpMyInternalFrame = getSelectedMyFrame();
		frame = new MyInternalFrame(this, tmpMyInternalFrame.connection,
				tmpMyInternalFrame.userid,
				tmpMyInternalFrame.getConnectionName());
		String tmpConncetionKey = tmpMyInternalFrame.getConncetionKey();
		frame.setConncetionKey(tmpConncetionKey);
		ConnectionInformation tmpConnectionInformation = (ConnectionInformation) connectionInfoByKey
				.get(tmpConncetionKey);
		tmpConnectionInformation.setNumberOfWindows(tmpConnectionInformation
				.getNumberOfWindows() + 1);
		frame.setKeywords(tmpConnectionInformation.getDatabaseDialect()
				.getKeywords());
		connectionInfoByKey.put(tmpConncetionKey, tmpConnectionInformation);
		desktop.add(frame);

		try {
			frame.setSelected(true);

		} catch (java.beans.PropertyVetoException e) {
		}
		createWindowMenu();
	}

	public MyInternalFrame getSelectedMyFrame() {
		if (desktop.getSelectedFrame() != null
				&& desktop.getSelectedFrame() instanceof MyInternalFrame)
			return (MyInternalFrame) desktop.getSelectedFrame();
		else
			JOptionPane
					.showMessageDialog(this,
							"Please activate and select the window you want to handle...");
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source instanceof WindowJMenuItem) {
			WindowJMenuItem tmpWindowJMenuItem = (WindowJMenuItem) source;
			MyInternalFrame tmp = null;
			try {
				tmp = (MyInternalFrame) myInternalFrameList
						.get(tmpWindowJMenuItem.getWindowIndex());
				desktop.getSelectedFrame().setSelected(false);
				desktop.setSelectedFrame(tmp);

				tmp.setSelected(true);
			} catch (PropertyVetoException e1) {
				e1.printStackTrace();
			}
		} else if (source == nextWindowItem) {
			// TODO code next window
			nextWindow();
		} else if (source == newConItem) {
			createFrame();
		}

		else if (source == newWinItem) {
			createMyNewWindow();
		}

		else if (source == saveItem) {
			try {
				doSaveCommand();
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(this, " " + ioe,
						"Territory Reporter", JOptionPane.WARNING_MESSAGE);
			}

		} else if (source == exortXMLItem) {
			try {
				doExportXMLCommand();
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(this, " " + ioe,
						"Territory Reporter", JOptionPane.WARNING_MESSAGE);
			}

		} else if (source == exortHTMLItem) {
			try {
				doExportHTMLCommand();
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(this, " " + ioe,
						"Territory Reporter", JOptionPane.WARNING_MESSAGE);
			}

		} else if (source == loadSQLItem) {
			try {
				doLoadSQL();
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(this, " " + ioe,
						"Territory Reporter", JOptionPane.WARNING_MESSAGE);
			}

		} else if (source == printItem) {
			try {
				doPrintCommand();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		else if (source == clearQueryItem) // Clear query menu item
		{

			getSelectedMyFrame().setSqlTextArea(""); // Clear SQL entry
		} else if (source == undoItem) // Clear query menu item
		{

			getSelectedMyFrame().undoLastEdit();
		} else if (source == redoItem) // Clear query menu item
		{

			getSelectedMyFrame().redoLastEdit();
		}

		else if (source == executeItem) {
			getSelectedMyFrame().prexecuteSQL();
		} else if (source == saveSQLItem) {
			try {
				doSaveSQL();
			} catch (IOException ioe) {
				JOptionPane.showMessageDialog(this, " " + ioe,
						"Territory Reporter", JOptionPane.WARNING_MESSAGE);
			}
		}

		else if (source == exitItem) // Exit menu item
		{
			dispose(); // Release the window resources
			System.exit(0); // End the application
		}

		else if (source == previousSQLItem) {
			if (getSelectedMyFrame().index_of_sqls == 0) {
				JOptionPane.showMessageDialog(this,
						"There is not any SQL statement saved...");
			} else {
				if (--getSelectedMyFrame().local_index_of_sqls >= 0) {
					getSelectedMyFrame()
							.setSqlTextArea(
									(String) getSelectedMyFrame().sqls
											.get(getSelectedMyFrame().local_index_of_sqls));

				} else {
					getSelectedMyFrame().local_index_of_sqls = getSelectedMyFrame().index_of_sqls - 1;
					getSelectedMyFrame()
							.setSqlTextArea(
									(String) getSelectedMyFrame().sqls
											.get(getSelectedMyFrame().local_index_of_sqls));
				}
			}
		}

		else if (source == objectTableItem) {
			if (getSelectedMyFrame().tableBrowseFlag == 0) {
				if (getSelectedMyFrame()
						.getConnectionInformation()
						.getDatabaseDialect()
						.getTableInfoSQLString("test",
								getSelectedMyFrame().getConnectionInformation()) == null) {
					JOptionPane
							.showMessageDialog(
									this,
									"This operation is not supported by this database dialect.",
									"Unsupported Operation",
									JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				new TableBrowse(getSelectedMyFrame(),
						getSelectedMyFrame().connection,
						getSelectedMyFrame().userid);
				getSelectedMyFrame().tableBrowseFlag = 1;
			} else if (getSelectedMyFrame().tableBrowseFlag == 1) {
			}
		}

		else if (source == objectIndexItem) {
			if (getSelectedMyFrame().indexBrowseFlag == 0) {
				if (getSelectedMyFrame()
						.getConnectionInformation()
						.getDatabaseDialect()
						.getTableInfoSQLString("test",
								getSelectedMyFrame().getConnectionInformation()) == null) {
					JOptionPane
							.showMessageDialog(
									this,
									"This operation is not supported by this database dialect.",
									"Unsupported Operation",
									JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				new IndexBrowse(getSelectedMyFrame(),
						getSelectedMyFrame().connection,
						getSelectedMyFrame().userid);

				getSelectedMyFrame().indexBrowseFlag = 1;
			}
		}

		else if (source == objectProcedureItem) {
			if (getSelectedMyFrame().procedureBrowseFlag == 0) {
				if (getSelectedMyFrame()
						.getConnectionInformation()
						.getDatabaseDialect()
						.getProcedureInfoSQLString("test",
								getSelectedMyFrame().getConnectionInformation()) == null) {
					JOptionPane
							.showMessageDialog(
									this,
									"This operation is not supported by this database dialect.",
									"Unsupported Operation",
									JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				new ProcedureBrowse(getSelectedMyFrame(),
						getSelectedMyFrame().connection,
						getSelectedMyFrame().userid);

				getSelectedMyFrame().procedureBrowseFlag = 1;
			}
		}

		else if (source == objectPackageItem) {
			if (getSelectedMyFrame().packageBrowseFlag == 0) {

				if (getSelectedMyFrame()
						.getConnectionInformation()
						.getDatabaseDialect()
						.getPackageInfoSQLString("test",
								getSelectedMyFrame().getConnectionInformation()) == null) {
					JOptionPane
							.showMessageDialog(
									this,
									"This operation is not supported by this database dialect.",
									"Unsupported Operation",
									JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				new PackageBrowse(getSelectedMyFrame(),
						getSelectedMyFrame().connection,
						getSelectedMyFrame().userid);

				getSelectedMyFrame().packageBrowseFlag = 1;
			}
		}

		else if (source == planItem) {
			getSelectedMyFrame().executePlanSQL();
		}

		else if (source == aboutpklItem) {
			aboutPkl();
		}

		else if (source == historyItem) {
			showHistory(getSelectedMyFrame());
		} else if (source == commitItem) {
			MyInternalFrame tmpMyInternalFrame = getSelectedMyFrame();
			if (tmpMyInternalFrame != null
					&& tmpMyInternalFrame.connection != null) {
				tmpMyInternalFrame.commitTransaction();
			}
		} else if (source == rollBackItem) {
			MyInternalFrame tmpMyInternalFrame = getSelectedMyFrame();
			if (tmpMyInternalFrame != null
					&& tmpMyInternalFrame.connection != null) {
				tmpMyInternalFrame.rollBackTransaction();
			}
		} else if (source == preferencesItem) {
			int yPos = 0;
			int xPos = 0;
			if (preferencesJDialog == null) {
				preferencesJDialog = new JDialog(this, true);
				preferencesJDialog.setTitle("Preferences");
				preferencesJDialog.setVisible(false);
				propertiyFrame.setJDialog(preferencesJDialog);
				preferencesJDialog.getContentPane().add(propertiyFrame);
				preferencesJDialog.setSize(Preferences.HEIGHT,
						Preferences.WIDTH + 20);
			}
			// tmpJDialog.pack();

			yPos = getBounds().y
					+ ((getHeight() / 2) - (preferencesJDialog.getHeight() / 2));
			xPos = getBounds().x
					+ ((getWidth() / 2) - (preferencesJDialog.getWidth() / 2));
			preferencesJDialog.setBounds(xPos, yPos,
					preferencesJDialog.getWidth(),
					preferencesJDialog.getHeight());

			preferencesJDialog.setVisible(true);
			preferencesJDialog.setResizable(false);

		} else if (source == scriptRunnerItem) {
			int yPos = 0;
			int xPos = 0;
			if (scriptPanel == null) {
				scriptPanel = new ScriptPanel();
				scriptPanel.init(config);
			}

			JFrame tmp = scriptPanel.getFrame();
			tmp.setSize(725, 670);
			yPos = getBounds().y + ((getHeight() / 2) - (tmp.getHeight() / 2));
			xPos = getBounds().x + ((getWidth() / 2) - (tmp.getWidth() / 2));
			tmp.setBounds(xPos, yPos, tmp.getWidth(), tmp.getHeight());

			tmp.setVisible(true);

		}
	}

	/**
     * 
     */
	public void nextWindow() {
		try {
			MyInternalFrame selectedInternalFrame = getSelectedMyFrame();
			MyInternalFrame nextInternalFrame = null;
			if (selectedInternalFrame == null) {
				return;
			}

			int tmpIndex = selectedInternalFrame.getFrameIndex();

			desktop.getSelectedFrame().setSelected(false);
			if (tmpIndex < (myInternalFrameList.size() - 1)) {
				nextInternalFrame = (MyInternalFrame) myInternalFrameList
						.get((tmpIndex + 1));

			} else {
				nextInternalFrame = (MyInternalFrame) myInternalFrameList
						.get(0);
			}
			desktop.setSelectedFrame(nextInternalFrame);
			nextInternalFrame.setSelected(true);
		} catch (PropertyVetoException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @param frame
	 */
	private void showHistory(MyInternalFrame frame) {
		int yPos = 0;
		int xPos = 0;
		JDialog test = new JDialog();
		test.setTitle("History");
		test.setModal(true);
		History history = new History(getSelectedMyFrame(), test);

		test.getContentPane().add(history);

		test.pack();
		yPos = getBounds().y + ((getHeight() / 2) - (test.getHeight() / 2));
		xPos = getBounds().x + ((getWidth() / 2) - (test.getWidth() / 2));
		test.setBounds(xPos, yPos, test.getWidth(), test.getHeight());
		test.setResizable(false);
		test.setVisible(true);
		test.repaint();

	}

	void doSaveSQL() throws IOException {
		FileDialog exportFile = new FileDialog(this, "Save File",
				FileDialog.SAVE);

		exportFile.setVisible(true);
		String curFile = exportFile.getFile();
		String filename = exportFile.getDirectory() + curFile;
		if (curFile == null) {
			return;
		}
		if (filename.lastIndexOf(".sql") == -1) {
			filename += ".sql";
		}
		File tmpFile = new File(filename);

		BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));
		out.write(getSelectedMyFrame().sqlTextArea.getText());
		out.close();
	}

	void doLoadSQL() throws IOException {

		FileDialog exportFile = new FileDialog(this, "Load File", FileDialog.LOAD);

		exportFile.setVisible(true);
		String curFile = exportFile.getFile();
		String filename = exportFile.getDirectory() + curFile;
		if (curFile == null) {
			return;
		}

		FileReader tmpFile = new FileReader(filename);
		BufferedReader in = new BufferedReader(tmpFile);
		String tmpSQL = "";
		String tmpSQL2 = null;
		while ((tmpSQL2 = in.readLine()) != null) {
			tmpSQL += tmpSQL2 + System.getProperty("line.separator");
		}
		in.close();
		getSelectedMyFrame().sqlTextArea.setText(tmpSQL);
	}

	void doPrintCommand() throws IOException {
		PrintTable tmp = new PrintTable();
		tmp.setJTable(getSelectedMyFrame().table);
		tmp.doPrintJTable();
	}

	void doSaveCommand() throws IOException {

		try {
			String delimiter = config.getExportDelimiterChar();
			String enclosedBy = config.getEncloseByChar();

			Connection con = getSelectedMyFrame().connection;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery(getSelectedMyFrame().commentHandling(
							getSelectedMyFrame().sqlTextArea.getText()));

			ResultSetMetaData rsmd = rs.getMetaData();
			int noOfColumns = rsmd.getColumnCount();
			String dataRow = "";

			FileDialog exportFile = new FileDialog(this, "Save File",
					FileDialog.SAVE);

			exportFile.setVisible(true);
			String curFile = exportFile.getFile();
			if (curFile == null) {
				return;
			}
			if (curFile.lastIndexOf(".csv") == -1) {
				curFile += ".csv";
			}

			String filename = exportFile.getDirectory() + curFile;

			File tmpFile = new File(filename);

			BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));

			for (int a = 1; a < noOfColumns + 1; a++) {
				if (a == 1) {
					dataRow = rsmd.getColumnName(a);

				} else {
					dataRow = dataRow + delimiter + rsmd.getColumnName(a);
				}

			}
			out.write(dataRow);
			out.newLine();

			while (rs.next()) {
				for (int a = 1; a < noOfColumns + 1; a++) {
					if (a == 1) {
						if (rs.getString(a) == null) {
							dataRow = enclosedBy + enclosedBy;
						} else {
							dataRow = enclosedBy + rs.getString(a) + enclosedBy;
						}
					} else {
						if (rs.getString(a) == null) {
							dataRow = dataRow + delimiter + enclosedBy
									+ enclosedBy;
						} else {
							dataRow = dataRow + delimiter + enclosedBy
									+ rs.getString(a) + enclosedBy;
						}
					}
				}
				out.write(dataRow);
				out.newLine();
			}
			// close the file
			out.close();

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(this, sqle.getMessage());
		}
	}

	void doExportXMLCommand() throws IOException {

		try {
			boolean useCdata = config.isUseCdata();
			Connection con = getSelectedMyFrame().connection;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery(getSelectedMyFrame().commentHandling(
							getSelectedMyFrame().sqlTextArea.getText()));

			ResultSetMetaData rsmd = rs.getMetaData();
			int noOfColumns = rsmd.getColumnCount();
			String dataRow = "";

			FileDialog exportFile = new FileDialog(this, "Save File",
					FileDialog.SAVE);

			exportFile.setVisible(true);
			String curFile = exportFile.getFile();
			if (curFile == null) {
				return;
			}
			if (curFile.lastIndexOf(".xml") == -1) {
				curFile += ".xml";
			}

			String filename = exportFile.getDirectory() + curFile;

			File tmpFile = new File(filename);

			BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));

			// out.write("<?xml version=\"1.0\" encoding=\"" +
			// System.getProperty("file.encoding")+ "\"?>");
			out.write("<?xml version=\"1.0\"?>");
			out.newLine();
			out.write("\t<resultSet>");
			out.newLine();
			int rowCount = 0;
			while (rs.next()) {
				out.write("\t\t<row rowNumber = \"" + String.valueOf(rowCount)
						+ "\" >");
				out.newLine();
				for (int a = 1; a < noOfColumns + 1; a++) {
					dataRow = "\t\t\t<" + rsmd.getColumnName(a);
					if (rs.getString(a) == null) {
						dataRow += " />";
					} else {
						dataRow += ">" + (useCdata ? "<![CDATA[" : "")
								+ rs.getString(a) + (useCdata ? "]]>" : "")
								+ "</" + rsmd.getColumnName(a) + ">";
					}
					out.write(dataRow);
					out.newLine();
				}
				out.write("\t\t</row>");
				out.newLine();
				rowCount++;
			}
			out.write("\t</resultSet>");
			out.newLine();
			out.close();

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(this, sqle.getMessage());
		}
	}

	void doExportHTMLCommand() throws IOException {
		try {
			boolean altRowColor = false;
			Connection con = getSelectedMyFrame().connection;
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(getSelectedMyFrame().commentHandling(getSelectedMyFrame().sqlTextArea.getText()));

			ResultSetMetaData rsmd = rs.getMetaData();
			int noOfColumns = rsmd.getColumnCount();

			FileDialog exportFile = new FileDialog(this, "Save File",
					FileDialog.SAVE);

			exportFile.setVisible(true);
			String curFile = exportFile.getFile();
			if (curFile == null) {
				return;
			}
			if (curFile.lastIndexOf(".html") == -1) {
				curFile += ".html";
			}

			String filename = exportFile.getDirectory() + curFile;

			File tmpFile = new File(filename);

			BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));
			out.write("<HTML>");
			out.newLine();
			out.write("<BODY>");
			out.newLine();
			out.write("<TABLE style=\"border-collapse= collapse\" border=\"1\" bordercolor=\""
					+ config.getBorderColor() + "\"  rules=\"cols,rows\">");
			out.newLine();
			out.write("<tbody>");
			out.newLine();
			out.write("<TR bgcolor=\"" + config.getHeaderColor() + "\">");
			out.newLine();
			for (int a = 1; a < noOfColumns + 1; a++) {
				out.write("<TH>" + Util.escapeHTML(rsmd.getColumnName(a))
						+ "</TH>");
				out.newLine();
			}
			while (rs.next()) {
				// Alternate row color
				if (altRowColor) {
					out.write("<TR bgcolor=\"" + config.getAltRowColor()
							+ "\" >");
					out.newLine();
					altRowColor = false;
				} else {
					altRowColor = true;
					out.write("<TR>");
					out.newLine();
				}
				for (int a = 1; a < noOfColumns + 1; a++) {
					if (rs.getString(a) == (null)) {
						out.write("<TD>&nbsp;</TD>");
						out.newLine();
					} else {
						out.write("<TD>" + Util.escapeHTML(rs.getString(a))
								+ "</TD>");
						out.newLine();
					}
				}
				out.write("</TR>");
				out.newLine();
			}

			out.write("</tbody>");
			out.newLine();
			out.write("</TABLE>");
			out.newLine();
			out.write("</BODY>");
			out.newLine();
			out.write("</HTML>");
			// close the file
			out.close();

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(this, sqle.getMessage());
		}
	}

	public void aboutPkl() {
		int yPos = 0;
		int xPos = 0;
		JDialog tmpJDialog = new JDialog(this, true);
		tmpJDialog.setTitle("About");

		AboutPanel tmpAboutPanel = new AboutPanel(tmpJDialog);
		tmpJDialog.getContentPane().add(tmpAboutPanel);
		tmpJDialog.pack();

		yPos = getBounds().y
				+ ((getHeight() / 2) - (tmpJDialog.getHeight() / 2));
		xPos = getBounds().x + ((getWidth() / 2) - (tmpJDialog.getWidth() / 2));
		tmpJDialog.setBounds(xPos, yPos, tmpJDialog.getWidth(),
				tmpJDialog.getHeight());

		tmpJDialog.setVisible(true);
		tmpJDialog.setResizable(false);

	}

	class HandleControlButton implements ActionListener {
		private int buttonID;

		public HandleControlButton(int buttonID) {
			this.buttonID = buttonID;
		}

		public void actionPerformed(ActionEvent e) {
			switch (buttonID) {
			case 1: // Execute SQL
				getSelectedMyFrame().prexecuteSQL();
				break;

			case 2: // New connection(session)
				createMyNewWindow();
				break;

			case 3: // Save
				try {
					doSaveCommand();
				} catch (IOException ioe) {
					System.out.println("IOException occured... " + ioe);
				}
				break;

			case 4: // Cut
				getSelectedMyFrame().sqlTextArea.cut();
				break;

			case 5: // Copy
				// if(getSelectedMyFrame().get)
				// {
				// int d = 0;
				// d=d;
				// }
				getSelectedMyFrame().sqlTextArea.copy();
				break;

			case 6: // Paste
				getSelectedMyFrame().sqlTextArea.paste();
				break;

			case 7: // Help
				aboutPkl();
				break;
			case 8: // History
				showHistory(getSelectedMyFrame());
				break;

			}
		}
	}

	class WindowHandler extends WindowAdapter {
		public void windowActivated(WindowEvent e) {
			// System.out.println("Window activated.");
		}

		public void windowLostFocus(WindowEvent e) {
			// System.out.println("Window lost focus.");
		}

		public void windowGainedFocus(WindowEvent e) {
			// System.out.println("Window gained focus.");
		}

		public void windowStateChanged(WindowEvent e) {
			// System.out.println("Window state changed.");
		}

		// Handler for window closing event
		public void windowClosing(WindowEvent e) {
			dispose(); // Release the window resources
			System.exit(0); // End the application
		}
	}

	class WinHandlerForSgaItem extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			isSgaFrameOpened = 0;
		}
	}

	protected void addButtons(JToolBar toolBar) {
		JButton button = null;

		button = new JButton(new ImageIcon("images/run.gif"));
		button.setToolTipText("Run Query");
		toolBar.add(button);
		button.addActionListener(new HandleControlButton(1));

		button = new JButton(new ImageIcon("images/New16.gif"));
		button.setToolTipText("New Window");
		toolBar.add(button);
		button.addActionListener(new HandleControlButton(2));

		button = new JButton(new ImageIcon("images/Save16.gif"));
		button.setToolTipText("Save Results to CSV File");
		toolBar.add(button);
		button.addActionListener(new HandleControlButton(3));

		button = new JButton(new ImageIcon("images/Cut16.gif"));
		button.setToolTipText("Cut");
		toolBar.add(button);
		button.addActionListener(new HandleControlButton(4));

		button = new JButton(new ImageIcon("images/Copy16.gif"));
		button.setToolTipText("Copy");
		toolBar.add(button);
		button.addActionListener(new HandleControlButton(5));

		button = new JButton(new ImageIcon("images/Paste16.gif"));
		button.setToolTipText("past");
		toolBar.add(button);
		button.addActionListener(new HandleControlButton(6));

		button = new JButton(new ImageIcon("images/Help16.gif"));
		button.setToolTipText("Help");
		toolBar.add(button);
		button.addActionListener(new HandleControlButton(7));
		button = new JButton(new ImageIcon("images/History16.gif"));
		button.setToolTipText("Show Statment History");
		toolBar.add(button);
		button.addActionListener(new HandleControlButton(8));

	}

	public class TabbedPanel extends JPanel {
		/**
         * 
         */
		private static final long serialVersionUID = 1L;
		String tabs[] = { "Library Cache", "Dictionary Cache", "Buffer Cache",
				"Sort", "Redo Log Request" };
		String result;
		String rs[] = { "xxx", "xxx" };

		public JTabbedPane tabbedPane = new JTabbedPane();

		public TabbedPanel() {
			setLayout(new BorderLayout());
			for (int i = 0; i < tabs.length; i++) {
				switch (i) {
				case 0:
					result = executeLibrarayCacheSQL();
					break;
				case 1:
					result = executeDictionaryCacheSQL();
					break;
				case 2:
					result = executeBufferCacheSQL();
					break;
				case 3:
					executeSortSQL();
					break;
				case 4:
					result = executeRedoLogSQL();
					break;
				}
				tabbedPane.addTab(tabs[i], null, createPane(i, " "));
			}
			tabbedPane.setSelectedIndex(0);
			add(tabbedPane, BorderLayout.CENTER);
		}

		public String executeLibrarayCacheSQL() {
			String query = "select to_char(trunc(sum(reloads)/sum(pins)*100, 5),99.99999)||'%'"
					+ " as libcache from v$librarycache";
			String libCacheRatio = "xxx";

			try {
				ResultSet r = frame.statement.executeQuery(query);
				while (r.next()) {
					libCacheRatio = r.getString("libcache");
				}
			} catch (SQLException sqle) {
				frame.statusBar.setMessagePane(sqle.getMessage()); // Display
																	// error
																	// message
			}

			return libCacheRatio;

		}

		public String executeDictionaryCacheSQL() {
			String query = "select trunc(sum(getmisses)/sum(gets)*100, 5)||'%  (less than 9.8%)' "
					+ " as dictionary from v$rowcache";
			String dictionaryRatio = "xxx";

			try {
				ResultSet r = frame.statement.executeQuery(query);
				while (r.next()) {
					dictionaryRatio = r.getString("dictionary");
				}
			} catch (SQLException sqle) {
				frame.statusBar.setMessagePane(sqle.getMessage()); // Display
																	// error
																	// message
			}

			return dictionaryRatio;

		}

		public String executeBufferCacheSQL() {
			String query = "select trunc((1 - (cc/(aa+bb)))*100, 5)||'%  (more than 60-70%)' "
					+ " as buffercache from buffer_cache";
			String bufferRatio = "xxx";

			try {
				frame.statement.executeUpdate("create table buffer_cache "
						+ "( aa   number(10), " + "bb   number(10), "
						+ "cc   number(10))");
				frame.statement
						.executeUpdate("insert into buffer_cache (aa) select value from v$sysstat "
								+ "where name ='db block gets'");
				frame.statement
						.executeUpdate("update buffer_cache set bb = (select value from v$sysstat "
								+ "where name ='consistent gets')");
				frame.statement
						.executeUpdate("update buffer_cache set cc = (select value from v$sysstat "
								+ "where name ='physical reads')");
				ResultSet r = frame.statement.executeQuery(query);
				while (r.next()) {
					bufferRatio = r.getString("buffercache");
				}
				frame.statement.executeUpdate("drop table buffer_cache");
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(this, sqle.getMessage());
				frame.statusBar.setMessagePane(sqle.getMessage()); // Display
																	// error
																	// message
			}

			return bufferRatio;

		}

		public void executeSortSQL() {
			String query = "select value from v$sysstat "
					+ "where name in ('sorts (memory)', 'sorts (disk)')";
			int j = 0;

			try {
				ResultSet r = frame.statement.executeQuery(query);
				while (r.next()) {
					rs[j] = r.getString("value");
					j++;
				}
			} catch (SQLException sqle) {
				frame.statusBar.setMessagePane(sqle.getMessage()); // Display
																	// error
																	// message
			}

		}

		public String executeRedoLogSQL() {
			String query = "select value as redologrequest "
					+ "from v$sysstat "
					+ "where name = 'redo log space requests'";
			String redoLogReq = "xxx";

			try {
				ResultSet r = frame.statement.executeQuery(query);
				while (r.next()) {
					redoLogReq = r.getString("redologrequest");
				}
			} catch (SQLException sqle) {
				frame.statusBar.setMessagePane(sqle.getMessage()); // Display
																	// error
																	// message
			}

			return redoLogReq;

		}

		JPanel createPane(int i, String s) {
			JPanel p = new JPanel();
			p.add(new JLabel(s));

			switch (i) {
			case 0:
				p.add(new JLabel("Library Cache Miss Ratio is " + result));
				p.add(new JLabel(
						"Libary cache miss ratio is good if it is less than 1~2%"));
				p.add(new JLabel(
						"Increase shared_pool_size value if it is more than 2%"));
				p.add(new JLabel(
						"If shared_pool_size value is too small, it will affect performance"));
				p.add(new JLabel("SGA should be less than 50% of OS memory"));
				break;
			case 1:
				p.add(new JLabel("Dictionary Cache Miss Ratio is " + result));
				break;
			case 2:
				p.add(new JLabel("Buffer Cache Hit Ratio is " + result));
				break;
			case 3:
				p.add(new JLabel("Memory Sort Count : " + rs[0] + ", "));
				p.add(new JLabel("Disk Sort Count : " + rs[1]));
				break;
			case 4:
				p.add(new JLabel("Redo Log Request Count is " + result));
				break;
			}
			return p;
		}
	}

	public void createWindowMenu() {
		JMenu tmpwindowMenu = menuBar.getMenu(4);
		WindowJMenuItem jMenuItem = null;
		MyInternalFrame tmp = null;
		tmpwindowMenu.removeAll();
		int size = myInternalFrameList.size();
		tmpwindowMenu.add(nextWindowItem);
		tmpwindowMenu.addSeparator();
		if (size > 0) {
			for (int x = 0; x < size; x++) {
				tmp = (MyInternalFrame) myInternalFrameList.get(x);
				tmp.setFrameIndex(x);
				jMenuItem = new WindowJMenuItem(tmp.getConnectionName());
				tmpwindowMenu.add(jMenuItem);
				if (x + 1 < 10) {
					jMenuItem.setAccelerator(KeyStroke.getKeyStroke(String
							.valueOf(x + 1).charAt(0), Event.CTRL_MASK));
				}
				jMenuItem.addActionListener(this);
				jMenuItem.setWindowIndex(x);
			}
		}
		tmpwindowMenu.addSeparator();
		tmpwindowMenu.add(scriptRunnerItem);
		tmpwindowMenu.add(preferencesItem);
	}

	/**
	 * @return
	 */
	public Vector<Container> getMyInternalFrameList() {
		return myInternalFrameList;
	}

	/**
	 * @return
	 */
	public Map<String, ConnectionInformation> getConnectionInfoByKey() {
		return connectionInfoByKey;
	}

	/**
	 * @return Returns the config.
	 */
	public Config getConfig() {
		return config;
	}

	/**
	 * @return Returns the props.
	 */
	public Preferences getpropertiyFrame() {
		if (propertiyFrame == null) {
			propertiyFrame = new Preferences();
		}
		return propertiyFrame;
	}
}
