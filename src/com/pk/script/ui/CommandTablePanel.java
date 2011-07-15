/*
 * Created on Sep 29, 2004
 *
 * @author ctaylor
 * 
 */
package com.pk.script.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import com.pk.script.Script;
import com.pk.script.ScriptCommand;

/**
 * @author ctaylor
 * 
 */
public class CommandTablePanel extends JPanel {
	/**
     * 
     */
	private static final long serialVersionUID = 405442575432649568L;
	private JTable commandTable;
	private Script script;

	/**
     * 
     */
	public CommandTablePanel(ActionListener argActionListener) {
		this.setSize(500, 300);
		commandTable = new JTable(new CommandTableModel(this));
		commandTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		commandTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		commandTable.setSize(new Dimension(500, 500));
		commandTable.getColumnModel().getColumn(0).setPreferredWidth(500);
		commandTable.getSelectionModel().addListSelectionListener(
				(ListSelectionListener) argActionListener);
		JScrollPane scrollPane = new JScrollPane(commandTable);
		commandTable
				.setPreferredScrollableViewportSize(new Dimension(650, 300));

		this.add(scrollPane);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}

	/**
	 * @return Returns the commands.
	 */
	public List<ScriptCommand> getCommands() {
		return getScript().getCommands();
	}

	/**
	 * @return Returns the script.
	 */
	public Script getScript() {
		if (script == null) {
			script = new Script();
		}
		return script;
	}

	/**
	 * @param script
	 *            The script to set.
	 */
	public void setScript(Script script) {
		this.script = script;
	}

	/**
	 * @return Returns the commandTable.
	 */
	public JTable getCommandTable() {
		return commandTable;
	}
}
