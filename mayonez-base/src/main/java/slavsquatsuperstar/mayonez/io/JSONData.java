package slavsquatsuperstar.mayonez.io;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import slavsquatsuperstar.mayonez.Logger;

import java.util.HashMap;
import java.util.Set;

/**
 * Represents a JavaScript-notated object, which stores data as key-value pairs.
 * Serves as a wrapper around external JSONObject classes for use within this application.
 *
 * @author SlavSquatSuperstar
 */
public class JSONData {

    private JSONObject json;

    /**
     * Creates an empty object with no JSON data.
     */
    public JSONData() {
        this.json = new JSONObject();
    }

    /**
     * Parses this object from a formatted JSON string and overwrites any existing data.
     *
     * @param jsonText formatted JSON text
     */
    public JSONData(String jsonText) {
        try {
            json = new JSONObject(new JSONTokener(jsonText));
        } catch (JSONException e) {
            Logger.error("JSON: Could not parse JSON file");
            json = new JSONObject();
        }
    }

    // JSON Getters and Setters

    /**
     * Retrieves the value stored under this key as a Java {@link Object}, or null if it does not exist.
     */
    public Object get(String key) {
        return json.get(key);
    }

    /**
     * Retrieves the value stored under this key as a {@link JSONObject}, or null if it does not exist.
     */
    public JSONObject getObject(String key) {
        try {
            return json.getJSONObject(key);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Retrieves the value stored under this key as a {@link JSONArray}, or null if it does not exist.
     */
    public JSONArray getArray(String key) {
        try {
            return json.getJSONArray(key);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Retrieves the value stored under this key as a string, or an empty string if it does not exist.
     */
    public String getString(String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    /**
     * Retrieves the value stored under this key as a boolean, or false if it does not exist.
     */
    public boolean getBoolean(String key) {
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * Retrieves the value stored under this key as a integer, or 0 if it does not exist.
     */
    public int getInt(String key) {
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            return 0;
        }
    }

    /**
     * Retrieves the value stored under this key as a float, or 0 if it does not exist.
     */
    public double getFloat(String key) {
        try {
            return json.getFloat(key);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    /**
     * Stores a values under this key, or overwrites if it already exists.
     */
    public void set(String key, Object value) {
        json.put(key, value);
    }

    // Helper Methods

    /**
     * Deletes all JSON key-value pairs.
     */
    public void clear() {
        json.clear();
    }

    /**
     * Returns an iterable set of keys.
     *
     * @return the key set
     */
    public Set<String> keys() {
        return json.keySet();
    }

    /**
     * Converts this JSON object into a Java {@link HashMap}
     *
     * @return a hash map
     */
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> map = new HashMap<>();
        for (String key : json.keySet()) map.put(key, json.get(key));
        return map;
    }

    /**
     * Returns this object as a formatted JSON string.
     *
     * @return the JSON string
     */
    @Override
    public String toString() {
        return json.toString(4);
    }
}
