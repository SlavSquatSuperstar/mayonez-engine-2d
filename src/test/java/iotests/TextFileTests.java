package iotests;

import slavsquatsuperstar.mayonez.assets.TextFile;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link TextFile} class.
 *
 * @author SlavSquatSuperstar
 */
public class TextFileTests {

    @Test
    public void readClasspathTextFile() {
        String[] properties = new TextFile("properties.txt", true).readLines();
        System.out.println(Arrays.toString(properties));
        assertEquals("Mayonez Engine", properties[0].split("=")[1]);
    }

    @Test
    public void readLocalTextFile() {
        String[] properties = new TextFile("src/test/resources/properties.txt", false).readLines();
        System.out.println(Arrays.toString(properties));
        assertEquals("Mayonez Engine", properties[0].split("=")[1]);
    }

    @Test
    public void writeLocalTextFile() {
        TextFile textFile = new TextFile("src/test/resources/foobar.txt", false);
        textFile.write("date=" + LocalDate.now(), "time=" + LocalTime.now());
    }

}
