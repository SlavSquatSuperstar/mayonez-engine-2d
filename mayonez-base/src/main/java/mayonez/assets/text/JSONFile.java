package mayonez.assets.text;

import mayonez.*;
import mayonez.assets.*;
import mayonez.io.*;
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

    private static final int INDENT_SPACES = 4;

    public JSONFile(String filename) {
        super(filename);
    }

    /**
     * Parses the JSON data in this file and returns a {@link mayonez.util.Record}.
     *
     * @return a record, or blank if it does not exist
     */
    public Record readJSON() {
        try (var stream = openInputStream()) {
            var jsonString = TextIOUtils.readText(stream);
            return new Record(new JSONObject(jsonString).toMap());
        } catch (JSONException e) {
            Logger.error("Could not parse JSON from %s", getFilename());
        } catch (IOException e) {
            Logger.error("Could not read file %s", getFilename());
        }
        return new Record();
    }

    /**
     * Saves JSON data to this file.
     *
     * @param json a record object
     */
    public void saveJSON(Record json) {
        try (var stream = openOutputStream(false)) {
            var jsonString = new JSONObject(json.toMap()).toString(INDENT_SPACES);
            TextIOUtils.write(stream, jsonString);
        } catch (JSONException e) {
            Logger.error("Could not convert %s to JSON", getFilename());
        } catch (IOException e) {
            Logger.error("Could not save to file %s", getFilename());
        }
    }

}