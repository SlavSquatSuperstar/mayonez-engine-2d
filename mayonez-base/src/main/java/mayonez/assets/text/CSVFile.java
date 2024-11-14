package mayonez.assets.text;

import mayonez.*;
import mayonez.assets.*;
import mayonez.io.text.*;
import mayonez.util.Record;

import java.io.IOException;
import java.util.*;

/**
 * A comma-separated value (.csv) file that stores tabular data.
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
        try (var stream = openInputStream()) {
            var lines = new LinesIOManager().read(stream);
            this.headers = lines[0].split(","); // Get headers
            for (var row = 1; row < lines.length; row++) {
                records.add(addRecordFromLine(lines[row]));
            }
            return records;
        } catch (IOException e) {
            Logger.error("Could not read file %s", getFilename());
            return records;
        }
    }

    private Record addRecordFromLine(String line) {
        var csvVals = line.split(",");
        var numCols = Math.min(headers.length, csvVals.length);
        var rec = new Record();
        for (var cols = 0; cols < numCols; cols++) {
            rec.set(headers[cols], csvVals[cols]);
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
            csvLines[row + 1] = getCSVLineFromRecord(records, headers, row);
        }
        try (var stream = openOutputStream(false)) {
            new LinesIOManager().write(stream, csvLines);
        } catch (IOException e) {
            Logger.error("Could not save to file %s", getFilename());
        }
    }

    private String getCSVLineFromRecord(List<Record> records, String[] headers, int row) {
        var rec = records.get(row);
        var csvVals = new String[headers.length];
        for (var col = 0; col < headers.length; col++) {
            csvVals[col] = rec.getString(headers[col]);
        }
        return String.join(",", csvVals);
    }

    public String[] getHeaders() {
        return headers;
    }

}
