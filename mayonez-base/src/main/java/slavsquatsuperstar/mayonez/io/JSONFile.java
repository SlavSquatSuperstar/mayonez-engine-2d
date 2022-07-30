package slavsquatsuperstar.mayonez.io;

import org.json.JSONArray;
import org.json.JSONObject;
import slavsquatsuperstar.mayonez.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Stores and manipulates data in JavaScript-Object Notation format from a .json file.
 *
 * @author SlavSquatSuperstar
 */
public class JSONFile extends Asset {

    private JSONData json;

    public JSONFile(String filename) {
        super(filename);
        json = new JSONData();
        Assets.setAsset(filename, this);
    }

    // File I/O Methods

    /**
     * Parses the JSON object stored in this file, overwriting any existing data.
     * Should not be called if this file has not been created and is meant for output only.
     */
    public JSONFile read() {
        try (InputStream in = inputStream()) {
            json = new JSONData(IOUtils.readText(in));
        } catch (FileNotFoundException e) {
            Logger.error("JSONFile: File \"%s\" not found", getFilename());
        } catch (IOException e) {
            Logger.error("JSONFile: Could not read file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
        return this;
    }

    /**
     * Saves JSON data to this file.
     */
    public void save() {
        try (OutputStream out = outputStream(false)) {
            IOUtils.write(out, json.toString());
        } catch (FileNotFoundException e) {
            Logger.error("JSONFile: File \"%s\" not found\n", getFilename());
        } catch (IOException e) {
            Logger.error("JSONFile: Could not save to file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
    }

    // JSON Getters and Setters

    public JSONData getJSON() {
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