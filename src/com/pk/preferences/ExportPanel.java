/*
 * Created on Sep 18, 2004
 *
 */
package com.pk.preferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


import com.pk.Preferences;

/**
 * @author Chris Taylor
 *
 */
public class ExportPanel extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = -4466210421863384525L;

    private JTextField txtExportDelimiter = null;
    private JTextField txtEncloseBy = null;
    private JCheckBox useCdata = null;
    private JTextField txtHeaderColor = null;
    private JTextField txtBorderColor = null;
    private JTextField txtAltRowColor = null;

    public ExportPanel(ActionListener argActionListener)
    {
        JPanel delimPanel = new JPanel();
        JPanel xmlPanel = new JPanel();
        JPanel htmlPanel = new JPanel();
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setSize(Preferences.HEIGHT, Preferences.WIDTH);

        JLabel label = new JLabel();
        delimPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(6, 15, 6, 15);

		gbc.gridx = 0;
		gbc.gridy = 0;
		label = new JLabel("Export Delimiter:", JLabel.LEFT);
		delimPanel.add(label, gbc);

		gbc.gridy++;
		label = new JLabel("Enclose By:", JLabel.LEFT);
		delimPanel.add(label, gbc);
		gbc.gridx = 1;
		gbc.gridy = 0;

		txtExportDelimiter = new JTextField(5);
		delimPanel.add(txtExportDelimiter, gbc);

		gbc.gridy++;
		txtEncloseBy = new JTextField(5);
		delimPanel.add(txtEncloseBy, gbc);
		delimPanel.setBorder(BorderFactory.createTitledBorder("Delimited File"));
		
		useCdata = new JCheckBox("Use CData");
		xmlPanel.add(useCdata);
		xmlPanel.setBorder(BorderFactory.createTitledBorder("XML File"));
		
		htmlPanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 0;
		label = new JLabel("Header Color:", JLabel.LEFT);
		htmlPanel.add(label, gbc);

		gbc.gridy++;
		label = new JLabel("Border Color:", JLabel.LEFT);
		htmlPanel.add(label, gbc);
		
		gbc.gridy++;
		label = new JLabel("Alt Row Color:", JLabel.LEFT);
		htmlPanel.add(label, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;

		txtHeaderColor = new JTextField(7);
		htmlPanel.add(txtHeaderColor, gbc);

		gbc.gridy++;
		txtBorderColor = new JTextField(7);
		htmlPanel.add(txtBorderColor, gbc);
		
		gbc.gridy++;
		txtAltRowColor = new JTextField(7);
		htmlPanel.add(txtAltRowColor, gbc);
		
		htmlPanel.setBorder(BorderFactory.createTitledBorder("HTML File"));

		this.add(delimPanel);
		this.add(xmlPanel);
		this.add(htmlPanel);
    }
    /**
     * @return Returns the txtEncloseBy.
     */
    public JTextField getTxtEncloseBy()
    {
        return txtEncloseBy;
    }
    /**
     * @return Returns the txtExportDelimiter.
     */
    public JTextField getTxtExportDelimiter()
    {
        return txtExportDelimiter;
    }
    /**
     * @return Returns the userCdata.
     */
    public JCheckBox getUseCdata()
    {
        return useCdata;
    }
    /**
     * @return Returns the altRowColor.
     */
    public JTextField getAltRowColor()
    {
        return txtAltRowColor;
    }
    /**
     * @return Returns the borderColor.
     */
    public JTextField getBorderColor()
    {
        return txtBorderColor;
    }
    /**
     * @return Returns the headerColor.
     */
    public JTextField getHeaderColor()
    {
        return txtHeaderColor;
    }
}
