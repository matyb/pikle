/*
 * Created on Sep 29, 2004
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author ctaylor
 *
 */
public class ConnectionPanel extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = 4254774480356323583L;
    private JTextField connNameLabel;
    private JTextField userNameLabel;
    private JButton newConnectionButton;
    private JButton loadButton;
    private JButton pasteButton;
    private JButton saveButton;
    private JButton runButton;
    private JButton closeButton;
    private JButton commitButton;
    private JButton rollbackButton;
    
    
    private ActionListener actionListener;
    
    public ConnectionPanel(ActionListener argActionListener)
    {
        actionListener = argActionListener;
        this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(6, 5, 6, 5);

		JLabel label = null; 
		gbc.gridx = 0;
		gbc.gridy = 0;
		label = new JLabel("Connection Name:", JLabel.LEFT);
		this.add(label, gbc);

		gbc.gridy++;
		label = new JLabel("User Name:", JLabel.LEFT);
		this.add(label, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;

		connNameLabel = new JTextField(10);
		connNameLabel.setEditable(false);
		connNameLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(connNameLabel, gbc);

		gbc.gridy++;
		userNameLabel = new JTextField(10);
		userNameLabel.setEditable(false);
		userNameLabel.setBorder(BorderFactory.createLineBorder(Color.black));
		this.add(userNameLabel, gbc);
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.anchor = GridBagConstraints.CENTER;
		gbc2.insets = new Insets(6, 5, 6, 5);
		gbc2.gridx = 2;
		gbc2.gridy = 0;

		this.setBorder(BorderFactory.createLineBorder(Color.black));
		newConnectionButton = new JButton("New Connection");
		newConnectionButton.addActionListener(actionListener);
		this.add(newConnectionButton,gbc2);

		gbc2.gridx++;
		loadButton = new JButton("Load Script");
		loadButton.setToolTipText("Load a script file into the script runner");
		loadButton.addActionListener(actionListener);
		this.add(loadButton,gbc2);
		
		gbc2.gridx++;
		saveButton = new JButton("Save Script");
		saveButton.setToolTipText("Save current script to a file");
		saveButton.addActionListener(actionListener);
		this.add(saveButton,gbc2);
		
		gbc2.gridx++;
		pasteButton = new JButton("Paste Script");
		pasteButton.setToolTipText("Paste a script or command from the clipboard");
		pasteButton.addActionListener(actionListener);
		this.add(pasteButton,gbc2);
		
		
		gbc2.gridx = 2;
		gbc2.gridy = 1;
		
		runButton = new JButton("Run");
		runButton.setToolTipText("Run current script");
		runButton.addActionListener(actionListener);
		this.add(runButton,gbc2);
		
		gbc2.gridx++;

		commitButton =  new JButton("Commit");
		commitButton.setToolTipText("Commit current transaction");
		commitButton.addActionListener(actionListener);
		this.add(commitButton,gbc2);
		
		gbc2.gridx++;
		rollbackButton = new JButton("Rollback");
		rollbackButton.setToolTipText("Rollback current transaction");
		rollbackButton.addActionListener(actionListener);
		this.add(rollbackButton,gbc2);
		
		gbc2.gridx++;
		closeButton = new JButton("Close");
		closeButton.setToolTipText("Close script runner");
		closeButton.addActionListener(actionListener);
		this.add(closeButton,gbc2);
		
		
		

    }
    /**
     * @return Returns the connNameLabel.
     */
    public JTextField getConnNameLabel()
    {
        return connNameLabel;
    }
    /**
     * @return Returns the newConnectionButton.
     */
    public JButton getNewConnectionButton()
    {
        return newConnectionButton;
    }
    /**
     * @return Returns the userNameLabel.
     */
    public JTextField getUserNameLabel()
    {
        return userNameLabel;
    }
    /**
     * @param actionListener The actionListener to set.
     */
    public void setActionListener(ActionListener actionListener)
    {
        this.actionListener = actionListener;
    }
    /**
     * @return Returns the loadButton.
     */
    public JButton getLoadButton()
    {
        return loadButton;
    }
    /**
     * @return Returns the pasteButton.
     */
    public JButton getPasteButton()
    {
        return pasteButton;
    }
    /**
     * @return Returns the saveButton.
     */
    public JButton getSaveButton()
    {
        return saveButton;
    }
    /**
     * @return Returns the runButton.
     */
    public JButton getRunButton()
    {
        return runButton;
    }
    /**
     * @return Returns the closeButton.
     */
    public JButton getCloseButton()
    {
        return closeButton;
    }
    /**
     * @param closeButton The closeButton to set.
     */
    public void setCloseButton(JButton closeButton)
    {
        this.closeButton = closeButton;
    }
    /**
     * @return Returns the commitButton.
     */
    public JButton getCommitButton()
    {
        return commitButton;
    }
    /**
     * @return Returns the rollbackButton.
     */
    public JButton getRollbackButton()
    {
        return rollbackButton;
    }
}
