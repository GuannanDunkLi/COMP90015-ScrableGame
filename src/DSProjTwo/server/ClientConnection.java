/**  
* This class defines the function of the thread of each connection. 
*/
package DSProjTwo.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import DSProjTwo.User;

public class ClientConnection extends Thread {
	private Socket clientSocket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private int clientNum;
	private boolean isfirst;
	private static int row;
	private static int col;
	private ServerState serverstate;
	private String username; // Store current client connection name

	public ClientConnection(Socket clientSocket, int clientNum) {
		try {
			this.clientSocket = clientSocket;
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
			this.clientNum = clientNum;
			this.isfirst = true;
			serverstate = ServerState.getInstance();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public BufferedWriter getWriter() {
		return writer;
	}

	public void setWriter(BufferedWriter writer) {
		this.writer = writer;
	}

	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread().getName() + " - Reading instructions from client's " + clientNum
					+ " connection");
			String clientMsg = null;
			while ((clientMsg = reader.readLine()) != null) {
				System.out.println(Thread.currentThread().getName() + " - Instruction from client " + clientNum
						+ " received: " + clientMsg);
				String[] Msg = clientMsg.split(" ");
				String instruction = Msg[0];
				if (instruction.equals("add")) {
					add(Msg);
				} else if (instruction.equals("checkUser")) {
					checkuser(Msg);
				} else if (instruction.equals("addUser")) {
					addUser(Msg);
				} else if (instruction.equals("invite")) {
					invite(Msg);
				} else if ("join".equals(instruction)) {
					join(Msg);
				} else if ("start".equals(instruction)) {
					start(Msg);
				} else if ("vote".equals(instruction)) {
					vote(Msg);
				} else if ("logout".equals(instruction)) {
					logout(Msg);
				} else if ("exit".equals(instruction)) {
					exit(Msg);
				} else if ("watch".equals(instruction)) {
					watch(Msg);
				} else if ("stopW".equals(instruction)) {
					try {
						writer.write("stopW" + "\n");
						writer.flush();
					} catch (IOException e) {
						// e.printStackTrace();
					}
				}
			}
			clientSocket.close();
			serverstate.clientDisconnected(username);
			System.out.println(Thread.currentThread().getName() + " - Client " + clientNum + " disconnected");
		} catch (Exception e) {
			// e.printStackTrace();
			// System.out.println("1");
			if (clientSocket != null) {
				try {
					clientSocket.close();
				} catch (IOException e1) {
					// e1.printStackTrace();
				}
			}
		}
	}

	// Needs to be synchronized because multiple threads can me invoking this method
	// at the same time
	public synchronized void broadWrite(String msg) {
		try {
			Map<String, ClientConnection> clients = serverstate.getConnectedClients();
			for (String name : clients.keySet()) {
				ClientConnection client = clients.get(name);
				client.writer.write(msg);
				client.writer.flush();
				System.out.println(Thread.currentThread().getName() + " - Message sent to client " + clientNum);
			}

		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public synchronized void vote(String[] Msg) {
		int i = Integer.parseInt(Msg[1]);
		String currentName = Msg[2];
		String dir = Msg[3];
		int count = Integer.parseInt(Msg[4]);
		serverstate.vote(i, currentName, dir, count);
	}

	public synchronized void add(String[] Msg) {
		row = Integer.parseInt(Msg[1]);
		col = Integer.parseInt(Msg[2]);
		List<String[]> buttons = serverstate.getButtons();
		buttons.add(new String[] { Msg[1], Msg[2], Msg[3] });
		String letter = Msg[3];
		String s = Msg[4];
		String player = Msg[5];
		broadWrite("add" + " " + row + " " + col + " " + letter + " " + s + " " + player + "\n");
	}

	public synchronized void checkuser(String[] Msg) {
		String username = Msg[1];
		List<User> users = serverstate.getUsers();
		String string = "";
		for (User user : users) {
			if (user.getUsername().equals(username)) {
				string = "exist";
			}
		}
		try {
			writer.write(string + "\n");
			writer.flush();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public synchronized void addUser(String[] Msg) {
		username = Msg[1];
		int score = Integer.parseInt(Msg[2]);
		serverstate.clientConnected(username, this);
		User user = new User(username, score);
		List<User> users = serverstate.getUsers();
		users.add(user);
		Set<String> playerlist = serverstate.getPlayerlist();
		Map<String, ClientConnection> clients = serverstate.getConnectedClients();
		try {
			for (String name : clients.keySet()) {
				ClientConnection client = clients.get(name);
				if (client.isfirst == true) {
					for (User u : users) {
						if (u.isIswait() == true) {
							client.writer.write("connection" + " " + u.getUsername() + " " + u.getScore() + " "
									+ serverstate.isGame() + "\n");
							client.writer.flush();
							client.isfirst = false;
						}
					}
					for (String player : playerlist) {
						client.writer.write("join" + " " + player + "\n");
						client.writer.flush();
					}
				} else if (client.isfirst == false) {
					client.writer
							.write("connection" + " " + username + " " + score + " " + serverstate.isGame() + "\n");
					client.writer.flush();
				}
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public synchronized void join(String[] Msg) {
		Set<String> pl = serverstate.getPlayerlist();
		HashMap<String, Integer> players = serverstate.getPlayers();
		if (!pl.contains(Msg[1])) {
			List<User> l = serverstate.getUsers();
			for (User user : l) {
				if (user.getUsername().equals(Msg[1])) {
					user.setIswait(false);
				}
			}
			pl.add(Msg[1]);
			players.put(Msg[1], 0);
			broadWrite("join" + " " + Msg[1] + "\n");
		}
	}

	public synchronized void invite(String[] Msg) {
		String inviteName = Msg[1];
		String name = Msg[2];
		ClientConnection client = serverstate.getConnectedClients().get(inviteName);
		try {
			client.writer.write("invite" + " " + name + "\n");
			client.writer.flush();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public synchronized void start(String[] Msg) {
		Set<String> playerlist = serverstate.getPlayerlist();
		String firstP = playerlist.iterator().next();
		serverstate.setTurnname(firstP);
		Map<String, ClientConnection> clientC = serverstate.getConnectedClients();
		List<User> users = serverstate.getUsers();
		serverstate.setGame(true);
		try {
			for (User user : users) {
				if (user.isIswait() == true) {
					ClientConnection client = clientC.get(user.getUsername());
					client.writer.write("ban" + "\n");
					client.writer.flush();
				}
			}
			for (String player : playerlist) {
				ClientConnection client = clientC.get(player);
				client.writer.write("start" + " " + firstP + "\n");
				client.writer.flush();
				for (String player1 : playerlist) {
					client.writer.write(
							"Info" + " " + player1 + " " + serverstate.getPlayers().get(player1) + " " + firstP + "\n");
					client.writer.flush();
				}
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public synchronized void logout(String[] Msg) {
		String name = Msg[1];
		serverstate.setGame(false);
		serverstate.getButtons().clear();
		List<User> users = serverstate.getUsers();
		HashMap<String, Integer> players = serverstate.getPlayers();
		Set<String> playerlist = serverstate.getPlayerlist();
		Map<String, ClientConnection> clientC = serverstate.getConnectedClients();
		try {
			Collection<ClientConnection> c = clientC.values();
			for (ClientConnection c1 : c) {
				for (String player1 : playerlist) {
					c1.writer.write("touser" + " " + player1 + "\n");
					c1.writer.flush();
				}
			}
			for (String player : playerlist) {
				ClientConnection client = clientC.get(player);
				client.writer.write("logout" + " " + name + "\n");
				client.writer.flush();
			}
			for (User user : users) {
				if (user.isIswait() == true) {
					ClientConnection client = clientC.get(user.getUsername());
					client.writer.write("endW" + "\n");
					client.writer.flush();
				}
			}
		} catch (IOException e) {
			// e.printStackTrace();
		}
		for (User user : users) {
			user.setIswait(true);
		}
		serverstate.getCyclelist().clear();
		playerlist.clear();
		players.clear();
	}

	public synchronized void exit(String[] Msg) {
		String name = Msg[1];
		Map<String, ClientConnection> clients = serverstate.getConnectedClients();
		try {
			for (String name1 : clients.keySet()) {
				ClientConnection client = clients.get(name1);
				client.writer.write("exit" + " " + name + "\n");
				client.writer.flush();
			}
			List<User> users = serverstate.getUsers();
			for (User user : users) {
				if (user.getUsername().equals(name)) {
					users.remove(user);
					break;
				}
			}
			HashMap<String, Integer> players = serverstate.getPlayers();
			Set<String> playerlist = serverstate.getPlayerlist();
			playerlist.remove(name);
			players.remove(name);
			serverstate.clientDisconnected(name);
			System.out.println(playerlist.toString() + "wtf");
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public synchronized void watch(String[] Msg) throws Exception {
		String name = Msg[1];
		Set<String> playerlist = serverstate.getPlayerlist();
		HashMap<String, Integer> players = serverstate.getPlayers();
		Map<String, ClientConnection> clients = serverstate.getConnectedClients();
		List<User> users = serverstate.getUsers();
		List<String[]> buttons = serverstate.getButtons();
		if (buttons.isEmpty()) {
			writer.write("watch" + " " + 0 + " " + 0 + " " + "" + " " + serverstate.getTurnname() + "\n");
			writer.flush();
			for (String player1 : playerlist) {
				writer.write(
						"Info" + " " + player1 + " " + players.get(player1) + " " + serverstate.getTurnname() + "\n");
				writer.flush();
			}
		} else {
			for (String[] is : buttons) {
				String row = is[0];
				String col = is[1];
				String letter = is[2];
				writer.write("watch" + " " + row + " " + col + " " + letter + " " + serverstate.getTurnname() + "\n");
				writer.flush();
			}
			for (String player1 : playerlist) {
				writer.write(
						"Info" + " " + player1 + " " + players.get(player1) + " " + serverstate.getTurnname() + "\n");
				writer.flush();
			}
		}
	}
}