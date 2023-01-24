package mayonez.io.text;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import mayonez.Logger;
import mayonez.util.Record;
import mayonez.util.StringUtils;

/**
 * Reads data in JavaScript Object Notation (JSON) format and saves it to a .json file.
 *
 * @author SlavSquatSuperstar
 */
public class JSONFile extends TextAsset {

    public JSONFile(String filename) {
        super(filename);
    }

    /**
     * Parses the JSON data in this file and returns a {@link mayonez.util.Record} object.
     *
     * @return the record, blank if the file does not exist
     */
    public Record readJSON() {
        var text = super.read();
        if (!text.equals("")) {
            try {
                return new Record(new JSONObject(new JSONTokener(text)).toMap());
            } catch (JSONException e) {
                Logger.error("JSON: Could not parse JSON file");
            }
        }
        return new Record();
    }

    /**
     * Saves JSON data to this file.
     *
     * @param json a record object
     */
    public void saveJSON(Record json) {
        super.save(false, StringUtils.split(json.toJSONString(), "\n"));
    }

}