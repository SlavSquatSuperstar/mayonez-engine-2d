package testbed.iotests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.fileio.AssetType;
import slavsquatsuperstar.fileio.JSONFile;

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

    @BeforeAll
    public static void readLocalJSONFile() {
        json = new JSONFile("src/test/resources/testassets/properties.json");
    }

    @Test
    public void readClasspathJSONFile() {
        JSONFile json = new JSONFile("testassets/properties.json");
        json.read();
    }

    @Test
    public void getJSONPropertiesSuccess() {
        assertTrue(json.getBool("in_progress"));
        assertTrue(json.getBool("uses_dependencies"));
        assertEquals("Java", json.getArray("languages").get(0));
        assertEquals(0, json.getInt("version"));

        assertEquals("Mayonez Engine", json.getStr("name"));
        assertTrue(json.getBool("in_progress"));
        assertEquals(0.6f, json.getFloat("version"), 0.0f);
        assertNull(json.getObj(null));
    }

    @Test
    public void getJSONPropertiesFail() {
        assertNull(json.getStr("version"));
    }

    @Test
    public void savePropertiesToJSON() {
        JSONFile json = new JSONFile("src/test/resources/testassets/foobar/foobar.json");
        json.setProperty("time", LocalTime.now());
        json.setProperty("date", LocalDate.now());
        json.save();
    }

}
