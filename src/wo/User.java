package wo;

import java.util.ArrayList;

import wo.server.ServerState;

public class User {
	private String username;
//	private static User instance;
//	private String ipAddress;
//	private String port;
	private int score;
	private boolean iswait;
//	private ArrayList<String> usernamelist;
//	public static synchronized User getInstance() {
//		if(instance == null) {
//			instance = new User();
//		}
//		return instance;
//	}
//	public ArrayList<String> getUsernamelist() {
//		return usernamelist;
//	}
//	public void setUsernamelist(ArrayList<String> usernamelist) {
//		this.usernamelist = usernamelist;
//	}
	public boolean isIswait() {
		return iswait;
	}
	public void setIswait(boolean iswait) {
		this.iswait = iswait;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
//	public String getIpAddress() {
//		return ipAddress;
//	}
//	public User(String username, String ipAddress, String port, int score) {
		public User(String username, int score) {
		super();
		this.username = username;
//		this.ipAddress = ipAddress;
//		this.port = port;
		this.score = score;
		this.iswait=true;
	}
//	public void setIpAddress(String ipAddress) {
//		this.ipAddress = ipAddress;
//	}
//	public String getPort() {
//		return port;
//	}
//	public void setPort(String port) {
//		this.port = port;
//	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
}
