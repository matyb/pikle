// Class defining a status bar
package com.pk;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

class StatusBar extends JPanel implements Constants
{
	/**
     * 
     */
    private static final long serialVersionUID = -4915779892342583217L;

    // Constructor
	public StatusBar()
	{
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
		setBackground(Color.lightGray);
		setBorder(BorderFactory.createLineBorder(Color.darkGray));
		setUserPane(DEFAULT_ELEMENT_USER);
		setElapsedTimePane("");
		setMessagePane("");
		add(userPane); // Add color pane to status bar
		add(elapsedTimePane);
		add(messagePane); // Add type pane to status bar
	}

	// Set type pane label
	public void setElapsedTimePane(String text)
	{
		elapsedTimePane.setText(text);
	}

	public void setMessagePane(String text)
	{
		messagePane.setText(text); // Set the pane text
	}

	public void setUserPane(String text)
	{
		userPane.setText(text); // Set the pane text
	}

	// Panes in the status bar
	public StatusPane messagePane = new StatusPane("");
	public StatusPaneTime elapsedTimePane = new StatusPaneTime("");
	private StatusPaneSmall userPane = new StatusPaneSmall("");

	// Class defining a status bar pane
	class StatusPane extends JLabel
	{
		/**
         * 
         */
        private static final long serialVersionUID = 3513784308329811461L;

        public StatusPane(String text)
		{
			setBackground(Color.lightGray); // Set background color
			setForeground(Color.black);
			//setFont(paneFont);                   // Set the fixed font
			setHorizontalAlignment(CENTER); // Center the pane text 
			setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			//setPreferredSize(new Dimension(400,20));
			setHorizontalAlignment(LEFT);
			setText(text); // Set the text in the pane
		}
		// Font for pane text
		//private Font paneFont = new Font("Serif", Font.PLAIN, 12);
	}

	class StatusPaneTime extends JLabel
	{
		/**
         * 
         */
        private static final long serialVersionUID = -6145213924965322048L;

        public StatusPaneTime(String text)
		{
			setBackground(Color.lightGray); // Set background color
			setForeground(Color.black);
			//setFont(paneFont);                   // Set the fixed font
			setHorizontalAlignment(CENTER); // Center the pane text 
			setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			//setPreferredSize(new Dimension(400,20));
			setHorizontalAlignment(LEFT);
			setText(text); // Set the text in the pane
		}
		// Font for pane text
		//private Font paneFont = new Font("Serif", Font.PLAIN, 12);
	}

	class StatusPaneSmall extends JLabel
	{
		/**
         * 
         */
        private static final long serialVersionUID = -2250303985616849791L;

        public StatusPaneSmall(String text)
		{
			setBackground(Color.lightGray); // Set background color
			setForeground(Color.black);
			//setFont(paneFont);                   // Set the fixed font
			setHorizontalAlignment(CENTER); // Center the pane text 
			setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			//setPreferredSize(new Dimension(100,20));
			setHorizontalAlignment(LEFT);
			setText(text); // Set the text in the pane
		}

		// Font for pane text
		//private Font paneFont = new Font("Serif", Font.PLAIN, 12);
		//private Font paneFont = new Font("Default", Font.PLAIN, 12);
	}
}
