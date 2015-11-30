package ie.gmit.sw;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Servant extends UnicastRemoteObject implements VigenereBreaker {

	private static final long serialVersionUID = 1L;
	
	private KeyEnumerator breaker;
	
	public Servant() throws Exception {
		breaker = new KeyEnumerator();
	}

	@Override
	public String decrypt(String cypherText, int maxKeyLength) throws RemoteException {
		return breaker.crackCypher(cypherText, maxKeyLength);
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Starting Remote Service!");
		LocateRegistry.createRegistry(1099);
		Naming.bind("Cypher-Breaker", new Servant());
		System.out.println("Service Started...");
	}
}