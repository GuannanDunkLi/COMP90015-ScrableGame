/**  
* This class is an implementation of createing login interface. 
*/
package DSProjTwo.client;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import DSProjTwo.User;

import java.awt.TextField;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

public class Login {
	private Socket socket;
	private BufferedReader reader;
	private BufferedWriter writer;
	private Gui gui;
	private String msg;

	public Login(Socket socket, BufferedReader reader, BufferedWriter writer) throws Exception {
		this.socket = socket;
		this.reader = reader;
		this.writer = writer;
		initialize();
	}

	// Initialize the contents of login GUI.
	private void initialize() throws Exception {
		final JFrame frame = new JFrame("Log in");
		frame.setSize(680, 480);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font font = new Font("Arial", Font.BOLD, 22); // Unify fonts
		frame.getContentPane().setFont(font);
		frame.getContentPane().setForeground(Color.BLACK);
		frame.getContentPane().setLayout(null);

		// User name input area
		final TextField textField = new TextField();
		textField.setBounds(130, 60, 350, 100);
		textField.setFont(new Font("Arial", Font.BOLD, 36));
		frame.getContentPane().add(textField);

		// Create Login button
		Button loginButton = new Button("Login");
		loginButton.setFont(font);
		loginButton.setBackground(Color.CYAN);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textField.getText();
				// Blank input check
				if (textField.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please input valid name", "Warning",
							JOptionPane.WARNING_MESSAGE);
				} else {
					// User name check
					try {
						writer.write("checkUser" + " " + name + "\n");
						writer.flush();
						msg = reader.readLine();
						if ("exist".equals(msg)) {
							JOptionPane.showMessageDialog(null, "User name is existed. Please change name.", "Warning",
									JOptionPane.WARNING_MESSAGE);
							msg = null;
						} else {
							try {
								// Initialize users' scores with zero
								User user = new User(name, 0);
								InviteGUI memberpool = new InviteGUI(socket, user, gui, writer);
								gui = new Gui(socket, user);
								MessageListener ml = new MessageListener(reader, writer, gui, memberpool);
								ml.start();
								memberpool.getFrame().setVisible(true);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							frame.setVisible(false);
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "Server closed.", "Error", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}
				}
			}
		});
		loginButton.setBounds(230, 290, 200, 60);
		frame.getContentPane().add(loginButton);

		// Create random pick name button
		Button randomNameButton = new Button("Random Name");
		randomNameButton.setBounds(490, 70, 160, 80);
		frame.getContentPane().add(randomNameButton);
		randomNameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
				Random random = new Random();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < 5; i++) {
					int number = random.nextInt(51);
					sb.append(str.charAt(number));
				}
				textField.setText(sb.toString());
			}
		});
		frame.setVisible(true);
	}
}
