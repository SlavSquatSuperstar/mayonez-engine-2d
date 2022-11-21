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
        CSVFile file = new CSVFile("src/test/resources/testassets/text/properties.csv");
        List<Record> recs = file.readCSV();
        // Check headers
        String[] headers = file.getHeaders();
        assertArrayEquals(headers, new String[]{"name", "version", "author"});
        // Check records
        assertNotNull(recs);
        Record rec = recs.get(0);
        assertEquals("Mayonez Engine", rec.getString("name"));
        assertEquals(0.7f, rec.getFloat("version"));
    }

    @Test
    public void readClasspathCSVFile() {
        List<Record> recs = new CSVFile("testassets/text/properties.csv").readCSV();
        assertNotNull(recs);
        assertFalse(recs.isEmpty());
    }

    @Test
    public void saveToLocalJSONFile() {
        Record rec1 = new Record();
        rec1.set("name", "time");
        rec1.set("value", LocalTime.now());
        Record rec2 = new Record();
        rec2.set("name", "date");
        rec2.set("value", LocalDate.now());
        List<Record> recs = List.of(new Record[]{rec1, rec2});

        CSVFile csv = new CSVFile("src/test/resources/testassets/out/out.csv");
        csv.saveCSV(recs, new String[]{"name", "value"});
    }

}
