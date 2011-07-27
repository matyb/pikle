/*
 * Created on 8-Jul-2004
 *
 */
package com.pk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.LookAndFeel;

/**
 * @author ctaylor
 * 
 */
public class Config {
	public static final int HISTORY_MODE_UNIQUE = 1;
	public static final int HISTORY_MODE_DIFF_FROM_LAST = 2;
	public static final int HISTORY_MODE_ALL = 3;

	private List<ConnectionConfig> connectionConfigs = null;

	private List<LookAndFeel> lookandFeels = null;
	private LookAndFeel selectedLookAndFeel = null;

	private String exportDelimiterChar = null;

	private String encloseByChar = null;
	private String scriptLineDelimiter = null;

	private boolean useCdata = true;
	private String headerColor = null;
	private String borderColor = null;
	private String altRowColor = null;

	private int historyMode = Config.HISTORY_MODE_ALL;

	public Config() {
		super();
		connectionConfigs = new ArrayList<ConnectionConfig>();
	}

	/**
	 * @return Returns the connections.
	 */
	public List<ConnectionConfig> getConnections() {
		if (connectionConfigs == null) {
			connectionConfigs = new ArrayList<ConnectionConfig>();
		}
		return connectionConfigs;
	}

	/**
	 * @param connections
	 *            The connections to set.
	 */
	public void setConnections(List<ConnectionConfig> argConnectionConfigs) {
		this.connectionConfigs = argConnectionConfigs;
	}

	public void addConnectionConfig(ConnectionConfig argConnectionConfig) {
		connectionConfigs.add(argConnectionConfig);
	}

	public void orderConnections() {
		Collections.sort(getConnectionConfigs());
	}

	public void writeFile() {
		ConnectionConfig connectionConfig = null;
		StringBuffer buffer = new StringBuffer("");
		buffer.append("<?xml version=\"1.0\"?>\n");
		buffer.append("<config>\n");
		buffer.append("\t<datasources>\n");
		int size = 0;
		if (connectionConfigs != null) {
			size = connectionConfigs.size();
		}
		for (int x = 0; x < size; x++) {

			connectionConfig = (ConnectionConfig) connectionConfigs.get(x);
			buffer.append("\t\t<datasource>\n");
			buffer.append("\t\t\t<name>");
			buffer.append(connectionConfig.getName());
			buffer.append("</name>\n");
			buffer.append("\t\t\t<driver>");
			buffer.append(connectionConfig.getDriver());
			buffer.append("</driver>\n");
			buffer.append("\t\t\t<url>");
			buffer.append(connectionConfig.getUrl());
			buffer.append("</url>\n");
			buffer.append("\t\t\t<databaseDialectName>");
			buffer.append(connectionConfig.getDatabaseDialectName());
			buffer.append("</databaseDialectName>\n");
			buffer.append("\t\t</datasource>\n");
		}
		buffer.append("\t</datasources>\n");
		buffer.append("\t<export>\n");
		buffer.append("\t\t<delimiterChar>");
		buffer.append(getExportDelimiterChar());
		buffer.append("</delimiterChar>\n");

		buffer.append("\t\t<encloseByChar>");
		buffer.append(getEncloseByChar());
		buffer.append("</encloseByChar>\n");

		buffer.append("\t\t<useCdata>");
		buffer.append(String.valueOf(isUseCdata()));
		buffer.append("</useCdata>\n");

		buffer.append("\t\t<headerColor>");
		buffer.append(getHeaderColor());
		buffer.append("</headerColor>\n");

		buffer.append("\t\t<borderColor>");
		buffer.append(getBorderColor());
		buffer.append("</borderColor>\n");

		buffer.append("\t\t<altRowColor>");
		buffer.append(getAltRowColor());
		buffer.append("</altRowColor>\n");

		buffer.append("\t</export>\n");
		buffer.append("\t<general>\n");
		buffer.append("\t\t<lafs>\n");

		size = lookandFeels.size();
		LookAndFeel tmpLookAndFeel = null;
		for (int x = 0; x < size; x++) {
			tmpLookAndFeel = (LookAndFeel) lookandFeels.get(x);
			buffer.append("\t\t\t<laf>\n");
			buffer.append("\t\t\t\t<class>");
			buffer.append(tmpLookAndFeel.getClass().getName());
			buffer.append("</class>\n");
			if (selectedLookAndFeel.getName().equals(tmpLookAndFeel.getName())) {
				buffer.append("\t\t\t\t<selected>true</selected>\n");
			} else {
				buffer.append("\t\t\t\t<selected>false</selected>\n");
			}
			buffer.append("\t\t\t</laf>\n");
		}

		buffer.append("\t\t</lafs>\n");
		buffer.append("\t\t\t<history>\n");
		buffer.append("\t\t\t\t<mode>");
		buffer.append(String.valueOf(getHistoryMode()));
		buffer.append("</mode>\n");
		buffer.append("\t\t\t</history>\n");
		buffer.append("</general>\n");
		buffer.append("</config>\n");
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(new File("config.xml")));
			out.write(buffer.toString());
			out.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void removeConnection(ConnectionConfig argConnectionConfig) {
		ConnectionConfig tmp = null;
		String name = argConnectionConfig.getName();
		int size = connectionConfigs.size();
		for (int x = 0; x < size; x++) {
			tmp = (ConnectionConfig) connectionConfigs.get(x);
			if (tmp.getName().equals(name)) {
				connectionConfigs.remove(x);
				break;
			}
		}
	}

