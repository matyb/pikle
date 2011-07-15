/*
 * Created on Mar 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.pk;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

/**
 * @author Isabelle
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WindowJMenuItem extends JMenuItem
{
	/**
     * 
     */
    private static final long serialVersionUID = 4505484817449760309L;
    private int windowIndex = 0;

	/**
	 * 
	 */
	public WindowJMenuItem()
	{
		super();
	}

	/**
	 * @param arg0
	 */
	public WindowJMenuItem(String arg0)
	{
		super(arg0);
	}
	public WindowJMenuItem(String arg0, int argWindowIndex)
	{
		super(arg0);
	}


	/**
	 * @param arg0
	 * @param arg1
	 */

	/**
	 * @param arg0
	 */
	public WindowJMenuItem(Action arg0)
	{
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public WindowJMenuItem(Icon arg0)
	{
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public WindowJMenuItem(String arg0, Icon arg1)
	{
		super(arg0, arg1);
		
	}

	/**
	 * @return
	 */
	public int getWindowIndex()
	{
		return windowIndex;
	}

	/**
	 * @param i
	 */
	public void setWindowIndex(int i)
	{
		windowIndex = i;
	}

}
