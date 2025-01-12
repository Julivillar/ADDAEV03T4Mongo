package es.florida.AEV03T4MongoViews;

import javax.swing.JFrame;
import javax.swing.JTextField;
import es.florida.AEV03T4MongoModel.UserModel;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class LoginView {

	JFrame frame;
	JTextField usernameField;
	JPasswordField passwordField;
	JButton btnNewButton;

	public LoginView(UserModel user) {

		frame = new JFrame();
		frame.setBounds(100, 100, 489, 329);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);

		usernameField = new JTextField();
		usernameField.setBounds(240, 89, 86, 20);
		frame.getContentPane().add(usernameField);
		usernameField.setColumns(10);

		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(158, 92, 72, 14);
		frame.getContentPane().add(usernameLabel);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(158, 145, 72, 14);
		frame.getContentPane().add(passwordLabel);

		JLabel loginLabel = new JLabel("Login");
		loginLabel.setBounds(10, 11, 72, 14);
		frame.getContentPane().add(loginLabel);

		passwordField = new JPasswordField();
		passwordField.setBounds(240, 142, 86, 20);
		frame.getContentPane().add(passwordField);

		btnNewButton = new JButton("Login");
		
		btnNewButton.setBounds(158, 203, 168, 23);
		frame.getContentPane().add(btnNewButton);
	}
	public JFrame getFrame() {
		return frame;
	}

	public JTextField getUsernameField() {
		return usernameField;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public JButton getBtnNewButton() {
		return btnNewButton;
	}
}
