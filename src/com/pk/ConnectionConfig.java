/*
 * Created on 8-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pk;

/**
 * @author ctaylor
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ConnectionConfig implements Comparable<ConnectionConfig>
{

    private String name;

    private String driver;

    private String url;

    private String databaseDialectName;
    
    private DatabaseDialect databaseDialect; 

    /**
     * @return Returns the databaseDilectName.
     */
    public String getDatabaseDialectName()
    {
        return databaseDialectName;
    }

    /**
     * @param databaseDilectName
     *            The databaseDilectName to set.
     */
    public void setDatabaseDialectName(String databaseDilectName)
    {
        this.databaseDialectName = databaseDilectName;
    }

    /**
     * @return Returns the driver.
     */
    public String getDriver()
    {
        return driver;
    }

    /**
     * @param driver
     *            The driver to set.
     */
    public void setDriver(String driver)
    {
        this.driver = driver;
    }

    /**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return Returns the url.
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param url
     *            The url to set.
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getName();
    }
    /**
     * @return Returns the databaseDialect.
     */
    public DatabaseDialect getDatabaseDialect()
    {
        if(databaseDialect == null)
        {
	        try
	        {
	            databaseDialect = (DatabaseDialect)this.getClass().getClassLoader().loadClass(databaseDialectName).newInstance();
	        }
	        catch (Exception e) 
	        {
	            //TODO display error when class not found. 
	            e.printStackTrace();
	        }
        }
        return databaseDialect;
    }

    public int compareTo(ConnectionConfig arg0)
    {
        return name.compareToIgnoreCase(arg0.getName());
    }
}