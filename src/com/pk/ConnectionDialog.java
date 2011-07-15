package com.pk;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ConnectionDialog extends JDialog implements FocusListener
{
	
    private static final long serialVersionUID = 3075853931225627975L;
    protected boolean canceled;
    protected JTextField passwordField;
    protected ConnectionInformation connectionInformation;
    protected JComboBox urlField;
    protected JTextField useridField;
    private Connection connection;

    private Config config;

    public ConnectionDialog(Config argConfig)
        {
            config = argConfig;
            setTitle("Connect To A Database");
            Toolkit dKit = getToolkit(); // Get the toolkit of window
            Dimension wSize = dKit.getScreenSize(); // Get the size of window
            setBounds(wSize.width / 2 - 200, wSize.height / 2 - 200, // location
                    wSize.width / 2, wSize.height / 2); // size
            buildDialogLayout();
            setSize(300, 200);
        }

    protected boolean attemptConnection()
    {
        try
        {
            connection = null;
            connectionInformation = new ConnectionInformation();
            
            ConnectionConfig tmpConnectionConfig = (ConnectionConfig) urlField.getSelectedItem();

            connectionInformation.setUrl(tmpConnectionConfig.getUrl());
            connectionInformation.setConnectionListKey(String.valueOf(System.currentTimeMillis()));
            connectionInformation.setPassword(passwordField.getText());
            connectionInformation.setUserID(useridField.getText());
            connectionInformation.setNumberOfWindows(1);
            connectionInformation.setDatabaseDialect(tmpConnectionConfig.getDatabaseDialect());
            connectionInformation.setConnectionName(tmpConnectionConfig.getName());
            connection = DriverManager.getConnection(connectionInformation.getUrl(), connectionInformation.getUserID(), connectionInformation.getPassword());
            connection.setAutoCommit(false);
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to " + "database: " + e.getMessage());
        }
        return false;
    }

    protected void buildDialogLayout()
    {
        JLabel label;
        urlField = new JComboBox();
        List<ConnectionConfig> connectionConfigs = config.getConnections();
        int size = connectionConfigs.size();
        for (int x = 0; x < size; x++)
        {
            urlField.addItem(connectionConfigs.get(x));
        }
        Container pane = getContentPane();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(6, 15, 6, 15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        label = new JLabel("Userid:", JLabel.LEFT);
        pane.add(label, gbc);

        gbc.gridy++;
        label = new JLabel("Password:", JLabel.LEFT);
        pane.add(label, gbc);

        gbc.gridy++;
        label = new JLabel("Url:", JLabel.LEFT);
        pane.add(label, gbc);

        gbc.gridy++;
        //label = new JLabel("Oracle:", JLabel.LEFT);

        gbc.gridx = 1;
        gbc.gridy = 0;

        useridField = new JTextField(10);
        pane.add(useridField, gbc);

        gbc.gridy++;
        passwordField = new JPasswordField(10);
        pane.add(passwordField, gbc);

        gbc.gridy++;
        //urlField = new JTextField("",15);

        pane.add(urlField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        pane.add(getButtonPanel(), gbc);
    }

    public void focusGained(FocusEvent e)
    {
    }

    public void focusLost(FocusEvent e)
    {
    }

    protected JPanel getButtonPanel()
    {
        JPanel panel = new JPanel();
        JButton btn1 = new JButton("Ok");
        getRootPane().setDefaultButton(btn1);
        btn1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                onDialogOk();
            }
        });
        panel.add(btn1);
        JButton btn2 = new JButton("Cancel");
        btn2.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                onDialogCancel();
            }
        });
        panel.add(btn2);
        return panel;
    }

    public Connection getConnection()
    {
        return connection;
    }

    protected void onDialogCancel()
    {
        useridField.setText("");
        passwordField.setText("");
        setVisible(false);
    }

    protected void onDialogOk()
    {

        if (attemptConnection())
        {
            useridField.setText("");
            passwordField.setText("");
            setVisible(false);
        }
    }

    public List<String> getKeywords()
    {
        return connectionInformation.getDatabaseDialect().getKeywords();
    }

    /**
     * @return Returns the connectionInformation.
     */
    public ConnectionInformation getConnectionInformation()
    {
        return connectionInformation;
    }
}