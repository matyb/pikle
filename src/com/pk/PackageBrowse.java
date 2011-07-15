package com.pk;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import java.sql.*;

class PackageBrowse extends JFrame implements ActionListener, TreeSelectionListener {

	private static final long serialVersionUID = 2959819232816106291L;

	public PackageBrowse(MyInternalFrame mif, Connection connection, String userID) {
		super("Package Browser");

		this.mif = mif;
		this.connection = connection;
		this.userID = userID;

		setBounds(0, 0, 400, 400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(new WindowHandler());

		status.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		getContentPane().add(status, BorderLayout.SOUTH);

		// Create tree to go in left split pane
		dbNode = new DefaultMutableTreeNode("No database");
		dbTreeModel = new DefaultTreeModel(dbNode);
		dbTree = new JTree(dbTreeModel);
		treePane = new JScrollPane(dbTree);
		treePane.setBorder(BorderFactory.createLineBorder(Color.darkGray));

		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setShowGrid(false);
		table.setFont(new Font("Default", Font.BOLD, 12));
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tablePane = new JScrollPane(table);
		tablePane.setBorder(BorderFactory.createLineBorder(Color.darkGray));

		JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				true, // Continuous relayout
				treePane, // Left pane content
				tablePane); // Right pane content
		getContentPane().add(splitpane, BorderLayout.CENTER);
		splitpane.setDividerLocation(150); // Left pane 150 pixels wide

		dbTree.addTreeSelectionListener(this);

		pack();

		setVisible(true); // Set window visible
	}

	private String userID = null;

	private String url = null;

	MyInternalFrame mif;
	Connection connection;
	Statement statement;

	// private String[] drivers = { // Oracle driver
	// "sun.jdbc.odbc.JdbcOdbcDriver", // ODBC bridge
	// "com.imaginary.sql.msql.MsqlDriver" // mSQL driver
	// };

	public void actionPerformed(ActionEvent e) {
		// Object source = e.getSource(); // Get source of the event
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
			dbTree.setRootVisible(true); // Now show the root node
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
		String[] tableTypes = { "PACKAGE" }; // We want only tables

		String catalog = connection.getCatalog();
		ResultSet tables = metadata.getTables(// Get the tables info
				catalog, userID.toUpperCase(), null, tableTypes);

		String tableName; // Stores a table name
		DefaultMutableTreeNode tableNode; // Stores a tree node for a table
		while (tables.next()) // For each table
		{
			// tableName = tables.getString("TABLE_NAME"); // get the table name
			tableName = tables.getString(3); // get the table name
			tableNode = new DefaultMutableTreeNode(tableName);
			dbNode.add(tableNode); // Add the node to the tree

			// Get all the columns for the current table
			// ResultSet columnNames = metadata.getColumns(null, null,
			// tableName, null);
			ResultSet columnNames = statement
					.executeQuery("SELECT OBJECT_NAME FROM USER_OBJECTS "
							+ "WHERE OBJECT_NAME = '" + tableName + "' "
							+ "AND  OBJECT_TYPE = 'PACKAGE BODY'");

			// System.out.println("User ID is "+userID+"\n");

			// Add nodes for the columns as children of the table node
			while (columnNames.next())
				tableNode.add(new DefaultMutableTreeNode(columnNames
						.getString("OBJECT_NAME")));

		}
	}

	public void valueChanged(TreeSelectionEvent e) {
		TreePath[] paths = dbTree.getSelectionPaths();
		if (paths == null)
			return;

		boolean tableSelected = false; // Set true if a table is selected
		String column; // Stores a column name from a path
		String tableName; // Stores a table name from a path
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
				message = "Complete " + tableParam + " package displayed";
				break;

			case 3: // Column selected
				tableName = (String) (((DefaultMutableTreeNode) (paths[j]
						.getPathComponent(1))).getUserObject());
				if (tableParam == null)
					tableParam = tableName;

				else if (tableParam != tableName)
					break;
				column = (String) (((DefaultMutableTreeNode) (paths[j]
						.getPathComponent(2))).getUserObject());
				if (columnsParam == null) // If no previous columns
					columnsParam = column; // add the column
				else
					// otherwise
					columnsParam += "," + column; // we need a comma too
				message = columnsParam + " displayed from " + tableParam
						+ " package.";
				break;
			}
			if (tableSelected) // If a table was selected
				break; // we are done
		}
		try {
			// Display the columns and change the scroll pane border
			if (tableSelected) {

				tableModel.setResultSet(statement.executeQuery(mif
						.getConnectionInformation()
						.getDatabaseDialect()
						.getPackageInfoSQLString(tableParam,
								mif.getConnectionInformation())));
				DefaultTableColumnModel tcm = (DefaultTableColumnModel) table
						.getColumnModel();
				TableColumn tc = tcm.getColumn(0);
				tc.setWidth(400);
				tc.setResizable(true);
			} else {
				tableModel
						.setResultSet(statement
								.executeQuery("SELECT DECODE(UPPER(TRIM(TEXT)),'END;',TEXT,SUBSTR(TEXT,1,LENGTH(TEXT)-1)) AS \"PACKAGE BODY SOURCE\" FROM USER_SOURCE "
										+ "WHERE TYPE='PACKAGE BODY' "
										+ "AND   NAME='"
										+ tableParam
										+ "'"
										+ "ORDER BY LINE"));
				DefaultTableColumnModel tcm = (DefaultTableColumnModel) table
						.getColumnModel();
				TableColumn tc = tcm.getColumn(0);
				tc.setWidth(400);
				tc.setResizable(true);
			}

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
			mif.packageBrowseFlag = 0;
		}
	}

	JTextArea status = new JTextArea(3, 30);

	DefaultMutableTreeNode dbNode; // Root node for the database tree
	DefaultTreeModel dbTreeModel; // Model for the database metadata
	JTree dbTree; // Tree to display the metadata
	JScrollPane treePane; // Scroll pane holding the tree

	JScrollPane tablePane; // Scroll pane holding the table
	ResultsModel tableModel = new ResultsModel();
	JTable table = new JTable(tableModel);

}
