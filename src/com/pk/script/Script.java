/*
 * Created on Sep 19, 2004
 * Chris Taylor
 */
package com.pk.script;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import com.pk.ConnectionInformation;
import com.pk.DatabaseDialect;

/**
 * @author Chris Taylor
 * 
 */
public class Script {
	public static final int NEW_COMMAND_INSERT_END_MODE = 0;
	public static final int NEW_COMMAND_INSERT_BEGINNING_MODE = 1;
	public static final int NEW_COMMAND_INSERT_AT_CURSER_MODE = 2;
	public static final int NEW_COMMAND_REPLACE_CURRENT_SCRIPT_MODE = 3;

	private List<ScriptCommand> commands = null;
	private Date startDate = null;
	private Date endDate = null;
	private ConnectionInformation connectionInformation = null;
	private Connection connection = null;

	/**
	 * @return Returns the commands.
	 */
	public List<ScriptCommand> getCommands() {
		if (commands == null) {
			commands = new ArrayList<ScriptCommand>();
		}
		return commands;
	}

	public void setCommands(ArrayList<ScriptCommand> commands) {
		this.commands = commands;
	}
	
	/**
	 * @return Returns the endDate.
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            The endDate to set.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return Returns the startDate.
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            The startDate to set.
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void removeCmmand(int argIndex) {
		if (commands == null)
			throw new ArrayIndexOutOfBoundsException();
		commands.remove(argIndex);
	}

	public void clearCommands() {
		if (commands != null) {
			commands.clear();
		}
		startDate = null;
		endDate = null;
	}

	public void parseScriptFile(String argPath, String argDelimiter) {
		if (argPath == null || argDelimiter == null) {
			throw new IllegalArgumentException();
		}

		FileReader tmpFile = null;
		BufferedReader in = null;
		StringBuffer fileString = new StringBuffer();
		String tmp = null;
		StringTokenizer tokenizer = null;
		ScriptCommand scriptCommand = null;
		String token = null;
		try {
			tmpFile = new FileReader(argPath);
			in = new BufferedReader(tmpFile);
			tmp = in.readLine();
			while (tmp != null) {
				fileString.append(tmp + "\n");
				tmp = in.readLine();
			}
			tokenizer = new StringTokenizer(fileString.toString(),
					argDelimiter, false);
			commands = new ArrayList<ScriptCommand>();
			while (tokenizer.hasMoreTokens()) {
				token = tokenizer.nextToken();
				if (token.length() == 1
						&& Character.isWhitespace(token.toCharArray()[0])) {
					// if the only character is white space skip it.
					continue;
				}
				scriptCommand = new ScriptCommand();
				scriptCommand.setCommand(token);
				commands.add(scriptCommand);
			}

		} catch (FileNotFoundException e) {
			// TODO Display error msg here.
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		}

	}

	public void parseScriptFromClipBoard(String argScriptText,
			String argDelimiter, int insertMode, int argCurserPosition) {
		String token = null;
		StringTokenizer tokenizer = null;
		ScriptCommand scriptCommand = null;
		tokenizer = new StringTokenizer(argScriptText, argDelimiter, false);
		List<ScriptCommand> newCommands = new ArrayList<ScriptCommand>();
		while (tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			if (token.length() == 1
					&& Character.isWhitespace(token.toCharArray()[0])) {
				// if the only character is white space skip it.
				continue;
			}
			scriptCommand = new ScriptCommand();
			scriptCommand.setCommand(token);
			newCommands.add(scriptCommand);
		}
		if (commands == null || insertMode == NEW_COMMAND_REPLACE_CURRENT_SCRIPT_MODE) {
			commands = new ArrayList<ScriptCommand>();
		}

		if (insertMode == NEW_COMMAND_INSERT_BEGINNING_MODE) {
			commands.addAll(0, newCommands);
		} else if (insertMode == NEW_COMMAND_INSERT_AT_CURSER_MODE) {
			if (argCurserPosition < 0) {
				commands.addAll(0, newCommands);
			} else {
				commands.addAll(argCurserPosition, newCommands);
			}
		} else {
			commands.addAll(newCommands);
		}

	}

	public int getNumberOfCommands() {
		if (commands == null) {
			return 0;
		}
		return commands.size();
	}

	public ScriptCommand getCommand(int argIndex) {
		if (commands == null) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return (ScriptCommand) commands.get(argIndex);
	}

	public void run() {
		if (commands == null || connection == null) {
			throw new IllegalStateException();
		}
		startDate = new Date();
		ScriptCommand scriptCommand = null;
		int size = commands.size();
		for (int x = 0; x < size; x++) {
			scriptCommand = (ScriptCommand) commands.get(x);
			scriptCommand.setStatus(ScriptCommand.STATUS_NOT_RUN);
			if (scriptCommand.isRun()) {
				try {
					prexecuteSQL(scriptCommand.getCommand());
					scriptCommand.setStatus(ScriptCommand.STATUS_SUCCESS);
					scriptCommand.setErrorMessage(null);
				} catch (SQLException ex) {
					scriptCommand.setStatus(ScriptCommand.STATUS_FAILED);
					scriptCommand.setErrorMessage(ex.getMessage());
				}
			}
		}
		endDate = new Date();
	}

	/**
	 * @return Returns the connection.
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            The connection to set.
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	/**
	 * @return Returns the connectionInformation.
	 */
	public ConnectionInformation getConnectionInformation() {
		return connectionInformation;
	}

