/**  
* This class is an implementation of createing invitation hall interface. 
*/
package DSProjTwo.client;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JList;
import DSProjTwo.User;
import java.awt.Label;

public class InviteGUI {
	private Socket socket;
	private BufferedWriter writer;
	private JFrame frame;
	private User user;
	Set users;
	private Gui gui;
	private DefaultListModel<String> model1;
	private DefaultListModel<String> model2;
	private JList<String> list1;
	private JList<String> list2;
	private JOptionPane jOptionPane;
	private Set<String> playerlist;
	private boolean isban;

	public boolean isIsban() {
		return isban;
	}

	public void setIsban(boolean isban) {
		this.isban = isban;
	}

	public Set<String> getPlayerlist() {
		return playerlist;
	}

	public void setPlayerlist(Set<String> playerlist) {
		this.playerlist = playerlist;
	}

	public JOptionPane getjOptionPane() {
		return jOptionPane;
	}

	public DefaultListModel<String> getModel1() {
		return model1;
	}

	public DefaultListModel<String> getModel2() {
		return model2;
	}

	public JList<String> getList1() {
		return list1;
	}

	public JList<String> getList2() {
		return list2;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public InviteGUI(Socket socket, User user, Gui gui, BufferedWriter writer) {
		this.socket = socket;
		this.user = user;
		this.gui = gui;
		this.writer = writer;
		playerlist = new HashSet<String>();
		isban = false;
		initialize();
	}

	// Initialize the contents of the frame.
	private void initialize() {
		try {
			if (user != null) {
				writer.write("addUser" + " " + user.getUsername() + " " + user.getScore() + "\n");
				writer.flush();
			}
			users = new HashSet<User>();
			frame = new JFrame(user.getUsername() + "'s Interface");
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.setBounds(100, 100, 769, 507);
			frame.getContentPane().setLayout(null);

			// Create waiting list
			Label waitList = new Label("Waiting List");
			waitList.setAlignment(Label.CENTER);
			waitList.setFont(new Font("Arial", Font.BOLD, 22));
			waitList.setBounds(94, 44, 174, 37);
			frame.getContentPane().add(waitList);

			list1 = new JList();
			list1.setFont(new Font("Arial", Font.BOLD, 20));
			list1.setBounds(94, 87, 174, 256);
			frame.getContentPane().add(list1);
			model1 = new DefaultListModel();

			// Create player list
			Label playList = new Label("Player List");
			playList.setAlignment(Label.CENTER);
			playList.setFont(new Font("Arial", Font.BOLD, 22));
			playList.setBounds(323, 44, 174, 37);
			frame.getContentPane().add(playList);

			list2 = new JList();
			list2.setFont(new Font("Arial", Font.BOLD, 20));
			list2.setBounds(323, 87, 174, 260);
			frame.getContentPane().add(list2);
			model2 = new DefaultListModel();

			// Create JoinList button
			Button joinListButton = new Button("JoinList");
			joinListButton.setFont(new Font("Arial", Font.BOLD, 22));
			joinListButton.setBounds(546, 87, 149, 47);
			frame.getContentPane().add(joinListButton);
			joinListButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (playerlist.contains(user.getUsername())) {
							JOptionPane.showMessageDialog(null, "You have already joined the player list.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						} else if (isban == true) {
							JOptionPane.showMessageDialog(null, "Game has started, you can only watch the game.",
									"Warning", JOptionPane.WARNING_MESSAGE);
						} else {
							writer.write("join" + " " + user.getUsername() + "\n");
							writer.flush();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});

			// Create Invite button
			Button inviteButton = new Button("Invite");
			inviteButton.setFont(new Font("Arial", Font.BOLD, 22));
			inviteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (list1.getSelectedValue() == null) {
							JOptionPane.showMessageDialog(null, "No player is chosen.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						} else if (isban == true) {
							JOptionPane.showMessageDialog(null, "Game has started, you can only watch the game.",
									"Warning", JOptionPane.WARNING_MESSAGE);
						} else if (!(user.getUsername().equals(list1.getSelectedValue()))) {
							writer.write("invite" + " " + list1.getSelectedValue() + " " + user.getUsername() + "\n");
							writer.flush();
						} else {
							JOptionPane.showMessageDialog(null, "You can't invite yourself.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			inviteButton.setBounds(546, 156, 149, 47);
			frame.getContentPane().add(inviteButton);

			// Create StartGame button
			Button startGameButton = new Button("StartGame");
			startGameButton.setFont(new Font("Arial", Font.BOLD, 22));
			startGameButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (!playerlist.contains(user.getUsername())) {
							JOptionPane.showMessageDialog(null, "You can't start the game.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						} else if (playerlist.size() > 1) {
							writer.write("start" + " " + user.getUsername() + "\n");
							writer.flush();
						} else {
							JOptionPane.showMessageDialog(null, "The playing number is smaller than two.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			startGameButton.setBounds(546, 229, 149, 47);
			frame.getContentPane().add(startGameButton);

			// Create WatchGame button
			Button watchGameButton = new Button("WatchGame");
			watchGameButton.setFont(new Font("Arial", Font.BOLD, 22));
			watchGameButton.setBounds(546, 296, 149, 47);
			frame.getContentPane().add(watchGameButton);
			watchGameButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						if (playerlist.contains(user.getUsername())) {
							JOptionPane.showMessageDialog(null, "You can't watch the game.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						} else {
							writer.write("watch" + " " + user.getUsername() + "\n");
							writer.flush();
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// Close the window
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				int ret = JOptionPane.showConfirmDialog(null, "Are you sure to exit?", "Attention",
						JOptionPane.YES_NO_OPTION);
				if (ret == JOptionPane.YES_OPTION) {
					try {
						writer.write("exit" + " " + user.getUsername() + "\n");
						writer.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.exit(0);
				} else {

				}
			}
		});
	}

	public void write(String msg) {
		try {
			writer.write(msg);
			writer.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
