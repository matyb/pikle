package com.pk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class MyInternalFrame extends JInternalFrame implements
		InternalFrameListener, Constants {

	private static final long serialVersionUID = 9103843865329019956L;

	class ConnectionDialog extends JInternalFrame {
		private static final long serialVersionUID = -5200934680040088833L;
		protected boolean canceled;
		public MyInternalFrame jif;
		protected JTextField passwordField;
		protected ConnectionInformation tmpConnectionInformation;
		protected JComboBox urlField;

		protected JTextField useridField;

		public ConnectionDialog(MyInternalFrame f) {

			setTitle("Connect To A Database");
			jif = f;
			Toolkit dKit = getToolkit(); // Get the toolkit of window
			Dimension wSize = dKit.getScreenSize(); // Get the size of window
			setBounds(wSize.width / 2 - 200, wSize.height / 2 - 200, // location
					wSize.width / 2, wSize.height / 2); // size
			buildDialogLayout();
			setSize(300, 200);
		}

		protected boolean attemptConnection() {
			try {
				tmpConnectionInformation = new ConnectionInformation();
				userid = useridField.getText();
				ConnectionConfig tmpConnectionConfig = (ConnectionConfig) urlField
						.getSelectedItem();
				jif.setConnectionName(tmpConnectionConfig.getName());

				tmpConnectionInformation.setUrl(tmpConnectionConfig.getUrl());
				tmpConnectionInformation.setConnectionListKey(String
						.valueOf(System.currentTimeMillis()));
				tmpConnectionInformation.setPassword(passwordField.getText());
				tmpConnectionInformation.setUserID(useridField.getText());
				tmpConnectionInformation.setNumberOfWindows(1);
				tmpConnectionInformation.setDatabaseDialect(tmpConnectionConfig
						.getDatabaseDialect());
				setKeywords(tmpConnectionConfig.getDatabaseDialect()
						.getKeywords());
				connection = DriverManager.getConnection(
						tmpConnectionInformation.getUrl(),
						tmpConnectionInformation.getUserID(),
						tmpConnectionInformation.getPassword());
				connection.setAutoCommit(false);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error connecting to "
						+ "database: " + e.getMessage());
			}
			return false;
		}

		protected void buildDialogLayout() {
			JLabel label;
			urlField = new JComboBox();
			List<ConnectionConfig> connectionConfigs = jif.mainFrame.getConfig().getConnections();
			int size = connectionConfigs.size();
			for (int x = 0; x < size; x++) {
				urlField.addItem(connectionConfigs.get(x));
			}
			Container pane = getContentPane();
			pane.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.insets = new Insets(6, 15, 6, 15);

			gbc.gridx = 0;
			gbc.gridy = 0;
			label = new JLabel("Userid:", JLabel.LEFT);
			pane.add(label, gbc);

			gbc.gridy++;
			label = new JLabel("Password:", JLabel.LEFT);
			pane.add(label, gbc);

			gbc.gridy++;
			label = new JLabel("Url:", JLabel.LEFT);
			pane.add(label, gbc);

			gbc.gridy++;
			// label = new JLabel("Oracle:", JLabel.LEFT);

			gbc.gridx = 1;
			gbc.gridy = 0;

			useridField = new JTextField(10);
			pane.add(useridField, gbc);

			gbc.gridy++;
			passwordField = new JPasswordField(10);
			pane.add(passwordField, gbc);

			gbc.gridy++;
			// urlField = new JTextField("",15);

			pane.add(urlField, gbc);

			gbc.gridx = 0;
			gbc.gridy = 4;
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.anchor = GridBagConstraints.CENTER;
			pane.add(getButtonPanel(), gbc);
		}

		protected JPanel getButtonPanel() {
			JPanel panel = new JPanel();
			JButton btn1 = new JButton("Ok");
			getRootPane().setDefaultButton(btn1);
			btn1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					onDialogOk();
				}
			});
			panel.add(btn1);
			JButton btn2 = new JButton("Cancel");
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					onDialogCancel();
				}
			});
			panel.add(btn2);
			return panel;
		}

		public Connection getConnection() {
			setVisible(true);
			return connection;
		}

		protected void onDialogCancel() {
			if (openFrameCount <= 1) {
				System.exit(0);
			} else {
				setVisible(false);
				jif.getDesktopPane().remove(this);
			}
		}

		protected void onDialogOk() {

			if (attemptConnection()) {
				setVisible(false);

				try {
					statement = connection.createStatement();
				}

				catch (SQLException sqle) {
					JOptionPane.showMessageDialog(this, "Oops! " + sqle);
					System.exit(0);
				}

				jif.setTitle(userid.toUpperCase());

				statusBar.setUserPane(jif.getConnectionName());

				jif.setConncetionKey(tmpConnectionInformation
						.getConnectionListKey());

				mainFrame.getConnectionInfoByKey().put(
						tmpConnectionInformation.getConnectionListKey(),
						tmpConnectionInformation);

				jif.setPreferredSize(new java.awt.Dimension(700, 500));
				jif.pack();
				jif.setVisible(true);
				jif.getDesktopPane().remove(this);
				jif.sqlTextArea.requestFocus();
			}
		}

	}

	class ListenerScrollBar implements AdjustmentListener {
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if (jsb.getMaximum() != 0) {
				System.out.println(String.valueOf(jsb.getMaximum()));
				if ((jsb.getMaximum() - jsb.getVisibleAmount()) <= e.getValue())
				// if ((jsb.getMaximum() - 300) <= e.getValue())
				{
					model.setNextResultSet(table);

					setMyTableWidth();

					try {
						if (model.gResults.getRow() == 0
								&& table.getBackground() != Color.black) {
							statusBar.messagePane.setVisible(true);
							statusBar.setMessagePane(model.totalRowCount
									+ " row(s) selected.");
						}
					} catch (SQLException sqle) {
						System.err.println(sqle);
					}
				}
			}
		}
	}

	static int openFrameCount = 0;
	static final int xOffset = 30, yOffset = 30;
	// Connection connect;

	private int frameIndex = -1;
	ConnectionDialog argConnectionDialog;
	int commentFlag;
	String conncetionKey = null;

	Connection connection;
	private String connectionName;
	// String dbUrl;
	int index_of_sqls = 0;
	int indexBrowseFlag = 0;

	JPanel jPanel1 = new JPanel();
	JScrollBar jsb;
	int local_index_of_sqls = 0;
	PrettyKid mainFrame;
	ResultsModel model = new ResultsModel();

	String newWinUrl;
	int packageBrowseFlag = 0;
	JTextArea planArea = new JTextArea();
	int procedureBrowseFlag = 0;

	String queryWithComment;
	JTabbedPane resultTabbedPane = new JTabbedPane();

	List<String> sqls = new ArrayList<String>();

	JTextPane sqlTextArea = new JTextPane();
	Statement statement;

	StatusBar statusBar = new StatusBar();
	JTable table = new JTable(model);

	int tableBrowseFlag = 0;

	String userid;
	final UndoManager undo = new UndoManager();

	public MyInternalFrame(PrettyKid argMainFrame) {
		super("Document #" + (++openFrameCount), true, true, true, true);

		mainFrame = argMainFrame;

		argConnectionDialog = new ConnectionDialog(this);
		connection = argConnectionDialog.getConnection();

		argConnectionDialog.setLocation(250, 100);
		argMainFrame.desktop.add(argConnectionDialog);
		try {
			argConnectionDialog.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
		init(argMainFrame, "", "");

	}

	public MyInternalFrame(PrettyKid jf, Connection con, String user, String url) {
		super("Document #" + (++openFrameCount), true, true, true, true);

		init(jf, user, url);
		this.connection = con;
		try {
			statement = connection.createStatement();
		} catch (SQLException sqle) {
			System.err.println(sqle); // error connection to database
			JOptionPane.showMessageDialog(this, "Can not create statement : "
					+ sqle);
		}

		pack();
		setVisible(true);
	}

	private void init(PrettyKid mainFrame, String user, String url) {
		this.mainFrame = mainFrame;
		this.userid = user;
		this.newWinUrl = url;
		setConnectionName(url);

		sqlTextArea.setFont(new Font("Courier", Font.BOLD, 12));

		CodeDocument doc = new CodeDocument();

		// undo function
		initUndo(doc);
		sqlTextArea.setDocument(doc);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(sqlTextArea, null);

		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Use scrollbars

		JScrollPane resultsPane = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Create
																// scrollpane
																// for table

		jsb = resultsPane.getVerticalScrollBar();

		planArea.setEditable(false);
		planArea.setFont(new Font("Default", Font.BOLD, 12));
		JScrollPane planScrollPane = new JScrollPane(planArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		resultTabbedPane.addTab("ResultSet", resultsPane);
		resultTabbedPane.addTab("Query plan", planScrollPane);

		BorderLayout bdLayout = new BorderLayout();
		jPanel1.setLayout(bdLayout);
		getContentPane().add(jPanel1, BorderLayout.CENTER);

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
				scrollPane, resultTabbedPane);
		jPanel1.add(splitPane, BorderLayout.CENTER);
		splitPane.setDividerLocation(150);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.SOUTH);

		addInternalFrameListener(this);
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
			}
		});
		jsb.addAdjustmentListener(new ListenerScrollBar());

		setLocation(xOffset * openFrameCount, yOffset * openFrameCount);

		statusBar.elapsedTimePane.setVisible(false);
		statusBar.messagePane.setVisible(false);

		setPreferredSize(new java.awt.Dimension(600, 450));

		setTitle(userid.toUpperCase());

		statusBar.setUserPane(newWinUrl);

	}

	/**
	 * @param doc
	 */
	private void initUndo(CodeDocument doc) {
		doc.addUndoableEditListener(new UndoableEditListener() {
			public void undoableEditHappened(UndoableEditEvent evt) {
				undo.addEdit(evt.getEdit());
			}
		});

		sqlTextArea.getActionMap().put("Undo", new AbstractAction("Undo") {
			/**
                 * 
                 */
			private static final long serialVersionUID = 4956600639019775601L;

			public void actionPerformed(ActionEvent evt) {
				try {
					if (undo.canUndo()) {
						undo.undo();
					}
				} catch (CannotUndoException e) {
				}
			}
		});

		sqlTextArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"),
				"Undo");

		sqlTextArea.getActionMap().put("Redo", new AbstractAction("Redo") {
			/**
                 * 
                 */
			private static final long serialVersionUID = 237382599844729867L;

			public void actionPerformed(ActionEvent evt) {
				try {
					if (undo.canRedo()) {
						undo.redo();
					}
				} catch (CannotRedoException e) {
				}
			}
		});

		sqlTextArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"),
				"Redo");

	}

	public String commentHandling(String query) {
		query = query.trim();
		int cmtAstStart = query.indexOf("/*");
		int cmtDasStart = query.indexOf("--");

		if (cmtAstStart == 0) {
			int cmtEnd = query.indexOf("*/");
			StringBuffer queryStrBuffer = new StringBuffer(query);
			queryStrBuffer = queryStrBuffer.delete(cmtAstStart, cmtEnd + 2);
			query = queryStrBuffer.toString();
		}

		if (cmtDasStart == 0) {
			int cmtEnd = query.indexOf("\n", cmtDasStart);

			StringBuffer queryStrBuffer = new StringBuffer(query);
			queryStrBuffer = queryStrBuffer.delete(cmtDasStart, cmtEnd - 1);
			query = queryStrBuffer.toString();
		}
		return query;
	}

	public void commitTransaction() {
		// make sure that this connection is used with only one window.
		// If it has more than one window ask user to confirm commit.

		try {
			if (connection.getAutoCommit()) {
				JOptionPane
						.showMessageDialog(this,
								"This connection is in auto-commit mode and cannot be committed manually.");
				return;
			}

			ConnectionInformation tmpConnectionInformation = (ConnectionInformation) mainFrame
					.getConnectionInfoByKey().get(conncetionKey);
			if (tmpConnectionInformation.getNumberOfWindows() > 1) {
				int answer = JOptionPane
						.showConfirmDialog(
								this,
								"This connection is being used by more than one window. \nAre you sure that you want to commit?",
								"Warning", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.NO_OPTION) {
					return;
				}
			}

			connection.commit();
			JOptionPane.showMessageDialog(this, "Transaction committed");
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}

	}

	public void executeDescSQL(String argQuery) {

		ConnectionInformation tmpConnectionInformation = getConnectionInformation();
		final String queryDesc = tmpConnectionInformation.getDatabaseDialect()
				.getDescribeSQLString(argQuery, tmpConnectionInformation);
		if (queryDesc == null || queryDesc.equals("")) {
			// if this dialect does not support this feature must report to user
			// and exit method.
			featureNotAvailable("Describe feature", tmpConnectionInformation
					.getDatabaseDialect().getClass().getName(), this);
			return;
		}
		doHistory(argQuery);
		new Thread(){
			public void run(){
				int color = 0;
				try {
					if (table.getBackground() == Color.black)
						color = 1;
					table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Use scrollbars
					table.setForeground(Color.black);
					table.setBackground(Color.white);
					table.setFont(new Font("Default", Font.BOLD, 12));
					table.setShowGrid(true);
					long startTime = System.currentTimeMillis();
					model.setResultSet(statement.executeQuery(queryDesc));
					float qTime = System.currentTimeMillis() - startTime;
					qTime = qTime / 1000;

					setMyTableWidth();

					statusBar.elapsedTimePane.setVisible(true);
					statusBar.messagePane.setVisible(true);
					statusBar.setElapsedTimePane(qTime + " Sec.");
					statusBar.setMessagePane(model.getRowCount() + " row(s) selected.");
				} catch (SQLException sqle) {
					JOptionPane.showMessageDialog(MyInternalFrame.this, sqle.getMessage());
					statusBar.messagePane.setVisible(true);
					statusBar.setMessagePane(sqle.getMessage()); // Display error
																	// message
					if (color == 1) {
						table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
						DefaultTableColumnModel tcm = (DefaultTableColumnModel) table
								.getColumnModel();
						TableColumn tc = tcm.getColumn(0);
						table.setShowGrid(false);
						tc.setWidth(600);
						table.setForeground(Color.white);
						table.setBackground(Color.black);
						table.setFont(new Font("Default", Font.BOLD, 12));
						tc.setResizable(true);
					}
				}
			}
		}.start();
	}

	public void executePlanSQL() {

		String planOut = "";

		try {
			ConnectionInformation tmpConnectionInformation = getConnectionInformation();
			planOut = tmpConnectionInformation.getDatabaseDialect()
					.doExecutePlan(sqlTextArea.getText(),
							tmpConnectionInformation, connection);
			if (planOut == null || planOut.equals("")) {
				// if this dialect does not support this feature must report to
				// user and exit method.
				featureNotAvailable("Execution Plan", tmpConnectionInformation
						.getDatabaseDialect().getClass().getName(), this);
				return;
			}

			planArea.setText(planOut);

			resultTabbedPane.setSelectedIndex(1);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(this, sqle.getMessage());
			sqle.printStackTrace();
			statusBar.messagePane.setVisible(true);
			statusBar.setMessagePane(sqle.getMessage()); // Display error
															// message
		}
	}

	public void executeSQL(String tmpQuery) {

		resultTabbedPane.setSelectedIndex(0);

		doHistory(tmpQuery);
		final String query = commentHandling(tmpQuery);

		if (query == null) // If there's nothing we are done
			return;
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Use scrollbars
		table.setForeground(Color.black);
		table.setBackground(Color.white);
		table.setFont(new Font("Default", Font.BOLD, 12));
		table.setShowGrid(true);
		new Thread(){
			public void run(){
				int color = 0;
				try{
					if (table.getBackground() == Color.black){
						color = 1;
					}
					sqlTextArea.setCursor(Cursor
							.getPredefinedCursor(Cursor.WAIT_CURSOR));
					MyInternalFrame.this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					long startTime = System.currentTimeMillis();
					model.setResultSet(statement.executeQuery(query));
					float qTime = System.currentTimeMillis() - startTime;
					MyInternalFrame.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					sqlTextArea.setCursor(Cursor
							.getPredefinedCursor(Cursor.TEXT_CURSOR));
		
					setMyTableWidth();
		
					qTime = qTime / 1000;
					statusBar.elapsedTimePane.setVisible(true);
					statusBar.setElapsedTimePane(qTime + " Sec.");
					if (model.totalRowCount < 100) {
						statusBar.messagePane.setVisible(true);
						statusBar.setMessagePane(model.getRowCount()
								+ " row(s) selected.");
					} else {
						statusBar.messagePane.setVisible(false);
					}
				} catch (Exception sqle) {
					JOptionPane.showMessageDialog(MyInternalFrame.this, sqle.getMessage());
					statusBar.messagePane.setVisible(true);
					statusBar.setMessagePane(sqle.getMessage()); // Display error
																	// message
					MyInternalFrame.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					sqlTextArea.setCursor(Cursor
							.getPredefinedCursor(Cursor.TEXT_CURSOR));
					if (color == 1) {
						table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						DefaultTableColumnModel tcm = (DefaultTableColumnModel) table
								.getColumnModel();
						TableColumn tc = tcm.getColumn(0);
						table.setShowGrid(false);
						table.setCellSelectionEnabled(true);
						tc.setWidth(600);
						table.setForeground(Color.white);
						table.setBackground(Color.black);
						table.setFont(new Font("Default", Font.BOLD, 12));
						tc.setResizable(true);
					}
				}
			}
		}.start();
	}

	/**
	 * @return
	 */
	public String getConncetionKey() {
		return conncetionKey;
	}

	/**
	 * @return
	 */
	public String getConnectionName() {
		return connectionName;
	}

	/**
	 * @return
	 */
	public List<String> getSqls() {
		return sqls;
	}

	public void internalFrameActivated(InternalFrameEvent e) {
		sqlTextArea.requestFocus();
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		ConnectionInformation tmpConnectionInformation = (ConnectionInformation) mainFrame
				.getConnectionInfoByKey().get(conncetionKey);

		if (tmpConnectionInformation.getNumberOfWindows() == 1) {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else {
			tmpConnectionInformation
					.setNumberOfWindows(tmpConnectionInformation
							.getNumberOfWindows() - 1);
			mainFrame.getConnectionInfoByKey().put(conncetionKey,
					tmpConnectionInformation);
		}
		openFrameCount--;
	}

	public void internalFrameClosing(InternalFrameEvent e) {
		mainFrame.getMyInternalFrameList().remove(this);
		mainFrame.createWindowMenu();
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
	}

	public void internalFrameIconified(InternalFrameEvent e) {
	}

	public void internalFrameOpened(InternalFrameEvent e) {
		mainFrame.getMyInternalFrameList().add(this);
		mainFrame.createWindowMenu();
		sqlTextArea.transferFocus();
	}

	public void prexecuteSQL() {
		DatabaseDialect tmpDatabaseDialect = getConnectionInformation()
				.getDatabaseDialect();

		String query = sqlTextArea.getSelectedText();
		if (query == null || query.length() <= 0) {
			query = sqlTextArea.getText();
		}

		query = commentHandling(query);

		query = query.trim();

		if (tmpDatabaseDialect.isSelectStatement(query)) {
			executeSQL(query);
		} else if (tmpDatabaseDialect.isDescribeStatement(query)) {
			executeDescSQL(query);
		} else if (query.equalsIgnoreCase("COMMIT")) {
			commitTransaction();
		} else if (query.equalsIgnoreCase("ROLLBACK")) {
			rollBackTransaction();
		} else {
			updateSQL(query);
		}
	}

	public void rollBackTransaction() {
		// make sure that this connection is used with only one window.
		// If it has more than one window ask user to confirm rollback.

		try {
			if (connection.getAutoCommit()) {
				JOptionPane
						.showMessageDialog(this,
								"This connection is in auto-commit mode and cannot be rolled back.");
				return;
			}

			ConnectionInformation tmpConnectionInformation = (ConnectionInformation) mainFrame
					.getConnectionInfoByKey().get(conncetionKey);
			if (tmpConnectionInformation.getNumberOfWindows() > 1) {
				int answer = JOptionPane
						.showConfirmDialog(
								this,
								"This connection is being used by more than one window. \nAre you sure that you want to rollback?",
								"Warning", JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE);
				if (answer == JOptionPane.NO_OPTION) {
					return;
				}
			}

			connection.rollback();
			JOptionPane.showMessageDialog(this, "Transaction Rolled back",
					"Information", JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}

	}

	/**
	 * @param string
	 */
	public void setConncetionKey(String string) {
		conncetionKey = string;
	}

	/**
	 * @param string
	 */
	public void setConnectionName(String string) {
		connectionName = string;
	}

	public void setMyTableWidth() {
		int tableWidth = 0;
		FontMetrics metrics = table.getFontMetrics(table.getFont());
		for (int i = 0; i < table.getColumnCount(); ++i) {
			int width = 0;
			int maxWidth = 0;
			List<String> data = new Vector<String>();
			data.add(table.getColumnName(i));
			for (int j = 0; j < table.getRowCount(); ++j) {
				if (table.getValueAt(j, i) == null) {
					data.add(" ");
				} else {
					data.add(table.getValueAt(j, i).toString());
				}
			}
			for (String sWidth : data) {
				if ((width = metrics.stringWidth(sWidth)) > maxWidth) {
					maxWidth = width + 30;
				}
			}
			Insets insets = ((JComponent) table.getCellRenderer(0, i))
					.getInsets();
			maxWidth += insets.left + insets.right;
			table.getColumnModel().getColumn(i).setPreferredWidth(maxWidth);
			tableWidth += maxWidth;
		}
		Dimension d = table.getSize();
		d.width = tableWidth;
		table.setSize(d);
		table.setCellSelectionEnabled(true);

	}

	public void setSqlTextArea(String s) {
		sqlTextArea.setText(s);
	}

	public void updateSQL(final String query) {

		final int[] rowsHandled = new int[0];

		doHistory(query);

		if (query == null) // If there's nothing we are done
			return;
		new Thread(){
			public void run(){
				try{
					long startTime = System.currentTimeMillis();
					rowsHandled[0] = statement.executeUpdate(query);
					model.setResultSet(null);
					float qTime = System.currentTimeMillis() - startTime;
					qTime = qTime / 1000;
					String tmpQuery = query.trim();
	
					statusBar.elapsedTimePane.setVisible(true);
					statusBar.messagePane.setVisible(true);
	
					if (tmpQuery.toUpperCase().startsWith("INSERT")) {
						statusBar.setElapsedTimePane(qTime + " Sec.");
						statusBar.setMessagePane(rowsHandled + " row(s) inserted.");
					} else if (tmpQuery.toUpperCase().startsWith("UPDATE")) {
						statusBar.setElapsedTimePane(qTime + " Sec.");
						statusBar.setMessagePane(rowsHandled + " row(s) updated.");
					} else if (tmpQuery.toUpperCase().startsWith("DELETE")) {
						statusBar.setElapsedTimePane(qTime + " Sec.");
						statusBar.setMessagePane(rowsHandled + " row(s) deleted.");
					} else {
						statusBar.setElapsedTimePane(qTime + " Sec.");
						statusBar.setMessagePane("Executed successfully ");
					}
				}catch (SQLException sqle) {
					JOptionPane.showMessageDialog(MyInternalFrame.this, sqle.getMessage());
					statusBar.messagePane.setVisible(true);
					statusBar.setMessagePane(sqle.getMessage()); // Display error message
				}
			}
		}.start();
	}

	public ConnectionInformation getConnectionInformation() {
		return (ConnectionInformation) mainFrame.getConnectionInfoByKey().get(
				conncetionKey);
	}

	public void setKeywords(List<String> argKeywords) {
		Document tmpDocument = sqlTextArea.getDocument();
		if (tmpDocument != null && tmpDocument instanceof CodeDocument) {
			((CodeDocument) tmpDocument).setKeywords(argKeywords);
		}
	}

	public void featureNotAvailable(String argfeatureName, String argDialect,
			Component argComponent) {
		Object tmp[] = { argfeatureName, argDialect };
		String msg = MessageFormat.format(
				"The {0} feature is not available for database dialect {1}.",
				tmp);
		JOptionPane.showMessageDialog(argComponent, msg,
				"Feature Not Available", JOptionPane.INFORMATION_MESSAGE);
	}

	public void undoLastEdit() {
		try {
			if (undo.canUndo()) {
				undo.undo();
			}
		} catch (CannotUndoException e) {
		}
	}

	public void redoLastEdit() {
		try {
			if (undo.canRedo()) {
				undo.redo();
			}
		} catch (CannotRedoException e) {
		}
	}

	/**
	 * @return Returns the frameIndex.
	 */
	public int getFrameIndex() {
		return frameIndex;
	}

	/**
	 * @param frameIndex
	 *            The frameIndex to set.
	 */
	public void setFrameIndex(int frameIndex) {
		this.frameIndex = frameIndex;
	}

	private void doHistory(String argQuery) {
		// Check to see if this is the first sql run.
		if (index_of_sqls != 0) {
			int historyMode = mainFrame.getConfig().getHistoryMode();
			if (historyMode == Config.HISTORY_MODE_UNIQUE) {
				// See if this query is already in the history
				int size = sqls.size();
				for (int x = 0; x < size; x++) {
					if (((String) sqls.get(x)).equals(argQuery)) {
						// if already in history do not add it again.
						return;
					}

				}
			} else if (historyMode == Config.HISTORY_MODE_DIFF_FROM_LAST) {
				if (((String) sqls.get(index_of_sqls - 1)).equals(argQuery)) {
					return;
				}
			}
		}
		if (this.index_of_sqls == 29) {
			sqls.remove(0);
			sqls.add((index_of_sqls - 1), argQuery);
		} else {
			sqls.add(index_of_sqls, argQuery);
			this.index_of_sqls++;
		}
		this.local_index_of_sqls = this.index_of_sqls - 1;
	}
}
