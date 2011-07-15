/*
 * Created on May 15, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.pk;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * @author Isabelle
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AboutPanel extends JPanel implements ActionListener
{
	/**
     * 
     */
    private static final long serialVersionUID = 179927281520551276L;
    private JDialog jDialog = null;
	private JButton okJButton = null;
	private JButton aboutPKJButton = null;

	/**
	 * 
	 */
	public AboutPanel(JDialog argJDialog)
	{
		super();
		jDialog = argJDialog;
		init();
	}

	/**
	 * @param arg0
	 */
	private void init()
	{
		ImageIcon tmpImageIcon = new ImageIcon("images/yoda.gif");
		JPanel topPanel = new JPanel();
		JPanel bottumPanel = new JPanel();
		JLabel spaceLable = new JLabel("       ");
		spaceLable.setMaximumSize(new Dimension(10,10));
		JLabel tmpJLabel = new JLabel("<html> V2.0 Beta 3 <P>Pklite SQL Client is a modified version of a program<P>called Pretty Kid For more information about Pretty Kid<P>click on the about pretty kid button<p><A href=\"http://pklite.sourceforge.net/\">pklite.sourceforge.net</A>");
		JLabel imgLabel = new JLabel(tmpImageIcon); 
		okJButton = new JButton("Ok");
		aboutPKJButton = new JButton("About Pretty Kid");
		
		okJButton.addActionListener(this);
		aboutPKJButton.addActionListener(this);
		
		topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.LINE_AXIS));
		topPanel.add(spaceLable);
		topPanel.add(imgLabel);
		topPanel.add(spaceLable);
		topPanel.add(tmpJLabel);
		
		bottumPanel.setLayout(new BorderLayout());
		bottumPanel.add(aboutPKJButton,BorderLayout.WEST);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(topPanel);
		add(okJButton);
		add(new JSeparator(SwingConstants.HORIZONTAL));
		add(bottumPanel);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0)
	{
		if(arg0.getSource().equals(okJButton))
		{
			jDialog.dispose();
		}
		if(arg0.getSource().equals(aboutPKJButton))
		{
			JOptionPane.showMessageDialog(this, "(c) Copyright 2001 by Daekyun Lim \nhttp://www.prettykid.com", "About Pretty Kid", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("images/mybaby2.jpg"));
		}
		
	}
	
}