	/**
	 * @return Returns the encloseByChar.
	 */
	public String getEncloseByChar() {
		return encloseByChar;
	}

	/**
	 * @param encloseByChar
	 *            The encloseByChar to set.
	 */
	public void setEncloseByChar(String encloseByChar) {
		this.encloseByChar = encloseByChar;
	}

	/**
	 * @return Returns the exportDelimiterChar.
	 */
	public String getExportDelimiterChar() {
		return exportDelimiterChar;
	}

	/**
	 * @param exportDelimiterChar
	 *            The exportDelimiterChar to set.
	 */
	public void setExportDelimiterChar(String exportDelimiterChar) {
		this.exportDelimiterChar = exportDelimiterChar;
	}

	/**
	 * @return Returns the scriptLineDelimiter.
	 */
	public String getScriptLineDelimiter() {
		if (scriptLineDelimiter == null) {
			scriptLineDelimiter = "/";
		}
		return scriptLineDelimiter;
	}

	/**
	 * @param scriptLineDelimiter
	 *            The scriptLineDelimiter to set.
	 */
	public void setScriptLineDelimiter(String scriptLineDelimiter) {
		this.scriptLineDelimiter = scriptLineDelimiter;
	}

	/**
	 * @return Returns the useCdata.
	 */
	public boolean isUseCdata() {
		return useCdata;
	}

	/**
	 * @param useCdata
	 *            The useCdata to set.
	 */
	public void setUseCdata(boolean useCdata) {
		this.useCdata = useCdata;
	}

	/**
	 * @return Returns the altRowColor.
	 */
	public String getAltRowColor() {
		return altRowColor;
	}

	/**
	 * @param altRowColor
	 *            The altRowColor to set.
	 */
	public void setAltRowColor(String altRowColor) {
		this.altRowColor = altRowColor;
	}

	/**
	 * @return Returns the borderColor.
	 */
	public String getBorderColor() {
		return borderColor;
	}

	/**
	 * @param borderColor
	 *            The borderColor to set.
	 */
	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * @return Returns the connectionConfigs.
	 */
	public List<ConnectionConfig> getConnectionConfigs() {
		return connectionConfigs;
	}

	/**
	 * @param connectionConfigs
	 *            The connectionConfigs to set.
	 */
	public void setConnectionConfigs(List<ConnectionConfig> connectionConfigs) {
		this.connectionConfigs = connectionConfigs;
	}

	/**
	 * @return Returns the headerColor.
	 */
	public String getHeaderColor() {
		return headerColor;
	}

	/**
	 * @param headerColor
	 *            The headerColor to set.
	 */
	public void setHeaderColor(String headerColor) {
		this.headerColor = headerColor;
	}

	/**
	 * @return Returns the lookandFeels.
	 */
	public List<LookAndFeel> getLookandFeels() {
		return lookandFeels;
	}

	public int addLookandFeel(LookAndFeel argLookandFeel) {
		if (lookandFeels == null) {
			lookandFeels = new ArrayList<LookAndFeel>();
		}
		lookandFeels.add(argLookandFeel);
		return lookandFeels.size() - 1;
	}

	/**
	 * @return Returns the selectedLookAndFeel.
	 */
	public LookAndFeel getSelectedLookAndFeel() {
		return selectedLookAndFeel;
	}

	/**
	 * @param selectedLookAndFeel
	 *            The selectedLookAndFeel to set.
	 */
	public void setSelectedLookAndFeel(LookAndFeel selectedLookAndFeel) {
		this.selectedLookAndFeel = selectedLookAndFeel;
	}

	/**
	 * @return Returns the historyMode.
	 */
	public int getHistoryMode() {
		return historyMode;
	}

	/**
	 * @param historyMode
	 *            The historyMode to set.
	 */
	public void setHistoryMode(int historyMode) {
		this.historyMode = historyMode;
	}
}