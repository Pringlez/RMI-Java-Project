package ie.gmit.sw;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Servant extends UnicastRemoteObject implements VigenereBreaker {

	private static final long serialVersionUID = 1L;
	
	public Servant() throws Exception {
	}

	@Override
	public String decrypt(String cypherText, int maxKeyLength) throws RemoteException {
		KeyEnumerator breaker = getNewKeyEnumerator();
		breaker.setCypherText(cypherText);
		breaker.setMaxKeyLength(maxKeyLength);
		
		Thread thread = new Thread(breaker);
		thread.start();
		
		try {
			thread.join();
		} 
		catch (InterruptedException error) {
			System.out.println("Problem found with method 'decrypt'!");
			System.out.println("Error: " + error);
		}
		
		return breaker.getCypherResult();
	}
	
	public KeyEnumerator getNewKeyEnumerator() {
		KeyEnumerator breaker = null;
		try {
			breaker = new KeyEnumerator();
		} 
		catch (Exception error) {
			System.out.println("Problem found with method 'getNewKeyEnumerator'!");
			System.out.println("Error: " + error);
		}
		return breaker;
	}
	
	public static void main(String[] args) throws Exception {
		try {
			System.out.println("Starting Remote Service!");
			// On linux machine you need to set rmi hostname to your IP address & disable your firewall
			//System.getProperty("java.rmi.server.hostname", "192.168.1.102");
			LocateRegistry.createRegistry(1099);
			Naming.bind("Cypher-Breaker", new Servant());
			System.out.println("Service Started...");
		} 
		catch (Exception error) {
			System.out.println("Error: " + error);
		}
	}
}