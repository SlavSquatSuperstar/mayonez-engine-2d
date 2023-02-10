package mayonez.io.text;

import mayonez.math.FloatMath;
import mayonez.util.Record;
import mayonez.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A comma-separated value (.csv) file that stores tabular data.
 *
 * @author SlavSquatSuperstar
 */
public class CSVFile extends TextAsset {

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
        var lines = super.readLines();
        if (lines.length > 2) {
            this.headers = lines[0].split(","); // Get headers
            // Create records from lines
            for (var row = 1; row < lines.length; row++) {
                var rec = new Record();
                var vals = lines[row].split(",");
                for (var cols = 0; cols < FloatMath.min(headers.length, vals.length); cols++)
                    rec.set(headers[cols], vals[cols]);
                records.add(rec);
            }
        }
        return records;
    }

    /**
     * Saves records as CSV data to this file.
     *
     * @param records a record list
     * @param headers the table headers
     */
    public void saveCSV(List<Record> records, String[] headers) {
        // add headers
        var csvLines = new String[records.size() + 1];
        csvLines[0] = StringUtils.join(headers, ",");
        // convert records to strings
        for (var row = 0; row < records.size(); row++) {
            var rec = records.get(row);
            var vals = new String[headers.length];
            for (var col = 0; col < headers.length; col++)
                vals[col] = rec.getString(headers[col]);
            csvLines[row + 1] = StringUtils.join(vals, ",");
        }
        super.save(false, csvLines);
    }

    public String[] getHeaders() {
        return headers;
    }
}
