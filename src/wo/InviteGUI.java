package wo;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

import java.awt.List;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JTextArea;
import java.awt.Label;

public class InviteGUI {

	private JFrame frame;
	private User user;
	Set users;
	private Socket socket;
	private BufferedWriter writer;
	private gui gui;
	private DefaultListModel<String> model;
	private JList<String> list;
	private JTextArea textArea;
	private JOptionPane j;

	public JOptionPane getJ() {
		return j;
	}
	public void setJ(JOptionPane j) {
		this.j = j;
	}
	public JTextArea getTextArea() {
		return textArea;
	}
	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}
	public DefaultListModel<String> getModel() {
		return model;
	}
	public void setModel(DefaultListModel<String> model) {
		this.model = model;
	}
	public JList<String> getList() {
		return list;
	}
	public void setList(JList<String> list) {
		this.list = list;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					InviteGUI window = new InviteGUI(socket);
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	/**
	 * Create the application.
	 */
	public InviteGUI(Socket socket, User user,gui gui) {
		this.socket = socket;
		this.user = user;
		this.gui = gui;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		try {
//			if(socket!=null) {
//				reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
//			}
			if (user!=null) {
				writer.write("addUser"+" "+user.getUsername()+" "+user.getScore()+"\n");
				writer.flush();
				System.out.println("adduser");
			}
			users = new HashSet<User>();
			frame = new JFrame();
			frame.setBounds(100, 100, 769, 507);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(null);
			
			list = new JList();//create the list 
			list.setBounds(94, 87, 174, 256);
			frame.getContentPane().add(list);
			model = new DefaultListModel();//create model
//			String message = ("aa,bb");//reveive players information form the server
//			while(reader!=null&&reader.readLine()!=null) {
//				message = reader.readLine();
//			}
		
//			String[] player = message.split(",");
//			for(int x=0; x<player.length;x++) {//add players to the model
//				model.add(x, player[x]);
//			}	
//			model.addElement("jack");
//			list.setModel(model);//add model to list
			
			//System.out.println(list.getSelectedValue().toString());
			
			Button button = new Button("Invite");
			button.setFont(new Font("Dialog", Font.PLAIN, 27));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					users.add(list.getSelectedValue());//add element of list to the set players
//					JOptionPane.showMessageDialog(null,  list.getSelectedValue(),"Invite the Playes:", JOptionPane.INFORMATION_MESSAGE); 
					try {
						if (!(list.getSelectedValue().equals(user.getUsername()))) {
							writer.write("invite"+" "+list.getSelectedValue()+" "+user.getUsername()+"\n");
							writer.flush();
						}
//						System.out.println(list.getSelectedValue());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			button.setBounds(546, 168, 149, 83);
			frame.getContentPane().add(button);
			
			Button button_1 = new Button("StartGame");
			button_1.setFont(new Font("Dialog", Font.PLAIN, 27));
			button_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					System.out.print(users.toString());//write players 
					try {
						writer.write("start"+" "+user.getUsername()+"\n");
						writer.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			button_1.setBounds(546, 261, 149, 86);
			frame.getContentPane().add(button_1);
			
			textArea = new JTextArea();
			textArea.setBounds(323, 87, 174, 260);
			frame.getContentPane().add(textArea);
			
			Label label = new Label("Player List");
			label.setAlignment(Label.CENTER);
			label.setFont(new Font("Dialog", Font.PLAIN, 22));
			label.setBounds(94, 44, 174, 37);
			frame.getContentPane().add(label);
			
			Label label_1 = new Label("Invite List");
			label_1.setAlignment(Label.CENTER);
			label_1.setFont(new Font("Dialog", Font.PLAIN, 22));
			label_1.setBounds(323, 44, 174, 37);
			frame.getContentPane().add(label_1);
			
			Button button_2 = new Button("JoinList");
			button_2.setFont(new Font("Dialog", Font.PLAIN, 27));
			button_2.setBounds(546, 87, 149, 75);
			frame.getContentPane().add(button_2);
			button_2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						writer.write("join"+" "+user.getUsername()+"\n");
						writer.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
		catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		 
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
