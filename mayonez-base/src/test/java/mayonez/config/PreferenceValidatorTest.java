package mayonez.config;

import mayonez.util.Record;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.config.PreferenceValidator} class.
 *
 * @author SlavSquatSuperstar
 */
class PreferenceValidatorTest {

    private static final String NAME = "Mayonez Engine";
    private static final String VERSION = "0.7.10";

    private static mayonez.util.Record defaults;

    private mayonez.util.Record preferences;

    @BeforeAll
    static void getDefaults() {
        defaults = new mayonez.util.Record();
        defaults.set("name", NAME);
        defaults.set("version", VERSION);
        defaults.set("width", 800);
        defaults.set("height", 600);
        defaults.set("height", 600);
        defaults.set("saveLogs", true);
        defaults.set("useGL", false);
    }

    @BeforeEach
    void clearPreferences() {
        preferences = new Record();
    }

    // String

    @Test
    void stringCannotBeEmpty() {
        var rule = new StringValidator("name", "version");
        rule.validate(preferences, defaults);
        assertEquals(NAME, preferences.getString("name"));
        assertEquals(VERSION, preferences.getString("version"));
    }

    @Test
    void stringIsNotEmpty() {
        final var name = "Mayonez Demos";
        final var version = "0.0.1-alpha";

        preferences.set("name", name);
        preferences.set("version", version);

        var rule = new StringValidator("name", "version");
        rule.validate(preferences, defaults);
        assertEquals(name, preferences.getString("name"));
        assertEquals(version, preferences.getString("version"));
    }

    // Int

    @Test
    void intCannotBeOutOfRange() {
        preferences.set("width", 0);
        preferences.set("height", 2000);

        var rule = new IntValidator(100, 1000, "width", "height");
        rule.validate(preferences, defaults);
        assertEquals(800, preferences.getInt("width"));
        assertEquals(600, preferences.getInt("height"));
    }

    @Test
    void intIsInRange() {
        preferences.set("width", 400);
        preferences.set("height", 300);
        var rule = new IntValidator(100, 1000, "width", "height");
        rule.validate(preferences, defaults);
        assertEquals(400, preferences.getInt("width"));
        assertEquals(300, preferences.getInt("height"));
    }

    // Boolean

    @Test
    void booleanIsTrueOrFalse() {
        preferences.set("saveLogs", "false");
        preferences.set("useGL", "true");

        var rule = new BooleanValidator("saveLogs", "useGL");
        rule.validate(preferences, defaults);
        assertFalse(preferences.getBoolean("saveLogs"));
        assertTrue(preferences.getBoolean("useGL"));
    }

    @Test
    void booleanIsYesOrNo() {
        preferences.set("saveLogs", "no");
        preferences.set("useGL", "yes");

        var rule = new BooleanValidator("saveLogs", "useGL");
        rule.validate(preferences, defaults);
        assertFalse(preferences.getBoolean("saveLogs"));
        assertTrue(preferences.getBoolean("useGL"));
    }

    @Test
    void invalidBooleanString() {
        preferences.set("saveLogs", "nah");
        preferences.set("useGL", "yeah");

        var rule = new BooleanValidator("saveLogs", "useGL");
        rule.validate(preferences, defaults);
        assertTrue(preferences.getBoolean("saveLogs"));
        assertFalse(preferences.getBoolean("useGL"));
    }

    @Test
    void booleanIs1Or0() {
        preferences.set("saveLogs", 0);
        preferences.set("useGL", 1);

        var rule = new BooleanValidator("saveLogs", "useGL");
        rule.validate(preferences, defaults);
        assertFalse(preferences.getBoolean("saveLogs"));
        assertTrue(preferences.getBoolean("useGL"));
    }

}
