package mayonez.assets.text;

import mayonez.util.Record;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.assets.text.CSVFile} class.
 *
 * @author SlavSquatSuperstar
 */
class CSVFileTest {

    @Test
    void readLocalCSVFile() {
        var file = new CSVFile("src/test/resources/testassets/text/in.csv");
        var recs = file.readCSV();

        // Check headers
        var headers = file.getHeaders();
        assertArrayEquals(new String[]{"str1", "int1", "float1", "bool1"}, headers);

        // Check records
        assertNotNull(recs);

        // All good
        var rec1 = recs.get(0);
        assertEquals("foo", rec1.getString("str1"));
        assertEquals(420, rec1.getInt("int1"));
        assertEquals(6.9f, rec1.getFloat("float1"));
        assertTrue(rec1.getBoolean("bool1"));

        // Missing values
        var rec2 = recs.get(1);
        assertEquals("", rec2.getString("str1"));
        assertEquals(0, rec2.getInt("int1"));
        assertEquals(0, rec2.getFloat("float1"));
        assertFalse(rec2.getBoolean("bool1"));
    }

    @Test
    void readClasspathCSVFile() {
        var recs = new CSVFile("testassets/text/in.csv").readCSV();
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
    }

    @Test
    void readComplexCSVFile() {
        var rec1 = new Record(Map.of(
                "str1", "foo",
                "list1", "item1,item2",
                "str2", "baz",
                "list2", "item3\nitem4",
                "str3", "spam"
        ));
        var rec2 = new Record(Map.of(
                "str1", "bar",
                "list1", "\"item5\",item6",
                "str2", "quux",
                "list2", "item7\n\"item8\"",
                "str3", "eggs"
        ));

        // Has commas, quotes, and newlines
        var recs = new CSVFile("testassets/text/test.csv").readCSV();
        assertEquals(List.of(rec1, rec2), recs);
    }

    @Test
    void saveToLocalCSVFile() {
        var rec1 = new Record();
        rec1.set("name", "time");
        rec1.set("value", LocalTime.now().toString());

        var rec2 = new Record();
        rec2.set("name", "date");
        rec2.set("value", LocalDate.now().toString());

        var recs = List.of(new Record[]{rec1, rec2});
        var csv = new CSVFile("src/test/resources/testassets/out/out.csv");
        assertDoesNotThrow(() -> csv.saveCSV(recs, new String[]{"name", "value"}));
    }

}
