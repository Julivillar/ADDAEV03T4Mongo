package es.florida.AEV03T4MongoViews;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HallOfFame {

	private JFrame frame;
	JScrollPane scrollPane;
	JTextArea hallOfFameTextArea;
	
	public JTextArea getHallOfFameTextArea() {
		return hallOfFameTextArea;
	}

	public HallOfFame() {

		frame = new JFrame();
		frame.setBounds(100, 100, 451, 590);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 417, 531);
		frame.getContentPane().add(scrollPane);
		
		hallOfFameTextArea = new JTextArea();
		scrollPane.setViewportView(hallOfFameTextArea);
		frame.setVisible(true);
	}
	
}
