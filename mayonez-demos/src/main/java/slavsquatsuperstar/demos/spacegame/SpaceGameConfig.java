package slavsquatsuperstar.demos.spacegame;

import mayonez.config.*;
import mayonez.input.*;
import mayonez.util.Record;
import slavsquatsuperstar.demos.spacegame.objects.ships.ShipPrefabs;

/**
 * The controls for the space game.
 *
 * @author SlavSquatSuperstar
 */
public final class SpaceGameConfig extends GameConfig {

    public static final String CONFIG_FILE_NAME = "user_config.json";
    private static final Record DEFAULTS;
    private static SpaceGameConfig config;

    static {
        DEFAULTS = new Record();
        DEFAULTS.set("move_forward", "w");
        DEFAULTS.set("move_backward", "s");
        DEFAULTS.set("move_left", "q");
        DEFAULTS.set("move_right", "e");
        DEFAULTS.set("turn_left", "a");
        DEFAULTS.set("turn_right", "d");
        DEFAULTS.set("brake", "space");
        DEFAULTS.set("auto_brake", "space");
        DEFAULTS.set("player_ship_index", 0);
        DEFAULTS.set("num_stars", 5000);
        DEFAULTS.set("enemy_multiplier", 1f);
        DEFAULTS.set("obstacle_multiplier", 1f);
    }

    private SpaceGameConfig(String path, Record defaults) {
        super(path, defaults);
    }

    public static void readConfig() {
        config = new SpaceGameConfig(CONFIG_FILE_NAME, DEFAULTS);
        config.readFromFile();
        config.validateUserPreferences(getRules());
    }

    private static PreferenceValidator<?>[] getRules() {
        return new PreferenceValidator<?>[]{
                new StringValidator(
                        "move_forward", "move_backward",
                        "move_left", "move_right",
                        "turn_left", "turn_right",
                        "brake", "auto_brake"
                ),
                new IntValidator(
                        0, ShipPrefabs.NUM_SHIP_TYPES, "player_ship_index"
                ),
                new IntValidator(
                        0, 20000, "num_stars"
                ),
                new FloatValidator(
                        0f, 5f, "enemy_multiplier", "obstacle_multiplier"
                )
        };
    }

    public static InputAxis getVerticalMoveAxis() {
        return new KeyAxis(
                Key.findWithName(config.getString("move_backward")),
                Key.findWithName(config.getString("move_forward"))
        );
    }

    public static InputAxis getHorizontalMoveAxis() {
        return new KeyAxis(
                Key.findWithName(config.getString("move_left")),
                Key.findWithName(config.getString("move_right"))
        );
    }

    public static InputAxis getTurnAxis() {
        return new KeyAxis(
                Key.findWithName(config.getString("turn_left")),
                Key.findWithName(config.getString("turn_right"))
        );
    }

    public static Key getBrakeKey() {
        return Key.findWithName(config.getString("brake"));
    }

    public static Key getAutoBrakeKey() {
        return Key.findWithName(config.getString("auto_brake"));
    }

    public static int getPlayerShipIndex() {
        return config.getInt("player_ship_index");
    }

    public static int getNumStars() {
        return config.getInt("num_stars");
    }

    public static float getEnemyMultiplier() {
        return config.getFloat("enemy_multiplier");
    }

    public static float getObstacleMultiplier() {
        return config.getFloat("obstacle_multiplier");
    }

}
