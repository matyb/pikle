/*
 * Created on Dec 20, 2004
 *
 * @author ctaylor
 * 
 */
package com.pk;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * @author ctaylor
 *
 */
public class MicrosoftSQLDialect implements DatabaseDialect
{

    public List<String> getKeywords()
    {
		Vector<String> keywords = new Vector<String>();
		keywords.addElement("select");
		keywords.addElement("set");
		keywords.addElement("or");
		keywords.addElement("and");
		keywords.addElement("where");
		keywords.addElement("from");
		keywords.addElement("insert");
		keywords.addElement("update");
		keywords.addElement("order");
		keywords.addElement("by");
		keywords.addElement("close");
		keywords.addElement("commit");
		keywords.addElement("rollback");
		keywords.addElement("create");
		keywords.addElement("database");
		keywords.addElement("drop");
		keywords.addElement("table");
		keywords.addElement("view");
		keywords.addElement("join");
		keywords.addElement("into");
		keywords.addElement("between");
		keywords.addElement("values");
		keywords.addElement("alter");
		keywords.addElement("add");
		keywords.addElement("modify");
		keywords.addElement("close");
		keywords.addElement("distinct");
		keywords.addElement("index");
		keywords.addElement("grant");
		keywords.addElement("delete");
		keywords.addElement("group");
		keywords.addElement("as");
		keywords.addElement("having");
		keywords.addElement("delete");
		keywords.addElement("like");
		keywords.addElement("unique");
		keywords.addElement("primary");
		keywords.addElement("key");
		keywords.addElement("not");
		keywords.addElement("null");
		keywords.addElement("asc");
		keywords.addElement("desc");
		keywords.addElement("revoke");
		keywords.addElement("remark");
		keywords.addElement("exists");
		keywords.addElement("union");
		keywords.addElement("decode");


		return keywords;
    }

    /* (non-Javadoc)
     * @see com.pk.DatabaseDialect#getTableInfoSQLString(java.lang.String, com.pk.ConnectionInformation)
     */
    public String getTableInfoSQLString(String argTableName, ConnectionInformation argConnectionInformation)
    {
        return "select COLUMN_NAME, IS_NULLABLE, DATA_TYPE, case when CHARACTER_MAXIMUM_LENGTH is not null then CHARACTER_MAXIMUM_LENGTH when NUMERIC_PRECISION is not null then str(NUMERIC_PRECISION) when DATETIME_PRECISION is not null then str(DATETIME_PRECISION) end as 'SIZE', NUMERIC_PRECISION as FIELD_PRECISION from INFORMATION_SCHEMA.COLUMNS where TABLE_NAME = '" + argTableName +"'";
    }

    /* (non-Javadoc)
     * @see com.pk.DatabaseDialect#getIndexInfoSQLString(java.lang.String, com.pk.ConnectionInformation)
     */
    public String getIndexInfoSQLString(String argTableName, ConnectionInformation argConnectionInformation)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.pk.DatabaseDialect#getProcedureInfoSQLString(java.lang.String, com.pk.ConnectionInformation)
     */
    public String getProcedureInfoSQLString(String argProcedureName, ConnectionInformation argConnectionInformation)
    {
        return "select ROUTINE_NAME, ROUTINE_DEFINITION from INFORMATION_SCHEMA.ROUTINES where ROUTINE_TYPE = 'PROCEDURE' and ROUTINE_NAME = " + argProcedureName;
    }

    /* (non-Javadoc)
     * @see com.pk.DatabaseDialect#getPackageInfoSQLString(java.lang.String, com.pk.ConnectionInformation)
     */
    public String getPackageInfoSQLString(String argProcedureName, ConnectionInformation argConnectionInformation)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.pk.DatabaseDialect#isSelectStatement(java.lang.String)
     */
    public boolean isSelectStatement(String argStatement)
    {
		boolean isSelect = false;
		if(argStatement.toUpperCase().startsWith("SELECT") || argStatement.toUpperCase().startsWith("SHOW") )
		{
			isSelect = true;
		}
		return isSelect;
    }

    /* (non-Javadoc)
     * @see com.pk.DatabaseDialect#isDescribeStatement(java.lang.String)
     */
    public boolean isDescribeStatement(String argStatement)
    {
		boolean isDescribe = false;
		if(argStatement.toUpperCase().startsWith("DESC"))
		{
			isDescribe = true;
		}
		return isDescribe;
    }

    /* (non-Javadoc)
     * @see com.pk.DatabaseDialect#getDescribeSQLString(java.lang.String, com.pk.ConnectionInformation)
     */
    public String getDescribeSQLString(String argQuery, ConnectionInformation argConnectionInformation)
    {
        String tName = argQuery.substring(5);
        return getTableInfoSQLString(tName,argConnectionInformation);
    }

    /* (non-Javadoc)
     * @see com.pk.DatabaseDialect#doExecutePlan(java.lang.String, com.pk.ConnectionInformation, java.sql.Connection)
     */
    public String doExecutePlan(String argStatement, ConnectionInformation argConnectionInformation, Connection argConnection) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

}
