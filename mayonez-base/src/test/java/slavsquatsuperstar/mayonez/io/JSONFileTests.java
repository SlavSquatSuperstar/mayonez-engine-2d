package slavsquatsuperstar.mayonez.io;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import slavsquatsuperstar.util.Record;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static slavsquatsuperstar.test.TestUtils.assertFloatEquals;

/**
 * Unit tests for {@link JSONFile} class.
 *
 * @author SlavSquatSuperstar
 */
public class JSONFileTests {

    static JSONFile file;
    static Record json;

    @BeforeAll
    public static void readLocalJSONFile() {
        file = new JSONFile("src/test/resources/testassets/properties.json");
        json = file.readJSON();
    }

    @Test
    public void readClasspathJSONFile() {
        new JSONFile("testassets/properties.json").readJSON();
    }

    @Test
    public void saveToLocalJSONFile() {
        JSONFile json = new JSONFile("src/test/resources/testassets/out/out.json");
        Record record = new Record();
        record.set("time", LocalTime.now());
        record.set("date", LocalDate.now());
        json.saveJSON(record);
    }

    @Test
    public void getJSONPropertiesSuccess() {
        assertTrue(json.getBoolean("in_progress")); // booleans
        assertTrue(json.getBoolean("uses_dependencies"));

        assertEquals("Java", json.getArray("languages").get(0)); // array
        assertEquals("Mayonez Engine", json.getString("name")); // string
        assertEquals("0.7", json.getString("version")); // string
        assertFloatEquals(0.7f, json.getFloat("version")); // string to float
        assertEquals(0, json.getInt("version")); // string to int

    }

    @Test
    public void getJSONPropertiesDefaultValues() {
        assertEquals(0, json.getInt("version")); // int default
        assertFloatEquals(0f, json.getFloat("name")); // float default
        assertFalse(json.getBoolean("languages")); // boolean default
        assertEquals(json.getString("date"), ""); // string default
        assertNull(json.get(null)); // null
    }

}
