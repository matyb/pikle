/*
 * Created on May 23, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.pk;

/**
 * @author Isabelle
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConnectionInformation
{
	private String propertiesKey = null;
	private String url = null;
	private String userID = null;
	private String password = null;
	private int numberOfWindows = 0;
	private String connectionListKey = null;
	private DatabaseDialect databaseDialect = null; 
	private String connectionName = null;
	
	/**
	 * @return
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @return
	 */
	public String getPropertiesKey()
	{
		return propertiesKey;
	}

	/**
	 * @return
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @return
	 */
	public String getUserID()
	{
		return userID;
	}

	/**
	 * @param string
	 */
	public void setPassword(String string)
	{
		password = string;
	}

	/**
	 * @param string
	 */
	public void setPropertiesKey(String string)
	{
		propertiesKey = string;
	}

	/**
	 * @param string
	 */
	public void setUrl(String string)
	{
		url = string;
	}

	/**
	 * @param string
	 */
	public void setUserID(String string)
	{
		userID = string;
	}

	/**
	 * @return
	 */
	public String getConnectionListKey()
	{
		return connectionListKey;
	}

	/**
	 * @return
	 */
	public int getNumberOfWindows()
	{
		return numberOfWindows;
	}

	/**
	 * @param string
	 */
	public void setConnectionListKey(String string)
	{
		connectionListKey = string;
	}

	/**
	 * @param i
	 */
	public void setNumberOfWindows(int i)
	{
		numberOfWindows = i;
	}

	/**
	 * @return
	 */
	public DatabaseDialect getDatabaseDialect()
	{
		return databaseDialect;
	}

	/**
	 * @param dialect
	 */
	public void setDatabaseDialect(DatabaseDialect dialect)
	{
		databaseDialect = dialect;
	}

    /**
     * @return Returns the connectionName.
     */
    public String getConnectionName()
    {
        return connectionName;
    }
    /**
     * @param connectionName The connectionName to set.
     */
    public void setConnectionName(String connectionName)
    {
        this.connectionName = connectionName;
    }
}
