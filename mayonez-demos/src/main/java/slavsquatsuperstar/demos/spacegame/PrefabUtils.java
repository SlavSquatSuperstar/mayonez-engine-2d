package slavsquatsuperstar.demos.spacegame;

import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.math.*;
import mayonez.util.Record;

import java.util.*;

/**
 * Utility methods for reading records from data files.
 *
 * @author SlavSquatSuperstar
 */
public final class PrefabUtils {

    private PrefabUtils() {
    }

    public static List<Record> getRecordsFromFile(String csvFileName) {
        var csvFile = Assets.getAsset(csvFileName, CSVFile.class);
        if (csvFile == null) return Collections.emptyList();
        else return csvFile.readCSV();
    }

    public static Vec2 getColliderSize(Record record) {
        return new Vec2(record.getFloat("colliderSizeX"), record.getFloat("colliderSizeY"));
    }

}
