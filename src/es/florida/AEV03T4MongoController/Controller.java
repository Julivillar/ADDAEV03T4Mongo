package es.florida.AEV03T4MongoController;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;

import es.florida.AEV03T4MongoModel.Card;
import es.florida.AEV03T4MongoModel.Config;
import es.florida.AEV03T4MongoModel.Game;
import es.florida.AEV03T4MongoModel.UserModel;
import es.florida.AEV03T4MongoViews.BoardView;
import es.florida.AEV03T4MongoViews.HallOfFame;
import es.florida.AEV03T4MongoViews.LoginView;
import es.florida.AEV03T4MongoViews.RegisterView;

public class Controller {

	BoardView boardView;
	LoginView loginView;
	RegisterView registerView;
	Game game = new Game();
	UserModel user = new UserModel();

	public Controller() {
		new Config();

		boardView = new BoardView();
		user.mongoClient = new MongoClient(Config.getIP(), Integer.parseInt(Config.getPort()));
		user.database = user.mongoClient.getDatabase(Config.getDatabase());
		setBoardListeners();
	}

	public void loadCards() {

		List<Card> cards_es = getCardList("cards_es");
		List<Card> cards_fr = getCardList("cards_fr");

		importCards(cards_es, Config.getCollections().getString("cards_es"));
		importCards(cards_fr, Config.getCollections().getString("cards_fr"));

	}

	/**
	 * Loads the card images from the specified folder. Format each image into Card
	 * class and adds them to the list
	 *
	 * @param cardType the type of cards, used to locate the folder containing card
	 *                 images.
	 *                 This value is appended to the path "img/" to determine the
	 *                 folder path.
	 * @return List<Card> Card objects created from the files in the specified
	 *         folder.
	 * @throws NumberFormatException if the value part of the file name cannot be
	 *                               parsed as an integer.
	 * @throws IOException           if an error occurs while reading the contents
	 *                               of a file.
	 */

