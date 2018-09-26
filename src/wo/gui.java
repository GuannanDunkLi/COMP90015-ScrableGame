package wo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

public class gui {
	private JTextField textField;
	private JFrame frame;
	private Socket socket;
	private BufferedWriter ClientWriter;
	private ArrayList<JButton> buttons;
	private JTextArea textOutput;
	private User user;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public JTextArea getTextOutput() {
		return textOutput;
	}
	public void setTextOutput(JTextArea textOutput) {
		this.textOutput = textOutput;
	}
	public ArrayList<JButton> getButtons() {
		return buttons;
	}
	public void setButtons(ArrayList<JButton> buttons) {
		this.buttons = buttons;
	}
	public gui() throws Exception {
		initialize();
	}
	public gui(Socket socket,User user) throws Exception {
		this.socket = socket;
		this.user =user;
		initialize();
		
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	private void initialize() throws Exception {
		frame = new JFrame();
		frame.setSize(1400, 800);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		p1.setPreferredSize(new Dimension(1000, 800));
		p2.setPreferredSize(new Dimension(400, 800));
		buttons=new ArrayList<JButton>();
		ClientWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
//		if (user!=null) {
//			
//			ClientWriter.write("addUser"+" "+user.getUsername()+" "+user.getScore()+"\n");
//			System.out.println("adduser");
//		}
		p1.setLayout(new GridLayout(20, 20));
		for (int row = 0; row < 20; row++) {
			for (int col = 0; col < 20; col++) {
				final JButton button = new JButton();
				buttons.add(button);
				button.addActionListener(new ActionListener() {
					private int row;
					private int col;
					@Override
					public void actionPerformed(ActionEvent e) {
						String s = JOptionPane.showInputDialog("Please input the letter from keyboard");
						
						try {
							if (socket==null) {
							System.out.println("socket is empty");
							}
							
							if ((s!=null) && s.length()!=0) {
								button.setText(s);
								ClientWriter.write("add"+" "+row+" "+col+" "+s+"\n");
								ClientWriter.flush();
							}
//							command(socket, "add", " "+row+" "+col+" "+s);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					public ActionListener accept(int row, int col) {
		                this.row = row;
		                this.col = col;
		                return this;
		            }
				}.accept(row, col));
				p1.add(button);
			}
		}
//		JTextArea ta = new JTextArea();
//		p2.add(ta);
		frame.getContentPane().add(p1,BorderLayout.WEST);
		frame.getContentPane().add(p2,BorderLayout.EAST);
		p2.setLayout(null);
		Font font = new Font("Arial", Font.BOLD, 22); // Unify fonts
		JLabel wordLabel = new JLabel("User Information");
		wordLabel.setFont(font);
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setBounds(35, 15, 350, 40);
		p2.add(wordLabel);
		
		// User list area
		textOutput = new JTextArea();
		textOutput.setFont(new Font("Arial", Font.BOLD, 30));
		JScrollPane jslp = new JScrollPane(textOutput);
		jslp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jslp.setBounds(15, 60, 400, 350);
		p2.add(jslp);
		
		JCheckBox chckbxHorizontal = new JCheckBox("Horizontal");
		chckbxHorizontal.setFont(font);
		chckbxHorizontal.setBounds(70, 430, 320, 55);
		p2.add(chckbxHorizontal);
		
		JCheckBox chckbxVertical = new JCheckBox("Vertical");
		chckbxVertical.setFont(font);
		chckbxVertical.setBounds(70, 480, 320, 55);
		p2.add(chckbxVertical);
		
		JButton submitButton = new JButton("Submit");
		submitButton.setBounds(35, 580, 150, 60);
		submitButton.setFont(font);
		p2.add(submitButton);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		JButton passButton = new JButton("Pass");
		passButton.setBounds(260, 580, 150, 60);
		passButton.setFont(font);
		p2.add(passButton);
		passButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		JButton logoutButton = new JButton("Log Out");
		logoutButton.setBounds(95, 690, 260, 60);
		logoutButton.setFont(font);
		p2.add(logoutButton);
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		if (user!=null) {
			
			ClientWriter.write("addUser"+" "+user.getUsername()+" "+user.getScore()+"\n");
			ClientWriter.flush();
			System.out.println("adduser");
		}
//		frame.setVisible(true);
	}
//	public void command(Socket socket, String command, String inputStr) throws IOException {
//		if (!command.equals("exit")) {
//			// Send the input string to the server by writing to the socket output stream
//			ClientWriter.write(command+inputStr + "\n");
//			ClientWriter.flush();
//		} else if (command.equals("exit"))
//			socket.close();
//	}
}
