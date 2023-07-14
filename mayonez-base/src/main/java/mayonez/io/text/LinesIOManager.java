package mayonez.io.text;

import kotlin.io.TextStreamsKt;
import mayonez.io.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Reads, writes, and appends lines of text to files as string arrays.
 *
 * @author SlavSquatSuperstar
 */
public class LinesIOManager implements AssetReader<String[]>, AssetWriter<String[]> {

    private static final char NEW_LINE = '\n';
    private static final Charset TEXT_CHARSET = StandardCharsets.UTF_8;

    @Override
    public String[] read(InputStream input) throws IOException {
        if (input == null) {
            throw new FileNotFoundException("File does not exist");
        }

        try (var reader = new BufferedReader(new InputStreamReader(input))) {
            return TextStreamsKt.readLines(reader).toArray(new String[0]);
        } catch (IOException e) {
            throw new IOException("Error while reading file");
        }
    }

    // Source: Apache Commons IO > IOUtils.write(Collection<?>, String, OutputStream, Charset)
    @Override
    public void write(OutputStream output, String[] lines) throws IOException {
        if (output == null) {
            throw new FileNotFoundException("File does not exist");
        }
        if (lines == null) return;

        try (output) {
            for (var line : lines) {
                output.write(line.getBytes(TEXT_CHARSET));
                output.write(NEW_LINE);
            }
        } catch (IOException e) {
            throw new IOException("Error while saving file");
        }
    }

}
