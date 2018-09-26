package wo.client;

import java.io.BufferedReader;

import javax.swing.JOptionPane;
import wo.gui;

public class MessageListener extends Thread {
	private BufferedReader reader;
	private gui gui;
	public MessageListener(BufferedReader reader, gui gui) {
		this.reader = reader;
		this.gui = gui;
	}
	@Override
	public void run() {
		try {
			String msg = null;
			System.out.println("waiting");
			while((msg = reader.readLine()) != null) {
//				gui.getMsg(msg);
				System.out.println(msg);
				String[] Msg = msg.split(" ");
				String instruction = Msg[0];
				int index = Integer.parseInt(Msg[1]);
				String letter = (Msg[2]);
				if (instruction.equals("add")) {
					gui.getButtons().get(index).setText(letter);;
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Server closed.","Error", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		} 
	}
}
