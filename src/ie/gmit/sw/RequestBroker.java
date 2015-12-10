package ie.gmit.sw;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class RequestBroker implements Runnable {
	
	protected BlockingQueue<DecryptRequest> requestQueue = null;
	protected HashMap<Long, String> requestDecypherMap = null;
	protected HashMap<Long, DecryptRequest> requestWorkMap = null;
	protected String remoteHost;

	public RequestBroker(BlockingQueue<DecryptRequest> requestQueue, HashMap<Long, String> requestDecypherMap, HashMap<Long, DecryptRequest> requestWorkMap, String remoteHost) {
		setRequests(requestQueue, requestDecypherMap, requestWorkMap, remoteHost);
    }
	
	public void setRequests(BlockingQueue<DecryptRequest> requestQueue, HashMap<Long, String> requestDecypherMap, HashMap<Long, DecryptRequest> requestWorkMap, String remoteHost) {
		this.requestQueue = requestQueue;
		this.requestDecypherMap = requestDecypherMap;
		this.requestWorkMap = requestWorkMap;
		this.remoteHost = remoteHost;
	}

    public void run() {
        try {
        	while(true){
        		
        		// Attempt to poll
        		DecryptRequest request = requestQueue.poll();
        		
        		if(request != null){
	        		SendRequest newRequest = new SendRequest(request.getCypherText(), request.getMaxKeyLength());
	        		request.setTimeToComplete(System.currentTimeMillis());
	        		String cypherResult = newRequest.getPlainText(remoteHost);
	        		request.setTimeToComplete((System.currentTimeMillis() - request.getTimeToComplete()) / 1000);
	        		requestDecypherMap.put(request.getJobNumber(), cypherResult);
	        		request.setComplete(true);
	        		requestWorkMap.put(request.getJobNumber(), request);
        		}
	            // Make thread sleep
	            Thread.sleep(2000);
        	}
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
    }
}