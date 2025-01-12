package es.florida.AEV03T4MongoViews;

import javax.swing.JFrame;
import javax.swing.JTextField;
import es.florida.AEV03T4MongoModel.UserModel;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;


public class RegisterView {

	JFrame frame;
	JTextField registerUsernameField;
	JPasswordField registerPasswordField;
	JPasswordField registerRepeatPasswordField;
	JButton registerUserBtn;

	public JFrame getFrame() {
		return frame;
	}

	public JTextField getRegisterUsernameField() {
		return registerUsernameField;
	}

	public JPasswordField getRegisterPasswordField() {
		return registerPasswordField;
	}

	public JPasswordField getRegisterRepeatPasswordField() {
		return registerRepeatPasswordField;
	}

	public JButton getRegisterUserBtn() {
		return registerUserBtn;
	}

	public RegisterView(UserModel user) {
		frame = new JFrame();
		frame.setBounds(100, 100, 491, 382);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		

		registerUsernameField = new JTextField();
		registerUsernameField.setBounds(240, 89, 86, 20);
		frame.getContentPane().add(registerUsernameField);
		registerUsernameField.setColumns(10);

		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setBounds(118, 92, 112, 14);
		frame.getContentPane().add(usernameLabel);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(118, 145, 112, 14);
		frame.getContentPane().add(passwordLabel);

		JLabel RegisterLabel = new JLabel("Register");
		RegisterLabel.setBounds(10, 11, 72, 14);
		frame.getContentPane().add(RegisterLabel);

		registerPasswordField = new JPasswordField();
		registerPasswordField.setBounds(240, 142, 86, 20);
		frame.getContentPane().add(registerPasswordField);

		registerUserBtn = new JButton("Register");
		
		registerUserBtn.setBounds(158, 240, 168, 23);
		frame.getContentPane().add(registerUserBtn);

		JLabel registerRepeatPasswordLabel = new JLabel("Repeat Password");
		registerRepeatPasswordLabel.setBounds(118, 192, 112, 14);
		frame.getContentPane().add(registerRepeatPasswordLabel);

		registerRepeatPasswordField = new JPasswordField();
		registerRepeatPasswordField.setBounds(240, 189, 86, 20);
		frame.getContentPane().add(registerRepeatPasswordField);

		frame.setVisible(true);
	}
}
