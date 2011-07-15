package com.pk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

class TableBrowse extends JFrame implements ActionListener, TreeSelectionListener {

	private static final long serialVersionUID = -974168039259650580L;

	public TableBrowse(MyInternalFrame mif, Connection connection, String userID) {
		super("Table Browser");

		this.mif = mif;
		this.connection = connection;
		this.userID = userID;

		setBounds(0, 0, 400, 400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowHandler());

		// Add message area
		status.setText("Enter a database URL and/or press Enter");
		status.setEditable(false); // No user input
		status.setLineWrap(true); // Lines wrap
		status.setWrapStyleWord(true); // on word boundaries
		status.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		getContentPane().add(status, BorderLayout.SOUTH);

		// Create tree to go in left split pane
		dbNode = new DefaultMutableTreeNode("No database");
		dbTreeModel = new DefaultTreeModel(dbNode);
		dbTree = new JTree(dbTreeModel);
		treePane = new JScrollPane(dbTree);
		treePane.setBorder(BorderFactory.createLineBorder(Color.darkGray));

		// Create table to go in right split pane
		tableModel = new ResultsModel();
		JTable table = new JTable(tableModel);
		// table.setPreferredSize(new Dimension(800,800)); // Table sizing...
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane = new JScrollPane(table);
		tablePane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		tablePane.setBorder(BorderFactory.createLineBorder(Color.darkGray));

		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				true, // Continuous relayout
				treePane, // Left pane content
				tablePane); // Right pane content
		getContentPane().add(splitpane, BorderLayout.CENTER);
		splitpane.setDividerLocation(150); // Left pane 150 pixels wide

		// Add event listeners
		database.addActionListener(this);
		userIDInput.addActionListener(this);
		passwordInput.addActionListener(this);
		dbTree.addTreeSelectionListener(this);

		pack();

