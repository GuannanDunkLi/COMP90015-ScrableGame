/**  
* This class is an implementation of client. 
*/
package DSProjTwo.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class Client {

	public static void main(String[] args) {
		Socket socket;
		BufferedReader ClientReader;
		BufferedWriter ClientWriter;
		try {
			// Create a stream socket and connect it to the server
			//socket = new Socket("localhost", 4444);
			socket = new Socket(args[0],Integer.parseInt(args[1])); // Assign ip address and port number
			// Get the Buffered reader and writer from the socket
			ClientReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			ClientWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			// Launch login GUI
			Login login = new Login(socket, ClientReader, ClientWriter);
		} catch (Exception e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Connection failed. Please restart or check command.","Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
