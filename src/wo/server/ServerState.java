package wo.server;

import java.util.ArrayList;
import java.util.List;

public class ServerState {
	private static ServerState instance;
	private List<ClientConnection> connectedClients;
	private ServerState() {
		connectedClients = new ArrayList<>();
	}
	public static synchronized ServerState getInstance() {
		if(instance == null) {
			instance = new ServerState();
		}
		return instance;
	}
	public synchronized void clientConnected(ClientConnection client) {
		connectedClients.add(client);
	}
	public synchronized void clientDisconnected(ClientConnection client) {
		connectedClients.remove(client);
	}
	public synchronized List<ClientConnection> getConnectedClients() {
		return connectedClients;
	}
}
