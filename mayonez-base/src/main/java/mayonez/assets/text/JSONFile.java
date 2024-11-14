package mayonez.assets.text;

import mayonez.*;
import mayonez.assets.*;
import mayonez.io.text.*;
import mayonez.util.Record;

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
        try (var stream = openInputStream()) {
            return new JsonIOManager().read(stream);
        } catch (IOException e) {
            Logger.error("Could not read file %s", getFilename());
            return new Record();
        }
    }

    /**
     * Saves JSON data to this file.
     *
     * @param json a record object
     */
    public void saveJSON(Record json) {
        try (var stream = openOutputStream(false)) {
            new JsonIOManager().write(stream, json);
        } catch (IOException e) {
            Logger.error("Could not save to file %s", getFilename());
        }
    }

}