package ie.gmit.sw;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CrackerHandler extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String remoteHost = "localhost";
	private static RequestBroker rb;
	private static Thread brokerThread;
	
	// Blocking queue & HashMap defined at class level 
	private BlockingQueue<DecryptRequest> requestQueue = new ArrayBlockingQueue<DecryptRequest>(100);
	private HashMap<Long, String> requestDecypherMap = new HashMap<Long, String>();
	private HashMap<Long, DecryptRequest> requestWorkMap = new HashMap<Long, DecryptRequest>();

	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
		remoteHost = ctx.getInitParameter("RMI_SERVER"); //Reads the value from the <context-param> in web.xml
		// Starting new request broker thread & passing references to the maps & blocking queue
		rb = new RequestBroker(requestQueue, requestDecypherMap, requestWorkMap, remoteHost);
		// Thread will create new instance of 'RequestBroker' to run
		brokerThread = new Thread(rb);
		// If thread already running ignore 
		if(!brokerThread.isAlive()){
			brokerThread.start();
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Reload time in ms
		int pageReloadTime = 10000;
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		// Getting request information from fields
		int maxKeyLength = Integer.parseInt(req.getParameter("frmMaxKeyLength"));
		String cypherText = req.getParameter("frmCypherText");
		String taskNumber = req.getParameter("frmStatus");
		long jobNumber = 0;
		
		// Getting jobNumber from http request when refresh is performed
		try {
			if(taskNumber != null){
				// Getting jobNumber from taskNumber when page refresh
				jobNumber = Long.parseLong(taskNumber.substring(1, taskNumber.length()));
			}
		} 
		catch (NumberFormatException error) {
			System.out.println("Error: " + error);
		}
		
		out.print("<html><head><title>Distributed Systems Assignment</title></head>");		
		out.print("<body>");
		
		// If task number is null then initialize variables
		if (taskNumber == null){
			
			// Generating long number for task
			long generateNumber = System.currentTimeMillis() / 100L;
			
			// Creating request object
			DecryptRequest dr = new DecryptRequest(generateNumber, cypherText, maxKeyLength);
			
			taskNumber = new String("T" + generateNumber);

			// Message out to user when request created
        	out.print("<h1>Processing request for Job #: " + taskNumber + "</h1>");
        	out.print("<p>RMI Server is located at " + remoteHost + "</p>");
        	out.print("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
            out.print("<p>CypherText: " + cypherText + "</p>");
            
            // Adding request to blocking queue & work map
            requestQueue.add(dr);
            // Work map is keep track of user requests being generated, does not store plain text decypher
            requestWorkMap.put(generateNumber, dr);
		}
		else{
        	/* Get job number from map and display result if job completed, if not completed
        	 * then display necessary message
        	 */
			if(!requestWorkMap.get(jobNumber).isComplete()){
				out.print("<h1>Working on request for Job #: " + taskNumber + "...</h1>");
        		out.print("<p>RMI Server is located at " + remoteHost + "</p>");
            	out.print("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
                out.print("<p>Cypher Text: " + cypherText + "</p>");
                out.print("<p>Task Complete: " + requestWorkMap.get(jobNumber).isComplete() + "</p>");
            }
            else{
            	/* If the cypher server is finished with the request it will appear in the decypher map
            	 * The decypher plain text will be displayed to the user for 30 seconds, then
            	 * it will disappear and be deleted from the decypher map
            	 */
            	if(requestDecypherMap.get(jobNumber) != null){
                	out.print("<h1>Finished work on Job #: " + taskNumber + " Successfully!</h1>");
                	out.print("<p>RMI Server is located at " + remoteHost + "</p>");
                	out.print("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
                    out.print("<p>Cypher Text: " + cypherText + "</p>");
                    out.print("<p><b style=\"color:red\">Message will disappear in 30 seconds!</b></p>");
                    out.print("<p>Plain Text: <b>" + requestDecypherMap.get(jobNumber) + "</b></p>");
                	// Removing request from map & setting another map indicating work is finished
                	requestDecypherMap.remove(jobNumber);
                	out.print("<p>Task Complete: " + requestWorkMap.get(jobNumber).isComplete() + "</p>");
                	out.print("<p>Completed in " + requestWorkMap.get(jobNumber).getTimeToComplete() + " seconds!</p>");
                	pageReloadTime = 30000;
                }
            	else{
                	// If job has been completed it will display this message, link to the home page provided
                	if(requestWorkMap.get(jobNumber).isComplete()){
                		out.print("<h1>Job #: " + taskNumber + " Complete!</h1>");
                		out.print("<p>RMI Server is located at " + remoteHost + "</p>");
                		out.print("<p>Task Complete: " + requestWorkMap.get(jobNumber).isComplete() + "</p>");
                		out.print("<p>Completed in " + requestWorkMap.get(jobNumber).getTimeToComplete() + " seconds!</p>");
                		out.print("<p><a href=\"http://localhost:8080/cracker\">Cracker Home Page</a></p>");
                	}
                }
        	}
        }
		
		// The cracker form information
		out.print("<form name=\"frmCracker\">");
		out.print("<input name=\"frmMaxKeyLength\" type=\"hidden\" value=\"" + maxKeyLength + "\">");
		out.print("<input name=\"frmCypherText\" type=\"hidden\" value=\"" + cypherText + "\">");
		out.print("<input name=\"frmStatus\" type=\"hidden\" value=\"" + taskNumber + "\">");
		out.print("</form>");								
		out.print("</body>");	
		out.print("</html>");	
		
		// Refreshing page after set amount of ms
		out.print("<script>");
		out.print("var wait=setTimeout(\"document.frmCracker.submit();\", " + pageReloadTime + ");");
		out.print("</script>");
		
		// Resetting after removing request from map
		pageReloadTime = 10000;
				
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

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
 	}
	
	public static String getHost(){
		return remoteHost;
	}
}