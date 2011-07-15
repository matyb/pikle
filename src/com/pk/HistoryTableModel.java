/*
 * Created on Oct 31, 2004
 *
 * @author ctaylor
 * 
 */
package com.pk;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * @author ctaylor
 * 
 */
public class HistoryTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 7819323023821508187L;
	private List<String> statementHistory = new ArrayList<String>();

	public int getColumnCount() {
		return 1;
	}

	public int getRowCount() {
		if (statementHistory != null) {
			return statementHistory.size();
		}
		return 0;
	}

	public Object getValueAt(int row, int column) {
		return statementHistory.get(row);
	}

	public String getColumnName(int arg0) {
		return "SQL Statement";
	}

	/**
	 * @param statementHistory The statementHistory to set.
	 */
	public void setStatementHistory(List<String> statementHistory) {
		this.statementHistory = statementHistory;
	}
}
