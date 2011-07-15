/*
 * Created on Aug 19, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pk.preferences;



import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;




import javax.swing.JButton;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextField;



/**
 * @author Chris Taylor
 *
 */
public class PropertiesPanel extends JPanel 
{


	/**
     * 
     */
    private static final long serialVersionUID = 286423509225605764L;
    private JButton newButton;
	private JButton removeButton;
	private ActionListener actionListener = null;
    private JTextField txtName = null;
    private JTextField txtURL = null;
    private JTextField txtDriver = null;
    private JComboBox txtDialect = null;
	
    
    /**
     * @return Returns the txtDialect.
     */
    public JComboBox getTxtDialect()
    {
        return txtDialect;
    }
    /**
     * @return Returns the txtDriver.
     */
    public JTextField getTxtDriver()
    {
        return txtDriver;
    }
    /**
     * @return Returns the txtName.
     */
    public JTextField getTxtName()
    {
        return txtName;
    }
    /**
     * @return Returns the txtURL.
     */
    public JTextField getTxtURL()
    {
        return txtURL;
    }
    /**
     * 
     */
    public PropertiesPanel(ActionListener argActionListener)
    {
        super();
        actionListener = argActionListener;
        JLabel label = new JLabel();
        this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(6, 15, 6, 15);

		gbc.gridx = 0;
		gbc.gridy = 0;
		label = new JLabel("Name:", JLabel.LEFT);
		this.add(label, gbc);

		gbc.gridy++;
		label = new JLabel("URL:", JLabel.LEFT);
		this.add(label, gbc);

		gbc.gridy++;
		label = new JLabel("Driver:", JLabel.LEFT);
		this.add(label, gbc);

		gbc.gridy++;
		label = new JLabel("Dialect:", JLabel.LEFT);
		this.add(label, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;

		txtName = new JTextField(25);
		this.add(txtName, gbc);

		gbc.gridy++;
		txtURL = new JTextField(25);
		this.add(txtURL, gbc);

		gbc.gridy++;
		txtDriver = new JTextField(25);
		this.add(txtDriver,gbc);
		
		gbc.gridy++;
		String dialects[] = {"com.pk.OracleDialect","com.pk.MySQLDialect","com.pk.DefaultDialect","com.pk.MicrosoftSQLDialect"};
		txtDialect = new JComboBox(dialects);
		txtDialect.setEditable(true);
		txtDialect.setSelectedIndex(-1);
		this.add(txtDialect, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.anchor = GridBagConstraints.CENTER;
		JPanel buttonPanel = new JPanel();
		newButton = new JButton("New Connection Config");
		newButton.addActionListener(actionListener);
		removeButton = new JButton("Remove");
		removeButton.addActionListener(actionListener);
		buttonPanel.add(newButton);
		buttonPanel.add(removeButton);
		this.add(buttonPanel, gbc);
    }


/**
 * @return Returns the actionListener.
 */
public ActionListener getActionListener() {
	return actionListener;
}
/**
 * @param actionListener The actionListener to set.
 */
public void setActionListener(ActionListener actionListener) {
	this.actionListener = actionListener;
}
	/**
	 * @return Returns the okButton.
	 */
	/**
	 * @return Returns the newButton.
	 */
	public JButton getNewButton() {
		return newButton;
	}
    /**
     * @return Returns the removeButton.
     */
    public JButton getRemoveButton()
    {
        return removeButton;
    }
    /**
     * @return Returns the closeButton.
     */
}
