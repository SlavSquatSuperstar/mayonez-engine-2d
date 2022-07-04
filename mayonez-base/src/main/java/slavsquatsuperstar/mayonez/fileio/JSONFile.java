package slavsquatsuperstar.mayonez.fileio;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import slavsquatsuperstar.mayonez.Logger;
import slavsquatsuperstar.mayonez.Preferences;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Stores and manipulates a json from a .json file.
 *
 * @author SlavSquatSuperstar
 */
public class JSONFile extends Asset {

    private JSON json;

    public JSONFile(String filename, AssetType type) {
        super(filename, type);
        read();
        Assets.setAsset(filename, this);
    }

    public JSONFile(String filename) {
        this(filename, AssetType.LOCAL);
    }

    // File I/O Methods

    /**
     * Parses the JSON object stored in this file. Called automatically upon object creation.
     */
    public void read() {
        try (InputStream in = inputStream()) {
            json = new JSON(IOUtils.toString(in, Preferences.getFileCharset()));
        } catch (FileNotFoundException e) {
            Logger.warn("JSONFile: File \"%s\" not found", path);
        } catch (IOException e) {
            Logger.warn("TextFile: Could not read file");
        }
    }

    /**
     * Saves JSON data to this file.
     */
    public void save() {
        try (OutputStream out = outputStream(false)) {
            IOUtils.write(json.toString().getBytes(StandardCharsets.UTF_8), out);
        } catch (FileNotFoundException e) {
            Logger.warn("TextFile: File \"%s\" not found\n", path);
        } catch (IOException e) {
            Logger.warn(ExceptionUtils.getStackTrace(e));
            Logger.warn("TextFile: Could not save to file");
        }
    }

    // JSON Getters and Setters

    public JSON getJSON() {
        return json;
    }

    public Object get(String key) {
        return json.get(key);
    }

    public JSONObject getObject(String key) {
        return json.getObject(key);
    }

    public JSONArray getArray(String key) {
        return json.getArray(key);
    }

    public String getString(String key) {
        return json.getString(key);
    }

    public boolean getBoolean(String key) {
        return json.getBoolean(key);
    }

    public int getInt(String key) {
        return json.getInt(key);
    }

    public double getFloat(String key) {
        return json.getFloat(key);
    }

    public void setProperty(String key, Object value) {
        json.set(key, value);
    }

}