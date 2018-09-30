package wo.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import wo.User;

public class ClientConnection extends Thread {
	private Socket clientSocket;
	private BufferedReader reader; 
	private BufferedWriter writer; 
	private int clientNum;
	private boolean isfirst;
	private static int row;
	private static int col;
//	private Set<String> cyclelist;
//	private boolean iswait;

	public ClientConnection(Socket clientSocket, int clientNum) {
		try {
			this.clientSocket = clientSocket;
			reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
			writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
			this.clientNum = clientNum;
			this.isfirst = true;
//			this.iswait=true;
		} catch (Exception e) {
			e.printStackTrace();
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
			System.out.println(Thread.currentThread().getName() + " - Reading instructions from client's " + clientNum + " connection");
			ServerState ss = ServerState.getInstance();
//			User u1 = new User("jack",0);
//			User u2 = new User("tom",0);
//			User u3 = new User("jerry",0);
//			List<User> l = ss.getUsers();
//			l.add(u1);
//			l.add(u2);
//			l.add(u3);
//			for(int i = 0; i<l.size();i++) {
//				User u = l.get(i);
////				broadWrite("connection"+" "+u.getUsername()+" "+u.getIpAddress()+" "+u.getPort()+" "+u.getScore()+"\n");
//				broadWrite("connection"+" "+u.getUsername()+" "+u.getScore()+"\n");
//			}
			String clientMsg = null;
			while ((clientMsg = reader.readLine()) != null) {
				System.out.println(Thread.currentThread().getName() + " - Instruction from client " + clientNum + " received: " + clientMsg);
				String[] Msg = clientMsg.split(" ");
				String instruction = Msg[0];
				if (instruction.equals("add")) {
					add(Msg);
				} 
				else if (instruction.equals("addUser")) {
//					l.add(e);
					String username = Msg[1];
					int score = Integer.parseInt(Msg[2]);
					ServerState.getInstance().clientConnected(username, this);
					User user = new User(username,score);
//					User u1 = new User("jack",0);
//					User u2 = new User("tom",0);
//					User u3 = new User("jerry",0);
					List<User> l = ss.getUsers();
					l.add(user);
					Set<String> playerlist = ss.getPlayerlist();
//					l.add(u1);
//					l.add(u2);
//					l.add(u3);
					Map<String, ClientConnection> clients = ss.getConnectedClients();
//					for(ClientConnection client : clients) {
					for (String name : clients.keySet()) {
						ClientConnection client = clients.get(name);
//						client.writer.write(msg);
//						client.writer.flush();
//						System.out.println(Thread.currentThread().getName() + " - Message sent to client " + clientNum);
						if (client.isfirst==true) {
							for(int i = 0; i<l.size();i++) {
								User u = l.get(i);
//								broadWrite("connection"+" "+u.getUsername()+" "+u.getIpAddress()+" "+u.getPort()+" "+u.getScore()+"\n");
//								broadWrite("connection"+" "+u.getUsername()+" "+u.getScore()+"\n");
								if (u.isIswait()==true) {
									client.writer.write("connection"+" "+u.getUsername()+" "+u.getScore()+"\n");
									client.writer.flush();
									client.isfirst=false;
								}
							}
							for (String player : playerlist) {
								client.writer.write("addplayer"+" "+player+"\n");
								client.writer.flush();
							}
						} else if (client.isfirst==false) {
							client.writer.write("connection"+" "+username+" "+score+"\n");
							client.writer.flush();
						}
					}
//					for(int i = 0; i<l.size();i++) {
//						User u = l.get(i);
////						broadWrite("connection"+" "+u.getUsername()+" "+u.getIpAddress()+" "+u.getPort()+" "+u.getScore()+"\n");
//						broadWrite("connection"+" "+u.getUsername()+" "+u.getScore()+"\n");
//					}
				} else if (instruction.equals("invite")) {
					String name=Msg[1];
					String iniName=Msg[2];
//					Set<String> pl = ss.getPlayerlist();
//					pl.add(iniName);
//					pl.add(name);
					ClientConnection client = ss.getConnectedClients().get(name);
//					int size =pl.size();
//					StringBuffer list = new StringBuffer();
//					for (String player:pl) {
//						list.append(player+" ");
//					}
//					client.writer.write("invite"+" "+size+" "+list+"\n");
					client.writer.write("invite"+" "+iniName+"\n");
					client.writer.flush();
				} else if ("join".equals(instruction)) {
					Set<String> pl = ss.getPlayerlist();
					if (!pl.contains(Msg[1])) {
//						ss.getConnectedClients().get(Msg[1]).iswait=false;
						List<User> l=ss.getUsers();
						for (User user : l) {
							if (user.getUsername().equals(Msg[1])) {
								user.setIswait(false);
							}
						}
						pl.add(Msg[1]);
						broadWrite("join"+" "+Msg[1]+"\n");
					}
				} else if ("start".equals(instruction)) {
//					String firstP = Msg[1];
					Set<String> playerlist = ss.getPlayerlist();
					String firstP = playerlist.iterator().next();
					Map<String, ClientConnection> clientC = ss.getConnectedClients();
					if (playerlist.size()>1) {
						for (String player : playerlist) {
							ClientConnection client = clientC.get(player);
							client.writer.write("start"+" "+firstP+"\n");
							client.writer.flush();
							for (String player1 : playerlist) {
								client.writer.write("Info"+" "+player1+"\n");
								client.writer.flush();
							}
						}
					}
					System.out.println("playerlist:"+playerlist.toString());
				} else if ("vote".equals(instruction)) {
					int i = Integer.parseInt(Msg[1]);
					String currentName = Msg[2];
					String dir = Msg[3];
//					System.out.println("vote de "+row+col);
					ss.vote(i, currentName,dir,row,col);
				}else if("logout".equals(instruction)) {
					String name = Msg[1];
					Set<String> playerlist = ss.getPlayerlist();
					Map<String, ClientConnection> clientC = ss.getConnectedClients();
					for (String player : playerlist) {
						ClientConnection client = clientC.get(player);
						for (String player1 : playerlist) {
							client.writer.write("touser"+" "+player1+"\n");
							client.writer.flush();
						}
						client.writer.write("logout"+" "+name+"\n");
						client.writer.flush();
					}
					playerlist.clear();
				}
//				else if (instruction.equals("add")) {
//					add(Msg);
//				} else if (instruction.equals("remove")) {
//					remove(Msg);
//				}
				
			}
			clientSocket.close();
			ss.clientDisconnected(this);
			System.out.println(Thread.currentThread().getName() + " - Client " + clientNum + " disconnected");
		} catch (Exception e) {
			e.printStackTrace();
			ServerState ss = ServerState.getInstance();
			Set<String> playerlist = ss.getPlayerlist();
			Map<String, ClientConnection> clientC = ss.getConnectedClients();
			for (String player : playerlist) {
				ClientConnection client = clientC.get(player);
				try {
					if (client!=null) {
					client.writer.write("stop"+" "+"\n");
					client.writer.flush();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	//Needs to be synchronized because multiple threads can me invoking this method at the same time
	public synchronized void broadWrite(String msg) {
		try {
			Map<String, ClientConnection> clients = ServerState.getInstance().getConnectedClients();
			for (String name : clients.keySet()) {
				ClientConnection client = clients.get(name);
				client.writer.write(msg);
				client.writer.flush();
				System.out.println(Thread.currentThread().getName() + " - Message sent to client " + clientNum);
			}
//			writer.write(msg);
//			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void add(String[] Msg) {
		row = Integer.parseInt(Msg[1]);
		col = Integer.parseInt(Msg[2]);
		String letter = Msg[3];
		String s = Msg[4];
		String player = Msg[5];
		int index = row*20+col;
		System.out.println(index);
//		System.out.println("add de "+row+col);
		broadWrite("add"+" "+index+" "+letter+" "+s+" "+player+"\n");
		//Broadcast the client message to all other clients connected
		//to the server.
//		List<ClientConnection> clients = ServerState.getInstance().getConnectedClients();
//		for(ClientConnection client : clients) {
//			client.write("add"+" "+index+" "+letter+"\n");
//		}
	}
}