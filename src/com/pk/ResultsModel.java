package com.pk;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

class ResultsModel extends AbstractTableModel {

	private static final long serialVersionUID = 6707027182873294943L;
	String[] columnNames = new String[0];
	List<String[]> dataRows; // Empty vector of rows

	ResultSet gResults;
	int gColumns;
	String[] gRowData;
	int arraySize = 100;
	int totalRowCount = 0;

	public void setResultSet(ResultSet results) {
		try {
			if (results == null) {
				gColumns = 0;
				totalRowCount = 0;
				columnNames = new String[0];
				fireTableChanged(null);
				return;
			}
			gResults = results;

			ResultSetMetaData metadata = results.getMetaData();

			int columns = metadata.getColumnCount(); // Get number of columns

			gColumns = columns;

			columnNames = new String[columns]; // Array to hold names
			totalRowCount = 0;

			// Get the column names
			for (int i = 0; i < columns; i++)
				columnNames[i] = metadata.getColumnLabel(i + 1);

			// Get all rows.
			dataRows = new Vector<String[]>(); // New Vector to store the data
			String[] rowData; // Stores one row
			while (results.next()) // For each row...
			{
				rowData = new String[columns]; // create array to hold the data
				for (int i = 0; i < columns; i++)
					// For each column
					rowData[i] = results.getString(i + 1); // retrieve the data
															// item

				dataRows.add(rowData); // Store the row in the vector
				totalRowCount++;
				if (results.getRow() == arraySize)
					break;
			}

			fireTableChanged(null); // Signal the table there is new model data
		} catch (SQLException sqle) {
			System.err.println(sqle);
		}

	}

	public void setNextResultSet(JTable table) {
		try {
			int lastRow = gResults.getRow() + arraySize;
			int[] colWidth = new int[gColumns];

			while (gResults.next()) // For each row...
			{
				gRowData = new String[gColumns]; // create array to hold the
													// data
				for (int i = 0; i < gColumns; i++)
					// For each column
					gRowData[i] = gResults.getString(i + 1); // retrieve the
																// data item

				dataRows.add(gRowData); // Store the row in the vector
				totalRowCount++;
				if (gResults.getRow() == lastRow)
					break;
			}

			for (int i = 0; i < gColumns; i++) {
				colWidth[i] = table.getColumnModel().getColumn(i).getWidth();
			}

			fireTableChanged(null);

			for (int i = 0; i < gColumns; i++) {
				table.getColumnModel().getColumn(i).setWidth(colWidth[i]);
			}
		} catch (SQLException sqle) {
			System.err.println(sqle);
		}

	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		// if(dataRows == null)
		// return 0;
		// else
		// return dataRows.size();
		return totalRowCount;
	}

	public Object getValueAt(int row, int column) {
		return ((String[]) (dataRows.get(row)))[column];
	}

	public String getColumnName(int column) {
		return columnNames[column] == null ? "No Name" : columnNames[column];
	}

}
