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
    void readLocalCSVFile1() {
        var file = new CSVFile("src/test/resources/testassets/text/engines.csv");
        var recs = file.readCSV();

        // Check headers
        var headers = file.getHeaders();
        assertArrayEquals(headers, new String[]{"name", "version", "author"});

        // Check records
        assertNotNull(recs);
        var rec1 = recs.get(0);
        assertEquals("Mayonez Engine", rec1.getString("name"));
        assertEquals(0.8f, rec1.getFloat("version"));
        assertEquals("SlavSquatSuperstar", rec1.getString("author"));
    }

    @Test
    void readLocalCSVFile2() {
        var file = new CSVFile("src/test/resources/testassets/text/languages.csv");
        var recs = file.readCSV();

        // Check headers
        var headers = file.getHeaders();
        assertArrayEquals(headers, new String[]{"name", "type", "difficulty", "version", "extension"});

        // Check records
        assertNotNull(recs);
        var rec1 = recs.get(0);
        assertEquals("Java", rec1.getString("name"));
        assertEquals(17, rec1.getInt("version"));
        assertEquals(".java", rec1.getString("extension"));

        var rec2 = recs.get(2);
        assertEquals("C++", rec2.getString("name"));
        assertEquals(20, rec2.getInt("version"));
        assertEquals(".cpp/.h", rec2.getString("extension"));
    }

    @Test
    void readClasspathCSVFile() {
        var recs = new CSVFile("testassets/text/engines.csv").readCSV();
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
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
        csv.saveCSV(recs, new String[]{"name", "value"});
    }

}
