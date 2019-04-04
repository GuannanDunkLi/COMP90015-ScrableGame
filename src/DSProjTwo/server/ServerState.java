/**  
* This class allows server to store game information. 
*/
package DSProjTwo.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import DSProjTwo.User;

public class ServerState {
	private static ServerState instance;
	private Map<String, ClientConnection> connectedClients;
	private List<User> users;
	private HashMap<String, Integer> players;
	private List<String[]> buttons;
	private Set<String> playerlist;
	private Set<String> cyclelist;
	private int count1 = 0;
	private int count2 = 0;
	private int pass = 0;
	private boolean isGame;
	private String turnname;
	
	public HashMap<String, Integer> getPlayers() {
		return players;
	}
	public String getTurnname() {
		return turnname;
	}
	public void setTurnname(String turnname) {
		this.turnname = turnname;
	}
	public List<String[]> getButtons() {
		return buttons;
	}
	public boolean isGame() {
		return isGame;
	}
	public void setGame(boolean isGame) {
		this.isGame = isGame;
	}
	private ServerState() {
		connectedClients = new HashMap<String, ClientConnection>();
		users = new ArrayList<User>();
		playerlist = new HashSet<String> ();
		cyclelist = new HashSet<String> ();
		buttons = new ArrayList<String[]> ();
		players = new HashMap<String, Integer> ();
		isGame = false;
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
	public synchronized void clientDisconnected(String name) {
		connectedClients.remove(name);
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
	public synchronized void vote(int i,String currentName,String dir, int count) {
		if (cyclelist.isEmpty()) {
			Iterator<String> iterator = playerlist.iterator();
			while (iterator.hasNext()) {
				cyclelist.add((String) iterator.next());
			}
		}
		if (i == 0) {
			count1++;
			count2++;
			pass = 0;
			if (count1 == (playerlist.size()-1)) {
				players.put(currentName, players.get(currentName)+count);
				// Calculate and synchronization scores
				try {
					Map<String, ClientConnection> clients = connectedClients;
					for (String name : playerlist) {
						ClientConnection client = clients.get(name);
						client.getWriter().write("score"+" "+currentName+" "+dir+" "+count+"\n");
						client.getWriter().flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else if (i==1) {
			count2++;
			pass = 0;
		} else if (i==2) {
			pass++;
			if (pass==playerlist.size()) {
				buttons.clear();
				pass=0;
				try {
					Collection<ClientConnection> c = connectedClients.values();
					for (ClientConnection c1:c) {
						for (String player1 : playerlist) {
							c1.getWriter().write("touser"+" "+player1+"\n");
							c1.getWriter().flush();
						}
					}
					for (String player : playerlist) {
						ClientConnection client = connectedClients.get(player);
						client.getWriter().write("logout"+" "+"1"+"\n");
						client.getWriter().flush();
					}
					for (User user:users) {
						if (user.isIswait()==true) {
							ClientConnection client = connectedClients.get(user.getUsername());
							client.getWriter().write("endW"+"\n");
							client.getWriter().flush();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				for (User user:users) {
					user.setIswait(true);
				}
				playerlist.clear();
				cyclelist.clear();
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
					turnname =cyclelist.iterator().next();
					client.getWriter().write("turn"+" "+turnname+"\n");
					client.getWriter().flush();
				}
				for (User user:users) {
					if (user.isIswait()==true) {
						ClientConnection client = clients.get(user.getUsername());
						for (String player:players.keySet()) {
							client.getWriter().write("Info"+" "+player+" "+players.get(player)+" "+turnname+"\n");
							client.getWriter().flush();
						}
					} else {
						System.out.println("f");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (i==3) {
			count2++;
			pass = 0;
		}
		if (count2 == (playerlist.size())-1) {
			count1 = 0;
			count2 = 0;
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
					turnname =cyclelist.iterator().next();
					client.getWriter().write("turn"+" "+turnname+"\n");
					client.getWriter().flush();
				}
				for (User user:users) {
					if (user.isIswait()==true) {
						ClientConnection client = clients.get(user.getUsername());
						for (String player:players.keySet()) {
							client.getWriter().write("Info"+" "+player+" "+players.get(player)+" "+turnname+"\n");
							client.getWriter().flush();
						}
					} else {
						System.out.println("f");
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
