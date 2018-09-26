package wo.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

import wo.gui;

public interface Game extends Remote {

	public void add(gui gui) throws RemoteException;
	
}