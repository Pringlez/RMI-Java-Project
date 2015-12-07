package ie.gmit.sw;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class RequestBroker implements Runnable {
	
	protected BlockingQueue<DecryptRequest> requestQueue = null;
	protected HashMap<Long, String> requestWorkDoneMap = null;
	protected HashMap<Long, Boolean> requestFinishedMap = null;

	public RequestBroker(BlockingQueue<DecryptRequest> requestQueue, HashMap<Long, String> requestWorkDoneMap, HashMap<Long, Boolean> requestFinishedMap) {
		setRequests(requestQueue, requestWorkDoneMap, requestFinishedMap);
    }
	
	public void setRequests(BlockingQueue<DecryptRequest> requestQueue, HashMap<Long, String> requestWorkDoneMap, HashMap<Long, Boolean> requestFinishedMap) {
		this.requestQueue = requestQueue;
		this.requestWorkDoneMap = requestWorkDoneMap;
		this.requestFinishedMap = requestFinishedMap;
	}

    public void run() {
        try {
        	while(true){
        		
        		// Attempt to poll
        		DecryptRequest request = requestQueue.poll();
        		
        		if(request != null){
	        		SendRequest newRequest = new SendRequest(request.getCypherText(), request.getMaxKeyLength());
	        		String cypherResult = newRequest.getPlainText();
	        		request.setTimeToComplete(System.currentTimeMillis() - request.getTimeToComplete());
	        		requestWorkDoneMap.put(request.getJobNumber(), cypherResult);
	        		requestFinishedMap.put(request.getJobNumber(), true);
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