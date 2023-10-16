package mayonez.io.text;

import mayonez.io.*;
import mayonez.util.*;
import mayonez.util.Record;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A JavaScript Object Notation (.json) file that stores an object record.
 *
 * @author SlavSquatSuperstar
 */
public class JSONFile extends Asset {

    public JSONFile(String filename) {
        super(filename);
    }

    /**
     * Parses the JSON data in this file and returns a {@link mayonez.util.Record}.
     *
     * @return a record, or blank if it does not exist
     */
    public Record readJSON() {
        try {
            var text = new TextIOManager().read(openInputStream());
            return new Record(new JSONObject(text).toMap());
        } catch (JSONException e) {
            Logger.error("Could not parse JSON in file \"%s\"", getFilename());
        } catch (IOException e) {
            Logger.error("Could not read file \"%s\"", getFilename());
        }
        return new Record();
    }

    /**
     * Saves JSON data to this file.
     *
     * @param json a record object
     */
    public void saveJSON(Record json) {
        try {
            var jsonString = new JSONObject(json.toMap()).toString(4);
            new TextIOManager().write(openOutputStream(false), jsonString);
        } catch (IOException e) {
            Logger.error("Could not save to file \"%s\"", getFilename());
        }
    }

}