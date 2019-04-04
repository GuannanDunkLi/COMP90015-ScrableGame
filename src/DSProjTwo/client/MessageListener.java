/**  
* This class is an implementation of reading message from server. 
*/
package DSProjTwo.client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import DSProjTwo.User;

public class MessageListener extends Thread {
	private BufferedReader reader;
	private BufferedWriter writer;
	private Gui gui;
	private HashMap<String, Integer> players;
	private InviteGUI memberpool;
	private ArrayList<String> winners;

	public MessageListener(BufferedReader reader, BufferedWriter writer, Gui gui, InviteGUI memberpool) {
		this.reader = reader;
		this.writer = writer;
		this.gui = gui;
		this.memberpool = memberpool;
		players = new HashMap<String, Integer>();
		winners = new ArrayList<String>();
	}

	@Override
	public void run() {
		try {
			String msg = null;
			while ((msg = reader.readLine()) != null) {
				System.out.println(msg);
				String[] Msg = msg.split(" ");
				String instruction = Msg[0];
				if (instruction.equals("add")) {
					add(Msg);
				} else if (instruction.equals("connection")) {
					connection(Msg);
				} else if ("invite".equals(instruction)) {
					invite(Msg);
				} else if ("join".equals(instruction)) {
					join(Msg);
				} else if ("start".equals(instruction)) {
					start(Msg);
				} else if ("Info".equals(instruction)) {
					info(Msg);
				} else if ("turn".equals(instruction)) {
					gui.setCurrentName(Msg[1]);
					gui.setState(true);
					gui.getTextOutput().setText("");
					gui.getTextOutput().append("It's " + gui.getCurrentName() + "'s turn\n");
					for (String player : players.keySet()) {
						gui.getTextOutput().append("name:" + player + " score: " + players.get(player) + "\n");
					}
				} else if ("score".equals(instruction)) {
					score(Msg);
				} else if ("logout".equals(instruction)) {
					logout(Msg);
				} else if ("touser".equals(instruction)) {
					toUser(Msg);
				} else if ("exit".equals(instruction)) {
					exit(Msg);
				} else if ("ban".equals(instruction)) {
					memberpool.setIsban(true);
				} else if ("watch".equals(instruction)) {
					watch(Msg);
				} else if ("stopW".equals(instruction)) {
					memberpool.getFrame().setVisible(true);
					gui.getPassButton().setEnabled(true);
					gui.getLogoutButton().setEnabled(true);
					gui.getFrame().setVisible(false);
					gui.setWatch(false);
				} else if ("endW".equals(instruction)) {
					gui.getJ().showMessageDialog(null, "Game over.", "Warning", JOptionPane.WARNING_MESSAGE);
					memberpool.getFrame().setVisible(true);
					gui.getPassButton().setEnabled(true);
					gui.getLogoutButton().setEnabled(true);
					gui.getFrame().setVisible(false);
					gui.setWatch(false);
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Server closed.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}

	public synchronized void add(String[] Msg) {
		int row = Integer.parseInt(Msg[1]);
		int col = Integer.parseInt(Msg[2]);
		String letter = (Msg[3]);
		String s = Msg[4];
		String player = gui.getCurrentName();
		User u = gui.getUser();
		int index = row * 20 + col;
		gui.getButtons().get(index).setText(letter);
		gui.getButtons().get(index).setForeground(Color.red);
		if ((!u.getUsername().equals(gui.getCurrentName())) && gui.getFrame().isVisible()) {
			if ("null".equals(s)) {
				gui.getButtons().get(index).setForeground(Color.red);
				if (gui.isWatch() == false) {
					memberpool.write("vote" + " " + 3 + " " + gui.getCurrentName() + " " + s + " " + 0 + "\n");
					gui.getJ().showMessageDialog(null, player + " choose to skip voting process.", "Confirm",
							JOptionPane.INFORMATION_MESSAGE);
				}
				gui.getButtons().get(index).setForeground(Color.black);
			} else {
				if ("horizontal".equals(s)) {
					int count = gui.count("horizontal", row, col);
					for (int i = gui.getCol1(); i < gui.getCol2(); i++) {
						gui.getButtons().get(gui.getRow1() * 20 + i).setForeground(Color.red);
					}
					if (gui.isWatch() == false) {
						int i = gui.getJ().showConfirmDialog(null, "Do you agree this word?", "Vote",
								JOptionPane.YES_NO_OPTION);
						memberpool.write("vote" + " " + i + " " + gui.getCurrentName() + " " + s + " " + count + "\n");
					}
					for (int i2 = gui.getCol1(); i2 < gui.getCol2(); i2++) {
						gui.getButtons().get(gui.getRow1() * 20 + i2).setForeground(Color.black);
					}
				} else if ("vertical".equals(s)) {
					int count = gui.count("vertical", row, col);
					for (int i = gui.getRow1(); i < gui.getRow2(); i++) {
						gui.getButtons().get(i * 20 + gui.getCol2()).setForeground(Color.red);
					}
					if (gui.isWatch() == false) {
						int i = gui.getJ().showConfirmDialog(null, "Do you agree this word?", "Vote",
								JOptionPane.YES_NO_OPTION);
						memberpool.write("vote" + " " + i + " " + gui.getCurrentName() + " " + s + " " + count + "\n");
					}
					for (int i2 = gui.getRow1(); i2 < gui.getRow2(); i2++) {
						gui.getButtons().get(i2 * 20 + gui.getCol2()).setForeground(Color.black);
					}
				}
			}
		}
		gui.getButtons().get(index).setForeground(Color.black);
	}

	public synchronized void score(String[] Msg) {
		String player = Msg[1];
		String dir = Msg[2];
		int score = players.get(player);
		int count = Integer.parseInt(Msg[3]);
		players.put(player, score + count);
	}

	public synchronized void connection(String[] Msg) {
		String username = Msg[1];
		String isGame = Msg[3];
		if (isGame.equals("true")) {
			memberpool.setIsban(true);
		}
		// Add user list to memberpool
		memberpool.getModel1().addElement(username);
		memberpool.getList1().setModel(memberpool.getModel1());
	}

	public synchronized void join(String[] Msg) {
		memberpool.getPlayerlist().add(Msg[1]);
		memberpool.getModel2().addElement(Msg[1]);
		memberpool.getList2().setModel(memberpool.getModel2());
		memberpool.getModel1().removeElement(Msg[1]);
		memberpool.getList1().setModel(memberpool.getModel1());
	}

	public synchronized void invite(String[] Msg) {
		int i = memberpool.getjOptionPane().showConfirmDialog(null, "Accept invitation?", "Invitation",
				JOptionPane.YES_NO_OPTION);
		if (i == 0) {
			try {
				writer.write("join" + " " + memberpool.getUser().getUsername() + "\n");
				writer.flush();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}

	public synchronized void toUser(String[] Msg) {
		String name = Msg[1];
		memberpool.getModel1().addElement(name);
		memberpool.getList1().setModel(memberpool.getModel1());
		gui.getTextOutput().setText("");
		ArrayList<JButton> buttons = gui.getButtons();
		for (JButton jButton : buttons) {
			jButton.setText("");
		}
		memberpool.getModel2().removeAllElements();
		memberpool.getList2().setModel(memberpool.getModel2());
		memberpool.getPlayerlist().clear();
		memberpool.setIsban(false);
	}

	public synchronized void start(String[] Msg) {
		String firstP = Msg[1];
		gui.setCurrentName(firstP);
		gui.setState(true);
		gui.getTextOutput().append("It's " + firstP + "'s turn\n");
		memberpool.getFrame().setVisible(false);
		gui.getFrame().setVisible(true);
	}

	public synchronized void info(String[] Msg) {
		gui.getTextOutput().setText("");
		String name = Msg[3];
		gui.getTextOutput().append("It's " + name + "'s turn\n");
		players.put(Msg[1], Integer.parseInt(Msg[2]));
		for (String player : players.keySet()) {
			gui.getTextOutput().append("name:" + player + " score: " + players.get(player) + "\n");
		}
	}

	public synchronized void logout(String[] Msg) {
		Collection<Integer> c = players.values();
		Object[] obj = c.toArray();
		Arrays.sort(obj);
		int score = (int) obj[obj.length - 1];
		for (String player : players.keySet()) {
			if (players.get(player) == score) {
				winners.add(player);
			}
		}
		// Declare winner
		String name = Msg[1];
		if (name.equals("1")) {
			JOptionPane.showMessageDialog(null, " All pass, winner is " + winners.toString(), "Results",
					JOptionPane.PLAIN_MESSAGE);
		}
		if ((!gui.getUser().getUsername().equals(name)) && (!name.equals("1"))) {
			JOptionPane.showMessageDialog(null, name + " has logout,winner is " + winners.toString(), "Results",
					JOptionPane.PLAIN_MESSAGE);
		}
		winners.clear();
		players.clear();
		gui.getFrame().setVisible(false);
		memberpool.getFrame().setVisible(true);
	}

	public synchronized void exit(String[] Msg) {
		String name = Msg[1];
		memberpool.getModel1().removeElement(name);
		memberpool.getList1().setModel(memberpool.getModel1());
		memberpool.getModel2().removeElement(name);
		memberpool.getList2().setModel(memberpool.getModel2());
	}

	public synchronized void watch(String[] Msg) {
		int row = Integer.parseInt(Msg[1]);
		int col = Integer.parseInt(Msg[2]);
		String letter = Msg[3];
		String turnname = Msg[4];
		int index = row * 20 + col;
		gui.getButtons().get(index).setText(letter);
		memberpool.getFrame().setVisible(false);
		gui.getPassButton().setEnabled(false);
		gui.getLogoutButton().setEnabled(false);
		gui.getFrame().setVisible(true);
		gui.setWatch(true);
	}
}
