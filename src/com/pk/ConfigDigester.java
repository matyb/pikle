/*
 * Created on 8-Jul-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pk;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.LookAndFeel;

import com.sandwich.util.xml.XmlNinja;

/**
 * @author ctaylor
 */
public class ConfigDigester {
	private List<String> errorMsgs = new ArrayList<String>();
	private Config tmpConfig = null;
	private String fileName = null;

	public Config createConfig(Component parentToErrorDialogs) throws Exception {
		tmpConfig = new Config();
		XmlNinja digester = new XmlNinja(this)
				.addCallMethod("config/datasources/datasource", "addDataSource", "name", "driver", "url", "databaseDialectName")
				.addCallMethod("config/export/delimiterChar", "setDelimiterChar")
				.addCallMethod("config/export/encloseByChar", "setEncloseByChar")
				.addCallMethod("config/export/useCdata", "setUseCdata")
				.addCallMethod("config/export/headerColor", "setHeaderColor")
				.addCallMethod("config/export/borderColor", "setBorderColor")
				.addCallMethod("config/export/altRowColor", "setAltRowColor")
				.addCallMethod("config/general/lafs/laf", "addLookAndFeel", "class", "selected")
				.addCallMethod("config/general/history/mode", "setHistoryMode");
		digester.parse(fileName);
		tmpConfig.orderConnections();
		return tmpConfig;
	}

	public List<String> getErrorMessages(){
		return new ArrayList<String>(errorMsgs);
	}
	
	public void addDataSource(String name, String driver, String url,
			String databaseDialectName) {
		tmpConfig.addConnectionConfig(new ConnectionConfig()
			.setName(name).setDriver(driver).setUrl(url).setDatabaseDialectName(databaseDialectName));
	}

	public void setDelimiterChar(String argDelimiterChar) {
		tmpConfig.setExportDelimiterChar(argDelimiterChar);
	}

	public void setEncloseByChar(String argEncloseByChar) {
		tmpConfig.setEncloseByChar(argEncloseByChar);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setHeaderColor(String headerColor) {
		tmpConfig.setHeaderColor(headerColor);
	}

	public void setBorderColor(String borderColor) {
		tmpConfig.setBorderColor(borderColor);
	}

	public void setAltRowColor(String altRowColor) {
		tmpConfig.setAltRowColor(altRowColor);
	}

	public void setUseCdata(String useCdata) {
		tmpConfig.setUseCdata("true".equalsIgnoreCase(useCdata));
	}

	public void addLookAndFeel(String argClass, String argSelected) {
		String errorMsg = null;
		try {
			Class<?> tmpClass = Class.forName(argClass);
			tmpConfig.addLookandFeel((LookAndFeel) tmpClass.newInstance());
			if ("true".equals(argSelected)) {
				tmpConfig.setSelectedLookAndFeel((LookAndFeel) tmpClass.newInstance());
			}
		} catch (ClassNotFoundException ex) {
			errorMsg = "Could not find look and feel class: ";
		} catch (InstantiationException e) {
			errorMsg = "Could not create an instance of the selected look and feel with the class: ";
		} catch (IllegalAccessException e) {
			errorMsg = "Not permitted to create an instance of the selected look and feel class: ";
		}
		if (errorMsg != null) {
			errorMsgs.add(errorMsg + argClass + ". It will not be available.");
		}
	}

	public void setHistoryMode(String argHistoryMode) {
		int tmpHistoryMode = Config.HISTORY_MODE_ALL;
		try {
			tmpHistoryMode = Integer.valueOf(argHistoryMode);
		} catch (Throwable ex) {}
		if (	   tmpHistoryMode != Config.HISTORY_MODE_ALL
				&& tmpHistoryMode != Config.HISTORY_MODE_DIFF_FROM_LAST
				&& tmpHistoryMode != Config.HISTORY_MODE_UNIQUE) {
			tmpHistoryMode = Config.HISTORY_MODE_ALL;
		}
		tmpConfig.setHistoryMode(tmpHistoryMode);
	}

}