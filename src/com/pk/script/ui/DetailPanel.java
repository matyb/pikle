/*
 * Created on Sep 30, 2004
 *
 * @author ctaylor
 * 
 */
package com.pk.script.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.pk.CodeDocument;
import com.pk.script.ScriptCommand;

/**
 * @author ctaylor
 * 
 */
public class DetailPanel extends JPanel {
	private static final long serialVersionUID = -7157415547408675119L;
	private JTextArea commandText;
	private JTextField statusField;
	private JButton applyButton;
	private JButton showErrorButton;
	private JButton removeCommandButton;
	private ActionListener actionListener;
	private JCheckBox runCheckBox;
	private ScriptCommand selectedScriptCommand = null;

	public DetailPanel(ActionListener argActionListener, List<String> argKeywords) {
		actionListener = argActionListener;
		if (argKeywords != null) {
			CodeDocument codeDocument = new CodeDocument();
			codeDocument.setKeywords(argKeywords);
			commandText = new JTextArea(codeDocument, "", 12, 45);
		} else {
			commandText = new JTextArea("", 12, 45);
		}
		JScrollPane sqlScrollPane = new JScrollPane(commandText);
		runCheckBox = new JCheckBox("Run");
		applyButton = new JButton("Apply");
		applyButton
				.setToolTipText("Applies changes to currently selected command");
		applyButton.addActionListener(actionListener);
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(6, 15, 6, 15);
		statusField = new JTextField(10);
		statusField.setEditable(false);
		statusField.setBorder(BorderFactory.createLineBorder(Color.black));

		gbc.gridx = 0;
		gbc.gridy = 0;
		this.add(sqlScrollPane, gbc);

		gbc.gridx = 1;

		JPanel panel = new JPanel();
		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		panel.add(runCheckBox);
		panel.add(new JLabel(" "));
		panel.add(statusField);
		panel.add(new JLabel(" "));
		showErrorButton = new JButton("Show Error");
		showErrorButton.setEnabled(false);
		showErrorButton.addActionListener(actionListener);
		panel.add(showErrorButton);
		panel.add(new JLabel(" "));
		removeCommandButton = new JButton("Remove");
		removeCommandButton.addActionListener(actionListener);
		panel.add(removeCommandButton);
		panel.add(new JLabel(" "));
		panel.add(applyButton);
		this.add(panel, gbc);
		gbc.gridx = 2;
		gbc.gridy = 0;
		// this.add(applyButton,gbc);
		this.setBorder(BorderFactory.createLineBorder(Color.black));

	}

	/**
	 * @return Returns the applyButton.
	 */
	public JButton getApplyButton() {
		return applyButton;
	}

	/**
	 * @return Returns the commandText.
	 */
	public JTextArea getCommandText() {
		return commandText;
	}

	/**
	 * @return Returns the statusField.
	 */
	public JTextField getStatusField() {
		return statusField;
	}

	/**
	 * @return Returns the showErrorButton.
	 */
	public JButton getShowErrorButton() {
		return showErrorButton;
	}

	/**
	 * @return Returns the selectedScriptCommand.
	 */
	public ScriptCommand getSelectedScriptCommand() {
		return selectedScriptCommand;
	}

	/**
	 * @param selectedScriptCommand
	 *            The selectedScriptCommand to set.
	 */
	public void setSelectedScriptCommand(ScriptCommand argSelectedScriptCommand) {
		if (argSelectedScriptCommand == null) {
			commandText.setText("");
			statusField.setText("");
			runCheckBox.setSelected(false);
			showErrorButton.setEnabled(false);

		} else {
			commandText.setText(argSelectedScriptCommand.getCommand());
			statusField.setText(argSelectedScriptCommand.getStatus());
			runCheckBox.setSelected(argSelectedScriptCommand.isRun());
			if (argSelectedScriptCommand.getErrorMessage() != null) {
				showErrorButton.setEnabled(true);
			} else {
				showErrorButton.setEnabled(false);
			}
		}
		this.selectedScriptCommand = argSelectedScriptCommand;
	}

	/**
	 * @return Returns the runCheckBox.
	 */
	public JCheckBox getRunCheckBox() {
		return runCheckBox;
	}

	/**
	 * @return Returns the removeCommand.
	 */
	public JButton getRemoveCommandButton() {
		return removeCommandButton;
	}

	/**
	 * @param removeCommand
	 *            The removeCommand to set.
	 */
	public void setRemoveCommandButton(JButton removeCommand) {
		this.removeCommandButton = removeCommand;
	}

	public void setKeywords(List<String> argKeywords) {
		if (argKeywords != null) {
			CodeDocument codeDocument = new CodeDocument();
			codeDocument.setKeywords(argKeywords);
			commandText.setDocument(codeDocument);
		}
	}
}
