/*
 * Created on Jul 3, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.pk;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author Isabelle
 */
public class DefaultDialect implements DatabaseDialect {

	public Vector<String> getKeywords() {
		Vector<String> keywords = new Vector<String>();
		keywords.add("add");
		keywords.add("all");
		keywords.add("alter");
		keywords.add("analyze");
		keywords.add("and");
		keywords.add("as");
		keywords.add("asc");
		keywords.add("asensitive");
		keywords.add("auto_increment");
		keywords.add("bdb");
		keywords.add("before");
		keywords.add("berkeleydb");
		keywords.add("between");
		keywords.add("both");
		keywords.add("by");
		keywords.add("call");
		keywords.add("cascade");
		keywords.add("case");
		keywords.add("change");
		keywords.add("check");
		keywords.add("collate");
		keywords.add("column");
		keywords.add("columns");
		keywords.add("condition");
		keywords.add("constraint");
		keywords.add("create");
		keywords.add("cross");
		keywords.add("cursor");
		keywords.add("database");
		keywords.add("databases");
		keywords.add("dec");
		keywords.add("declare");
		keywords.add("default");
		keywords.add("delete");
		keywords.add("desc");
		keywords.add("describe");
		keywords.add("deterministic");
		keywords.add("distinct");
		keywords.add("distinctrow");
		keywords.add("div");
		keywords.add("drop");
		keywords.add("else");
		keywords.add("elseif");
		keywords.add("enclosed");
		keywords.add("escaped");
		keywords.add("exists");
		keywords.add("explain");
		keywords.add("false");
		keywords.add("fetch");
		keywords.add("fields");
		keywords.add("for");
		keywords.add("force");
		keywords.add("foreign");
		keywords.add("found");
		keywords.add("from");
		keywords.add("fulltext");
		keywords.add("grant");
		keywords.add("group");
		keywords.add("having");
		keywords.add("if");
		keywords.add("ignore");
		keywords.add("in");
		keywords.add("index");
		keywords.add("infile");
		keywords.add("inner");
		keywords.add("insert");
		keywords.add("into");
		keywords.add("is");
		keywords.add("iterate");
		keywords.add("join");
		keywords.add("key");
		keywords.add("keys");
		keywords.add("leading");
		keywords.add("left");
		keywords.add("like");
		keywords.add("like");
		keywords.add("lock");
		keywords.add("not");
		keywords.add("null");
		keywords.add("on");
		keywords.add("optimize");
		keywords.add("or");
		keywords.add("order");
		keywords.add("outer");
		keywords.add("primary");
		keywords.add("rename");
		keywords.add("replace");
		keywords.add("right");
		keywords.add("select");
		keywords.add("set");
		keywords.add("show");
		keywords.add("starting");
		keywords.add("table");
		keywords.add("then");
		keywords.add("to");
		keywords.add("trailing");
		keywords.add("true");
		keywords.add("union");
		keywords.add("unique");
		keywords.add("update");
		keywords.add("use");
		keywords.add("values");
		keywords.add("when");
		keywords.add("where");
		keywords.add("while");
		keywords.add("with");
		keywords.add("verbose");
		return keywords;
	}

	public String getTableInfoSQLString(String argTableName, ConnectionInformation argConnectionInformation) {
		return null;
	}

	public String getIndexInfoSQLString(String argTableName, ConnectionInformation argConnectionInformation) {
		return null;
	}

	public String getProcedureInfoSQLString(String argProcedureName, ConnectionInformation argConnectionInformation) {
		return null;
	}

	public boolean isSelectStatement(String argStatement) {
		boolean isSelect = false;
		if (		argStatement.toUpperCase().startsWith("SELECT")
				||  argStatement.toUpperCase().startsWith("SHOW")
				||  argStatement.toUpperCase().startsWith("DESC")
				||  argStatement.toUpperCase().startsWith("EXPLAIN")) {
			isSelect = true;
		}
		return isSelect;
	}

	public boolean isDescribeStatement(String argStatement) {
		return false;
	}

	public String getDescribeSQLString(String argQuery, ConnectionInformation argConnectionInformation) {
		return null;
	}

	public String doExecutePlan(String argStatement, ConnectionInformation argConnectionInformation, Connection argConnection) throws SQLException {
		return null;
	}
	
	public String getPackageInfoSQLString(String argProcedureName, ConnectionInformation argConnectionInformation) {
		return null;
	}
}
