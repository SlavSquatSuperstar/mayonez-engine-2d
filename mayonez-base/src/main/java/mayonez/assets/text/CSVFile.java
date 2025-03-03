package mayonez.assets.text;

import mayonez.*;
import mayonez.assets.*;
import mayonez.util.Record;
import org.apache.commons.csv.CSVFormat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * A comma-separated value (.csv) file that stores tabular data.
 * This program uses the standard CSV specification, which is listed under
 * <a href="https://datatracker.ietf.org/doc/html/rfc4180#section-2">RFC 4180</a>.
 * <p>
 * Usage: Newlines are used as record separators, commas are used as field delimiters,
 * and quotes are used to escape newline, comma, and quote characters.
 * Additionally, the first row contains headers for all the record fields
 *
 * @author SlavSquatSuperstar
 */
public class CSVFile extends Asset {

    private String[] headers;

    public CSVFile(String filename) {
        super(filename);
    }

    /**
     * Parses the CSV data in this file and returns a list of {@link mayonez.util.Record} objects.
     *
     * @return the records, empty if the file does not exist
     */
    public List<Record> readCSV() {
        var records = new ArrayList<Record>();
        try (var reader = new BufferedReader(new InputStreamReader(openInputStream()))) {
            var parser = CSVFormat.DEFAULT;
            var lines = parser.parse(reader).getRecords();
            if (lines.isEmpty()) return records; // No lines

            this.headers = lines.getFirst()
                    .toList().toArray(new String[0]); // Get headers

            for (var row = 1; row < lines.size(); row++) {
                var line = lines.get(row);
                var rec = getRecordFromLine(line.values());
                records.add(rec);
            }
            return records;
        } catch (IOException e) {
            Logger.error("Could not read file %s", getFilename());
            return records;
        }
    }

    private Record getRecordFromLine(String[] fields) {
        var numCols = Math.min(headers.length, fields.length);
        var rec = new Record();
        for (var cols = 0; cols < numCols; cols++) {
            rec.set(headers[cols], fields[cols]);
        }
        return rec;
    }

    /**
     * Saves records as CSV data to this file.
     *
     * @param records a record list
     * @param headers the table headers
     */
    public void saveCSV(List<Record> records, String[] headers) {
        var csvLines = new String[records.size() + 1];
        csvLines[0] = String.join(",", headers); // add headers

        for (var row = 0; row < records.size(); row++) {
            csvLines[row + 1] = getLineFromRecord(records, headers, row);
        }
        try (var stream = openOutputStream(false)) {
            TextIOUtils.write(stream, csvLines);
        } catch (IOException e) {
            Logger.error("Could not save to file %s", getFilename());
        }
    }

    private String getLineFromRecord(List<Record> records, String[] headers, int row) {
        var rec = records.get(row);
        var csvVals = new String[headers.length];
        for (var col = 0; col < headers.length; col++) {
            csvVals[col] = rec.getString(headers[col]);
            // TODO commas
        }
        return String.join(",", csvVals);
    }

    public String[] getHeaders() {
        return headers;
    }

}
