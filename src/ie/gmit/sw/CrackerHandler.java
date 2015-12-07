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
	private static long jobNumber = 0;
	
	// Blocking queue & HashMap defined at class level 
	private static BlockingQueue<DecryptRequest> requestQueue = new ArrayBlockingQueue<DecryptRequest>(10);
	private static HashMap<Long, String> requestWorkDoneMap = new HashMap<Long, String>();
	private static HashMap<Long, Boolean> requestFinishedMap = new HashMap<Long, Boolean>();

	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
		remoteHost = ctx.getInitParameter("RMI_SERVER"); //Reads the value from the <context-param> in web.xml
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		int pageReloadTime = 10000;
		
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		// Getting request information
		int maxKeyLength = Integer.parseInt(req.getParameter("frmMaxKeyLength"));
		String cypherText = req.getParameter("frmCypherText");
		String taskNumber = req.getParameter("frmStatus");

		out.print("<html><head><title>Distributed Systems Assignment</title></head>");		
		out.print("<body>");
		
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

        	out.print("<h1>Processing request for Job #: " + taskNumber + "</h1>");
        	out.print("<p>RMI Server is located at " + remoteHost + "</p>");
        	out.print("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
            out.print("<p>CypherText: " + cypherText + "</p>");
            
            requestQueue.add(dr);
            dr.setTimeToComplete(System.currentTimeMillis());
            requestFinishedMap.put(jobNumber, false);
		}
		else{
        	/* Get job number from map and display result if job completed, if job not complete
        	 * then display necessary message
        	 */
			if(!requestFinishedMap.get(jobNumber)){
				out.print("<h1>Working on request for Job #: " + taskNumber + "...</h1>");
        		out.print("<p>RMI Server is located at " + remoteHost + "</p>");
            	out.print("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
                out.print("<p>Cypher Text: " + cypherText + "</p>");
                out.print("<p>Task Complete: " + requestFinishedMap.get(jobNumber) + "</p>");
            }
            else{
            	if(requestWorkDoneMap.get(jobNumber) != null){
                	out.print("<h1>Finished work on Job #: " + taskNumber + " Successfully!</h1>");
                	out.print("<p>RMI Server is located at " + remoteHost + "</p>");
                	out.print("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
                    out.print("<p>Cypher Text: " + cypherText + "</p>");
                    out.print("<p><b style=\"color:red\">Message will disappear in 30 seconds!</b></p>");
                    out.print("<p>Plain Text: <b>" + requestWorkDoneMap.get(jobNumber) + "</b></p>");
                	// Removing request from map & setting another map indicating work is finished
                	requestWorkDoneMap.remove(jobNumber);
                	out.print("<p>Task Complete: " + requestFinishedMap.get(jobNumber) + "</p>");
                	out.print("<p>Completed in " + "TODO" + " seconds!</p>");
                	pageReloadTime = 30000;
                }
            	else{
                	// If job has been fully completed & removed from finished queue then display
                	if(requestFinishedMap.get(jobNumber)){
                		out.print("<h1>Job #: " + taskNumber + " Complete!</h1>");
                		out.print("<p>RMI Server is located at " + remoteHost + "</p>");
                		out.print("<p>Task Complete: " + requestFinishedMap.get(jobNumber) + "</p>");
                		out.print("<p>Return to cracker page - <a href=\"http://" + remoteHost + ":8080/cracker\">Home Page</a></p>");
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