	private static List<Card> getCardList(String cardType) {
		File cards_es_folder = new File("img/" + cardType);
		File[] cards = cards_es_folder.listFiles();
		List<Card> cardsArr = new ArrayList<>();
		int i = 1;
		for (File card : cards) {
			String fileNameWithOutExt = card.getName().replaceFirst("[.][^.]+$", "");
			String[] cardNameSplit = fileNameWithOutExt.split("_");
			String cardName = cardNameSplit[0];
			int cardValue = Integer.parseInt(cardNameSplit[1]);

			if (cardValue >= 11) {
				cardValue = 10;
			}
			String encodedString = "";
			try {
				byte[] fileContent = Files.readAllBytes(card.toPath());
				encodedString = Base64.getEncoder().encodeToString(fileContent);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Card c = new Card(cardName, cardValue, encodedString, i);
			cardsArr.add(c);
			i++;
		}

		return cardsArr;
	}

	/**
	 * Imports a list of Card objects into a MongoDB collection.
	 *
	 * If the specified MongoDB collection does not exist, it will be created. If
	 * the collection
	 * already exists, it is dropped and recreated. Each Card in the provided list
	 * is converted
	 * into a MongoDB document and inserted into the collection.
	 *
	 * @param card       the list of Card objects to be imported into the database.
	 * @param collection the name of the MongoDB collection where the cards will be
	 *                   stored.
	 * @throws NullPointerException if the {@code user.database} is not properly
	 *                              initialized.
	 */

	private void importCards(List<Card> card, String collection) {

		MongoCollection<Document> mongoCollection = user.database.getCollection(collection);

		if (mongoCollection == null) {
			user.database.createCollection(collection);
		} else {
			mongoCollection.drop();
			user.database.createCollection(collection);
		}
		for (Card c : card) {

			Document doc = new Document();
			doc.append("numericIndex", c.numericIndex);
			doc.append("suit", c.name);
			doc.append("points", c.value);
			doc.append("base64", c.base64Name);
			mongoCollection.insertOne(doc);

		}
		JOptionPane.showMessageDialog(boardView.getFrame(), "Cards loaded");
	}

	/**
	 * Handles the current turn of a player or the dealer in the game.
	 *
	 * <p>
	 * This method selects a random card from the game's card set, updates the game
	 * state, and
	 * returns the drawn card. If no cards are available, a placeholder {@code Card}
	 * with the name
	 * "noCard" and a value of -1 is returned. Special handling is applied if the
	 * drawn card is an Ace
	 * (value of 1) to adjust its value based on the player's or dealer's score.
	 *
	 * @param game the Game instance representing the current game state.
	 * @param user the UserModel instance representing the user taking the turn.
	 * @return the Card drawn during the turn. If no cards are available, a
	 *         placeholder card is returned.
	 */

	public Card handleTurn(Game game, UserModel user) {
		int randomIndex = randomIndex(game);
		if (randomIndex == -1) {
			return new Card("noCard", -1);
		}
		game.selectedCards.add(randomIndex);

		Card c = getRandomCard(randomIndex, game.getSelectedCardSet());
		if (c.value == -1) {
			JOptionPane.showMessageDialog(boardView.getFrame(), "No cards left");
		} else if (c.value == 1) {
			if (game.getCurrentTurn().equals("crupier")) {
				c.value = game.getCrupierScore() + c.value > 21 ? 1 : 11;
			} else {
				c.value = drawnCardIsAce();
			}
		}
		return c;
	}

	/**
	 * Decodes a Base64-encoded string into an Image object.
	 *
	 * <p>
	 * The method converts the provided Base64 string into a byte array, creates a
	 * BufferedImage
	 * from the byte array, and then scales the image to a height of 400 pixels
	 * while maintaining the aspect
	 * ratio.
	 *
	 * @param base64Name the Base64-encoded string representing the image data.
	 * @return the decoded and scaled Image object, or null if an error occurs
	 *         during decoding or scaling.
	 * @throws IllegalArgumentException if the provided Base64 string is invalid.
	 * @throws IOException              if an error occurs while reading the image
	 *                                  data from the byte array.
	 */

	private static Image getParsedImage(String base64Name) {
		byte[] base64ToBytes = Base64.getDecoder().decode(base64Name);
		Image cardImage = null;
		try {
			BufferedImage cardBufferedImage = ImageIO.read(new ByteArrayInputStream(base64ToBytes));
			cardImage = cardBufferedImage.getScaledInstance(-1, 400, Image.SCALE_SMOOTH);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return cardImage;

	}

	/**
	 * Determines the winner of the game and disables interactive buttons.
	 *
	 * @return a String indicating the winner of the game
	 */

	public void handleFinishGame() {
		disableBtns();
		if (game.getPlayerScore() > game.getCrupierScore()) {
			JOptionPane.showMessageDialog(boardView.getFrame(), "player wins");
		} else {
			JOptionPane.showMessageDialog(boardView.getFrame(), "crupier wins");
		}
	}

	/**
	 * Generates a random index for a card from the selected card set, ensuring the
	 * card has not been selected already.
	 *
	 * @param game the Game instance containing the current game state, including
	 *             the selected cards.
	 * @return a random index for a card if there are still available cards in the
	 *         selected card set, or -1 if all cards have been selected.
	 */

	private static int randomIndex(Game game) {

		int cardNumber = game.getSelectedCardSet().contains("ES") ? 40 : 52;

		if ((game.getSelectedCardSet().contains("ES") && game.getSelectedCards().size() == 40)
				|| (game.getSelectedCardSet().contains("FR") && game.getSelectedCards().size() == 52)) {
			return -1;
		}
		int selectedCardIndex;
		do {
			selectedCardIndex = (int) (Math.random() * cardNumber) + 1;
		} while (game.getSelectedCards().contains(selectedCardIndex));
		return selectedCardIndex;
	}

	/**
	 * Retrieves a random card from the database based on the given index and card
	 * set.
	 * 
	 * @param selectedRandomIndex the numeric index of the card to retrieve.
	 * @param selectedCardSet     the name of the selected card set, used to
	 *                            determine which database collection to query.
	 * @return a Card object with the details retrieved from the database, or a card
	 *         with default values if no match is found.
	 * @throws NullPointerException if the database or collection is not initialized
	 *                              correctly.
	 * @throws JSONException        if the card data retrieved from the database
	 *                              cannot be parsed correctly.
	 */

	public Card getRandomCard(int selectedRandomIndex, String selectedCardSet) {
		MongoCollection<Document> mongoCollection = user.database
				.getCollection("cards_" + selectedCardSet.toLowerCase());
		MongoCursor<Document> cursor = mongoCollection.find(eq("numericIndex", selectedRandomIndex)).iterator();

		String suit = "";
		int value = -1;
		int numericIndex = -1;
		String base64 = "";

		while (cursor.hasNext()) {
			JSONObject obj = new JSONObject(cursor.next().toJson());

			suit = obj.getString("suit");
			value = obj.getInt("points");
			numericIndex = obj.getInt("numericIndex");
			base64 = obj.getString("base64");
		}
		return new Card(suit, value, base64, numericIndex);
	}

	private void setBoardListeners() {
		/**
		 * Listens for the "Load Cards" button click event and triggers the loading of
		 * cards.
		 */
		boardView.getLoadCardsBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				loadCards();
			}
		});

