/*
 * Created on Sep 29, 2004
 *
 * @author ctaylor
 * 
 */
package com.pk.script.ui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.pk.script.ScriptCommand;

/**
 * @author ctaylor
 * 
 */
public class CommandTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 9135226937966427291L;
	private String columnNames[] = { "Command", "Status", "Run" };
	private CommandTablePanel commandTablePanel;

	public CommandTableModel(CommandTablePanel argCommandTablePanel) {
		super();
		commandTablePanel = argCommandTablePanel;
	}

	public int getColumnCount() {
		return columnNames.length;
	}
 
	public int getRowCount() {
		if (getCommands() != null) {
			return getCommands().size();
		}
		return 0;
	}

	public Object getValueAt(int row, int column) {
		ScriptCommand scriptCommand = (ScriptCommand) getCommands().get(row);
		if (column == 0) {
			return scriptCommand.getDisplayCommand();
		} else if (column == 1) {
			return scriptCommand.getStatus();
		} else {
			if (scriptCommand.isRun())
				return "Yes";
			return "No";
		}
	}

	public String getColumnName(int arg0) {
		return columnNames[arg0];
	}

	public List<ScriptCommand> getCommands() {
		return commandTablePanel.getCommands();
	}
}
