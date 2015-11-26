package ie.gmit.sw.client;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import ie.gmit.sw.VigenereBreaker;

public class Client {
	
	public static void main(String[] args) throws RemoteException, NotBoundException, Exception {

		// Local host lookup
		VigenereBreaker vb = (VigenereBreaker) Naming.lookup("///Cypher-Breaker");
		
		// Remote host lookup
		//VigenereBreaker vb = (VigenereBreaker) Naming.lookup("rmi://192.168.1.102:1099/Cypher-Breaker");
		
		System.out.println("Client Running!");
		
		String result = "";
		try {
			result = vb.decrypt("JPHOWBLTH", 4);
		}catch (Exception e) {
			System.out.println("Problem Found with Decrypt!");
		}
		
		System.out.println("Result " + result);
	}
}