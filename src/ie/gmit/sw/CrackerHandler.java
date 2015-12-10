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
	private static String remoteHost = null;
	private static RequestBroker rb;
	private static Thread brokerThread;
	
	// Blocking queue & HashMap defined at class level 
	private BlockingQueue<DecryptRequest> requestQueue = new ArrayBlockingQueue<DecryptRequest>(100);
	private HashMap<Long, String> requestDecypherMap = new HashMap<Long, String>();
	private HashMap<Long, DecryptRequest> requestWorkMap = new HashMap<Long, DecryptRequest>();

	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
		remoteHost = ctx.getInitParameter("RMI_SERVER"); //Reads the value from the <context-param> in web.xml
		// Starting new request broker thread
		rb = new RequestBroker(requestQueue, requestDecypherMap, requestWorkMap, remoteHost);
		brokerThread = new Thread(rb);
		if(!brokerThread.isAlive()){
			brokerThread.start();
		}
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int pageReloadTime = 10000;
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		// Getting request information
		int maxKeyLength = Integer.parseInt(req.getParameter("frmMaxKeyLength"));
		String cypherText = req.getParameter("frmCypherText");
		String taskNumber = req.getParameter("frmStatus");
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
		
		out.print("<html><head><title>Distributed Systems Assignment</title></head>");		
		out.print("<body>");
		
		// If task number is null then initialize vars
		if (taskNumber == null){
			
			// Generating long number for task
			long generateNumber = System.currentTimeMillis() / 1000L;
			
			// Creating request object singleton
			DecryptRequest dr = new DecryptRequest(generateNumber, cypherText, maxKeyLength);
			
			taskNumber = new String("T" + generateNumber);

        	out.print("<h1>Processing request for Job #: " + taskNumber + "</h1>");
        	out.print("<p>RMI Server is located at " + remoteHost + "</p>");
        	out.print("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
            out.print("<p>CypherText: " + cypherText + "</p>");
            
            requestQueue.add(dr);
            requestWorkMap.put(generateNumber, dr);
		}
		else{
        	/* Get job number from map and display result if job completed, if job not complete
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
                	// If job has been fully completed & removed from finished queue then display
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
		
		out.print("<form name=\"frmCracker\">");
		out.print("<input name=\"frmMaxKeyLength\" type=\"hidden\" value=\"" + maxKeyLength + "\">");
		out.print("<input name=\"frmCypherText\" type=\"hidden\" value=\"" + cypherText + "\">");
		out.print("<input name=\"frmStatus\" type=\"hidden\" value=\"" + taskNumber + "\">");
		out.print("</form>");								
		out.print("</body>");	
		out.print("</html>");	
		
		out.print("<script>");
		out.print("var wait=setTimeout(\"document.frmCracker.submit();\", " + pageReloadTime + ");");
		out.print("</script>");
		
		// Resetting after removing task from map
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