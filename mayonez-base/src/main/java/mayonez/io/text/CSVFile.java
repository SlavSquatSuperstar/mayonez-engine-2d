package mayonez.io.text;

import mayonez.math.FloatMath;
import mayonez.util.Record;
import mayonez.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads data in Comma-Separated Value (CSV) format and saves it to a .csv file.
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
        ArrayList<Record> records = new ArrayList<>();
        String[] lines = super.readArray();
        if (lines.length > 2) {
            this.headers = lines[0].split(","); // Get headers
            // Create records from lines
            for (int row = 1; row < lines.length; row++) {
                String[] vals = lines[row].split(",");
                Record rec = new Record();
                for (int cols = 0; cols < FloatMath.min(headers.length, vals.length); cols++)
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
        String[] csvLines = new String[records.size() + 1];
        csvLines[0] = StringUtils.join(headers, ",");
        // convert records to strings
        for (int row = 0; row < records.size(); row++) {
            Record rec = records.get(row);
            String[] vals = new String[headers.length];
            for (int col = 0; col < headers.length; col++) {
                vals[col] = rec.getString(headers[col]);
            }
            csvLines[row + 1] = StringUtils.join(vals, ",");
        }
        super.save(false, csvLines);
    }

    public String[] getHeaders() {
        return headers;
    }
}