		setVisible(true); // Set window visible
	}

	private String userID = null;
	private String password = null;
	private String url = null;

	MyInternalFrame mif;
	Connection connection;
	Statement statement;

	private JTextField database = new JTextField(url);
	private JTextField userIDInput = new JTextField(userID);
	private JPasswordField passwordInput = new JPasswordField(password);
	private JTextArea status = new JTextArea(3, 30);

	private DefaultMutableTreeNode dbNode; // Root node for the database tree
	private DefaultTreeModel dbTreeModel; // Model for the database metadata
	private JTree dbTree; // Tree to display the metadata
	private JScrollPane treePane; // Scroll pane holding the tree

	ResultsModel tableModel; // Model for table

	JScrollPane tablePane; // Scroll pane holding the table

	// private String[] drivers = { // Oracle driver
	// "sun.jdbc.odbc.JdbcOdbcDriver", // ODBC bridge
	// "com.imaginary.sql.msql.MsqlDriver" // mSQL driver
	// };

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource(); // Get source of the event
		if (source == database || // If its URL input,
				source == userIDInput || // or userID input,
				source == passwordInput) // or password input,
		{ // we will try for a connection
			url = database.getText(); // Get database URL
			userID = userIDInput.getText(); // Get user ID

			char[] pw = passwordInput.getPassword(); // Get password
			if (pw != null)
				password = new String(pw);

			if (url == null || url.length() == 0) {
				status.setText("Please specify a database URL ");
				return;
			}
			openConnection();
			password = null; // For security
		}
	}

	public void openConnection() {
		try {
			status.setText("Database connection established");

			statement = connection.createStatement(); // Create statement for
														// query

			dbNode = new DefaultMutableTreeNode(url); // Root node is URL
			dbTreeModel.setRoot(dbNode); // Set root in model
			setupTree(connection.getMetaData()); // Set up tree with metadata

			treePane.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.darkGray),
					// url,
					userID.toUpperCase(), TitledBorder.CENTER,
					TitledBorder.DEFAULT_POSITION));
			dbTree.setRootVisible(false); // Now show the root node
			dbTreeModel.reload(); // Get the tree redisplayed

		} catch (SQLException sqle) {
			status.setText(sqle.getMessage()); // Display first message
			do // loop through exceptions
			{
				System.err.println("Exception occurred:\nMessage: "
						+ sqle.getMessage());
				System.err.println("SQL state: " + sqle.getSQLState());
				System.err.println("Vendor code: " + sqle.getErrorCode()
						+ "\n----------------");
			} while ((sqle = sqle.getNextException()) != null);
		}
	}

	private void setupTree(DatabaseMetaData metadata) throws SQLException {
		String[] tableTypes = { "TABLE" }; // We want only tables

		String catalog = connection.getCatalog();
		ResultSet tables = metadata.getTables(// Get the tables info
				catalog, userID.toUpperCase(), null, tableTypes);

		String tableName; // Stores a table name
		DefaultMutableTreeNode tableNode; // Stores a tree node for a table
		while (tables.next()) // For each table
		{
			tableName = tables.getString(3); // get the table name
			tableNode = new DefaultMutableTreeNode(tableName);
			dbNode.add(tableNode); // Add the node to the tree
		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		TreePath[] paths = dbTree.getSelectionPaths();
		if (paths == null)
			return;

		boolean tableSelected = false; // Set true if a table is selected
		String column; // Stores a column name from a path
		String table; // Stores a table name from a path
		String columnsParam = null; // Column names in SQL SELECT
		String tableParam = null; // Table name in SQL SELECT
		String message = null; // Message for status area
		for (int j = 0; j < paths.length; j++) {
			switch (paths[j].getPathCount()) {
			case 2: // We have a table selected
				tableParam = (String) (((DefaultMutableTreeNode) (paths[j]
						.getPathComponent(1))).getUserObject());
				columnsParam = "*"; // Select all columns
				tableSelected = true; // Set flag for a table selected
				message = "Complete " + tableParam + " table displayed";
				break;

			case 3: // Column selected
				table = (String) (((DefaultMutableTreeNode) (paths[j]
						.getPathComponent(1))).getUserObject());
				if (tableParam == null)
					tableParam = table;

				else if (tableParam != table)
					break;
				column = (String) (((DefaultMutableTreeNode) (paths[j]
						.getPathComponent(2))).getUserObject());
				if (columnsParam == null) // If no previous columns
					columnsParam = column; // add the column
				else
					// otherwise
					columnsParam += "," + column; // we need a comma too
				message = columnsParam + " displayed from " + tableParam
						+ " table.";
				break;
			}
			if (tableSelected) // If a table was selected
				break; // we are done
		}
		try {
			ConnectionInformation tmpConnectionInformation = mif
					.getConnectionInformation();
			// tableModel.setResultSet(statement.executeQuery("SELECT COLUMN_NAME, DECODE(NULLABLE,'N', 'NOT NULL', 'Y', NULL)  AS NULLABLE, "
			// +
			// "DATA_TYPE, DECODE(DATA_TYPE, 'VARCHAR2', TO_CHAR(DATA_LENGTH), 'NUMBER', "
			// +
			// "DECODE(DATA_SCALE,0,TO_CHAR(DATA_PRECISION),NULL,NULL,DATA_PRECISION||','||DATA_SCALE)) AS \"SIZE\" "
			// + "FROM USER_TAB_COLUMNS " + "WHERE TABLE_NAME = '" + tableParam
			// + "'" + " ORDER BY COLUMN_ID"));
			tableModel.setResultSet(statement
					.executeQuery(tmpConnectionInformation.getDatabaseDialect()
							.getTableInfoSQLString(tableParam,
									tmpConnectionInformation)));
			tablePane.setBorder(BorderFactory.createTitledBorder(
					BorderFactory.createLineBorder(Color.darkGray), tableParam,
					TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
		} catch (SQLException sqle) {
			message = "Selection event Error\n" + sqle.getMessage();
			System.err.println(message);
		}
		if (message != null)
			status.setText(message);
	}

	// Inner class defining handler for window events
	class WindowHandler extends WindowAdapter {
		public void windowOpened(WindowEvent e) {
			openConnection();
		}

		// Handler for window closing event
		public void windowClosing(WindowEvent e) {
			mif.tableBrowseFlag = 0;
		}
	}
}
