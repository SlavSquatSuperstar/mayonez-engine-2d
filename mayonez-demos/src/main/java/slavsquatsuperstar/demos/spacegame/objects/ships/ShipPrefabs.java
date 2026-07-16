package slavsquatsuperstar.demos.spacegame.objects.ships;

import slavsquatsuperstar.demos.spacegame.PrefabUtils;

import java.util.List;

/**
 * Defines different types of spacecraft available in the game.
 *
 * @author SlavSquatSuperstar
 */
public final class ShipPrefabs {

    private static final List<SpaceshipProperties> SPACESHIP_TYPES;
    public static final SpaceshipProperties SHUTTLE1_PROPERTIES;
    public static final SpaceshipProperties SHUTTLE2_PROPERTIES;
    public static final SpaceshipProperties FIGHTER_PROPERTIES;
    public static final SatelliteProperties SATELLITE_PROPERTIES;

    public static final int NUM_SHIP_TYPES;

    static {
        // Read spaceship data file
        SPACESHIP_TYPES = PrefabUtils.getObjectsFromFile(
                "assets/spacegame/data/ships/spaceships.csv",
                SpaceshipProperties::new
        );

        NUM_SHIP_TYPES = SPACESHIP_TYPES.size();
        SHUTTLE1_PROPERTIES = SPACESHIP_TYPES.get(0);
        SHUTTLE2_PROPERTIES = SPACESHIP_TYPES.get(1);
        FIGHTER_PROPERTIES = SPACESHIP_TYPES.get(2);

        // Read satellite data file
        var satelliteTypes = PrefabUtils.getObjectsFromFile(
                "assets/spacegame/data/ships/satellites.csv",
                SatelliteProperties::new
        );
        SATELLITE_PROPERTIES = satelliteTypes.getFirst();
    }

    private ShipPrefabs() {
    }

    public static SpaceshipProperties getSpaceShipProperty(int index) {
        return SPACESHIP_TYPES.get(index);
    }

}
