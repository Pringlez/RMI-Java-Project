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
	private BlockingQueue<DecryptRequest> requestQueue = new ArrayBlockingQueue<DecryptRequest>(10);
	private HashMap<Long, String> requestDoneMap = new HashMap<Long, String>();
	
	private RequestBroker rb = new RequestBroker(requestQueue, requestDoneMap);
	private Thread thread = new Thread(rb);

	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
		remoteHost = ctx.getInitParameter("RMI_SERVER"); //Reads the value from the <context-param> in web.xml
	    thread.start();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		
		// Getting request information
		int maxKeyLength = Integer.parseInt(req.getParameter("frmMaxKeyLength"));
		String cypherText = req.getParameter("frmCypherText");
		String taskNumber = req.getParameter("frmStatus");
		
		// Creating request object
		DecryptRequest dr = null;

		out.print("<html><head><title>Distributed Systems Assignment</title>");		
		out.print("</head>");		
		out.print("<body>");
		
		if (taskNumber == null){
			
			taskNumber = new String("T" + jobNumber);
			jobNumber++;
			
            if(taskNumber != null){
            	out.print("<h1>Processing request for Job #: " + taskNumber + "</h1>");
                out.print("RMI Server is located at " + remoteHost);
                out.print("<p>Maximum Key Length: " + maxKeyLength + "</p>");		
                out.print("<p>CypherText: " + cypherText + "</p>");
                
                dr = new DecryptRequest(jobNumber, cypherText, maxKeyLength);
                requestQueue.add(dr);
            }
		}else{
			//Check out-queue for finished job
            if(taskNumber != null){
                try{
                    if(requestDoneMap.get(jobNumber) != null){
                    	out.print("<h1>Finished work on Job#: " + taskNumber + "...</h1>");
                    	out.print("<p>Plain Text: " + requestDoneMap.get(jobNumber) + "</p>");
                    	requestDoneMap.remove(jobNumber);
                    }
                    else{
                		out.print("<h1>Working on request for Job#: " + taskNumber + "...</h1>");
                    }
                }
                catch(Exception e){
                	out.print("<h1>Something Failed! - Error</h1>");
                    out.print("<p>Map Size: " + requestDoneMap.size() + "</p>");
                }
            }
		}
		
		/*out.print("<P>This servlet should only be responsible for handling client request and returning responses. Everything else should be handled by different objects.");
		out.print("<P>Note that any variables declared inside this doGet() method are thread safe. Anything defined at a class level is shared between HTTP requests.");*/				
		
		out.print("<form name=\"frmCracker\">");
		out.print("<input name=\"frmMaxKeyLength\" type=\"hidden\" value=\"" + maxKeyLength + "\">");
		out.print("<input name=\"frmCypherText\" type=\"hidden\" value=\"" + cypherText + "\">");
		out.print("<input name=\"frmStatus\" type=\"hidden\" value=\"" + taskNumber + "\">");
		out.print("</form>");								
		out.print("</body>");	
		out.print("</html>");	
		
		out.print("<script>");
		out.print("var wait=setTimeout(\"document.frmCracker.submit();\", 10000);");
		out.print("</script>");
				
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
