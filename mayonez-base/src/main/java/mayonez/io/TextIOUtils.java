package mayonez.io;

import java.io.*;
import java.util.*;

/**
 * Reads and writes strings or lines from plain text files.
 *
 * @author SlavSquatSuperstar
 */
public final class TextIOUtils {

    private static final char NEW_LINE = '\n';

    private TextIOUtils() {
    }

    private static List<String> read(InputStream input) throws IOException {
        if (input == null) {
            throw new IOException("File does not exist or is not readable");
        }
        try {
            var reader = new BufferedReader(new InputStreamReader(input));
            List<String> lines = new ArrayList<>();

            var line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            return lines;
        } catch (IOException e) {
            throw new IOException("Error while reading file");
        }
    }

    /**
     * Reads text from a stream as a single string.
     *
     * @param input the input stream
     * @return the text as a string
     * @throws java.io.IOException if the file cannot be read
     */
    public static String readText(InputStream input) throws IOException {
        StringBuilder contents = new StringBuilder();
        read(input).forEach(line -> {
            contents.append(line);
            contents.append(NEW_LINE);
        });
        return contents.toString();
    }

    /**
     * Reads text from a stream as a list of strings. The line separators
     * are removed after every line.
     *
     * @param input the input stream
     * @return the text as lines
     * @throws java.io.IOException if the file cannot be read
     */
    public static String[] readLines(InputStream input) throws IOException {
        return read(input).toArray(new String[0]);
    }

    /**
     * Writes any number of lines of text to a stream. A new line character
     * ('\n') is inserted after every line.
     *
     * @param output the output stream
     * @param lines the lines of text
     * @throws java.io.FileNotFoundException if the file cannot be written to
     */
    public static void write(OutputStream output, String... lines) throws IOException {
        if (output == null) {
            throw new IOException("File is not writable");
        } else if (lines == null) {
            return;
        }
        try {
            var writer = new BufferedWriter(new OutputStreamWriter(output));
            for (var line : lines) {
                if (line == null) continue;
                writer.write(line);
                writer.write(NEW_LINE);
            }
            writer.flush();
        } catch (IOException e) {
            throw new IOException("Error while writing to file");
        }
    }

}
