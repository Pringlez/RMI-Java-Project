package ie.gmit.sw;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Remote interface defined
public interface VigenereBreaker extends Remote {
	public String decrypt(String cypherText, int maxKeyLength) throws RemoteException;
}