/**  
* This class stores information of users. 
*/
package DSProjTwo;

public class User {
	private String username;
	private int score;
	private boolean iswait;

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
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public User(String username, int score) {
		super();
		this.username = username;
		this.score = score;
		this.iswait=true;
	}
}
