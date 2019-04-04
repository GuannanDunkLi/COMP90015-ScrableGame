/**  
* This class is an implementation of client. 
*/
package DSProjTwo.client;

import javax.swing.*;
import DSProjTwo.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Gui {
	private JFrame frame;
	private Socket socket;
	private BufferedWriter ClientWriter;
	private ArrayList<JButton> buttons;
	private JButton passButton;
	private JButton logoutButton;
	private JTextArea textOutput;
	private User user;
	private String currentName;
	private String s;
	private int row;
	private int col;
	private int row1;
	private int row2;
	private int col1;
	private int col2;
	private JOptionPane j;
	private boolean state;
	private boolean isWatch;
	

	public JButton getPassButton() {
		return passButton;
	}
	public JButton getLogoutButton() {
		return logoutButton;
	}
	public boolean isWatch() {
		return isWatch;
	}
	public void setWatch(boolean isWatch) {
		this.isWatch = isWatch;
	}

	public boolean getState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public int getRow1() {
		return row1;
	}

	public int getRow2() {
		return row2;
	}

	public int getCol1() {
		return col1;
	}

	public int getCol2() {
		return col2;
	}

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

	public Gui(Socket socket, User user) throws Exception {
		this.socket = socket;
		this.user = user;
		this.state = true;
		this.isWatch = false;
		initialize();
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	private void initialize() throws Exception {
		frame = new JFrame(user.getUsername() + "'s Interface");
		frame.setSize(1400, 800);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.LEFT));
		p1.setPreferredSize(new Dimension(1000, 800));
		p2.setPreferredSize(new Dimension(400, 800));
		buttons = new ArrayList<JButton>();
		ClientWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		p1.setLayout(new GridLayout(20, 20));
		for (row = 0; row < 20; row++) {
			for (col = 0; col < 20; col++) {
				final JButton button = new JButton();
				button.setFont(new Font("Arial", Font.BOLD, 16));
				buttons.add(button);
				button.addActionListener(new ActionListener() {
					private int row;
					private int col;
					@Override
					public void actionPerformed(ActionEvent e) {
						if (user.getUsername().equals(currentName) && state==true && button.getText()==""&& isWatch==false) {
							s = JOptionPane.showInputDialog("Please input the letter from keyboard");
							try {
								if ((s != null) && s.length() != 0 && s.length() == 1&&(!hasSpecialChar(s))) {
									button.setText(s);
									button.setForeground(Color.red);
									Object[] possibleValues = { "horizontal", "vertical", "null" };
									String s1 = (String) JOptionPane.showInputDialog(null, "Choose direction", "Input",
											JOptionPane.INFORMATION_MESSAGE, null, possibleValues, possibleValues[0]);
									System.out.println("s1="+s1);
									if (s1==null) {
										button.setText("");
									} else {
										ClientWriter.write("add" + " " + row + " " + col + " " + s + " " + s1 + " "
												+ user.getUsername() + "\n");
										ClientWriter.flush();
										state = false;
									}
								} else if (s != null) {
									JOptionPane.showMessageDialog(null, "You can only input one valid letter.", "Warning",
											JOptionPane.WARNING_MESSAGE);
								}
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						} else if (button.getText()!="") {
							JOptionPane.showMessageDialog(null, "The word is existed.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						} else if (state==false) {
							JOptionPane.showMessageDialog(null, "Wait for the votes from others.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						} else if (isWatch==true) {
							JOptionPane.showMessageDialog(null, "You can't play the game", "Warning",
									JOptionPane.WARNING_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "It's not your turn.", "Warning",
									JOptionPane.WARNING_MESSAGE);
						}
					}

					// Read row and column
					public ActionListener accept(int row, int col) {
						this.row = row;
						this.col = col;
						return this;
					}
				}.accept(row, col));
				p1.add(button);
			}
		}
		frame.getContentPane().add(p1, BorderLayout.WEST);
		frame.getContentPane().add(p2, BorderLayout.EAST);
		p2.setLayout(null);
		Font font = new Font("Arial", Font.BOLD, 22); // Unify fonts
		
		// Create user list
		JLabel wordLabel = new JLabel("User Information");
		wordLabel.setFont(font);
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setBounds(35, 15, 350, 40);
		p2.add(wordLabel);
		
		textOutput = new JTextArea();
		textOutput.setFont(new Font("Arial", Font.BOLD, 30));
		JScrollPane jslp = new JScrollPane(textOutput);
		jslp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jslp.setBounds(15, 60, 400, 350);
		p2.add(jslp);

		//Create Pass button
		passButton = new JButton("Pass");
		passButton.setBounds(80, 500, 260, 60);
		passButton.setFont(font);
		p2.add(passButton);
		passButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (currentName.equals(user.getUsername()) && state==true) {
						ClientWriter.write("vote" + " " + 2 + " " + currentName + " " + "h" + " "+1+"\n");
						ClientWriter.flush();
					} else if (state==false) {
						JOptionPane.showMessageDialog(null, "Wait for the votes from others.", "Warning",
								JOptionPane.WARNING_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "It's not your turn.", "Warning",
								JOptionPane.WARNING_MESSAGE);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		//Create Log Out button
		logoutButton = new JButton("Log Out");
		logoutButton.setBounds(80, 660, 260, 60);
		logoutButton.setFont(font);
		p2.add(logoutButton);
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ClientWriter.write("logout" + " " + user.getUsername() + "\n");
					ClientWriter.flush();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		// Close the window
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				if (isWatch==true) {
					int ret = JOptionPane.showConfirmDialog(null, "Are you sure to exit?", "Attention",
							JOptionPane.YES_NO_OPTION);
					if (ret == JOptionPane.YES_OPTION) {
						try {
							for(JButton button:buttons) {
								button.setText("");
							}
							ClientWriter.write("stopW"+"\n");
							ClientWriter.flush();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
					}
				}
			}
		});
	}

	// Score calculation
	public int count(String direction, int row, int col) {
		int r = row, c = col;
		String[][] button = new String[20][20];
		int length = 0;
		Iterator it = buttons.iterator();
		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 20; y++) {
				JButton b = (JButton) it.next();
				button[x][y] = b.getText();
			}
		}
		if (direction.equals("horizontal")) {
			if ((col == 0 && button[row][col + 1].equals("")) || (col == 19 && button[row][col - 1].equals(""))
					|| (col > 1 && col < 18 && button[row][col - 1].equals("") && button[row][col + 1].equals(""))) {
				return 1;
			} else {
				while (col < 20 && col >= 0 && button[row][col] != "") {
					col--;
					length++;
				}
				row1 = r;
				if (col==-1) {
					col = 0;
				} else {
					col1 = col;
				}
				row = r;
				col = c;
				while (col < 20 && col >= 0 && button[row][col] != "") {
					col++;
					length++;
				}
				row2 = r;
				col2 = col;
			}
		} else if (direction.equals("vertical")) {
			if ((row > 0 && row < 18 && button[row - 1][col] == "" && button[row + 1][col] == "")
					|| (row == 0 && button[row + 1][col] == "") || (row == 19 && button[row - 1][col] == "")) {
				return 1;
			} else {
				while (row < 20 && row >= 0 && button[row][col] != "") {
					row--;
					length++;
				}
				if (row==-1) {
					row = 0;
				} else {
					row1 = row;
				}
				col1 = c;
				row = r;
				col = c;
				while (row >= 0 && row < 20 && button[row][col] != "") {
					row++;
					length++;
				}
				row2 = row;
				col2 = c;
			}
		}
		return length - 1;
	}
	
	// Special character handle
	public boolean hasSpecialChar(String str) { 
		String regEx = "[0123456789_`~!@#$%^&*()+=|{}\\[\\].<>/?~£¡@#£¤%¡­¡­&*£¨£©¡ª¡ª+|{}¡¾¡¿¡®£»£º¡±¡°¡¯¡££¬¡¢£¿]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

}
