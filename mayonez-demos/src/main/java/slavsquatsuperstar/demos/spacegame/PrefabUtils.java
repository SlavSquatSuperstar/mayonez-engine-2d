package slavsquatsuperstar.demos.spacegame;

import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.math.*;
import mayonez.util.Record;

import java.util.*;
import java.util.function.*;

/**
 * Utility methods for reading records from data files.
 *
 * @author SlavSquatSuperstar
 */
public final class PrefabUtils {

    private PrefabUtils() {
    }

    /**
     * Returns a list of {@link mayonez.util.Record}s from the given CSV file.
     *
     * @param csvFileName the CSV file's name
     * @return the record list
     */
    public static List<Record> getRecordsFromFile(String csvFileName) {
        var csvFile = Assets.getAsset(csvFileName, CSVFile.class);
        if (csvFile == null) return Collections.emptyList();
        else return csvFile.readCSV();
    }

    /**
     * Returns a list of Java objects of the specified type from the given CSV
     * file.
     *
     * @param csvFileName       the CSV file's name
     * @param objectConstructor a function creating an object from a record
     * @param <T>               the object's type
     * @return the object list
     */

    public static <T> List<T> getObjectsFromFile(
            String csvFileName, Function<Record, T> objectConstructor
    ) {
        return getRecordsFromFile(csvFileName).stream()
                .map(objectConstructor).toList();
    }

    public static Vec2 getVec2(Record record, String xKey, String yKey) {
        return new Vec2(record.getFloat(xKey), record.getFloat(yKey));
    }

}
