package es.florida.AEV03T4MongoModel;

import java.util.ArrayList;
import java.util.List;

public class Game {

	String currentTurn;
	int crupierScore;
	int playerScore;
	boolean didCrupierStand = false;
	boolean didPlayerStand = false;
	String selectedCardSet;
	public List<Integer> selectedCards = new ArrayList<>();

	public Game(String currentTurn, int crupierScore, int playerScore, String selectedCardSet) {
		super();
		this.currentTurn = currentTurn;
		this.crupierScore = crupierScore;
		this.playerScore = playerScore;
		this.selectedCardSet = selectedCardSet;
	}

	public Game() {
		super();
	}

	public String getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(String currentTurn) {
		this.currentTurn = currentTurn;
	}

	public int getCrupierScore() {
		return crupierScore;
	}

	public void setCrupierScore(int crupierScore) {
		this.crupierScore = crupierScore;
	}

	public int getPlayerScore() {
		return playerScore;
	}

	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
	}

	public List<Integer> getSelectedCards() {
		return selectedCards;
	}

	public void setSelectedCards(List<Integer> selectedCards) {
		this.selectedCards = selectedCards;
	}

	public String getSelectedCardSet() {
		return selectedCardSet;
	}

	public void setSelectedCardSet(String selectedCardSet) {
		this.selectedCardSet = selectedCardSet;
	}

	public boolean isDidCrupierStand() {
		return didCrupierStand;
	}

	public void setDidCrupierStand(boolean didCrupierStand) {
		this.didCrupierStand = didCrupierStand;
	}

	public boolean isDidPlayerStand() {
		return didPlayerStand;
	}

	public void setDidPlayerStand(boolean didPlayerStand) {
		this.didPlayerStand = didPlayerStand;
	}

	public void reset() {
		this.currentTurn = null;
		this.crupierScore = 0;
		this.playerScore = 0;
		this.didCrupierStand = false;
		this.didPlayerStand = false;
		this.selectedCards = new ArrayList<>();
		this.selectedCardSet = null;
	}

}
