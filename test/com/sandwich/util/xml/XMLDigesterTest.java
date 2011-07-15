package com.sandwich.util.xml;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.sandwich.util.Digester;


public class XMLDigesterTest {

	private String toString = "This Test";
	
	private String addDataSourceCalls, setDelimiterCalls, setEnclosedByCharCalls, setUseCdataCalls, 
		setHeaderColorCalls, setBorderColorCalls, setAltRowColorCalls, addLookAndFeelCalls, setHistoryModeCalls;
	
	@Before
	public void setUp(){
		addDataSourceCalls = setDelimiterCalls = setEnclosedByCharCalls = setUseCdataCalls = 
		setHeaderColorCalls = setBorderColorCalls = setAltRowColorCalls = addLookAndFeelCalls = setHistoryModeCalls = "";
	}
	
	@Test
	public void testAddingMethodCalls() throws Exception {
        Digester digester = createAndAddMethodCalls();
		assertEquals(new StringBuilder(
				XMLDigester.class.getSimpleName()).append("[Receiver: "+toString+" [").append(
				"[DeferredInvocation [path=[config, datasources, datasource], method=addDataSource, elements=[name, driver, url, databaseDialectName]], ").append(
				 "DeferredInvocation [path=[config, export], method=setDelimiterChar, elements=[delimiterChar]], ").append(
				 "DeferredInvocation [path=[config, export], method=setEncloseByChar, elements=[encloseByChar]], ").append(
				 "DeferredInvocation [path=[config, export], method=setUseCdata, elements=[useCdata]], ").append(
				 "DeferredInvocation [path=[config, export], method=setHeaderColor, elements=[headerColor]], ").append(
				 "DeferredInvocation [path=[config, export], method=setBorderColor, elements=[borderColor]], ").append(
				 "DeferredInvocation [path=[config, export], method=setAltRowColor, elements=[altRowColor]], ").append(
				 "DeferredInvocation [path=[config, general, lafs, laf], method=addLookAndFeel, elements=[class, selected]], ").append(
				 "DeferredInvocation [path=[config, general, history], method=setHistoryMode, elements=[mode]]]]]").toString(),
				digester.toString());
	}
	
	@Test
	public void testParsing(){
		Digester digester = createAndAddMethodCalls();
		digester.parse("./test/com/sandwich/util/xml/config.xml");
		assertEquals(new StringBuilder
					 ("aTest,com.mysql.jdbc.Driver,jdbc:mysql://localhost:3306/test,com.pk.MySQLDialect").append
					 ("HsqlDataSource,org.hsqldb.jdbcDriver,jdbc:mysql://localhost:3306/test555,com.pk.OracleDialect").append
					 ("test,com.mysql.jdbc.Driver,jdbc:mysql://localhost:3306/test,com.pk.MySQLDialect").toString()
					, addDataSourceCalls);
		assertEquals(new StringBuilder
					 ("com.incors.plaf.kunststoff.KunststoffLookAndFeel,true").append
					 ("javax.swing.plaf.metal.MetalLookAndFeel,false").append
					 ("com.sun.java.swing.plaf.windows.WindowsLookAndFeel,false").append
					 ("com.sun.java.swing.plaf.motif.MotifLookAndFeel,false").toString()
					, addLookAndFeelCalls);
		assertEquals("#b9ffb9", setAltRowColorCalls);
		assertEquals("green",	setBorderColorCalls);
		assertEquals(",",		setDelimiterCalls);
		assertEquals("\"",		setEnclosedByCharCalls);
		assertEquals("#ffff80",	setHeaderColorCalls);
		assertEquals("2",		setHistoryModeCalls);
		assertEquals("true",	setUseCdataCalls);
	}
	
	private Digester createAndAddMethodCalls() {
		return new XMLDigester(this)
			.addCallMethod("config/datasources/datasource", "addDataSource", "name", "driver", "url", "databaseDialectName")
	        .addCallMethod("config/export/delimiterChar",	"setDelimiterChar")
	        .addCallMethod("config/export/encloseByChar",	"setEncloseByChar")
	        .addCallMethod("config/export/useCdata",		"setUseCdata")
	        .addCallMethod("config/export/headerColor",		"setHeaderColor")
	        .addCallMethod("config/export/borderColor",		"setBorderColor")
	        .addCallMethod("config/export/altRowColor",		"setAltRowColor")
	        .addCallMethod("config/general/lafs/laf",		"addLookAndFeel", "class", "selected")
	        .addCallMethod("config/general/history/mode",	"setHistoryMode");
	}

	public void addDataSource(String name, String driver, String url, String databaseDialectName){
		addDataSourceCalls += append(name, driver, url, databaseDialectName);
	}
	
	public void setDelimiterChar(String delimiterChar){
		setDelimiterCalls += append(delimiterChar);
	}
	
	public void setEncloseByChar(String encloseByChar){
		setEnclosedByCharCalls += append(encloseByChar); 
	}
	
	public void setUseCdata(String useCdata){
		setUseCdataCalls += append(useCdata);
	}
	
	public void setHeaderColor(String headerColor){
		setHeaderColorCalls += append(headerColor);
	}
	
	public void setBorderColor(String borderColor){
		setBorderColorCalls += append(borderColor);
	}

	public void setAltRowColor(String altRowColor){
		setAltRowColorCalls += append(altRowColor);
	}
	
	public void addLookAndFeel(String clazz, String selected){
		addLookAndFeelCalls += append(clazz, selected);
	}
	
	public void setHistoryMode(String historyMode){
		setHistoryModeCalls += append(historyMode);
	}
	
	public String toString(){
		return toString;
	}
	
	private String append(String...strings){
		String base = strings[0];
		for(int i = 1; i < strings.length; i++){
			base+=","+strings[i];
		}
		return base;
	}
	
}
