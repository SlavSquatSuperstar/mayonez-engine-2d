package com.slavsquatsuperstar.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.slavsquatsuperstar.mayonez.Logger;

// TODO extends TextFile
// TODO private method read internal
public class JSONFile {

    private String filename;
    private JSONObject json;

    public JSONFile(String filename) {
        this.filename = filename;
        readJSON();
    }

    // File I/O Methods

    /**
     * Parses JSON data from this file.
     *
     * @return The JSON object stored in this file.
     * @return {@code null} if this file is invalid.
     */
    public void readJSON() {
        try {
            json = (JSONObject) new JSONParser().parse(new TextFile(filename).readText());
        } catch (ParseException e) {
            Logger.log("JSONUtil: Could not parse JSON file");
        }
    }

    /**
     * Saves JSON data to this file.
     */
    public void saveJSON() {
        new TextFile(filename).save(json.toJSONString());
    }

    // JSON Methods

    public JSONObject getObj(String key) {
        try {
            return (JSONObject) json.get(key);
        } catch (ClassCastException e) {
            System.out.println("JSONFile: Value is not a JSONObject");
        }
        return null;
    }

    public JSONArray getArr(String key) {
        try {
            return (JSONArray) json.get(key);
        } catch (ClassCastException e) {
            System.out.println("JSONFile: Value is not a JSONArray");
        }
        return null;
    }

    public String getStr(String key) {
        try {
            return json.get(key).toString();
        } catch (NullPointerException e) {
            return null;
        }
    }

    public boolean getBool(String key) {
        String val = getStr(key);
        return val.equalsIgnoreCase("true") || val.equalsIgnoreCase("yes");
        // TODO check for number
    }

    public int getInt(String key) {
        try {
            return Integer.parseInt(getStr(key));
        } catch (NumberFormatException e) {
            System.out.println("JSONFile: Value is not an integer");
        }
        return -1;
    }

    public double getFloat(String key) {
        try {
            return Float.parseFloat(getStr(key));
        } catch (NumberFormatException e) {
            System.out.println("JSONFile: Value is not a float");
        }
        return -1f;
    }

    @SuppressWarnings("unchecked")
	public void setProperty(String key, Object value) {
        json.put(key, value);
    }
}
