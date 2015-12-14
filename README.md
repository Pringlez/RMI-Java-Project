# Distributed Systems RMI Project
## John Walsh

Project Details
---------------
0. The current version of the cypher server is multithreaded, it will run each decypher request on its own thread. This could be useful because multiple tomcat servlets can send requests to the cypher server and return the result.
0. The tomcat client is built to handle multiple requests from end users. Each request will be queued and sent off to the decypher server asynchronously.
0. When the decypher is complete, it will return the result to the specific user that requested the decypher request. Usually keys higher than 5 will take a considerable amount of time to decypher, be patient.


Usage
-----
The tomcat client & cypher server have been developed & tested on a Windows environment, confirmed working. A Linux environment shouldn't have any problems running the application.

To run the java cypher server, open a command prompt / terminal window. Navigate to the directory containing the 'vigenere.jar' file and run the following command. Also make sure to include the 'WarAndPeace.txt' file to allow the application build the quadgrams.

```
java –cp ./vigenere.jar ie.gmit.sw.Servant
```

To run the tomcat client, drop the 'cracker.war' file into the 'webapps' directory in your tomcat installation. This should automatically deploy the application. The web.xml file is currently pointing the 'RMI_SERVER' variable to 'localhost'. You should be able to run the application locally with no problems.

You can also edit the web.xml file if you wish to point the tomcat client to a different machine running the java cypher server on your local network / internet connection. It's located in the 'cracker' directory in the 'webapps' of your tomcat installation after deploying the 'cracker.war' file correctly.

Test Case
---------
The following example uses text from 'War and Peace' to decypher. First, start the cypher server using the provided jar file and deploy the tomcat client war file to your tomcat installation.

The following plain text was cyphered using the key 'BAGS'.

### Plain Text
WELLPRINCESOGENOAANDLUCCAARENOWJUSTFAMILYESTATESOFTHEBUONAPARTESBUTIWARNYOUIFYOUDONTTELLMETHATTHISMEANSWARIFYOUSTILLTRYTODEFENDTHEINFAMIESANDHORRORSPERPETRATEDBYTHATANTICHRISTIREALLYBELIEVEHEISANTICHRISTIWILLHAVENOTHINGMORE

### Cypher Text
XERDQROFDEYGHETGBATVMUIUBAXWOOCBVSZXBMODZEYLBTKKPFZZFBAGOAVSSTKKCUZAXAXFZOAAGYUMEOTLUERDNEZZBTZZJSSWBNYOBROXZOAKUIRDURELPDKXFNJLIEOFGASAFSGFEHUJSOXKQEXHFTXSUEJTZTNSUATLJCNJJSZASEGDMYHWMIKNFHKATATLJCNJJSZAXIRDIABWOOZZJNMEPRK

Copy and paste the cypher text to the tomcat client and set the max key length to 4 and press the form button. This should generate a new request for the cypher server to process. Watch the output of the cypher server terminal window, it should eventually print out the best key, which should be 'BAGS'.

Below is more cypher texts to test the application, run the cypher texts through the tomcat client to see what key they produce.

### Cypher Text
PIDEIVAGVIKHZIFHTEFWEYUVTEJXGSOCNWLYTQAERIKMTXWLHJLAXFMHGEHTKXWLUYLBPEJGRSMBYCGNWSFMMIDEFILATXLABWEXTRKPTVAYRSMLMMDEMVQMHHWYXRVMAIAGYEEBXWSGWLGKKSJLIIJIXXJTMIVURXZTMEFMBGZKBWLBKISEECTXEMWOXLWBLEFMBGZKBWLBPMDEAENXGSLABRYFHVW

### Cypher Text
FSSYYFPALSZBPSUBJOUQUIJPJOYRWCDWDGASJAPYHSZGJHLFXTAUNPBBWOWNAHLFKIAVFOYAHCBVOMVHMCUGCSSYVSAUJHAURGTRJBZJJFPSHCBFCWSYCFFGXRLSNBKGQSPAOOTVNGHAMVVEACYFYSYCNHYNCSKOHHONCOUGRQOERGAVASHYUMIRUWLINVLVBOUGRQOERGAVFWSYQOCRWCAURBNZXFL

The next cypher text used a key length of 5 to encrypt the text. Set the max key length of 5 when submitting the request.

### Cypher Text
SEWDWNIYULOORWUKALFKHUNUHWRPFVSJFKABAXASUEDLHPEDGMPHPTBKNLHHNTPKIQTTOHNNJGBEFJGBZOYLAALWELPHLLADIDELWNDOHNIQQVQSEASHTCQAKDPXLJDEZLENQSTEEDSUZHZJYKRDHLNPPLYWTPVIUTSSAWNEAJDRTKAERPSSHYMWSEEGWOAIDSUPINZYESEADELWZHREYGADIYYTKRP
