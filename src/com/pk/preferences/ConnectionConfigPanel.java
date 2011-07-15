/*
 * Created on Aug 8, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pk.preferences;

import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * @author Chris Taylor
 *
 */
public class ConnectionConfigPanel extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = 8715965417429455084L;
    JTextArea txtName = new JTextArea();
    JTextArea txtDriver = new JTextArea();
    JTextArea txtDialect = new JTextArea();
    JTextArea txtURL = new JTextArea();
}
