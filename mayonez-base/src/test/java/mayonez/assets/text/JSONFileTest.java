package mayonez.assets.text;

import mayonez.util.Record;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.text.JSONFile} class.
 *
 * @author SlavSquatSuperstar
 */
class JSONFileTest {

    @Test
    void readLocalJSONFile() {
        var rec = new JSONFile("src/test/resources/testassets/text/in.json").readJSON();
        assertNotNull(rec);

        // All good
        assertEquals("foo", rec.getString("str1"));
        assertEquals(420, rec.getInt("int1"));
        assertEquals(6.9f, rec.getFloat("float1"));
        assertTrue(rec.getBoolean("bool1"));
        assertEquals(List.of("item1", "item2"), rec.getArray("list1"));
        assertNull(rec.get("null1"));

        // Missing values
        assertEquals(0, rec.getInt("null1"));
        assertEquals(0f, rec.getFloat("null1"));
        assertFalse(rec.getBoolean("null1"));
        assertEquals("", rec.getString("null1"));
        assertNull(rec.get("not_a_key"));
    }

    @Test
    void readComplexJSONFile() {
        var rec = new JSONFile("testassets/text/test.json").readJSON();
        assertNotNull(rec);

        // Get record
        var rec1 = new Record(Map.of("str1", "baz", "int1", 42));
        assertEquals(rec1, rec.getObject("rec1"));

        // Get list of records
        var arr1 = rec.getArray("list1"); // Stored as maps
        assertNotNull(arr1);
        assertEquals(2, arr1.size());

        var items = List.of(
                new Record(Map.of("str1", "foo", "int1", 420)),
                new Record(Map.of("str1", "bar", "int1", 69))
        );
        for (int i = 0; i < 2; i++) {
            assertInstanceOf(Map.class, arr1.get(i));
            assertEquals(items.get(i), Record.from(arr1.get(i)));
        }
    }

    @Test
    void readClasspathJSONFile() {
        var rec = new JSONFile("testassets/text/in.json").readJSON();
        assertNotNull(rec);
    }

    @Test
    void saveToLocalJSONFile() {
        var json = new JSONFile("src/test/resources/testassets/out/out.json");
        var rec = new Record();
        rec.set("time", LocalTime.now().toString());
        rec.set("date", LocalDate.now().toString());
        assertDoesNotThrow(() -> json.saveJSON(rec));
    }

}
