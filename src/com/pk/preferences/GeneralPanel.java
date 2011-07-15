/*
 * Created on Oct 23, 2004
 *
 * @author ctaylor
 * 
 */
package com.pk.preferences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;


import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;

import com.pk.Config;


/**
 * @author ctaylor
 *
 */
public class GeneralPanel extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = 5037263415761414357L;
    
    private class HistoryModeDisplay
    {
            
        String displayString = "";
        Integer value = null;
        
        public HistoryModeDisplay(String argDisplayString, Integer argValue)
        {
            displayString = argDisplayString;
            value = argValue;
            
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            return displayString;
        }
    }
    
    private JComboBox lookAndFeelComboBox = null;
    private JComboBox historyModeComboBox = null;
    
    public GeneralPanel (ActionListener argActionListener)
    {
        JLabel label = new JLabel();
        this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(6, 15, 6, 15);

		gbc.gridx = 0;
		gbc.gridy = 0;
		label = new JLabel("Look and Feel:", JLabel.LEFT);
		this.add(label, gbc);

		gbc.gridx++;
		
		lookAndFeelComboBox = new JComboBox();
		//lookAndFeelComboBox.setEditable(true);
		this.add(lookAndFeelComboBox, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        label = new JLabel("History Mode:", JLabel.LEFT);
        this.add(label, gbc);

        gbc.gridx++;
        
        historyModeComboBox = new JComboBox();
        //lookAndFeelComboBox.setEditable(true);
        this.add(historyModeComboBox, gbc);
    }
    
    public void setLookandFeels(Config argConfig)
    {
        if(argConfig != null && lookAndFeelComboBox != null)
        {
            int size = argConfig.getLookandFeels().size();
            for(int x = 0; x < size; x++)
            {
                lookAndFeelComboBox.addItem(((LookAndFeel)argConfig.getLookandFeels().get(x)).getName());
            }
            lookAndFeelComboBox.setSelectedItem(argConfig.getSelectedLookAndFeel().getName());
        }
    }
    
    public void setSelectedHistoryMode(int argSelectedMode)
    {
        if(historyModeComboBox.getItemCount() == 0)
        {
            historyModeComboBox.addItem(new HistoryModeDisplay("Unique", new Integer(Config.HISTORY_MODE_UNIQUE)));
            historyModeComboBox.addItem(new HistoryModeDisplay("Different From Last Run", new Integer(Config.HISTORY_MODE_DIFF_FROM_LAST)));
            historyModeComboBox.addItem(new HistoryModeDisplay("All", new Integer(Config.HISTORY_MODE_ALL)));
        }
        if(argSelectedMode == Config.HISTORY_MODE_UNIQUE)
        {
            historyModeComboBox.setSelectedIndex(0);
        }
        else if(argSelectedMode == Config.HISTORY_MODE_DIFF_FROM_LAST)
        {
            historyModeComboBox.setSelectedIndex(1);
        }
        else if(argSelectedMode == Config.HISTORY_MODE_ALL)
        {
            historyModeComboBox.setSelectedIndex(2);
        }
    }
    
    public int getSelectedHistoryMode()
    {
         if(historyModeComboBox != null)
         {
             return ((HistoryModeDisplay)historyModeComboBox.getSelectedItem()).value.intValue();
         }
         return Config.HISTORY_MODE_ALL;
    }
    /**
     * @return Returns the lookAndFeelComboBox.
     */
    public JComboBox getLookAndFeelComboBox()
    {
        return lookAndFeelComboBox;
    }
}
