package slavsquatsuperstar.test.iotests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.mayonez.fileio.JSONFile;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JSONFile} class.
 *
 * @author SlavSquatSuperstar
 */
public class JSONTests {

    static JSONFile json;

    @Test
    public void readClasspathJSONFile() {
        JSONFile json = new JSONFile("testassets/properties.json");
        json.read();
    }

    @BeforeAll
    public static void readLocalJSONFile() {
        json = new JSONFile("src/test/resources/testassets/properties.json");
    }

    @Test
    public void saveToLocalJSONFile() {
        JSONFile json = new JSONFile("src/test/resources/testassets/foobar/foobar.json");
        json.setProperty("time", LocalTime.now());
        json.setProperty("date", LocalDate.now());
        json.save();
    }

    @Test
    public void getJSONPropertiesSuccess() {
        assertTrue(json.getBoolean("in_progress"));
        assertTrue(json.getBoolean("uses_dependencies"));

        assertEquals("Java", json.getArray("languages").get(0));
        assertEquals("Mayonez Engine", json.getString("name"));
        assertEquals(0.6f, json.getFloat("version"), 0.0f);

    }

    @Test
    public void getJSONPropertiesDefaultValues() {
        assertEquals(0, json.getInt("version"));
        assertNull(json.getObject(null));
        assertEquals(json.getString("version"), "");
    }

}
