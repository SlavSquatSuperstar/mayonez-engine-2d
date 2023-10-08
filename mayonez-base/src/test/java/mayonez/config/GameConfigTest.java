package mayonez.config;

import mayonez.util.Record;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.config.GameConfig} class.
 *
 * @author SlavSquatSuperstar
 */
class GameConfigTest {

    private static final String NAME = "Mayonez Engine";
    private static final String VERSION = "0.7.10";

    private static mayonez.util.Record defaults;
    private static PreferenceValidator<?>[] rules;

    @BeforeAll
    static void getDefaults() {
        defaults = new Record();
        defaults.set("name", NAME);
        defaults.set("version", VERSION);
        defaults.set("width", 800);
        defaults.set("height", 600);

        rules = new PreferenceValidator[]{
                new StringValidator("name", "version"),
                new IntValidator(100, 2000, "width", "height")
        };
    }

    @Test
    void configFileReadCorrectly() {
        GameConfig config = new GameConfig("testassets/config/user_config1.json", defaults);
        config.readFromFile();

        assertEquals("Mayonez Demos", config.getString("name"));
        assertEquals("0.0.1-alpha", config.getString("version"));
        assertEquals(1080, config.getInt("width"));
        assertEquals(720, config.getInt("height"));
    }

    @Test
    void allPreferencesAreValid() {
        var config = getConfig("testassets/config/user_config1.json");

        assertEquals("Mayonez Demos", config.getString("name"));
        assertEquals("0.0.1-alpha", config.getString("version"));
        assertEquals(1080, config.getInt("width"));
        assertEquals(720, config.getInt("height"));
    }

    @Test
    void somePreferencesAreValid() {
        var config = getConfig("testassets/config/user_config2.json");

        assertEquals("Mayonez Demos", config.getString("name"));
        assertEquals(VERSION, config.getString("version"));
        assertEquals(1080, config.getInt("width"));
        assertEquals(600, config.getInt("height"));
    }

    @Test
    void preferencesFileIsEmpty() {
        var config = getConfig("testassets/config/user_config3.json");

        assertEquals(NAME, config.getString("name"));
        assertEquals(VERSION, config.getString("version"));
        assertEquals(defaults.getInt("width"), config.getInt("width"));
        assertEquals(defaults.getInt("height"), config.getInt("height"));
    }

    private GameConfig getConfig(String filename) {
        var config = new GameConfig(filename, defaults);
        config.readFromFile();
        config.validateUserPreferences(rules);
        return config;
    }

}
