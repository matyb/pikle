/*
 * Created on Jul 2, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.pk;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Isabelle
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface DatabaseDialect
{
	public List<String> getKeywords();
	public String getTableInfoSQLString(String argTableName, ConnectionInformation argConnectionInformation);
	public String getIndexInfoSQLString(String argTableName, ConnectionInformation argConnectionInformation);
	public String getProcedureInfoSQLString(String argProcedureName, ConnectionInformation argConnectionInformation);
	public String getPackageInfoSQLString(String argProcedureName, ConnectionInformation argConnectionInformation);
	
	//this method should return true if the query will return a resultset
	//If the query is an update or modify statement it should return false  
	public boolean isSelectStatement(String argStatement);
	
	//this method should return true if the databse dialect needs to modify the query
	//to display table information. 
	//i.e the Oracle database could use the following query to do a 'describe' on a table
	//SELECT COLUMN_NAME, DECODE(NULLABLE,'N', 'NOT NULL', 'Y', NULL)  AS NULLABLE, DATA_TYPE, DECODE(DATA_TYPE, 'VARCHAR2', TO_CHAR(DATA_LENGTH), 'NUMBER', DECODE(DATA_SCALE,0,TO_CHAR(DATA_PRECISION),NULL,NULL,DATA_PRECISION||','||DATA_SCALE)) AS \SIZE\ FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =  <Table Name>  ORDER BY COLUMN_ID
	public boolean isDescribeStatement(String argStatement);
	
	//This method should take the text that was entered in the text box and transform it in to 
	// a SQL statement that will return all information on the specified table.
	public String getDescribeSQLString(String argQuery, ConnectionInformation argConnectionInformation);
	
	public String doExecutePlan(String argStatement, ConnectionInformation argConnectionInformation, Connection argConnection) throws SQLException;
}