		/**
		 * Listens for the "Register" button click event and triggers the register view.
		 */
		boardView.getRegisterBtn().addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (user.username == null) {
					registerView = new RegisterView(user);
					setRegisterListeners();
				} else {
					JOptionPane.showMessageDialog(boardView.getFrame(), "No hay usuario, registrese");
				}
			}
		});

		/**
		 * Listens for the "Login" button click event and triggers the login view.
		 */
		boardView.getLoginBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//
				if (user.username == null) {
					loginView = new LoginView(user);
					boardView.getLoginBtn().setBackground(Color.GREEN);
					setLoginListeners();
				} else {
					JOptionPane.showMessageDialog(boardView.getFrame(), "Ya hay un usuario");
				}

			}
		});
		/**
		 * Listens for the "Stand" button click event, shows the option dialog and
		 * triggers the crupier turn if needed
		 */
		boardView.getStandMatchBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean sessionActive = checkSessionActive();
				if (!sessionActive) {
					return;
				}
				int playerStanding = -1;
				String[] options = { "No", "Yes" };

				playerStanding = JOptionPane.showOptionDialog(boardView.getFrame(), "Stand and finish game?", "",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[0]);
				if (playerStanding == 1) {
					game.setDidPlayerStand(true);
					if (game.isDidCrupierStand()) {
						handleFinishGame();
					} else {
						crupierTurn();
					}
				}
			}
		});
		/**
		 * Listens for the "Start" button click event, shows the option dialog and
		 * triggers the crupier or player turn if needed
		 */
		boardView.getStartGameBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartBoard();

				int startingPlayer = -1;
				String currentTurn = "";
				boolean sessionActive = checkSessionActive();
				if (!sessionActive) {
					return;
				}

				String[] options = { "Crupier (A.I.)", "User (Human)" };

				startingPlayer = JOptionPane.showOptionDialog(boardView.getFrame(), "Who starts?", "",
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[0]);
				String selectedCardSet = (String) boardView.getCardSuitComboBox().getSelectedItem();
				game.setSelectedCardSet(selectedCardSet);
				currentTurn = startingPlayer == 0 ? "crupier" : "player";
				game.setCurrentTurn(currentTurn);

				if (startingPlayer != -1) {
					if (currentTurn.equals("player")) {
						playerTurn();
					} else {
						crupierTurn();
						// playerTurn();
					}
				}
			}
		});

		/**
		 * Listens for the "Logout" button click event, and restarts the board and game
		 */
		boardView.getLogoutBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean sessionActive = checkSessionActive();
				if (!sessionActive) {
					return;
				}
				user.username = null;
				restartBoard();
				game.reset();
				boardView.getLoginBtn().setBackground(new Color(212, 226, 240));

			}
		});

		/**
		 * Listens for the "New Card" button click event and triggers the playerTurn
		 * method
		 */
		boardView.getDrawNewCardBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean sessionActive = checkSessionActive();
				if (!sessionActive) {
					return;
				}

				playerTurn();

			}
		});

		/**
		 * Listens for the "Save" button click event and inserts into mongo DB the user,
		 * suit, points and timestamp
		 */
		boardView.getSaveGameBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean sessionActive = checkSessionActive();
				if (!sessionActive) {
					return;
				}
				MongoCollection<Document> mongoCollection = user.database
						.getCollection(Config.getCollections().getString("scores"));
				Document doc = new Document();

				doc.append("user", user.username);
				doc.append("suit", game.getSelectedCardSet().toLowerCase());
				doc.append("points", game.getPlayerScore());
				doc.append("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

				mongoCollection.insertOne(doc);
			}
		});

		/**
		 * Listens for the "Hall of fame" button click event, gets the scores from de
		 * mongo DB and triggers the HallOfFame window with the scores
		 */
		boardView.getHallOfFameBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean sessionActive = checkSessionActive();
				if (!sessionActive) {
					return;
				}
				MongoCollection<Document> mongoCollection = user.database
						.getCollection(Config.getCollections().getString("scores"));

				MongoCursor<Document> cursor = mongoCollection.find().sort(descending("points")).iterator();
				String formattedScore = "SCORES\n";
				String user = "";
				int points = -1;
				String suit = "";
				String timestamp = "";

				while (cursor.hasNext()) {
					JSONObject obj = new JSONObject(cursor.next().toJson());

					user = obj.getString("user");
					suit = obj.getString("suit");
					points = obj.getInt("points");
					timestamp = obj.getString("timestamp");
					formattedScore += user + " " + points + " points ( Suit " + suit + ", " + timestamp + ")\n";

				}

				HallOfFame hallOfFame = new HallOfFame();
				hallOfFame.getHallOfFameTextArea().setText(formattedScore);

			}
		});
	}

	/**
	 * Handles the crupier's turn in the game.
	 *
	 * This method controls the crupier's actions, including drawing cards, updating
	 * the crupier's score,
	 * and checking the game conditions for win, loss, or the crupier standing. It
	 * also updates the user interface
	 * and determines when to switch to the player's turn or finish the game.
	 * 
	 * If the crupier's score exceeds 21, the crupier loses and the player wins. If
	 * the crupier's score reaches 21,
	 * the player loses. If the crupier's score is 16 or more, the crupier stands.
	 * If the player has already stood,
	 * the crupier will continue drawing cards until one of these conditions is met.
	 *
	 * @throws NullPointerException if there is an issue with accessing UI
	 *                              components or game state.
	 */

	private void crupierTurn() {
		if (game.isDidCrupierStand() == false) {
			JOptionPane.showMessageDialog(boardView.getFrame(), "Crupier's turn");
			Card c = handleTurn(game, user);
			Image image = getParsedImage(c.base64Name);

			boardView.getCrupierCardPlacementBtn().setIcon(new ImageIcon(image));
			int total = Integer.parseInt(boardView.getCrupierTotalScoreValue().getText());
			total = total + c.value;
			game.setCrupierScore(total);
			boardView.getCrupierTotalScoreValue().setText(String.valueOf(total));

			String scoreHistory = String.valueOf(boardView.getCrupierScoreHistoryValue().getText());
			scoreHistory = scoreHistory.equals("0") ? String.valueOf(c.value) : scoreHistory + " " + c.value;
			boardView.getCrupierScoreHistoryValue().setText(scoreHistory);

			if (game.getCrupierScore() == 21) {
				// win
				JOptionPane.showMessageDialog(boardView.getFrame(), "You lose");
				disableBtns();
				return;
			} else if (game.getCrupierScore() >= 22) {
				// lose
				JOptionPane.showMessageDialog(boardView.getFrame(), "You win");
				disableBtns();
				return;
			} else if (game.getCrupierScore() >= 16) {
				game.setDidCrupierStand(true);
				JOptionPane.showMessageDialog(boardView.getFrame(), "Crupier stands");
				if (game.isDidPlayerStand()) {
					handleFinishGame();
				}
			}
			if (game.isDidPlayerStand() == true && game.isDidCrupierStand() == false) {
				game.setCurrentTurn("crupier");
				crupierTurn();
			}
		} else if (game.isDidCrupierStand() == true && game.isDidPlayerStand() == false) {
			game.setCurrentTurn("player");
			playerTurn();
		} else if (game.isDidCrupierStand() == true && game.isDidPlayerStand() == true) {
			handleFinishGame();
			return;
		}

	}

	/**
	 * Handles the player's turn in the game.
	 *
	 * This method controls the player's actions, including drawing cards, updating
	 * the player's score,
	 * and checking for win or loss conditions. It also updates the user interface
	 * and determines when to switch to the crupier's turn or finish the game.
	 *
	 * If the player's score exceeds 21, they lose. If the player's score reaches
	 * exactly 21, they win. After
	 * each card draw, the player's total score is updated, and the game checks if
	 * the crupier has stood or not.
	 * If the crupier has not yet stood, the game switches to the crupier's turn.
	 *
	 * @throws NullPointerException if there is an issue with accessing UI
	 *                              components or game state.
	 */

	private void playerTurn() {
		if (game.isDidPlayerStand() == false) {
			JOptionPane.showMessageDialog(boardView.getFrame(), "Players's turn");
			int totalPlayer;
			Card c = handleTurn(game, user);
			Image image = getParsedImage(c.base64Name);
			boardView.getPlayerCardPlacementBtn().setIcon(new ImageIcon(image));

			totalPlayer = c.value + Integer.parseInt(boardView.getPlayerTotalScoreValue().getText());
			boardView.getPlayerTotalScoreValue().setText(String.valueOf(totalPlayer));
			game.setPlayerScore(totalPlayer);

			String scoreHistory = String.valueOf(boardView.getPlayerScoreHistoryValue().getText());
			scoreHistory = scoreHistory.equals("0") ? String.valueOf(c.value) : scoreHistory + " " + c.value;
			boardView.getPlayerScoreHistoryValue().setText(scoreHistory);

			if (game.getPlayerScore() == 21) {
				// win
				JOptionPane.showMessageDialog(boardView.getFrame(), "You win");
				disableBtns();
				return;
			} else if (game.getPlayerScore() >= 22) {
				// lose
				JOptionPane.showMessageDialog(boardView.getFrame(), "You lose");
				disableBtns();
				return;
			}
			if (game.isDidCrupierStand() == false) {
				game.setCurrentTurn("crupier");
				crupierTurn();
				// return;
			} else {
				JOptionPane.showMessageDialog(boardView.getFrame(), "Crupier standed. Players's turn");
			}
		} else if (game.isDidPlayerStand() == true && game.isDidCrupierStand() == false) {
			crupierTurn();
		} else if (game.isDidPlayerStand() == true && game.isDidCrupierStand() == true) {
			handleFinishGame();
			// return;
		}

	}

	/**
	 * Shows option dialog for the user to select the card value. 1 or 11
	 * 
	 * @return the value selected by the player
	 */
	public int drawnCardIsAce() {
		String[] options = { "1", "11" };
		int isAce = JOptionPane.showOptionDialog(boardView.getFrame(), "Select a value for the card", "",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
		JOptionPane.showMessageDialog(boardView.getFrame(), isAce == 0 ? 1 : 11);
		return isAce == 0 ? 1 : 11;
	}

	/**
	 * Restarts the board and game variable
	 */
	public void restartBoard() {
		boardView.getPlayerTotalScoreValue().setText("0");
		boardView.getCrupierTotalScoreValue().setText("0");
		boardView.getPlayerCardPlacementBtn().setIcon(null);
		boardView.getCrupierCardPlacementBtn().setIcon(null);
		boardView.getPlayerScoreHistoryValue().setText("0");
		boardView.getCrupierScoreHistoryValue().setText("0");
		boardView.getDrawNewCardBtn().setEnabled(true);
		boardView.getStandMatchBtn().setEnabled(true);

		game.reset();
	}

	/**
	 * Disabled action buttons
	 */
	private void disableBtns() {
		boardView.getDrawNewCardBtn().setEnabled(false);
		boardView.getStandMatchBtn().setEnabled(false);
	}

	/**
	 * Checks if the session is active so the flow can continue
	 * 
	 * @return false if the session is not active
	 */
	private boolean checkSessionActive() {

		if (user.username == null) {
			JOptionPane.showMessageDialog(boardView.getFrame(), "Debe iniciar sesion");
			return false;
		}
		return true;
	}

	public void setLoginListeners() {
		loginView.getBtnNewButton().addActionListener(new ActionListener() {
			/**
			 * Listener that will call login view when the login button is pressed and shows
			 * the corresponding message
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = loginView.getUsernameField().getText();
				char[] password = loginView.getPasswordField().getPassword();
				String pw = "";
				try {
					pw = user.hashPw(password);
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}

				String sucessfullLogin = user.login(username, pw);

				if (sucessfullLogin.equals("user doesnt exist")) {
					JOptionPane.showMessageDialog(loginView.getFrame(), "Invalid username or password");
				} else if (sucessfullLogin.equals("ok")) {
					JOptionPane.showMessageDialog(loginView.getFrame(), "Login successful");
					loginView.getFrame().setVisible(false);
					user.username = username;
					user.pw = pw;
				}
			}
		});
	}

	public void setRegisterListeners() {
		registerView.getRegisterUserBtn().addActionListener(new ActionListener() {
			/**
			 * Listener that will call handleNewUser when the register button is pressed and
			 * shows the corresponding message
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = registerView.getRegisterUsernameField().getText();
				char[] password = registerView.getRegisterPasswordField().getPassword();
				char[] repeatedPassword = registerView.getRegisterRepeatPasswordField().getPassword();

				try {
					String newUserResult = user.handleNewUser(username, password, repeatedPassword);
					if (newUserResult.equals("userExists")) {
						JOptionPane.showMessageDialog(registerView.getFrame(), "User already exists");
					} else if (newUserResult.equals("passwordKO")) {
						JOptionPane.showMessageDialog(registerView.getFrame(), "Password don't match");
					} else if (newUserResult.equals("ok")) {
						JOptionPane.showMessageDialog(registerView.getFrame(), "Created sucessfully");
						registerView.getFrame().setVisible(false);
					} else {
						JOptionPane.showMessageDialog(registerView.getFrame(), "Something went wrong");
					}
					registerView.getFrame().setVisible(false);
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
