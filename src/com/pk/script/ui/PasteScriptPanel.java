/*
 * Created on Oct 3, 2004
 *
 * @author ctaylor
 * 
 */
package com.pk.script.ui;

import java.awt.Color;
import java.awt.Component;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.pk.script.Script;

/**
 * @author ctaylor
 *
 */
public class PasteScriptPanel extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = -3708779830891735747L;
    private JTextArea scriptTextArea;
    private JButton okButton;
    private JButton cancelButton;
    private JTextField delimiterTextField;
    private JRadioButton insertEndRadioButton;
    private JRadioButton insertBeginningRadioButton;
    private JRadioButton insertAtCurserRadioButton;
    private JRadioButton replaceCurrentScriptRadioButton;
    private ActionListener actionListener;
    
    public PasteScriptPanel(ActionListener argActionListener)
    {
        actionListener = argActionListener;
        JPanel radioButtonPanel = new JPanel();
        insertEndRadioButton = new JRadioButton("Insert at End");
        insertEndRadioButton.setSelected(true);
        radioButtonPanel.add(insertEndRadioButton);
        insertBeginningRadioButton = new JRadioButton("Insert at Beginning");
        radioButtonPanel.add(insertBeginningRadioButton);
        insertAtCurserRadioButton = new JRadioButton("Insert at Curser");
        radioButtonPanel.add(insertAtCurserRadioButton);
        replaceCurrentScriptRadioButton = new JRadioButton("Replace Current Script");
        radioButtonPanel.add(replaceCurrentScriptRadioButton);
        radioButtonPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(insertEndRadioButton);
        radioGroup.add(insertBeginningRadioButton);
        radioGroup.add(insertAtCurserRadioButton);
        radioGroup.add(replaceCurrentScriptRadioButton);
        
        
        this.add(radioButtonPanel);
        
        scriptTextArea = new JTextArea(20,40);
        JScrollPane sqlScrollPane = new JScrollPane(scriptTextArea);
        
        okButton = new JButton("Ok");
        okButton.addActionListener(actionListener);

        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(actionListener);
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
     
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
        okButton.setSize(cancelButton.getSize());
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
 
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(6, 15, 6, 15);
		gbc.gridx = 0;
		gbc.gridy = 0;
        centerPanel.add(sqlScrollPane,gbc);
        gbc.gridx = 1;
        centerPanel.add(buttonPanel,gbc);
        this.add(centerPanel);
    }
    /**
     * @return Returns the cancelButton.
     */
    public JButton getCancelButton()
    {
        return cancelButton;
    }
    /**
     * @return Returns the delimiterTextField.
     */
    public JTextField getDelimiterTextField()
    {
        return delimiterTextField;
    }
    /**
     * @return Returns the insertAtCurserRadioButton.
     */
    public JRadioButton getInsertAtCurserRadioButton()
    {
        return insertAtCurserRadioButton;
    }
    /**
     * @return Returns the insertBeginningRadioButton.
     */
    public JRadioButton getInsertBeginningRadioButton()
    {
        return insertBeginningRadioButton;
    }
    /**
     * @return Returns the insertEndRadioButton.
     */
    public JRadioButton getInsertEndRadioButton()
    {
        return insertEndRadioButton;
    }
    /**
     * @return Returns the okButton.
     */
    public JButton getOkButton()
    {
        return okButton;
    }
    /**
     * @return Returns the replaceCurrentScriptRadioButton.
     */
    public JRadioButton getReplaceCurrentScriptRadioButton()
    {
        return replaceCurrentScriptRadioButton;
    }
    /**
     * @return Returns the scriptTextArea.
     */
    public JTextArea getScriptTextArea()
    {
        return scriptTextArea;
    }
    /**
     * @return
     */
    public int getInsertMode()
    {
        if(insertEndRadioButton.isSelected())
        {
            return Script.NEW_COMMAND_INSERT_END_MODE;
        }
        else if(insertBeginningRadioButton.isSelected())
        {
            return Script.NEW_COMMAND_INSERT_BEGINNING_MODE;
        }
        else if(insertAtCurserRadioButton.isSelected())
        {
            return Script.NEW_COMMAND_INSERT_AT_CURSER_MODE;
        }
        else
        {
            return Script.NEW_COMMAND_REPLACE_CURRENT_SCRIPT_MODE;
        }
    }
}
