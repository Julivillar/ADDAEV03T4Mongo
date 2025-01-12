package es.florida.AEV03T4MongoModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;

public class UserModel {

	public String username;
	public String pw;
	public MongoDatabase database;
	public MongoClient mongoClient;
	
	public UserModel(String username, char[] pw) throws NoSuchAlgorithmException {
		this.username = username;
		this.pw = hashPw(pw);
	}
	public UserModel() {
		super();
	}


	public String login(String username, String pw){
				
		boolean userExists = userExists(username,pw);
		
		if (userExists) {
			return "ok";
		}else{
			return "user doesnt exist";
		}
	}

	public String handleNewUser(String usernameField, char[] passwordField,
			char[] repeatPasswordField) throws NoSuchAlgorithmException {
		
				String hashedPassword = hashPw(passwordField);
		String hashedRepeatedPassword = hashPw(repeatPasswordField);

		if (!hashedPassword.equals(hashedRepeatedPassword)) {
			return "passwordKO";
		}
		boolean userExists = userExists(usernameField, hashedPassword);
		if(userExists) {
			return "userExists";
		}

		this.database = mongoClient.getDatabase("casino");

		MongoCollection<Document> coleccion = this.database.getCollection("users");
		Document doc = new Document();
		doc.append("user", usernameField);
		doc.append("pass", hashedPassword);
		coleccion.insertOne(doc);
		
		
		return "ok";
	}

    public boolean userExists(String name, String password) {
        
        Bson queryUserPassword = and(
                eq("user", name), 
                eq("pass", password)
        );
        
        MongoCollection<Document> users = this.database.getCollection("users");
        
        MongoCursor<Document> usersCursor = users.find(queryUserPassword).iterator();
        
        return usersCursor.hasNext() ? true : false;
    }

	public String hashPw(char[] pw) throws NoSuchAlgorithmException {

		byte[] pwBytes = String.valueOf(pw).getBytes(StandardCharsets.UTF_8);
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] bytesToHash = messageDigest.digest(pwBytes);

		return parseByteToString(bytesToHash);
	}

	private String parseByteToString(byte[] bytesToHash) {
		String result = "";
		// Format each byte as Hexadecimal integer with a minimum of 2 digits.
		// If needed to achieve 2 digits, precede it with 0
		for (byte b : bytesToHash) {
			result += String.format("%02x", b);
		}
		return result;
	}

}
