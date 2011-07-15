/*
 * Created on 8-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pk;

import java.io.IOException;

import javax.swing.LookAndFeel;

import org.xml.sax.SAXException;

import com.sandwich.util.Digester;
import com.sandwich.util.xml.XMLDigester;

/**
 * @author ctaylor
 */
public class ConfigDigester 
{
    private Config tmpConfig = null;

    private String fileName = null;


    public Config createConfig() throws IOException, SAXException
    {
        tmpConfig = new Config();
        Digester digester = new XMLDigester(this)
        	.addCallMethod("config/datasources/datasource", "addDataSource", "name", "driver", "url", "databaseDialectName")
	        .addCallMethod("config/export/delimiterChar",	"setDelimiterChar")
	        .addCallMethod("config/export/encloseByChar",	"setEncloseByChar")
	        .addCallMethod("config/export/useCdata",		"setUseCdata")
	        .addCallMethod("config/export/headerColor",		"setHeaderColor")
	        .addCallMethod("config/export/borderColor",		"setBorderColor")
	        .addCallMethod("config/export/altRowColor",		"setAltRowColor")
	        .addCallMethod("config/general/lafs/laf",		"addLookAndFeel", "class", "selected")
	        .addCallMethod("config/general/history/mode",	"setHistoryMode");

        digester.parse(fileName);
        tmpConfig.orderConnections();
        return tmpConfig;
    }

    public void addDataSource(String name, String driver, String url, String databaseDialectName)
    {
        ConnectionConfig tmp = new ConnectionConfig();
        tmp.setName(name);
        tmp.setDriver(driver);
        tmp.setUrl(url);
        tmp.setDatabaseDialectName(databaseDialectName);
        tmpConfig.addConnectionConfig(tmp);
    }
    
    public void setDelimiterChar(String argDelimiterChar)
    {
        tmpConfig.setExportDelimiterChar(argDelimiterChar);
    }
    
    public void setEncloseByChar(String argEncloseByChar)
    {
        tmpConfig.setEncloseByChar(argEncloseByChar);
    }
    /**
     * @return Returns the connectionConfigs.
     */
    /**
     * @return Returns the fileName.
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName
     *            The fileName to set.
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }
    public void setHeaderColor(String headerColor)
    {
        tmpConfig.setHeaderColor(headerColor);
    }
    public void setBorderColor(String borderColor)
    {
        tmpConfig.setBorderColor(borderColor);
    }
    public void setAltRowColor(String altRowColor)
    {
        tmpConfig.setAltRowColor(altRowColor);
    }
    public void setUseCdata(String useCdata)
    {
        if(useCdata != null && useCdata.equalsIgnoreCase("TRUE"))
        {
            tmpConfig.setUseCdata(true);
        }
        else
        {
            tmpConfig.setUseCdata(false);
        }
    }
    public void addLookAndFeel(String argClass, String argSelected)
    {
        try
        {
            Class<?> tmpClass = Class.forName(argClass);
            tmpConfig.addLookandFeel((LookAndFeel)tmpClass.newInstance());
            if(argSelected != null && argSelected.equalsIgnoreCase("true"))
            {
                tmpConfig.setSelectedLookAndFeel((LookAndFeel)tmpClass.newInstance());
            }
        }
        catch(ClassNotFoundException ex)
        {
            ex.printStackTrace();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    public void setHistoryMode(String argHistoryMode)
    {
        int tmpHistoryMode = Config.HISTORY_MODE_ALL;
        try
        {
            tmpHistoryMode = Integer.parseInt(argHistoryMode);
        }
        catch(Throwable ex)
        {
            tmpHistoryMode = Config.HISTORY_MODE_ALL;
        }
        if(tmpHistoryMode != Config.HISTORY_MODE_ALL && tmpHistoryMode != Config.HISTORY_MODE_DIFF_FROM_LAST && tmpHistoryMode != Config.HISTORY_MODE_UNIQUE)
        {
            tmpHistoryMode = Config.HISTORY_MODE_ALL;
        }
        tmpConfig.setHistoryMode(tmpHistoryMode);
    }

}