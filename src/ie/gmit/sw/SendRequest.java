package ie.gmit.sw;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class SendRequest {
	
	private String cypher;
	private int key;
	
	public SendRequest(String cypher, int maxKeyLength) {
		setCypher(cypher);
		setKey(maxKeyLength);
	}
	
	public String getPlainText(String remoteHost) throws MalformedURLException, RemoteException, NotBoundException{
		
		VigenereBreaker vb = (VigenereBreaker) Naming.lookup("rmi://" + remoteHost + ":1099/Cypher-Breaker");
		
		String result = "";
		try {
			result = vb.decrypt(getCypher(), getKey());
		}
		catch (Exception error) {
        	System.out.println("Problem found with method 'getPlainText'!");
        	System.out.println("Error: " + error);
		}
		
		return result;
	}

	public String getCypher() {
		return cypher;
	}

	public void setCypher(String cypher) {
		this.cypher = cypher;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
}