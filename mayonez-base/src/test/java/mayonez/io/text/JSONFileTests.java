package mayonez.io.text;

import org.junit.jupiter.api.Test;
import mayonez.util.Record;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static mayonez.test.TestUtils.assertFloatEquals;

/**
 * Unit tests for the {@link mayonez.io.text.JSONFile} class.
 *
 * @author SlavSquatSuperstar
 */
public class JSONFileTests {

    @Test
    public void readLocalJSONFile() {
        var rec = new JSONFile("src/test/resources/testassets/text/properties.json").readJSON();
        assertNotNull(rec);
    }

    @Test
    public void readClasspathJSONFile() {
        var rec = new JSONFile("testassets/text/properties.json").readJSON();
        assertNotNull(rec);
    }

    @Test
    public void saveToLocalJSONFile() {
        var json = new JSONFile("src/test/resources/testassets/out/out.json");
        var rec = new Record();
        rec.set("time", LocalTime.now());
        rec.set("date", LocalDate.now());
        json.saveJSON(rec);
    }

    @Test
    public void getJSONPropertiesSuccess() {
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
    public void getJSONPropertiesDefaultValues() {
        var rec = new JSONFile("src/test/resources/testassets/text/properties.json").readJSON();
        assertEquals(0, rec.getInt("version")); // int default
        assertFloatEquals(0f, rec.getFloat("name")); // float default
        assertFalse(rec.getBoolean("languages")); // boolean default
        assertEquals(rec.getString("date"), ""); // string default
        assertNull(rec.get(null)); // null
    }

}
