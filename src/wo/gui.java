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
import java.util.Iterator;

public class gui {
	private JTextField textField;
	private JFrame frame;
	private Socket socket;
	private BufferedWriter ClientWriter;
	private ArrayList<JButton> buttons;
	private JTextArea textOutput;
	private User user;
	private ButtonGroup g;
	private JRadioButton chckbxHorizontal;
	private JRadioButton chckbxVertical;
	private JButton submitButton;
	private String currentName;
	private String s;
	private int row;
	private int col;
	private JOptionPane j;
//	private ArrayList<String> usernamelist;
//	private JButton button;

//	public ArrayList<String> getUsernamelist() {
//		return usernamelist;
//	}
//	public void setUsernamelist(ArrayList<String> usernamelist) {
//		this.usernamelist = usernamelist;
//	}
	public JOptionPane getJ() {
		return j;
	}
	public void setJ(JOptionPane j) {
		this.j = j;
	}
	
	public String getCurrentName() {
		return currentName;
	}
	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}
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
		frame = new JFrame(user.getUsername()+"'s Interface");
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
//		usernamelist = new ArrayList<String>();
		p1.setLayout(new GridLayout(20, 20));
//		System.out.println("1:"+user.getUsername());
//		System.out.println("2:"+currentName);
		for (row = 0; row < 20; row++) {
			for (col = 0; col < 20; col++) {
				final JButton button = new JButton();
				buttons.add(button);
//				if (user.getUsername().equals(currentName)) {
					button.addActionListener(new ActionListener() {
						private int row;
						private int col;
						@Override
						public void actionPerformed(ActionEvent e) {
//							if (user.getUsername().equals(currentName)) {
//							final String s=null;
							if (user.getUsername().equals(currentName)) {
								s = JOptionPane.showInputDialog("Please input the letter from keyboard");
							
							try {
								if ((s!=null) && s.length()!=0) {
									button.setText(s);
//									int i = JOptionPane.showConfirmDialog(null, "H choose yes", "choose", JOptionPane.YES_NO_OPTION);
									Object[] possibleValues = { "h", "v" };
									String s1 = (String) JOptionPane.showInputDialog(null, "Choose one", "Input",
											JOptionPane.INFORMATION_MESSAGE, null,possibleValues, possibleValues[0]);
//									submitButton.addActionListener(new ActionListener() {
//										private String s;
//										private int row;
//										private int col;
//										public void actionPerformed(ActionEvent e) {
//											try {
//												if (chckbxHorizontal.isSelected()) {
													ClientWriter.write("add"+" "+row+" "+col+" "+s+" "+s1+" "+user.getUsername()+"\n");
													System.out.println("zheli"+row+col);
													ClientWriter.flush();
//												}else if (chckbxVertical.isSelected()) {
//													ClientWriter.write("add"+" "+row+" "+col+" "+s+" "+"v"+" "+user.getUsername()+"\n");
//													System.out.println("v add");
//													ClientWriter.flush();
//												}
//											} catch (IOException e1) {
//												// TODO Auto-generated catch block
//												e1.printStackTrace();
//											}
//										}
//										public ActionListener accept(String s,int row, int col) {
//							                this.s = s;
//							                this.row = row;
//							                this.col = col;
//							                return this;
//							            }
//									}.accept(s,row, col)
//									);
	//								ClientWriter.write("add"+" "+row+" "+col+" "+s+"\n");
	//								ClientWriter.flush();
								}
	//							command(socket, "add", " "+row+" "+col+" "+s);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							}
						}
						public ActionListener accept(int row, int col) {
			                this.row = row;
			                this.col = col;
			                return this;
			            }
					}.accept(row, col));
//				}
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
		
		chckbxHorizontal = new JRadioButton("Horizontal",true);
		chckbxHorizontal.setFont(font);
		chckbxHorizontal.setBounds(70, 430, 320, 55);
		p2.add(chckbxHorizontal);
		
		chckbxVertical = new JRadioButton("Vertical");
		chckbxVertical.setFont(font);
		chckbxVertical.setBounds(70, 480, 320, 55);
		p2.add(chckbxVertical);
		
		g=new ButtonGroup();
		g.add(chckbxHorizontal);
		g.add(chckbxVertical);
		
		submitButton = new JButton("Submit");
		submitButton.setBounds(35, 580, 150, 60);
		submitButton.setFont(font);
		p2.add(submitButton);
//		submitButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					if (chckbxHorizontal.isSelected()) {
//						ClientWriter.write("submit"+" "+"h");
//					}else if (chckbxVertical.isSelected()) {
//						ClientWriter.write("");
//					}
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		});
//		submitButton.addActionListener(new ActionListener() {
//			private String s;
//			private int row;
//			private int col;
//			public void actionPerformed(ActionEvent e) {
//				try {
//					System.out.println(s+row+col);
//					if (chckbxHorizontal.isSelected()) {
//						ClientWriter.write("add"+" "+row+" "+col+" "+s+" "+"h"+" "+user.getUsername()+"\n");
//						System.out.println("h: add "+row+" "+col);
//						ClientWriter.flush();
//					}else if (chckbxVertical.isSelected()) {
//						ClientWriter.write("add"+" "+row+" "+col+" "+s+" "+"v"+" "+user.getUsername()+"\n");
//						System.out.println("v add");
//						ClientWriter.flush();
//					}
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//			public ActionListener accept(String s,int row, int col) {
//                this.s = s;
//                this.row = row;
//                this.col = col;
//                return this;
//            }
//		}.accept(s,row, col)
//		);
		
		JButton passButton = new JButton("Pass");
		passButton.setBounds(260, 580, 150, 60);
		passButton.setFont(font);
		p2.add(passButton);
		passButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (currentName.equals(user.getUsername())) {
						ClientWriter.write("vote"+" "+2+" "+currentName+" "+"h"+"\n");
						ClientWriter.flush();
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		JButton logoutButton = new JButton("Log Out");
		logoutButton.setBounds(95, 690, 260, 60);
		logoutButton.setFont(font);
		p2.add(logoutButton);
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
						ClientWriter.write("logout"+" "+user.getUsername()+"\n");
						ClientWriter.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
//		if (user!=null) {
//			
//			ClientWriter.write("addUser"+" "+user.getUsername()+" "+user.getScore()+"\n");
//			ClientWriter.flush();
//			System.out.println("adduser");
//		}
//		frame.setVisible(true);
	}
	public int count(String direction,int row, int col) {
		int r= row, c = col;
		String[][] button = new String[20][20];
		int length = 0;
		Iterator it = buttons.iterator();
		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 20; y++) {
				JButton b=(JButton) it.next();
				button [x][y] = b.getText();
			}
		}
//		System.out.print(button[17][17]);
		if (direction.equals("h")) {
			if((col==0&&button[row][col+1].equals(""))||(col==19&&button[row][col-1].equals(""))||(col>1&&col<18&&button[row][col-1].equals("")&&button[row][col+1].equals(""))) {
				return 1;
			}
			else {
				while(col<20&&col>=0&&button[row][col]!="") {
					col--;
					length++;
				}
				row=r;col=c;
				while(col<20&&col>=0&&button[row][col]!="") {
					col++;
					length++;
				}
			}
		}
		else if (direction.equals("v")) {
			if((row>0&&row<18&&button[row-1][col]==""&&button[row+1][col]=="")||(row==0&&button[row+1][col]=="")||(row==19&&button[row-1][col]=="")) {
				return 1;
			}
			else {
				while(row<20&&row>=0&&button[row][col]!="") {
					row--;
					length++;
				}
				row=r;col=c;
				while(row>=0&&row<20&&button[row][col]!="") {
					row++;
					length++;
			}
		}
		}
	return length-1;
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
