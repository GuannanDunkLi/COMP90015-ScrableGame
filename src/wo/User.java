package wo;

public class User {
	private String username;
//	private String ipAddress;
//	private String port;
	private int score;
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
