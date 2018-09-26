package wo;
/**  
* @author Pengfei Xiao  pengfei123xiao@gmail.com
* @date 26 Sep. 2018  
*/
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.BorderLayout;

public class memberPool {
	public memberPool() {
		initialize();
	}

	// Initialize the contents of the frame.
	private void initialize() {
		JFrame frame = new JFrame("Member List");
		frame.setSize(700, 500);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		//frame.setLayout(null);
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 694, 51);
		frame.getContentPane().add(panel);
		Font font = new Font("Arial", Font.BOLD, 22); // Unify fonts
		JLabel wordLabel = new JLabel("Online User List");
		wordLabel.setFont(font);
		wordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		wordLabel.setBounds(35, 15, 100, 40);
		panel.add(wordLabel);
		// User list area
		JTextArea textOutput = new JTextArea();
		textOutput.setFont(new Font("Arial", Font.BOLD, 30));
		JScrollPane jslp = new JScrollPane(textOutput);
		jslp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jslp.setBounds(15, 60, 400, 350);
		panel.add(jslp);
		frame.setVisible(true);

	}
}