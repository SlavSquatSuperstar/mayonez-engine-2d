package testbed.iotests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.fileio.TextFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link TextFile} class.
 *
 * @author SlavSquatSuperstar
 */
public class TextFileTests {

    @Test
    public void readClasspathTextFile() {
        String[] properties = new TextFile("testassets/properties.txt", true).readLines();
        System.out.println(Arrays.toString(properties));
        assertEquals("Mayonez Engine", properties[0].split("=")[1]);
    }

    @Test
    public void readLocalTextFile() {
        String[] properties = new TextFile("src/test/resources/testassets/properties.txt", false).readLines();
        System.out.println(Arrays.toString(properties));
        assertEquals("Mayonez Engine", properties[0].split("=")[1]);
    }

    @Test
    public void writeLocalTextFile() {
        TextFile textFile = new TextFile("src/test/resources/testassets/foobar/foobar.txt", false);
        textFile.write("date=" + LocalDate.now(), "time=" + LocalTime.now());
    }

}
