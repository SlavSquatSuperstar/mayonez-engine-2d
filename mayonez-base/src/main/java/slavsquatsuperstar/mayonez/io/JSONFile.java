package slavsquatsuperstar.mayonez.io;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import slavsquatsuperstar.mayonez.util.Logger;
import slavsquatsuperstar.mayonez.util.Record;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

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
     * Parses the JSON data in this file and returns a {@link Record} object.
     *
     * @return the record, blank if the file does not exist
     */
    public Record readJSON() {
        String text = super.read();
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
        try (OutputStream out = outputStream(false)) {
            IOUtils.write(out, json.toJSONString());
        } catch (FileNotFoundException e) {
            Logger.error("JSONFile: File \"%s\" not found\n", getFilename());
        } catch (IOException e) {
            Logger.error("JSONFile: Could not save to file \"%s\"", getFilename());
            Logger.printStackTrace(e);
        }
    }

}