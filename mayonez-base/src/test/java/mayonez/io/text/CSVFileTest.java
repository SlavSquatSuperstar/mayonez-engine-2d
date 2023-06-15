package mayonez.io.text;

import mayonez.util.Record;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.text.CSVFile} class.
 *
 * @author SlavSquatSuperstar
 */
class CSVFileTest {

    @Test
    void readLocalCSVFile() {
        var file = new CSVFile("src/test/resources/testassets/text/properties.csv");
        var recs = file.readCSV();
        // Check headers
        var headers = file.getHeaders();
        assertArrayEquals(headers, new String[]{"name", "version", "author"});
        // Check records
        assertNotNull(recs);
        var rec = recs.get(0);
        assertEquals("Mayonez Engine", rec.getString("name"));
        assertEquals(0.7f, rec.getFloat("version"));
    }

    @Test
    void readClasspathCSVFile() {
        var recs = new CSVFile("testassets/text/properties.csv").readCSV();
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
