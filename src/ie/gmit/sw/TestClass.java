package ie.gmit.sw;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TestClass {

	private static String remoteHost = "192.168.1.102";
	
	// Blocking queue & HashMap defined at class level 
	private BlockingQueue<DecryptRequest> requestQueue = new ArrayBlockingQueue<DecryptRequest>(100);
	private HashMap<Long, String> requestDecypherMap = new HashMap<Long, String>();
	private HashMap<Long, DecryptRequest> requestWorkMap = new HashMap<Long, DecryptRequest>();
	
	private static RequestBroker rb;
	private static Thread brokerThread;

	public TestClass() {
		// Starting new request broker thread
		rb = new RequestBroker(requestQueue, requestDecypherMap, requestWorkMap, remoteHost);
		brokerThread = new Thread(rb);
		if(!brokerThread.isAlive()){
			brokerThread.start();
		}
		else{
			System.out.println("Thread already running!");
		}
	    doGet(4, "PIDEIVAGVIKHZIFHTEFWEYUVTEJXGSOCNWLYTQAERIKMTXWLHJLAXFMHGEHTKXWLUYLBPEJGRSMBYCGNWSFMMIDEFILATXLABWEXTRKPTVAYRSMLMMDEMVQMHHWYXRVMAIAGYEEBXWSGWLGKKSJLIIJIXXJTMIVURXZTMEFMBGZKBWLBKISEECTXEMWOXLWBLEFMBGZKBWLBPMDEAENXGSLABRYFHVWPIDEIVAGVIKHZIFHTEFWEYUVTEJXGSOCNWLYTQAERIKMTXWLHJLAXFMHGEHTKXWLUYLBPEJGRSMBYCGNWSFMMIDEFILATXLABWEXTRKPTVAYRSMLMMDEMVQMHHWYXRVMAIAGYEEBXWSGWLGKKSJLIIJIXXJTMIVURXZTMEFMBGZKBWLBKISEECTXEMWOXLWBLEFMBGZKBWLBPMDEAENXGSLABRYFHVWPIDEIVAGVIKHZIFHTEFWEYUVTEJXGSOCNWLYTQAERIKMTXWLHJLAXFMHGEHTKXWLUYLBPEJGRSMBYCGNWSFMMIDEFILATXLABWEXTRKPTVAYRSMLMMDEMVQMHHWYXRVMAIAGYEEBXWSGWLGKKSJLIIJIXXJTMIVURXZTMEFMBGZKBWLBKISEECTXEMWOXLWBLEFMBGZKBWLBPMDEAENXGSLABRYFHVWPIDEIVAGVIKHZIFHTEFWEYUVTEJXGSOCNWLYTQAERIKMTXWLHJLAXFMHGEHTKXWLUYLBPEJGRSMBYCGNWSFMMIDEFILATXLABWEXTRKPTVAYRSMLMMDEMVQMHHWYXRVMAIAGYEEBXWSGWLGKKSJLIIJIXXJTMIVURXZTMEFMBGZKBWLBKISEECTXEMWOXLWBLEFMBGZKBWLBPMDEAENXGSLABRYFHVWPIDEIVAGVIKHZIFHTEFWEYUVTEJXGSOCNWLYTQAERIKMTXWLHJLAXFMHGEHTKXWLUYLBPEJGRSMBYCGNWSFMMIDEFILATXLABWEXTRKPTVAYRSMLMMDEMVQMHHWYXRVMAIAGYEEBXWSGWLGKKSJLIIJIXXJTMIVURXZTMEFMBGZKBWLBKISEECTXEMWOXLWBLEFMBGZKBWLBPMDEAENXGSLABRYFHVWPIDEIVAGVIKHZIFHTEFWEYUVTEJXGSOCNWLYTQAERIKMTXWLHJLAXFMHGEHTKXWLUYLBPEJGRSMBYCGNWSFMMIDEFILATXLABWEXTRKPTVAYRSMLMMDEMVQMHHWYXRVMAIAGYEEBXWSGWLGKKSJLIIJIXXJTMIVURXZTMEFMBGZKBWLBKISEECTXEMWOXLWBLEFMBGZKBWLBPMDEAENXGSLABRYFHVW", null);
	}

	public void doGet(int maxKeyLength, String cypherText, String taskNumber) {
		
		while(true){
			
			int pageReloadTime = 10000;
			long jobNumber = 0;
			
			// Getting jobNumber from http request when refresh is performed
			try {
				if(taskNumber != null){
					jobNumber = Long.parseLong(taskNumber.substring(1, taskNumber.length()));
				}
			} 
			catch (NumberFormatException error) {
				System.out.println("Error: " + error);
			}
			
			System.out.println("<html><head><title>Distributed Systems Assignment</title></head>");		
			System.out.println("<body>");
			
			// If task number is null then initialize vars
			if (taskNumber == null){
				
				// Generating long number for task
				long generateNumber = System.currentTimeMillis() / 1000L;
				
				// Creating request object singleton
				DecryptRequest dr = new DecryptRequest(generateNumber, cypherText, maxKeyLength);
				
				taskNumber = new String("T" + generateNumber);

	        	System.out.println("<h1>Processing request for Job #: " + taskNumber + "</h1>");
	        	System.out.println("<p>RMI Server is located at " + remoteHost + "</p>");
	        	System.out.println("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
	            System.out.println("<p>CypherText: " + cypherText + "</p>");
	            
	            requestQueue.add(dr);
	            requestWorkMap.put(generateNumber, dr);
			}
			else{
	        	/* Get job number from map and display result if job completed, if job not complete
	        	 * then display necessary message
	        	 */
				if(!requestWorkMap.get(jobNumber).isComplete()){
					System.out.println("<h1>Working on request for Job #: " + taskNumber + "...</h1>");
	        		System.out.println("<p>RMI Server is located at " + remoteHost + "</p>");
	            	System.out.println("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
	                System.out.println("<p>Cypher Text: " + cypherText + "</p>");
	                System.out.println("<p>Task Complete: " + requestWorkMap.get(jobNumber).isComplete() + "</p>");
	            }
	            else{
	            	if(requestDecypherMap.get(jobNumber) != null){
	                	System.out.println("<h1>Finished work on Job #: " + taskNumber + " Successfully!</h1>");
	                	System.out.println("<p>RMI Server is located at " + remoteHost + "</p>");
	                	System.out.println("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
	                    System.out.println("<p>Cypher Text: " + cypherText + "</p>");
	                    System.out.println("<p><b style=\"color:red\">Message will disappear in 30 seconds!</b></p>");
	                    System.out.println("<p>Plain Text: <b>" + requestDecypherMap.get(jobNumber) + "</b></p>");
	                	// Removing request from map & setting another map indicating work is finished
	                	requestDecypherMap.remove(jobNumber);
	                	System.out.println("<p>Task Complete: " + requestWorkMap.get(jobNumber).isComplete() + "</p>");
	                	System.out.println("<p>Completed in " + requestWorkMap.get(jobNumber).getTimeToComplete() + " seconds!</p>");
	                	pageReloadTime = 30000;
	                }
	            	else{
	                	// If job has been fully completed & removed from finished queue then display
	                	if(requestWorkMap.get(jobNumber).isComplete()){
	                		System.out.println("<h1>Job #: " + taskNumber + " Complete!</h1>");
	                		System.out.println("<p>RMI Server is located at " + remoteHost + "</p>");
	                		System.out.println("<p>Task Complete: " + requestWorkMap.get(jobNumber).isComplete() + "</p>");
	                		System.out.println("<p>Completed in " + requestWorkMap.get(jobNumber).getTimeToComplete() + " seconds!</p>");
	                		System.out.println("<p><a href=\"http://localhost:8080/cracker\">Cracker Home Page</a></p>");
	                	}
	                }
	        	}
	        }
			
			System.out.println("<form name=\"frmCracker\">");
			System.out.println("<input name=\"frmMaxKeyLength\" type=\"hidden\" value=\"" + maxKeyLength + "\">");
			System.out.println("<input name=\"frmCypherText\" type=\"hidden\" value=\"" + cypherText + "\">");
			System.out.println("<input name=\"frmStatus\" type=\"hidden\" value=\"" + taskNumber + "\">");
			System.out.println("</form>");								
			System.out.println("</body>");	
			System.out.println("</html>");	
			
			System.out.println("<script>");
			System.out.println("var wait=setTimeout(\"document.frmCracker.submit();\", " + pageReloadTime + ");");
			System.out.println("</script>");
			
			// Resetting after removing task from map
			pageReloadTime = 10000;
			
			try {
				Thread.sleep(pageReloadTime);
			} catch (InterruptedException e) {
			}
		}
				
		/*-----------------------------------------------------------------------     
		 *  Next Steps: just in case you removed the above....
		 *-----------------------------------------------------------------------
		 * 1) Generate a big random number to use a a job number, or just increment a static long variable declared at a class level, e.g. jobNumber
		 * 2) Create some type of a "message request" object from the maxKeyLength, cypherText and jobNumber.
		 * 3) Add the "message request" object to a LinkedList or BlockingQueue (the IN-queue)
		 * 4) Return the jobNumber to the client web browser with a wait interval using <meta http-equiv="refresh" content="10">. The content="10" will wait for 10s.
		 * 4) Have some process check the LinkedList or BlockingQueue for "message requests" 
		 * 5) Poll a "message request" from the front of the queue and make an RMI call to the Vigenere Cypher Service
		 * 6) Get the result and add to a Map (the OUT-queue) using the jobNumber and the key and the result as a value
		 * 7) Return the cyphertext to the client next time a request for the jobNumber is received and delete the key / value pair from the Map.
		 */
		
	}
	
	public static String getHost(){
		return remoteHost;
	}

	public static void main(String[] args) throws RemoteException, NotBoundException, Exception {
		new TestClass();
	}
}