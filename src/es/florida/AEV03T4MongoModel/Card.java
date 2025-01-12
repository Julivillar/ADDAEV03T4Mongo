package es.florida.AEV03T4MongoModel;

public class Card {
	public String name;
	public int value;
	public String base64Name;
	public int numericIndex;
	
	public Card(String name, int value, String base64Name, int numericIndex) {
		super();
		this.name = name;
		this.value = value;
		this.base64Name = base64Name;
		this.numericIndex = numericIndex;
	}

	public Card(String name, int value) {
		super();
		this.name = name;
		this.value = value;
	}
	public Card() {
		super();
	}

	@Override
	public String toString() {
		return "Card [name=" + name + ", value=" + value + "]";
	}	
}
