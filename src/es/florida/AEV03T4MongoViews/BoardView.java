package es.florida.AEV03T4MongoViews;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import javax.swing.DefaultComboBoxModel;

public class BoardView {

	private JFrame frame;

	JComboBox cardSuitComboBox;
	JLabel crupierTotalScoreValue;
	JLabel crupierScoreHistoryValue;
	JLabel playerTotalScoreValue;
	JLabel playerScoreHistoryValue;
	JLabel crupierTotalScoreLabel;
	JLabel crupierScoreHistoryLabel;
	JLabel playerTotalScoreLabel;
	JLabel playerScoreHistoryLabel;
	JButton crupierCardPlacementBtn;
	JButton playerCardPlacementBtn;
	JButton loadCardsBtn;
	JButton registerBtn;
	JButton loginBtn;
	JButton startGameBtn;
	JButton saveGameBtn;
	JButton HallOfFameBtn;
	JButton logoutBtn;
	JButton drawNewCardBtn;
	JButton standMatchBtn;

	public JFrame getFrame() {
		return frame;
	}

	public JComboBox getCardSuitComboBox() {
		return cardSuitComboBox;
	}

	public JLabel getCrupierTotalScoreValue() {
		return crupierTotalScoreValue;
	}

	public JLabel getCrupierScoreHistoryValue() {
		return crupierScoreHistoryValue;
	}

	public JLabel getPlayerTotalScoreValue() {
		return playerTotalScoreValue;
	}

	public JLabel getPlayerScoreHistoryValue() {
		return playerScoreHistoryValue;
	}

	public JLabel getCrupierTotalScoreLabel() {
		return crupierTotalScoreLabel;
	}

	public JLabel getCrupierScoreHistoryLabel() {
		return crupierScoreHistoryLabel;
	}

	public JLabel getPlayerTotalScoreLabel() {
		return playerTotalScoreLabel;
	}

	public JLabel getPlayerScoreHistoryLabel() {
		return playerScoreHistoryLabel;
	}

	public JButton getCrupierCardPlacementBtn() {
		return crupierCardPlacementBtn;
	}

	public JButton getPlayerCardPlacementBtn() {
		return playerCardPlacementBtn;
	}

	public JButton getLoadCardsBtn() {
		return loadCardsBtn;
	}

	public JButton getRegisterBtn() {
		return registerBtn;
	}

	public JButton getLoginBtn() {
		return loginBtn;
	}

	public JButton getStartGameBtn() {
		return startGameBtn;
	}

	public JButton getSaveGameBtn() {
		return saveGameBtn;
	}

	public JButton getHallOfFameBtn() {
		return HallOfFameBtn;
	}

	public JButton getLogoutBtn() {
		return logoutBtn;
	}

	public JButton getDrawNewCardBtn() {
		return drawNewCardBtn;
	}

	public JButton getStandMatchBtn() {
		return standMatchBtn;
	}

	public BoardView() {

		frame = new JFrame();
		frame.setBounds(100, 100, 943, 640);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 919, 592);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		crupierTotalScoreValue = new JLabel("0");
		crupierTotalScoreValue.setBounds(133, 493, 56, 14);
		panel.add(crupierTotalScoreValue);

		crupierScoreHistoryValue = new JLabel("0");
		crupierScoreHistoryValue.setBounds(133, 518, 194, 14);
		panel.add(crupierScoreHistoryValue);

		playerTotalScoreValue = new JLabel("0");
		playerTotalScoreValue.setBounds(614, 493, 56, 14);
		panel.add(playerTotalScoreValue);

		playerScoreHistoryValue = new JLabel("0");
		playerScoreHistoryValue.setBounds(614, 518, 193, 14);
		panel.add(playerScoreHistoryValue);

		crupierCardPlacementBtn = new JButton("");
		crupierCardPlacementBtn.setBounds(40, 113, 287, 364);
		panel.add(crupierCardPlacementBtn);

		playerCardPlacementBtn = new JButton("");
		playerCardPlacementBtn.setBounds(520, 113, 287, 364);
		panel.add(playerCardPlacementBtn);

		loadCardsBtn = new JButton("Load Cards");
		loadCardsBtn.setBounds(23, 28, 106, 23);
		panel.add(loadCardsBtn);

		registerBtn = new JButton("Register");
		registerBtn.setBounds(139, 28, 89, 23);
		panel.add(registerBtn);

		loginBtn = new JButton("Login");

		loginBtn.setBounds(238, 28, 89, 23);
		panel.add(loginBtn);

		cardSuitComboBox = new JComboBox();
		cardSuitComboBox.setModel(new DefaultComboBoxModel(new String[] { "ES", "FR" }));
		cardSuitComboBox.setSelectedIndex(0);
		cardSuitComboBox.setBounds(426, 28, 47, 22);
		panel.add(cardSuitComboBox);

		JLabel lblNewLabel = new JLabel("Cards suit");
		lblNewLabel.setBounds(348, 33, 68, 14);
		panel.add(lblNewLabel);

		startGameBtn = new JButton("Start");
		
		startGameBtn.setBounds(494, 28, 89, 23);
		panel.add(startGameBtn);

		saveGameBtn = new JButton("Save");
		
		saveGameBtn.setBounds(605, 28, 89, 23);
		panel.add(saveGameBtn);

		HallOfFameBtn = new JButton("Hall of fame");
		HallOfFameBtn.setBounds(704, 28, 106, 23);
		panel.add(HallOfFameBtn);

		logoutBtn = new JButton("Logout");
		logoutBtn.setBounds(820, 28, 89, 23);
		panel.add(logoutBtn);

		drawNewCardBtn = new JButton("New Card");
		drawNewCardBtn.setBounds(646, 558, 89, 23);
		panel.add(drawNewCardBtn);

		standMatchBtn = new JButton("Stand");
		
		standMatchBtn.setBounds(745, 558, 89, 23);
		panel.add(standMatchBtn);

		JLabel lblCrupier = new JLabel("Crupier");
		lblCrupier.setBounds(40, 88, 56, 14);
		panel.add(lblCrupier);

		JLabel lblPlayer = new JLabel("Player");
		lblPlayer.setBounds(520, 88, 56, 14);
		panel.add(lblPlayer);

		crupierTotalScoreLabel = new JLabel("Total score");
		crupierTotalScoreLabel.setBounds(40, 493, 68, 14);
		panel.add(crupierTotalScoreLabel);

		crupierScoreHistoryLabel = new JLabel("Score history");
		crupierScoreHistoryLabel.setBounds(40, 518, 89, 14);
		panel.add(crupierScoreHistoryLabel);

		playerTotalScoreLabel = new JLabel("Total score");
		playerTotalScoreLabel.setBounds(520, 493, 82, 14);
		panel.add(playerTotalScoreLabel);

		playerScoreHistoryLabel = new JLabel("Score history");
		playerScoreHistoryLabel.setBounds(520, 518, 89, 14);
		panel.add(playerScoreHistoryLabel);

		frame.setVisible(true);
	}
}
