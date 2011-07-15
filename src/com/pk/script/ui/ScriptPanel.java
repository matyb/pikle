/*
 * Created on Sep 27, 2004
 *
 * @author ctaylor
 */
package com.pk.script.ui;

import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.pk.Config;
import com.pk.ConnectionDialog;
import com.pk.script.Script;
import com.pk.script.ScriptCommand;

/**
 * @author ctaylor
 * 
 */
public class ScriptPanel extends JPanel implements ActionListener,
		ListSelectionListener {
	/**
     * 
     */
	private static final long serialVersionUID = 6870801579299984459L;
	private ConnectionPanel connectionPanel = null;
	private PasteScriptPanel pasteScriptPanel = null;
	private CommandTablePanel commandTablePanel = null;
	private DetailPanel detailPanel = null;
	private JDialog pasteScriptJDialog = null;
	private Script script = null;

	private JFrame frame = null;
	private String delimiter = null;

	private ConnectionDialog connectionDialog = null;

	private Config config = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		if (source == getDetailPanel().getApplyButton()) {
			ScriptCommand tmpCommand = getDetailPanel()
					.getSelectedScriptCommand();
			tmpCommand.setCommand(getDetailPanel().getCommandText().getText());
			tmpCommand.setRun(getDetailPanel().getRunCheckBox().isSelected());
			getCommandTablePanel().getCommandTable().updateUI();
		} else if (source == getDetailPanel().getRemoveCommandButton()) {
			ScriptCommand tmpScriptCommand = getDetailPanel()
					.getSelectedScriptCommand();
			if (tmpScriptCommand != null) {
				script.getCommands().remove(tmpScriptCommand);
				commandTablePanel.getCommandTable().updateUI();
			}
		} else if (source == getDetailPanel().getShowErrorButton()) {
			JOptionPane.showMessageDialog(this, getDetailPanel()
					.getSelectedScriptCommand().getErrorMessage());
		} else if (source == getConnectionPanel().getNewConnectionButton()) {
			if (connectionDialog == null) {
				connectionDialog = new ConnectionDialog(config);
				connectionDialog.setModal(true);
			}
			connectionDialog.setVisible(true);
			if (connectionDialog.getConnection() != null) {
				script.setConnection(connectionDialog.getConnection());
				script.setConnectionInformation(connectionDialog
						.getConnectionInformation());
				getConnectionPanel().getUserNameLabel().setText(
						script.getConnectionInformation().getUserID());
				getConnectionPanel().getConnNameLabel().setText(
						script.getConnectionInformation().getConnectionName());
				getDetailPanel().setKeywords(
						script.getConnectionInformation().getDatabaseDialect()
								.getKeywords());
			}

		} else if (source == getConnectionPanel().getPasteButton()) {
			if (pasteScriptPanel == null) {
				pasteScriptPanel = new PasteScriptPanel(this);
				pasteScriptJDialog = new JDialog();
				// pasteScriptJDialog.setModal(true);
				pasteScriptJDialog.setTitle("Paste Script");
				pasteScriptJDialog.getContentPane().add(pasteScriptPanel);
				pasteScriptJDialog.setSize(600, 500);
			}
			pasteScriptPanel.getScriptTextArea().setText("");

			pasteScriptJDialog.setVisible(true);
			pasteScriptPanel.getScriptTextArea().requestFocus();
		} else if (source == connectionPanel.getCloseButton()) {
			Connection tmpConn = script.getConnection();
			if (tmpConn != null) {
				try {
					if (!tmpConn.isClosed()) {
						tmpConn.close();
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				script.setConnection(null);

				connectionPanel.getConnNameLabel().setText("");
				connectionPanel.getUserNameLabel().setText("");
			}
			script.setCommands(new ArrayList<ScriptCommand>());
			frame.setVisible(false);
		} else if (pasteScriptPanel != null
				&& source == pasteScriptPanel.getOkButton()) {
			pasteScriptJDialog.setVisible(false);
			script.parseScriptFromClipBoard(pasteScriptPanel
					.getScriptTextArea().getText(), "/", pasteScriptPanel
					.getInsertMode(), getCommandTablePanel().getCommandTable()
					.getSelectedRow());
			this.commandTablePanel.getCommandTable().updateUI();
			this.commandTablePanel.getCommandTable().requestFocus();
		} else if (pasteScriptPanel != null
				&& source == pasteScriptPanel.getCancelButton()) {
			pasteScriptJDialog.setVisible(false);
			this.commandTablePanel.getCommandTable().updateUI();
			this.commandTablePanel.getCommandTable().requestFocus();
		} else if (source == connectionPanel.getRunButton()) {
			if (script.getConnection() == null) {
				JOptionPane
						.showMessageDialog(this,
								"You must create a connection before running a script.");
				return;
			}
			this.script.run();
			this.commandTablePanel.getCommandTable().updateUI();
		} else if (source == connectionPanel.getLoadButton()) {
			FileDialog exportFile = new FileDialog(frame, "Load Script",
					FileDialog.LOAD);
			// TODO set drop down list to .sql
			// java.awt.dnd.DropTarget tre = new java.awt.dnd.DropTarget();

			exportFile.setVisible(true);
			String curFile = exportFile.getFile();
			String filename = exportFile.getDirectory() + curFile;
			if (curFile == null) {
				return;
			}

			script.parseScriptFile(filename, delimiter);
			commandTablePanel.getCommandTable().updateUI();

		} else if (source == connectionPanel.getSaveButton()) {
			java.io.PrintWriter exportFile = null;
			FileDialog importFile = new FileDialog(frame, "Save Script",
					FileDialog.SAVE);
			// TODO set drop down list to .sql
			// java.awt.dnd.DropTarget tre = new java.awt.dnd.DropTarget();

			importFile.setVisible(true);
			String curFile = importFile.getFile();
			String filename = importFile.getDirectory() + curFile;
			if (curFile == null) {
				return;
			}
			if (curFile.lastIndexOf(".sql") == -1) {
				curFile += ".sql";
			}
			try {
				exportFile = new java.io.PrintWriter(
						new java.io.BufferedWriter(new java.io.FileWriter(
								filename, true)));
				List<ScriptCommand> commands = getScript().getCommands();
				int size = commands.size();
				ScriptCommand scriptCommand = null;
				for (int x = 0; x < size; x++) {
					scriptCommand = (ScriptCommand) commands.get(x);
					exportFile.println(scriptCommand.getCommand());
					exportFile.println(delimiter);
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, e.getMessage());
				e.printStackTrace();
			} finally {
				if (exportFile != null) {
					exportFile.close();
				}
			}
		} else if (source == getConnectionPanel().getCommitButton()) {
			if (script.getConnection() != null) {
				try {
					script.getConnection().commit();
					JOptionPane.showMessageDialog(this,
							"Transaction committed.");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
					e.printStackTrace();
				}
			}
		} else if (source == getConnectionPanel().getRollbackButton()) {
			if (script.getConnection() != null) {
				try {
					script.getConnection().rollback();
					JOptionPane.showMessageDialog(this,
							"Transaction rolled back.");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * @return Returns the connectionPanel.
	 */
	public ConnectionPanel getConnectionPanel() {
		return connectionPanel;
	}

	/**
	 * @param connectionPanel
	 *            The connectionPanel to set.
	 */
	public void setConnectionPanel(ConnectionPanel connectionPanel) {
		this.connectionPanel = connectionPanel;
	}

	/**
	 * @return Returns the script.
	 */
	public Script getScript() {
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
	 * @return Returns the commandTablePanel.
	 */
	public CommandTablePanel getCommandTablePanel() {
		return commandTablePanel;
	}

	/**
	 * @param commandTablePanel
	 *            The commandTablePanel to set.
	 */
	public void setCommandTablePanel(CommandTablePanel commandTablePanel) {
		this.commandTablePanel = commandTablePanel;
	}

	/**
	 * @return Returns the frame.
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * @param frame
	 *            The frame to set.
	 */
	public void setFrame(JFrame frame) {
		if (frame != null) {
			frame.setTitle("Script Runner");
		}
		this.frame = frame;
	}

	/**
	 * @return Returns the delimiter.
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * @param delimiter
	 *            The delimiter to set.
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event
	 * .ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent argEvent) {
		ListSelectionModel listSelectionModel = (ListSelectionModel) argEvent
				.getSource();
		if (argEvent.getValueIsAdjusting()
				|| listSelectionModel.isSelectionEmpty()) {
			return;
		}
		detailPanel.setSelectedScriptCommand(getScript().getCommand(
				listSelectionModel.getMinSelectionIndex()));
	}

	/**
	 * @return Returns the detailPanel.
	 */
	public DetailPanel getDetailPanel() {
		return detailPanel;
	}

	/**
	 * @param detailPanel
	 *            The detailPanel to set.
	 */
	public void setDetailPanel(DetailPanel detailPanel) {
		this.detailPanel = detailPanel;
	}

	public void init(Config argConfig) {
		config = argConfig;
		script = new Script();
		frame = new JFrame("Script Runner");
		setConnectionPanel(new ConnectionPanel(this));
		add(getConnectionPanel());

		setDelimiter(config.getScriptLineDelimiter());

		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.CENTER);
		frame.getContentPane().setLayout(layout);
		frame.getContentPane().add(this);

		setCommandTablePanel(new CommandTablePanel(this));

		commandTablePanel.setScript(getScript());
		frame.getContentPane().add(commandTablePanel);
		DetailPanel detailPanel = new DetailPanel(this, null);
		setDetailPanel(detailPanel);
		frame.getContentPane().add(detailPanel);
	}
}
