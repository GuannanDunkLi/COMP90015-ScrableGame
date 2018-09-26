package wo.client;

import java.awt.Font;
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
			int num = 1;
			while((msg = reader.readLine()) != null) {
//				gui.getMsg(msg);
				System.out.println(msg);
//				if ("connection".equals(msg)) {
//					gui.getTextOutput().append("client"+num+"\n");
////					gui.getTextOutput().setText("client"+num);
//					num++;
//				}else {
					String[] Msg = msg.split(" ");
					String instruction = Msg[0];
					
					if (instruction.equals("add")) {
						int index = Integer.parseInt(Msg[1]);
						String letter = (Msg[2]);
						gui.getButtons().get(index).setText(letter);
					} else if (instruction.equals("connection")) {
						String username = Msg[1];
//						String ip = Msg[2];
//						String port = Msg[3];
//						String score = Msg[4];
						String score = Msg[2];
//						gui.getTextOutput().append(num+"."+username+" "+ip+" "+port+" "+score+"\n");
						gui.getTextOutput().append(num+"."+username+" "+score+"\n");
						num++;
					}
				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Server closed.","Error", JOptionPane.PLAIN_MESSAGE);
			System.exit(0);
		} 
	}
}
