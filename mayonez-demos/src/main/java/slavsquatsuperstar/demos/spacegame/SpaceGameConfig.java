package slavsquatsuperstar.demos.spacegame;

import mayonez.config.*;
import mayonez.input.*;
import mayonez.util.Record;

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
    }

    private SpaceGameConfig(String filename, Record defaults) {
        super(filename, defaults);
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
                        "brake"
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

}
