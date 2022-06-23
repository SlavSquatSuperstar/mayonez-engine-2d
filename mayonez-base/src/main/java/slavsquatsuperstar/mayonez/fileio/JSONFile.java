package slavsquatsuperstar.mayonez.fileio;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Preferences;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Stores and manipulates a JSON object from a .json file.
 *
 * @author SlavSquatSuperstar
 */
public class JSONFile extends Asset {

    private JSONObject json;

    public JSONFile(String filename, AssetType type) {
        super(filename, type);
        read();
        Assets.setAsset(filename, this);
    }

    public JSONFile(String filename) {
        this(filename, AssetType.LOCAL);
    }

    // File I/O Methods

    private static void logError(String key, String type) {
        Logger.warn("JSONFile: Value at \"%s\" is not %s.", key, type);
    }

    // JSON Methods

    /**
     * Parses the JSON object stored in this file. Called automatically upon object creation.
     */
    public void read() {
        try (InputStream in = inputStream()) {
            json = new JSONObject(new JSONTokener(IOUtils.toString(in, Preferences.CHARSET)));
        } catch (FileNotFoundException e) {
            Logger.warn("JSONFile: File \"%s\" not found", path);
        } catch (IOException e) {
            Logger.warn("TextFile: Could not read file");
        } catch (JSONException e) {
            Logger.warn("JSONUtil: Could not parse JSON file");
        }
    }

    // JSON Getters and Setters

    /**
     * Saves JSON data to this file.
     */
    public void save() {
        try (OutputStream out = outputStream(false)) {
            IOUtils.write(json.toString(4).getBytes(StandardCharsets.UTF_8), out);
        } catch (FileNotFoundException e) {
            Logger.warn("TextFile: File \"%s\" not found\n", path);
        } catch (IOException e) {
            Logger.warn(ExceptionUtils.getStackTrace(e));
            Logger.warn("TextFile: Could not save to file");
        }
    }

    public JSONObject getObj(String key) {
        try {
            return json.getJSONObject(key);
        } catch (JSONException e) {
            logError(key, "an object");
        }
        return null;
    }

    public JSONArray getArray(String key) {
        try {
            return json.getJSONArray(key);
        } catch (JSONException e) {
            logError(key, "an array");
        }
        return null;
    }

    public String getStr(String key) {
        return json.getString(key);
    }

    public boolean getBool(String key) {
        return json.getBoolean(key);
    }

    public int getInt(String key) {
        return json.getInt(key);
    }

    public double getFloat(String key) {
        return json.getFloat(key);
    }

    public void setProperty(String key, Object value) {
        json.put(key, value);
    }

}