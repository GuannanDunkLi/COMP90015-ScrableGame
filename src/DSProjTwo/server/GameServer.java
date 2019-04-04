/**  
* This class is an implementation of server. 
*/
package DSProjTwo.server;

import java.net.ServerSocket;
import java.net.Socket;
import DSProjTwo.server.ClientConnection;


public class GameServer {
	public static void main(String[] args)  {
		ServerSocket listeningSocket = null;
		try {
            //Create a server socket
			//listeningSocket = new ServerSocket(4444);
			listeningSocket = new ServerSocket(Integer.parseInt(args[0]));
			System.out.println(Thread.currentThread().getName() + 
					" - Server listening on port for a connection");
			int clientNum = 0;
			//Listen for incoming connections forever 
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
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
}