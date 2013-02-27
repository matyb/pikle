Install instructions: 
1) Unzip zip file into the folder you want to have pikle installed. 
2) Copy any .jar or .zip files that contain any JDBC drivers into the lib directory. 
3) Run run.sh or run.bat depending on which OS you're using.

Currently pikle supports 3 database dialects. New dialects are easy to add, simply place your compiled class file in the lib directory of your pikle installation.

database dialects avalable:
oracle (com.pk.OracleDialect)
mySQL (com.pk.MySQLDialect)
defaultDialect (com.pk.DefaultDialect)
MicrosoftSQLDialect (com.pk.MicrosoftSQLDialect)*

For full Firebird support you must run the included firebird_views.sql and use the Oracle dialect.

*Microsoft SQL Server is only partially supported.

Fixed issues:
1. apache commons dependancy is no more
2. defaults to oracle supported look and feel but kunstoff is still available in distribution
3. deleted a lot of dead code
4. code formatting and redundancy corrections
5. sql execution in pikle does not block the EDT (no more hour glass)

Known issues:
1)If you get the following error with MS SQL server "Can't start a cloned connection while in manual transaction mode."
Then you must set the SelectMethod property of the connection to Cursor. For example the URL should look like this
jdbc:microsoft:sqlserver://server1:1433;SelectMethod=Cursor 

Credits:
This program is a modification of a program called pklite, which in turn is a modification of a program called Pretty Kid (www.prettykid.com)
pklite was originally developed by sfn.chris@sympatico.ca
The look and feel was found at http://www.incors.org/
