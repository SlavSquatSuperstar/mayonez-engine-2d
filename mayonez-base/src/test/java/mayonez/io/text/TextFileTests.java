package mayonez.io.text;

import mayonez.io.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.text.TextFile} class.
 *
 * @author SlavSquatSuperstar
 */
public class TextFileTests {

    @Test
    public void readClasspathTextFile() {
        Assets.clearAssets();
        Assets.scanResources("testassets");

        var properties = new TextFile("testassets/text/properties.txt").readLines();
        System.out.println(Arrays.toString(properties));
        assertEquals("Mayonez Engine", properties[0].split("=")[1]);
    }

    @Test
    public void readLocalTextFile() {
        var properties = new TextFile("src/test/resources/testassets/text/properties.txt").readLines();
        System.out.println(Arrays.toString(properties));
        assertEquals("Mayonez Engine", properties[0].split("=")[1]);
    }

    @Test
    public void saveToLocalTextFile() {
        var textFile = new TextFile("src/test/resources/testassets/out/out.txt");
        textFile.write("date=" + LocalDate.now(), "time=" + LocalTime.now());
    }

}
