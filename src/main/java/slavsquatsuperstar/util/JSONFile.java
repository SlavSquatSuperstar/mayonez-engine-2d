package slavsquatsuperstar.util;

import slavsquatsuperstar.mayonez.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

// TODO extends TextFile
// TODO private method readInternal

/**
 * Stores and manipulates a JSON object from a .json file.
 */
public class JSONFile {

    private final String filename;
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
            json = new JSONObject(new JSONTokener(new TextFile(filename).readText()));
        } catch (JSONException e) {
            Logger.log("JSONUtil: Could not parse JSON file");
        }
    }

    /**
     * Saves JSON data to this file.
     */
    public void saveJSON() {
        new TextFile(filename).write(json.toString(4));
    }

    // JSON Methods

    public JSONObject getObj(String key) {
        try {
            return json.getJSONObject(key);
        } catch (JSONException e) {
            logError(key, "an object");
        }
        return null;
    }

    public JSONArray getArr(String key) {
        try {
            return json.getJSONArray(key);
        } catch (JSONException e) {
            logError(key, "an array");
        }
        return null;
    }

    public String getStr(String key) {
        try {
            return json.getString(key);
        } catch (JSONException e) {
            logError(key, "a string");
        }
        return null;
    }

    public boolean getBool(String key) {
        try {
            return json.getBoolean(key);
        } catch (JSONException e) {
            logError(key, "a boolean");
        }
        return false;
    }

    public int getInt(String key) {
        try {
            return json.getInt(key);
        } catch (JSONException e) {
            logError(key, "an integer");
        }
        return -1;
    }

    public double getFlt(String key) {
        try {
            return json.getFloat(key);
        } catch (NumberFormatException e) {
            logError(key, "a float");
        }
        return -1f;
    }

    public void setProperty(String key, Object value) {
        json.put(key, value);
    }

    private static void logError(String key, String type) {
        Logger.log("JSONFile: Value at \"%s\" is not %s.", key, type);
    }
}
