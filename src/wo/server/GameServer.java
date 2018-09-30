package wo.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;

import wo.server.ClientConnection;
import wo.server.ServerState;
import wo.remote.Game;


public class GameServer {
	public static void main(String[] args)  {
		ServerSocket listeningSocket = null;
		try {
//			Game game = new GameImp();
//			LocateRegistry.createRegistry(1099);
//            java.rmi.Naming.rebind("rmi://localhost:1099/Game", game);
//            System.out.println("server ready");
            //Create a server socket listening on port 4444
			listeningSocket = new ServerSocket(4444);
			System.out.println(Thread.currentThread().getName() + 
					" - Server listening on port 4444 for a connection");
			int clientNum = 0;
			//Listen for incoming connections for ever 
			while (true) {
				//Accept an incoming client connection request 
				Socket clientSocket = listeningSocket.accept(); 
				System.out.println(Thread.currentThread().getName() 
						+ " - Client conection accepted");
				clientNum++;
				//Create a client connection to listen for and process all the messages
				//sent by the client
				ClientConnection clientConnection = new ClientConnection(clientSocket, clientNum);
				clientConnection.setName("Thread" + clientNum);
				clientConnection.start(); 
				//Update the server state to reflect the new connected client
//				ServerState.getInstance().clientConnected(clientConnection);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}