package wo.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import wo.gui;
import wo.remote.Game;


public class GameImp extends UnicastRemoteObject implements Game {

	private int numberOfComputations;
	
	protected GameImp() throws RemoteException {
		numberOfComputations = 0;
	}

	@Override
	public void add(gui gui) throws RemoteException {
		
	}

}
