package mayonez.assets.text;

import java.io.*;
import java.util.*;

/**
 * Reads and writes plain text to and from {@link InputStream}s and
 * {@link OutputStream}s. This class does not automatically close streams after
 * I/O operations, so it the user's responsibility to do so.
 *
 * @author SlavSquatSuperstar
 */
public final class TextIOUtils {

    /**
     * The file separator on the user's current operating system.
     */
    public static final String LINE_SEPARATOR = System.lineSeparator();

    private TextIOUtils() {
    }

    // Reader/Writer Methods

    /**
     * Create a {@link BufferedReader} that will read text from the given
     * {@link InputStream}.
     *
     * @param input the input stream
     * @return the buffered reader
     */
    public static BufferedReader getReader(InputStream input) {
        return new BufferedReader(new InputStreamReader(input));
    }

    /**
     * Create a {@link BufferedWriter} that will write or append text to the
     * given {@link OutputStream}.
     *
     * @param output the output stream
     * @return the buffered writer
     */
    public static BufferedWriter getWriter(OutputStream output) {
        return new BufferedWriter(new OutputStreamWriter(output));
    }

    // Read/Write Text Methods

    /**
     * Reads text from an {@link InputStream} as a single string. The stream
     * remains open after the operation.
     *
     * @param input the input stream
     * @return the text as a string
     * @throws java.io.IOException if the stream cannot be read from.
     */
    public static String readText(InputStream input) throws IOException {
        StringBuilder contents = new StringBuilder();
        read(input).forEach(line -> {
            contents.append(line);
            contents.append(LINE_SEPARATOR);
        });
        return contents.toString();
    }

    /**
     * Reads text from an {@link InputStream} as an array of strings. The line
     * separators are removed after each line, and the stream remains open
     * after the operation
     *
     * @param input the input stream
     * @return the text as lines
     * @throws java.io.IOException if the stream cannot be read from
     */
    public static String[] readLines(InputStream input) throws IOException {
        return read(input).toArray(new String[0]);
    }

    private static List<String> read(InputStream input) throws IOException {
        if (input == null) {
            throw new IOException("Input stream is null");
        }
        try {
            var reader = getReader(input);
            List<String> lines = new ArrayList<>();

            var line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            // Keep stream open
            return lines;
        } catch (IOException e) {
            throw new IOException("Error while reading from stream");
        }
    }

    /**
     * Writes or appends any number of lines of text to an
     * {@link OutputStream}. A new line character is inserted after every line,
     * and the stream remains open after the operation.
     *
     * @param output the output stream
     * @param lines  the lines of text
     * @throws java.io.FileNotFoundException if the stream cannot be written to
     */
    public static void write(OutputStream output, String... lines) throws IOException {
        if (output == null) {
            throw new IOException("Output stream is null");
        } else if (lines == null) {
            return;
        }
        try {
            var writer = getWriter(output);
            for (var line : lines) {
                if (line == null) continue;
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            throw new IOException("Error while writing to stream");
        }
    }

}
