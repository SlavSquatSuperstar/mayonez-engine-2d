package mayonez.io.text;

import mayonez.util.Record;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static mayonez.test.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.text.JSONFile} class.
 *
 * @author SlavSquatSuperstar
 */
class JSONFileTests {

    @Test
    void readLocalJSONFile() {
        var rec = new JSONFile("src/test/resources/testassets/text/properties.json").readJSON();
        assertNotNull(rec);
    }

    @Test
    void readClasspathJSONFile() {
        var rec = new JSONFile("testassets/text/properties.json").readJSON();
        assertNotNull(rec);
    }

    @Test
    void saveToLocalJSONFile() {
        var json = new JSONFile("src/test/resources/testassets/out/out.json");
        var rec = new Record();
        rec.set("time", LocalTime.now().toString());
        rec.set("date", LocalDate.now().toString());
        json.saveJSON(rec);
    }

    @Test
    void getJSONPropertiesSuccess() {
        var rec = new JSONFile("src/test/resources/testassets/text/properties.json").readJSON();

        assertTrue(rec.getBoolean("in_progress")); // booleans
        assertTrue(rec.getBoolean("uses_dependencies"));

        assertEquals("Java", rec.getArray("languages").get(0)); // array
        assertEquals("Mayonez Engine", rec.getString("name")); // string

        assertEquals("0.7", rec.getString("version")); // string
        assertFloatEquals(0.7f, rec.getFloat("version")); // string to float
        assertEquals(0, rec.getInt("version")); // string to int

    }

    @Test
    void getJSONPropertiesDefaultValues() {
        var rec = new JSONFile("src/test/resources/testassets/text/properties.json").readJSON();

        assertEquals(0, rec.getInt("version")); // int default
        assertFloatEquals(0f, rec.getFloat("name")); // float default
        assertFalse(rec.getBoolean("languages")); // boolean default
        assertEquals(rec.getString("date"), ""); // string default
        assertNull(rec.get(null)); // not a key
    }

}
