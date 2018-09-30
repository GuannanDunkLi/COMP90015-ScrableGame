package wo.client;

import java.awt.Font;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import wo.InviteGUI;
import wo.User;
import wo.gui;
import wo.login;

public class MessageListener extends Thread {
	private BufferedReader reader;
	private gui gui;
//	private login login;
//	private Set<String> players;
	private HashMap<String, Integer> players;
	private InviteGUI memberpool;
	private ArrayList<String> winners;
	public MessageListener(BufferedReader reader, gui gui,InviteGUI memberpool) {
		this.reader = reader;
		this.gui = gui;
		this.memberpool =memberpool;
//		this.login = login;
//		players= new HashSet<String>();
		players= new HashMap<String,Integer>();
		winners = new ArrayList<String>();
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
						String s = Msg[3];
//						String s1 = Msg[4];
						String player = gui.getCurrentName();
						User u =gui.getUser();
						gui.getButtons().get(index).setText(letter);
						if ((!u.getUsername().equals(gui.getCurrentName()))&&gui.getFrame().isVisible()) {
							int i = gui.getJ().showConfirmDialog(null, player+" choose "+s, "Vote", JOptionPane.YES_NO_OPTION);
							memberpool.write("vote"+" "+i+" "+gui.getCurrentName()+" "+s+"\n");
//							System.out.println("vote"+" "+i+" "+gui.getCurrentName());
						}
					} else if (instruction.equals("connection")) {
						String username = Msg[1];
//						String ip = Msg[2];
//						String port = Msg[3];
//						String score = Msg[4];
//						String score = Msg[2];
//						gui.getTextOutput().append(num+"."+username+" "+ip+" "+port+" "+score+"\n");
//						gui.getTextOutput().append(num+"."+username+" "+score+"\n");
//						memberpool.getModel().addElement(num+"."+username+" "+score+"\n");
						memberpool.getModel().addElement(username);
//						gui.getUsernamelist().add(username);
//						login =login.getInstance();
//						if (login.getUsernamelist()!=null) {
//						login.getUsernamelist().add(username);
//						}
						memberpool.getList().setModel(memberpool.getModel());
						num++;
					} else if ("invite".equals(instruction)) {
//						int size=Integer.parseInt(Msg[1]);
//						int i = memberpool.getJ().showConfirmDialog(null, "Accept", "Invitation", JOptionPane.YES_NO_OPTION);
//						if (i==0) {
//							for (int j = 0;j<size;j++) {
////								memberpool.getTextArea().append(Msg[j+2]+"\n");
//								players.add(Msg[j+2]);
//							}
//						}
						int i = memberpool.getJ().showConfirmDialog(null, "Accept", "Invitation", JOptionPane.YES_NO_OPTION);
						if (i ==0) {
							memberpool.write("join"+" "+memberpool.getUser().getUsername()+"\n");
						}
					} else if ("join".equals(instruction)) {
//						players.add(Msg[1]);
						memberpool.getTextArea().append(Msg[1]+"\n");
						memberpool.getModel().removeElement(Msg[1]);
						memberpool.getList().setModel(memberpool.getModel());
					} else if ("addplayer".equals(instruction)) {
//						players.add(Msg[1]);
						memberpool.getTextArea().append(Msg[1]+"\n");
					} else if ("start".equals(instruction)) {
						String firstP = Msg[1];
						gui.setCurrentName(firstP);
						gui.getTextOutput().append("It's "+firstP+"'s turn\n");
						memberpool.getFrame().setVisible(false);
						gui.getFrame().setVisible(true);
					} else if ("Info".equals(instruction)) {
						players.put(Msg[1], 0);
						gui.getTextOutput().append("name:"+Msg[1]+" score: "+players.get(Msg[1])+"\n");
					} else if ("turn".equals(instruction)) {
						gui.setCurrentName(Msg[1]);
						gui.getTextOutput().setText("");
						gui.getTextOutput().append("It's "+gui.getCurrentName()+"'s turn\n");
						for (String player:players.keySet()) {
							gui.getTextOutput().append("name:"+player+" score: "+players.get(player)+"\n");
						}
					} else if ("score".equals(instruction)) {
						String player = Msg[1];
						String dir = Msg[2];
						int row = Integer.parseInt(Msg[3]);
						int col = Integer.parseInt(Msg[4]);
						int score = players.get(player);
						int count = gui.count(dir, row, col);
						System.out.println("count:"+count);
						players.put(player, score+count);
//						System.out.println("score"+row+" "+col);
					} else if ("stop".equals(instruction)) {
//						double maxV=0;
//						  int maxK=-1;
//						  Iterator keys = players.keySet().iterator();
//						  while(keys.hasNext()){
//						   Object key = keys.next();
//						   double value = Double.parseDouble(players.get(key).toString());
//						   if(value>maxV){
//						    maxV=value;
//						    maxK=Integer.parseInt(key.toString());
//						   }
//						  }
						JOptionPane.showMessageDialog(null,"someone disconnected,winner is "
						,"Error", JOptionPane.PLAIN_MESSAGE);
						System.exit(0);
					} else if ("logout".equals(instruction)) {
						Collection<Integer> c =players.values();
						Object[] obj = c.toArray();
						Arrays.sort(obj);
						int score = (int) obj[obj.length-1];
						for (String player : players.keySet()) {
							if (players.get(player)==score) {
								winners.add(player);
							}
						}
						String name =Msg[1];
//						String winnerss = winn
						if (name.equals("1")) {
							JOptionPane.showMessageDialog(null," All pass,winner is "+winners.toString()
									,"Error", JOptionPane.PLAIN_MESSAGE);
						}
						if ((!gui.getUser().getUsername().equals(name))&&(!name.equals("1"))) {
							JOptionPane.showMessageDialog(null,name+" has logout,winner is "+winners.toString()
									,"Error", JOptionPane.PLAIN_MESSAGE);
						}
						winners.clear();
						gui.getFrame().setVisible(false);
						memberpool.getFrame().setVisible(true);
					}else if ("touser".equals(instruction)) {
						String name=Msg[1];
						memberpool.getModel().addElement(name);
						memberpool.getList().setModel(memberpool.getModel());
						gui.getTextOutput().setText("");
						ArrayList<JButton> buttons = gui.getButtons();
						for (JButton jButton : buttons) {
							jButton.setText("");
						}
						memberpool.getTextArea().setText("");
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
