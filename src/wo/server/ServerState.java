package wo.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import wo.User;

public class ServerState {
	private static ServerState instance;
//	private List<ClientConnection> connectedClients;
	private Map<String, ClientConnection> connectedClients;
	private List<User> users;
	private Set<String> playerlist;
	private Set<String> cyclelist;
	private int count1 = 0;
	private int count2 = 0;
	private int pass = 0;
	private ServerState() {
		connectedClients = new HashMap<String, ClientConnection>();
		users = new ArrayList<User>();
		playerlist = new HashSet<String> ();
		cyclelist = new HashSet<String> ();
	}
	public static synchronized ServerState getInstance() {
		if(instance == null) {
			instance = new ServerState();
		}
		return instance;
	}
	public synchronized void clientConnected(String name, ClientConnection client) {
		connectedClients.put(name, client);
	}
	public synchronized void clientDisconnected(ClientConnection client) {
		connectedClients.remove(client);
	}
	public synchronized Map<String, ClientConnection> getConnectedClients() {
		return connectedClients;
	}
	public synchronized List<User> getUsers() {
		return users;
	}
	public synchronized Set<String> getPlayerlist() {
		return playerlist;
	}
	public synchronized Set<String> getCyclelist() {
		return cyclelist;
	}
	public synchronized void vote(int i,String currentName,String dir,int row,int col) {
//		Iterator<String> iterator = playerlist.iterator();
		if (cyclelist.isEmpty()) {
			Iterator<String> iterator = playerlist.iterator();
			while (iterator.hasNext()) {
				cyclelist.add((String) iterator.next());
			}
		}
		if (i == 0) {
			count1++;
			count2++;
			if (count1 == (playerlist.size()-1)) {
				//Ëã·Ö
				try {
					Map<String, ClientConnection> clients = connectedClients;
					for (String name : playerlist) {
						ClientConnection client = clients.get(name);
//						String turnname =cyclelist.iterator().next();
						client.getWriter().write("score"+" "+currentName+" "+dir+" "+row+" "+col+"\n");
						client.getWriter().flush();
//						cyclelist.remove(turnname);
//						System.out.println(Thread.currentThread().getName() + " - Message sent to client " + clientNum);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (i==1) {
			count2++;
		} else if (i==2) {
			pass++;
			if (pass==playerlist.size()) {
				try {
					for (String player : playerlist) {
						ClientConnection client = connectedClients.get(player);
						for (String player1 : playerlist) {
							client.getWriter().write("touser"+" "+player1+"\n");
							client.getWriter().flush();
						}
						client.getWriter().write("logout"+" "+"1"+"\n");
						client.getWriter().flush();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				playerlist.clear();
			}
			cyclelist.remove(currentName);
			if (cyclelist.isEmpty()) {
				Iterator<String> iterator = playerlist.iterator();
				while (iterator.hasNext()) {
					cyclelist.add((String) iterator.next());
				}
			}
			try {
				Map<String, ClientConnection> clients = connectedClients;
				for (String name : playerlist) {
					ClientConnection client = clients.get(name);
					String turnname =cyclelist.iterator().next();
					client.getWriter().write("turn"+" "+turnname+"\n");
					client.getWriter().flush();
//					cyclelist.remove(turnname);
//					System.out.println(Thread.currentThread().getName() + " - Message sent to client " + clientNum);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (count2 == (playerlist.size())-1) {
			count1 = 0;
			count2 = 0;
			System.out.println(cyclelist.toString());
			cyclelist.remove(currentName);
			System.out.println(cyclelist.toString());
			if (cyclelist.isEmpty()) {
				Iterator<String> iterator = playerlist.iterator();
				while (iterator.hasNext()) {
					cyclelist.add((String) iterator.next());
				}
			}
			try {
				Map<String, ClientConnection> clients = connectedClients;
				for (String name : playerlist) {
					ClientConnection client = clients.get(name);
					String turnname =cyclelist.iterator().next();
					client.getWriter().write("turn"+" "+turnname+"\n");
					client.getWriter().flush();
					System.out.println(name+"vote feedback sent");
//					cyclelist.remove(turnname);
//					System.out.println(Thread.currentThread().getName() + " - Message sent to client " + clientNum);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
