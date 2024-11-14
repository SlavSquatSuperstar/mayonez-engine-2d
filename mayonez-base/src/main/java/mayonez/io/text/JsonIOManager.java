package mayonez.io.text;

import mayonez.io.*;
import mayonez.util.Record;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;


/**
 * Reads and writes text to JSON files as records.
 *
 * @author SlavSquatSuperstar
 */
public class JsonIOManager implements AssetReader<Record>, AssetWriter<Record> {

    private static final int INDENT_SPACES = 4;

    @Override
    public Record read(InputStream input) throws IOException {
        if (input == null) {
            throw new FileNotFoundException("File does not exist");
        }

        try {
            var reader = new BufferedReader(new InputStreamReader(input));
            StringBuilder contents = new StringBuilder();

            var line = reader.readLine();
            while (line != null) {
                contents.append(line);
                contents.append('\n');
                line = reader.readLine();
            }
            return new Record(new JSONObject(contents.toString()).toMap());
        } catch (JSONException e) {
            throw new IOException("Could not parse JSON text");
        } catch (IOException e) {
            throw new IOException("Error while reading file");
        }
    }

    @Override
    public void write(OutputStream output, Record json) throws IOException {
        if (output == null) {
            throw new FileNotFoundException("File does not exist");
        }
        if (json == null) return;

        try {
            var writer = new BufferedWriter(new OutputStreamWriter(output));
            var jsonString = new JSONObject(json.toMap()).toString(INDENT_SPACES);
            writer.write(jsonString);
        } catch (JSONException e) {
            throw new IOException("Could not convert to JSON text");
        } catch (IOException e) {
            throw new IOException("Error while writing to file");
        }
    }

}
