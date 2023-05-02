package mayonez.io.text;

import mayonez.*;
import mayonez.util.Record;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A JavaScript Object Notation (.json) file that stores an object record.
 *
 * @author SlavSquatSuperstar
 */
public class JSONFile extends TextAsset {

    public JSONFile(String filename) {
        super(filename);
    }

    /**
     * Parses the JSON data in this file and returns a {@link mayonez.util.Record}.
     *
     * @return a record, or blank if it does not exist
     */
    public Record readJSON() {
        var text = super.readText();
        try {
            return new Record(new JSONObject(text).toMap());
        } catch (JSONException e) {
            Logger.error("JSON: Could not parse JSON file");
        }
        return new Record();
    }

    /**
     * Saves JSON data to this file.
     *
     * @param json a record object
     */
    public void saveJSON(Record json) {
        var jsonString = new JSONObject(json.toMap()).toString(4);
        super.save(false, jsonString);
    }

}