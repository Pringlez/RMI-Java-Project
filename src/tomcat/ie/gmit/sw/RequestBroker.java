package ie.gmit.sw;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class RequestBroker implements Runnable {
	
	protected BlockingQueue<DecryptRequest> requestQueue = null;
	protected HashMap<Long, String> requestDoneMap = null;

	public RequestBroker(BlockingQueue<DecryptRequest> requestQueue, HashMap<Long, String> requestDoneMap) {
		setRequests(requestQueue, requestDoneMap);
    }
	
	public void setRequests(BlockingQueue<DecryptRequest> requestQueue, HashMap<Long, String> requestDoneMap) {
		this.requestQueue = requestQueue;
		this.requestDoneMap = requestDoneMap;
	}

    public void run() {
        try {
        	while(true){
        		
        		// Attempt to poll
        		DecryptRequest request = requestQueue.poll();
        		
        		if(request != null){
	        		SendRequest newRequest = new SendRequest(request.getCypherText(), request.getMaxKeyLength());
	        		String cypherResult = newRequest.getPlainText(CrackerHandler.getHost());
	        		requestDoneMap.put(request.getJobNumber(), cypherResult);
        		}
	            // Make thread sleep
	            Thread.sleep(2000);
	            
        	}
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
