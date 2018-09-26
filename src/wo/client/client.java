package wo.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import wo.client.MessageListener;
import wo.gui;
import wo.login;
import wo.remote.Game;


public class client {

	public static void main(String[] args) {
		Socket socket;
		BufferedReader ClientReader;
		try {
			socket = new Socket("127.0.0.1", 4444);
			ClientReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			//Connect to the rmiregistry that is running on localhost
//			Registry registry = LocateRegistry.getRegistry("localhost");
			//Retrieve the stub/proxy for the remote math object from the registry
//			Game game = (Game) Naming.lookup("rmi://localhost:1099/Game");
//			gui gui = new gui(socket);
//			gui.getFrame().setVisible(true);
			login login = new login(socket);
//			login.getFrame().setVisible(true);
			//Call methods on the remote object as if it was a local object
//			game.add(gui);
//			System.out.println("em");
			// Launch a new thread in charge of listening for any messages that arrive through the socket's input stream (any data sent by the server)
//			MessageListener ml = new MessageListener(ClientReader, gui);
			MessageListener ml = new MessageListener(ClientReader, login.getGui());
			ml.start();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