	/**
	 * @param connectionInformation
	 *            The connectionInformation to set.
	 */
	public void setConnectionInformation(
			ConnectionInformation connectionInformation) {
		this.connectionInformation = connectionInformation;
	}

	public void prexecuteSQL(String argQuery) throws SQLException {
		DatabaseDialect tmpDatabaseDialect = getConnectionInformation()
				.getDatabaseDialect();

		argQuery = commentHandling(argQuery);

		argQuery = argQuery.trim();

		if (tmpDatabaseDialect.isSelectStatement(argQuery)) {
			executeSQL(argQuery);
		} else if (tmpDatabaseDialect.isDescribeStatement(argQuery)) {
			// query = query.substring(5);
			executeDescSQL(argQuery);
		} else if (argQuery.toUpperCase().equals("COMMIT")) {
			connection.commit();
		} else if (argQuery.toUpperCase().equals("ROLLBACK")) {
			connection.rollback();
		} else {
			updateSQL(argQuery);
		}
	}

	public String commentHandling(String query) {
		query = query.trim();
		int cmtAstStart = query.indexOf("/*");
		int cmtDasStart = query.indexOf("--");
		while (cmtAstStart != -1 || cmtDasStart != -1) {
			if (cmtAstStart != -1) {
				int cmtEnd = query.indexOf("*/");
				StringBuffer queryStrBuffer = new StringBuffer(query);
				queryStrBuffer = queryStrBuffer.delete(cmtAstStart, cmtEnd + 2);
				queryStrBuffer = queryStrBuffer.append(" ");
				query = queryStrBuffer.toString();
			}

			if (cmtDasStart != -1) {
				int cmtEnd = query.indexOf("\n", cmtDasStart);

				StringBuffer queryStrBuffer = new StringBuffer(query);
				queryStrBuffer = queryStrBuffer.delete(cmtDasStart, cmtEnd);
				queryStrBuffer = queryStrBuffer.append(" ");
				query = queryStrBuffer.toString();
			}
			cmtAstStart = query.indexOf("/*");
			cmtDasStart = query.indexOf("--");
		}
		return query;
	}

	public void executeSQL(String argQuery) throws SQLException {

		if (argQuery == null) // If there's nothing we are done
			return;
		Statement statement = connection.createStatement();
		statement.executeQuery(argQuery);

	}

	public void executeDescSQL(String argQuery) throws SQLException {

		ConnectionInformation tmpConnectionInformation = getConnectionInformation();
		// String queryDesc =
		// "SELECT COLUMN_NAME, DECODE(NULLABLE,'N', 'NOT NULL', 'Y', NULL)  AS NULLABLE, "
		// +
		// "DATA_TYPE, DECODE(DATA_TYPE, 'VARCHAR2', TO_CHAR(DATA_LENGTH), 'NUMBER', "
		// +
		// "DECODE(DATA_SCALE,0,TO_CHAR(DATA_PRECISION),NULL,NULL,DATA_PRECISION||','||DATA_SCALE)) AS \"SIZE\" "
		// + "FROM ALL_TAB_COLUMNS " + "WHERE TABLE_NAME = '" + tName + "'" +
		// " ORDER BY COLUMN_ID";
		String queryDesc = tmpConnectionInformation.getDatabaseDialect()
				.getDescribeSQLString(argQuery, tmpConnectionInformation);

		if (queryDesc == null) // If there's nothing we are done
			return;

		Statement statement = connection.createStatement();
		statement.executeQuery(queryDesc);
	}

	public void updateSQL(String argQuery) throws SQLException {

		if (argQuery == null) // If there's nothing we are done
			return;
		Statement statement = connection.createStatement();
		statement.executeUpdate(argQuery);
	}

	public void exportResultsDelimited(String argPath, String argDelimiter,
			String argEncloseByChar) {
		File tmpFile = new File(argPath);

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(tmpFile));
		} catch (Exception ex) {
			// TODO report error to user
			ex.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
