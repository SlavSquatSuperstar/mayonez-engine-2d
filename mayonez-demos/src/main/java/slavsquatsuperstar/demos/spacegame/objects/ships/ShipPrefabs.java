package slavsquatsuperstar.demos.spacegame.objects.ships;

import slavsquatsuperstar.demos.spacegame.PrefabUtils;

/**
 * Defines different types of spacecraft available in the game.
 *
 * @author SlavSquatSuperstar
 */
public final class ShipPrefabs {

    public static final SpaceshipProperties SHUTTLE_PROPERTIES1;
    public static final SpaceshipProperties SHUTTLE_PROPERTIES2;
    public static final SpaceshipProperties FIGHTER_PROPERTIES;
    public static final SatelliteProperties SATELLITE_PROPERTIES;

    static {
        // Read spaceship data file
        var spaceshipTypes = PrefabUtils.getObjectsFromFile(
                "assets/spacegame/data/ships/spaceships.csv",
                SpaceshipProperties::new
        );

        SHUTTLE_PROPERTIES1 = spaceshipTypes.get(0);
        SHUTTLE_PROPERTIES2 = spaceshipTypes.get(1);
        FIGHTER_PROPERTIES = spaceshipTypes.get(2);

        // Read satellite data file
        var satelliteTypes = PrefabUtils.getObjectsFromFile(
                "assets/spacegame/data/ships/satellites.csv",
                SatelliteProperties::new
        );
        SATELLITE_PROPERTIES = satelliteTypes.getFirst();
    }

    private ShipPrefabs() {
    }

}
