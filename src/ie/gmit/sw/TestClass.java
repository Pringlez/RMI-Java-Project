package ie.gmit.sw;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TestClass {

	private static String remoteHost = "localhost";
	private static long jobNumber = 0;
	
	// Blocking queue & HashMap defined at class level 
	private static BlockingQueue<DecryptRequest> requestQueue = new ArrayBlockingQueue<DecryptRequest>(10);
	private static HashMap<Long, String> requestWorkDoneMap = new HashMap<Long, String>();
	private static HashMap<Long, Boolean> requestFinishedMap = new HashMap<Long, Boolean>();

	public TestClass() {
	    doGet(4, "PIDEIVAGVIKHZIFHTEFWEYUVTEJXGSOCNWLYTQAERIKMTXWLHJLAXFMHGEHTKXWLUYLBPEJGRSMBYCGNWSFMMIDEFILATXLABWEXTRKPTVAYRSMLMMDEMVQMHHWYXRVMAIAGYEEBXWSGWLGKKSJLIIJIXXJTMIVURXZTMEFMBGZKBWLBKISEECTXEMWOXLWBLEFMBGZKBWLBPMDEAENXGSLABRYFHVW", null);
	}

	public void doGet(int maxKeyLength, String cypherText, String taskNumber) {
		
		int pageReloadTime = 10000;
		
		while(true){
			
			// If task number is null then initialize vars
			if (taskNumber == null){
				
				RequestBroker rb = new RequestBroker(requestQueue, requestWorkDoneMap, requestFinishedMap);
				Thread thread = new Thread(rb);
				
				if(!thread.isAlive()){
					thread.start();
				}
				
				jobNumber++;
				
				// Creating request object singleton
				DecryptRequest dr = new DecryptRequest(jobNumber, cypherText, maxKeyLength);
				
				taskNumber = new String("T" + jobNumber);

	        	System.out.println("<h1>Processing request for Job #: " + taskNumber + "</h1>");
	        	System.out.println("<p>RMI Server is located at " + remoteHost + "</p>");
	        	System.out.println("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
	            System.out.println("<p>CypherText: " + cypherText + "</p>");
	            
	            requestQueue.add(dr);
	            dr.setTimeToComplete(System.currentTimeMillis());
	            requestFinishedMap.put(jobNumber, false);
			}
			else{
	        	/* Get job number from map and display result if job completed, if job not complete
	        	 * then display necessary message
	        	 */
				if(!requestFinishedMap.get(jobNumber)){
					System.out.println("<h1>Working on request for Job #: " + taskNumber + "...</h1>");
	        		System.out.println("<p>RMI Server is located at " + remoteHost + "</p>");
	            	System.out.println("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
	                System.out.println("<p>Cypher Text: " + cypherText + "</p>");
	                System.out.println("<p>Task Complete: " + requestFinishedMap.get(jobNumber) + "</p>");
	            }
	            else{
	            	if(requestWorkDoneMap.get(jobNumber) != null){
	                	System.out.println("<h1>Finished work on Job #: " + taskNumber + " Successfully!</h1>");
	                	System.out.println("<p>RMI Server is located at " + remoteHost + "</p>");
	                	System.out.println("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
	                    System.out.println("<p>Cypher Text: " + cypherText + "</p>");
	                    System.out.println("<p><b style=\"color:red\">Message will disappear in 30 seconds!</b></p>");
	                    System.out.println("<p>Plain Text: <b>" + requestWorkDoneMap.get(jobNumber) + "</b></p>");
	                	// Removing request from map & setting another map indicating work is finished
	                	requestWorkDoneMap.remove(jobNumber);
	                	System.out.println("<p>Task Complete: " + requestFinishedMap.get(jobNumber) + "</p>");
	                	System.out.println("<p>Completed in " + "TODO" + " seconds!</p>");
	                	pageReloadTime = 30000;
	                }
	            	else{
	                	// If job has been fully completed & removed from finished queue then display
	                	if(requestFinishedMap.get(jobNumber)){
	                		System.out.println("<h1>Job #: " + taskNumber + " Complete!</h1>");
	                		System.out.println("<p>RMI Server is located at " + remoteHost + "</p>");
	                		System.out.println("<p>Task Complete: " + requestFinishedMap.get(jobNumber) + "</p>");
	                		System.out.println("<p>Return to cracker page - <a href=\"http://" + remoteHost + ":8080/cracker\">Home Page</a></p>");
	                	}
	                }
	        	}
	        }
			
			try {
			    Thread.sleep(pageReloadTime);
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			// Resetting after removing task from map
			pageReloadTime = 10000;
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