package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.util.Record;

import java.util.*;

/**
 * Defines different types of spaceships available in the game.
 *
 * @author SlavSquatSuperstar
 */
public final class SpaceshipPrefabs {

    public static final SpaceshipProperties SHUTTLE_PROPERTIES1;
    public static final SpaceshipProperties SHUTTLE_PROPERTIES2;
    public static final SpaceshipProperties FIGHTER_PROPERTIES;

    static {
        var spaceshipTypes =
                getRecordsFromFile("assets/spacegame/data/ships/spaceships.csv")
                        .stream().map(SpaceshipProperties::new).toList();

        SHUTTLE_PROPERTIES1 = spaceshipTypes.get(0);
        SHUTTLE_PROPERTIES2 = spaceshipTypes.get(1);
        FIGHTER_PROPERTIES = spaceshipTypes.get(2);
    }

    private SpaceshipPrefabs() {
    }

    public static List<Record> getRecordsFromFile(String csvFileName) {
        var csvFile = Assets.getAsset(csvFileName, CSVFile.class);
        if (csvFile == null) return Collections.emptyList();
        else return csvFile.readCSV();
    }

}
