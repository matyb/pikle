PKLite SQL Version 2 Beta 3

Install instructions: 
1) unzip zip file into the folder you want to have pklite installed. 
2) Copy any .jar or .zip files that contain any JDBC drivers into the lib directory. 
3) Call java -jar startup.jar that’s it have fun hope you like the program and if you have any questions just email me. (sfn.chris@sympatico.ca)
4) Enter any database information in the datasource section of the preferences windows. 

For this release there are only three database dialects available others will be added
as they are created.  You can create your own by implementing the com.pk.DatabaseDialect interface.
Once you class is created package it in a jar or zip and place it in the lib directory of you pklite
SQL client installation.
If you do create more I would ask that you send them to me so I can add them to the next release.


database dialects avalable:
oracle (com.pk.OracleDialect)
mySQL (com.pk.MySQLDialect)
defaultDialect (com.pk.DefaultDialect)
MicrosoftSQLDialect (com.pk.MicrosoftSQLDialect)

For full Firebird support you must run the included firebird_views.sql and use the Oracle dialect.

What's New
This is mostly a code refactoring and stabilizing build.  Limited support for Microsoft SQL Server was added.

Fixed issues:
1. When a SQL file is load the carriage return was removed.
2. Data is not lost when the window is maximized.
3. Only unique SQL statements will be placed in the history.
	This is configurable through the preferences under general




Known issues:
1)If you get the following error with MS SQL server "Can't start a cloned connection while in manual transaction mode."
Then you must set the SelectMethod property of the connection to Cursor. For example the URL should look like this
jdbc:microsoft:sqlserver://server1:1433;SelectMethod=Cursor 
2)If you find any other issues please notify me using the contact info listed below. 

Credits:
This program is a modification of a program call Pretty Kid (www.prettykid.com)
The look and feel was found at http://www.incors.org/

Contact Information:
for more info about this program visit http://pklite.sourceforge.net 
or email me @ sfn.chris@sympatico.ca


