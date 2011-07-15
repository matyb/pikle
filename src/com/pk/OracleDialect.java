/*
 * Created on Jul 2, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.pk;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

/**
 * @author Isabelle
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OracleDialect implements DatabaseDialect {
	public static final String QUERYPLANSQL = "SELECT ' '||level||'      ' lev, lpad(operation, length(operation) + 4*(level-1)) || decode(id, 0, '   Optimizer='||optimizer, null) ||'  '||options||\n"
			+ "decode(object_name,null,null,'  OF  ')||object_name||\n"
			+ "decode(object_type,'UNIQUE', ' (U) ', 'NON-UNIQUE',\n"
			+ "'(NU)',null) plan \n"
			+ "FROM   PLAN_TABLE \n"
			+ "START with ID = 0 and STATEMENT_ID = 'pk00001' \n"
			+ "CONNECT by prior ID = PARENT_ID and STATEMENT_ID = 'pk00001' \n";

	public static final String DELETEPLANSQL = "DELETE FROM PLAN_TABLE";

	public List<String> getKeywords() {
		List<String> keywords = new Vector<String>();
		keywords.add("select");
		keywords.add("set");
		keywords.add("or");
		keywords.add("and");
		keywords.add("where");
		keywords.add("from");
		keywords.add("insert");
		keywords.add("update");
		keywords.add("order");
		keywords.add("by");
		keywords.add("close");
		keywords.add("commit");
		keywords.add("rollback");
		keywords.add("create");
		keywords.add("database");
		keywords.add("drop");
		keywords.add("table");
		keywords.add("view");
		keywords.add("join");
		keywords.add("into");
		keywords.add("between");
		keywords.add("values");
		keywords.add("alter");
		keywords.add("add");
		keywords.add("modify");
		keywords.add("close");
		keywords.add("distinct");
		keywords.add("index");
		keywords.add("grant");
		keywords.add("delete");
		keywords.add("group");
		keywords.add("as");
		keywords.add("having");
		keywords.add("delete");
		keywords.add("like");
		keywords.add("unique");
		keywords.add("primary");
		keywords.add("key");
		keywords.add("not");
		keywords.add("null");
		keywords.add("asc");
		keywords.add("desc");
		keywords.add("revoke");
		keywords.add("remark");
		keywords.add("exists");
		keywords.add("union");
		keywords.add("decode");

		return keywords;
	}

	public String getTableInfoSQLString(String argTableName,
			ConnectionInformation argConnectionInformation) {
		String sqlString = "SELECT COLUMN_NAME, DECODE(NULLABLE,'N', 'NOT NULL', 'Y', NULL)  AS NULLABLE, "
				+ "DATA_TYPE, DECODE(DATA_TYPE, 'VARCHAR2', TO_CHAR(DATA_LENGTH), 'NUMBER', "
				+ "DECODE(DATA_SCALE,0,TO_CHAR(DATA_PRECISION),NULL,NULL,DATA_PRECISION||','||DATA_SCALE)) AS \"SIZE\" "
				+ "FROM USER_TAB_COLUMNS "
				+ "WHERE TABLE_NAME = '"
				+ argTableName + "'" + " ORDER BY COLUMN_ID";
		return sqlString;
	}

	public String getIndexInfoSQLString(String argTableName,
			ConnectionInformation argConnectionInformation) {
		String sqlString = "SELECT TABLE_NAME,INDEX_NAME, "
				+ "COLUMN_POSITION AS ID,COLUMN_NAME "
				+ "FROM USER_IND_COLUMNS " + "WHERE   TABLE_NAME  LIKE UPPER('"
				+ argTableName + "')||'%' "
				+ "ORDER BY TABLE_NAME,INDEX_NAME,COLUMN_POSITION";
		return sqlString;
	}

	public String getProcedureInfoSQLString(String argProcedureName,
			ConnectionInformation argConnectionInformation) {
		String sqlString = "SELECT DECODE(UPPER(TRIM(TEXT)),'END;',TEXT,SUBSTR(TEXT,1,LENGTH(TEXT)-1)) AS \"PROCEDURE SOURCE\" FROM USER_SOURCE "
				+ "WHERE TYPE='PROCEDURE' "
				+ "AND   NAME='"
				+ argProcedureName
				+ "'" + "ORDER BY LINE";
		return sqlString;
	}
	
	public boolean isSelectStatement(String argStatement) {
		boolean isSelect = false;
		if (argStatement.toUpperCase().startsWith("SELECT")
				|| argStatement.toUpperCase().startsWith("SHOW")) {
			isSelect = true;
		}
		return isSelect;
	}

	public boolean isDescribeStatement(String argStatement) {
		boolean isDescribe = false;
		if (argStatement.toUpperCase().startsWith("DESC")) {
			isDescribe = true;
		}
		return isDescribe;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pk.DatabaseDialect#getDescribeSQLString(java.lang.String,
	 * com.pk.ConnectionInformation)
	 */
	public String getDescribeSQLString(String argQuery,
			ConnectionInformation argConnectionInformation) {
		String tName = argQuery.substring(5);
		String describeSQLString = "SELECT COLUMN_NAME, DECODE(NULLABLE,'N', 'NOT NULL', 'Y', NULL)  AS NULLABLE, "
				+ "DATA_TYPE, DECODE(DATA_TYPE, 'VARCHAR2', TO_CHAR(DATA_LENGTH), 'NUMBER', "
				+ "DECODE(DATA_SCALE,0,TO_CHAR(DATA_PRECISION),NULL,NULL,DATA_PRECISION||','||DATA_SCALE)) AS \"SIZE\" "
				+ "FROM ALL_TAB_COLUMNS "
				+ "WHERE TABLE_NAME = '"
				+ tName.toUpperCase() + "'" + " ORDER BY COLUMN_ID";
		return describeSQLString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pk.DatabaseDialect#doExecutePlan(java.lang.String,
	 * com.pk.ConnectionInformation, java.sql.Connection)
	 */
	public String doExecutePlan(String argStatement,
			ConnectionInformation argConnectionInformation,
			Connection argConnection) throws SQLException {
		String query = "EXPLAIN PLAN SET STATEMENT_ID = 'pk00001' FOR "
				+ argStatement;
		String planOut = "";
		Statement statement = argConnection.createStatement();
		statement.executeQuery(query);

		ResultSet planResult = statement.executeQuery(QUERYPLANSQL);

		while (planResult.next()) {
			planOut = planOut
					+ // planResult.getString("id") +
					// planResult.getString("p_id") +
					planResult.getString("lev") + planResult.getString("plan")
					+ "\n";
		}

		statement.executeUpdate(DELETEPLANSQL);

		return planOut;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pk.DatabaseDialect#getPackageInfoSQLString(java.lang.String,
	 * com.pk.ConnectionInformation)
	 */
	public String getPackageInfoSQLString(String argProcedureName,
			ConnectionInformation argConnectionInformation) {
		return "SELECT DECODE(UPPER(TRIM(TEXT)),'END;',TEXT,SUBSTR(TEXT,1,LENGTH(TEXT)-1)) AS \"PACKAGE SOURCE\" FROM USER_SOURCE "
				+ "WHERE TYPE='PACKAGE' "
				+ "AND   NAME='"
				+ argProcedureName
				+ "'" + "ORDER BY LINE";
	}

}
