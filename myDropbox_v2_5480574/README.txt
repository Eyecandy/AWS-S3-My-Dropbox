


Classes:
Runner - starts up application
Parser - parses and validates command typed in.
CommandHandler - executes commands given S3Connection & RDS Connections correct.
S3Service - contains all S3 related services.
RDSService- contains all RDS services
User - singleton which has id & S3connection attributes and getter/setter.
MessageHandler - called to print some errors and success messages.

Other files:
mysql.properties - this file is read when connecting to RDS db.

CommandHandler contains global Variable bucketName, change this string variable to connect to your S3 bucket.

This application can be started from netbeans IDE  by pressing the play button or by going to project folder (myDropbox_5480575) —> store. In store the folder there is a fat jar named myDropbox_5480574.jar. To run it type in: java -jar myDropbox_5480574.jar.

 
The parser only understands lower case letters for key words, like in the example from the slides. For example login will work, but Login will not.
And gives at least OK, when command is correctly typed in.except for quit, which will give a good bye message.
Here are the commands:

1.newuser <username> password <password>

Creates a new entry in RDS-database, username and password + surrogate key (auto increment)

2.login <username> <password>

Establish connection to RDS-database by prepared sql statement, retrieving userid, if username & password exists. (Not yet encrypted). Also gives User class a S3connection as well as an Id.This only works, if no user is already logged in.

3. put <filepath>
uses s3Connection if exists of user and Id to upload to correct folder.


4. get <filepath>
uses s3Connection if exists of user and User Id to download from correct folder. 

5. view
uses s3Connection if exists of user and User Id to find files in correct folder.

6.logout
Set s3Connection to null & user id to null. If no user logged in does nothing.
7. quit
Sets running = false, exits main while loop and returns, hence exiting the application.







