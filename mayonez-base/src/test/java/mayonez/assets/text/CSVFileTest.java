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
        var rec1 = new Record(Map.of(
                "name", "time",
                "value", LocalTime.now().toString()
        ));
        var rec2 = new Record(Map.of(
                "name", "date",
                "value", LocalDate.now().toString()
        ));

        var headers = new String[]{"name", "value"};
        var recs = List.of(rec1, rec2);
        var csv = new CSVFile("src/test/resources/testassets/out/out.csv");
        assertDoesNotThrow(() -> csv.saveCSV(recs, headers));

        // Ensure records can be read again
        var recs2 = csv.readCSV();
        var headers2 = csv.getHeaders();
        assertArrayEquals(headers, headers2);
        assertEquals(recs, recs2);
    }

    @Test
    void saveToComplexCSVFile() {
        var rec1 = new Record(Map.of(
                "\"str1\"", "foo",
                "list1,comma", "item1,item2",
                "str2", "baz",
                "list2,nl", "item3\nitem4",
                "str3", "spam"
        ));
        var rec2 = new Record(Map.of(
                "\"str1\"", "bar",
                "list1,comma", "\"item5\",item6",
                "str2", "quux",
                "list2,nl", "item7\n\"item8\"",
                "str3", "eggs"
        ));
        var headers = new String[]{"\"str1\"", "list1,comma", "str2", "list2,nl", "str3"};
        var recs = List.of(rec1, rec2);

        // Has commas, quotes, and newlines
        var csv = new CSVFile("src/test/resources/testassets/out/test2.csv");
        csv.saveCSV(recs, headers);

        // Ensure records can be read again
        var recs2 = csv.readCSV();
        var headers2 = csv.getHeaders();
        assertArrayEquals(headers, headers2);
        assertEquals(recs, recs2);
    }

}
