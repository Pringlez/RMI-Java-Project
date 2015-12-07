package ie.gmit.sw;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
	
	public static void main(String[] args) throws RemoteException, NotBoundException, Exception {

		// Local host lookup
		VigenereBreaker vb = (VigenereBreaker) Naming.lookup("///Cypher-Breaker");
		
		// Remote host lookup
		//VigenereBreaker vb = (VigenereBreaker) Naming.lookup("rmi://192.168.1.102:1099/Cypher-Breaker");
		
		System.out.println("Client Running!");
		
		String result = "";
		try {
			result = vb.decrypt("PIDEIVAGVIKHZIFHTEFWEYUVTEJXGSOCNWLYTQAERIKMTXWLHJLAXFMHGEHTKXWLUYLBPEJGRSMBYCGNWSFMMIDEFILATXLABWEXTRKPTVAYRSMLMMDEMVQMHHWYXRVMAIAGYEEBXWSGWLGKKSJLIIJIXXJTMIVURXZTMEFMBGZKBWLBKISEECTXEMWOXLWBLEFMBGZKBWLBPMDEAENXGSLABRYFHVW", 4);
		}
		catch (Exception error) {
			System.out.println("Problem found with decryption!");
			System.out.println("Error: " + error);
		}  
		
		System.out.println("Result " + result);
	}
}