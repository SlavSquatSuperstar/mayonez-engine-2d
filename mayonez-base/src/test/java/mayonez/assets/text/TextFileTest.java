package mayonez.assets.text;

import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.text.TextFile} class.
 *
 * @author SlavSquatSuperstar
 */
class TextFileTest {

    @Test
    void readClasspathTextFile() {
        var lines = new TextFile("testassets/text/in.txt").readLines();
        testReadTextFile(lines);
    }

    @Test
    void readLocalTextFile() {
        var lines = new TextFile("src/test/resources/testassets/text/in.txt").readLines();
        testReadTextFile(lines);
    }

    @Test
    void saveToLocalTextFile() {
        var textFile = new TextFile("src/test/resources/testassets/out/out.txt");
        assertDoesNotThrow(() ->
                textFile.write("date=" + LocalDate.now(), "time=" + LocalTime.now()));
    }

    private static void testReadTextFile(String[] lines) {
        assertEquals(3, lines.length);
        assertEquals("foo.bar", lines[0]);
        assertArrayEquals(new String[] {"quux", "baz"}, lines[1].split("\\."));
    }

}
