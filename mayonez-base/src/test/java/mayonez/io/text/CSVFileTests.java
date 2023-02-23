package mayonez.io.text;

import org.junit.jupiter.api.Test;
import mayonez.util.Record;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.io.text.CSVFile} class.
 *
 * @author SlavSquatSuperstar
 */
public class CSVFileTests {

    @Test
    public void readLocalCSVFile() {
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
    public void readClasspathCSVFile() {
        var recs = new CSVFile("testassets/text/properties.csv").readCSV();
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
    }

    @Test
    public void saveToLocalJSONFile() {
        var rec1 = new Record();
        rec1.set("name", "time");
        rec1.set("value", LocalTime.now());

        var rec2 = new Record();
        rec2.set("name", "date");
        rec2.set("value", LocalDate.now());

        var recs = List.of(new Record[]{rec1, rec2});
        var csv = new CSVFile("src/test/resources/testassets/out/out.csv");
        csv.saveCSV(recs, new String[]{"name", "value"});
    }

}
