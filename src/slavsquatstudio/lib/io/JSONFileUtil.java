package slavsquatstudio.lib.io;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONFileUtil {
	
	public static JSONObject parseJSONFile(String path) {

		JSONParser parser = new JSONParser();
		JSONObject obj = null;

		try {
			obj = (JSONObject) parser.parse(new FileReader(path));
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File could not be read");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("JSON could not be parsed");
			e.printStackTrace();
		}

		return obj;

	}

	public static void saveToJsonFile(String path, JSONObject obj) {

		String text = obj.toJSONString();

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(text);
			writer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("File could not be saved");
			e.printStackTrace();
		}

	}

	public static JSONObject getJSONObject(String key, JSONObject obj) {

		JSONObject object = null;

		try {
			object = (JSONObject) obj.get(key);
		} catch (NullPointerException e) {
			System.out.println("Referenced object is null");
			e.printStackTrace();
		} catch (ClassCastException e) {
			System.out.println("Value is not a JSONObject");
			e.printStackTrace();
		}

		return object;

	}

	public static JSONArray getJSONArray(String key, JSONObject obj) {

		JSONArray array = null;

		try {
			array = (JSONArray) obj.get(key);
		} catch (NullPointerException e) {
			System.out.println("Referenced object is null");
			e.printStackTrace();
		} catch (ClassCastException e) {
			System.out.println("Value is not a JSONArray");
			e.printStackTrace();
		}

		return array;

	}

	public static String getString(String key, JSONObject obj) {
		String string = null;

		try {
			string = obj.get(key).toString();
		} catch (NullPointerException e) {
			System.out.println("Referenced object is null");
			e.printStackTrace();
		}

		return string;
	}

	public static int getInt(String key, JSONObject obj) {
		int val = 0;

		try {
			val = Integer.parseInt(getString(key, obj));
		} catch (NumberFormatException e) {
			System.out.println("Value is not an integer");
			e.printStackTrace();
		}

		return val;
	}

	public static double getDouble(String key, JSONObject obj) {
		double val = 0;

		try {
			val = Double.parseDouble(getString(key, obj));
		} catch (NumberFormatException e) {
			System.out.println("Value is not an integer");
			e.printStackTrace();
		}

		return val;
	}

}
