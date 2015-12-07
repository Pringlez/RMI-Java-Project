# Distributed Systems RMI Project
## John Walsh

Project Details
---------------
0. The current version of the cypher server is multithreaded, it will run each decypher request on its own thread.
0. The tomcat client is built to handle multiple requests from end users. Each request will be queued and sent off to the decypher server.
0. When decypher is complete, it will return the result to the specific user that requested the decypher. Usually keys higher then 5 will take a considerable amount of time to decypher. 


Usage
-----
To run the java cypher server, open a command prompt / terminal window. Navigate to the directory containing the 'vigenere.jar' file and run the following command. Also make sure to include the 'WarAndPeace.txt' file to allow the application build the quadgrams.

```
java –cp ./vigenere.jar ie.gmit.sw.Servant
```

To run the tomcat client, drop the 'cracker.war' file into the 'webapps' directory in your tomcat installation. This should automatically deploy the application.

You can also edit the web.xml file if you wish to point the tomcat client to a different machine running the java cypher server on your local network / internet connection. It's located in the 'cracker' directory in the 'webapps' of your tomcat installation after deploying the 'cracker.war' file correctly.