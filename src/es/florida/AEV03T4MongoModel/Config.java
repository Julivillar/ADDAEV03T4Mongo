package es.florida.AEV03T4MongoModel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONObject;

public class Config {

	static private String user;
	static private String pwd;
	static private String IP;
	static private String port;
	static private String database;
	static private JSONObject collections;

	public Config() {
		Path path = Paths.get("./config.json");
		String config = "";

		try {
			config = Files.readString(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JSONObject configObject = new JSONObject(config);
		user = configObject.getString("user");
		pwd = configObject.getString("pwd");
		IP = configObject.getString("IP");
		port = configObject.getString("port");
		database = configObject.getString("database");

		collections = configObject.getJSONObject("collections");

	}

	public static String getUser() {
		return user;
	}

	public static String getPwd() {
		return pwd;
	}

	public static String getIP() {
		return IP;
	}

	public static String getPort() {
		return port;
	}

	public static String getDatabase() {
		return database;
	}

	public static JSONObject getCollections() {
		return collections;
	}

}
