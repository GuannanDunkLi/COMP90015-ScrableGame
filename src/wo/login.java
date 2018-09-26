package wo;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.TextField;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

public class login {
	private Socket socket;
//	private JFrame frame;
	private gui gui;
//	public JFrame getFrame() {
//		return frame;
//	}
//	public void setFrame(JFrame frame) {
//		this.frame = frame;
//	}
	public gui getGui() {
		return gui;
	}
	public void setGui(gui gui) {
		this.gui = gui;
	}

	
	public login() throws Exception {
		initialize();
	}
	public login(Socket socket) throws Exception {
		this.socket = socket;
		initialize();
		
		
	}

	// Initialize the contents of the frame.
	private void initialize() throws Exception {
		final JFrame frame = new JFrame("Log in");
//		if (socket!=null) {
		gui = new gui(socket);
//		System.out.println("Bushi null");
//		}
		gui.getFrame().setVisible(false);
		frame.setSize(680, 480);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font font = new Font("Arial", Font.BOLD, 22); // Unify fonts
		frame.getContentPane().setFont(font);
		frame.getContentPane().setForeground(Color.BLACK);
		frame.getContentPane().setLayout(null);

		final TextField textField = new TextField();
		textField.setBounds(130, 60, 350, 100);
		textField.setFont(new Font("Arial", Font.BOLD, 36));
		frame.getContentPane().add(textField);

		Button loginButton = new Button("Login");
		loginButton.setFont(font);
		loginButton.setBackground(Color.CYAN);
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 用户名查重
				if (textField.getText().equals("1")) {
					JOptionPane.showMessageDialog(null, "User name is existed. Please change name.","Warning",JOptionPane.WARNING_MESSAGE);
				} else {
					try {
						
						gui.getFrame().setVisible(true);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					frame.setVisible(false);
				}

			}
		});
		loginButton.setBounds(230, 290, 200, 60);
		frame.getContentPane().add(loginButton);

		Button randomNameButton = new Button("Random Pick");
		randomNameButton.setBounds(490, 70, 160, 80);
		frame.getContentPane().add(randomNameButton);
		randomNameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
				// String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
				Random random = new Random();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < 8; i++) {
					int number = random.nextInt(51);
					sb.append(str.charAt(number));
				}
				textField.setText(sb.toString());
			}
		});
		frame.setVisible(true);
	}
}
