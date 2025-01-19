package slavsquatsuperstar.demos.spacegame.objects.ships;

import mayonez.assets.*;
import mayonez.assets.text.*;
import mayonez.util.Record;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.WeaponHardpoint;
import slavsquatsuperstar.demos.spacegame.movement.ThrusterProperties;

import java.util.*;

/**
 * Defines different types of spaceships available in the game.
 *
 * @author SlavSquatSuperstar
 */
public final class SpaceshipPrefabs {

    public static final SpaceshipProperties SHUTTLE_PROPERTIES1;
    public static final SpaceshipProperties SHUTTLE_PROPERTIES2;

    static {
        SHUTTLE_PROPERTIES1 = new SpaceshipProperties(
                "assets/spacegame/textures/ships/shuttle.png",
                8, 4,
                getThrusters("assets/spacegame/data/shuttle_thrusters.csv"),
                getHardpoints("assets/spacegame/data/shuttle_hardpoints.csv")
        );
        SHUTTLE_PROPERTIES2 = new SpaceshipProperties(
                "assets/spacegame/textures/ships/shuttle_rusty.png",
                6, 0,
                getThrusters("assets/spacegame/data/shuttle_thrusters.csv"),
                getHardpoints("assets/spacegame/data/shuttle_hardpoints.csv")
        );
    }

    private SpaceshipPrefabs() {
    }

    private static List<ThrusterProperties> getThrusters(String csvFileName) {
        return getRecordsFromFile(csvFileName)
                .stream().map(ThrusterProperties::new).toList();
    }

    private static List<WeaponHardpoint> getHardpoints(String csvFileName) {
        return getRecordsFromFile(csvFileName)
                .stream().map(WeaponHardpoint::new).toList();
    }

    private static List<Record> getRecordsFromFile(String csvFileName) {
        var csvFile = Assets.getAsset(csvFileName, CSVFile.class);
        if (csvFile == null) return Collections.emptyList();
        else return csvFile.readCSV();
    }

}
