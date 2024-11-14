package mayonez.io.text;

import mayonez.io.*;

import java.io.*;

/**
 * Reads, writes, and appends text to files as single strings.
 *
 * @author SlavSquatSuperstar
 */
public class TextIOManager implements AssetReader<String>, AssetWriter<String> {

    @Override
    public String read(InputStream input) throws IOException {
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
            return contents.toString();
        } catch (IOException e) {
            throw new IOException("Error while reading file");
        }
    }

    @Override
    public void write(OutputStream output, String text) throws IOException {
        if (output == null) {
            throw new FileNotFoundException("File does not exist");
        }
        if (text == null) return;

        try {
            var writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write(text);
        } catch (IOException e) {
            throw new IOException("Error while writing to file");
        }
    }

}
