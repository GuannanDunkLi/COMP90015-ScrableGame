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
	
	public ArrayList<JButton> getButtons() {
		return buttons;
	}
	public void setButtons(ArrayList<JButton> buttons) {
		this.buttons = buttons;
	}
	public gui() throws Exception {
		initialize();
	}
	public gui(Socket socket) throws Exception {
		initialize();
		this.socket = socket;
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
						button.setText(s);
						try {
							System.out.println(row+" "+col+" "+s);
							ClientWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
							ClientWriter.write("add"+" "+row+" "+col+" "+s+"\n");
							ClientWriter.flush();
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
		
		textField = new JTextField();
		p2.add(textField);
		textField.setColumns(50);
		
		JButton btnNewButton = new JButton("New button");
		p2.add(btnNewButton);
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
