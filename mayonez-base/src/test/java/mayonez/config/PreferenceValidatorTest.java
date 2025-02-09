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

    private static Record defaults;

    private Record preferences;

    @BeforeAll
    static void getDefaults() {
        defaults = new Record();
        defaults.set("str1", "foo");
        defaults.set("str2", "bar");
        defaults.set("int1", 800);
        defaults.set("int2", 600);
        defaults.set("float1", 1f);
        defaults.set("float2", 2f);
        defaults.set("bool1", true);
        defaults.set("bool2", false);
    }

    @BeforeEach
    void clearPreferences() {
        preferences = new Record();
    }

    // Not Null

    @Test
    void nullValuesNotValid() {
        preferences.set("str1", (String) null);
        preferences.set("int1", (Integer) null);
        preferences.set("bool1", (Boolean) null);

        var rule = new ValueNotNullValidator("str1", "int1", "bool1");
        rule.validate(preferences, defaults);
        assertEquals("foo", preferences.getString("str1"));
        assertEquals(800, preferences.getInt("int1"));
        assertTrue(preferences.getBoolean("bool1"));
    }

    @Test
    void notNullValuesValid() {
        preferences.set("str1", "spam");
        preferences.set("int1", 100);
        preferences.set("bool1", false);

        var rule = new ValueNotNullValidator("str1", "int1", "bool1");
        rule.validate(preferences, defaults);
        assertEquals("spam", preferences.getString("str1"));
        assertEquals(100, preferences.getInt("int1"));
        assertFalse(preferences.getBoolean("bool1"));
    }

    // String

    @Test
    void stringEmptyIsNotValid() {
        var rule = new StringValidator("str1", "str2");
        rule.validate(preferences, defaults);
        assertEquals("foo", preferences.getString("str1"));
        assertEquals("bar", preferences.getString("str2"));
    }

    @Test
    void strongNonEmptyIsValid() {
        preferences.set("str1", "spam");
        preferences.set("str2", "eggs");

        var rule = new StringValidator("str1", "str2");
        rule.validate(preferences, defaults);
        assertEquals("spam", preferences.getString("str1"));
        assertEquals("eggs", preferences.getString("str2"));
    }

    // Float

    @Test
    void floatOutOfRangeIsNotValid() {
        preferences.set("float1", -1f);
        preferences.set("float2", 10f);

        var rule = new FloatValidator(0f, 5f, "float1", "float2");
        rule.validate(preferences, defaults);
        assertEquals(1f, preferences.getFloat("float1"));
        assertEquals(2f, preferences.getFloat("float2"));
    }

    @Test
    void floatInRangeIsValid() {
        preferences.set("float1", 3f);
        preferences.set("float2", 4f);

        var rule = new FloatValidator(0f, 5f, "float1", "float2");
        rule.validate(preferences, defaults);
        assertEquals(3f, preferences.getFloat("float1"));
        assertEquals(4f, preferences.getFloat("float2"));
    }

    // Int

    @Test
    void intOutOfRangeIsNotValid() {
        preferences.set("int1", 0);
        preferences.set("int2", 2000);

        var rule = new IntValidator(100, 1000, "int1", "int2");
        rule.validate(preferences, defaults);
        assertEquals(800, preferences.getInt("int1"));
        assertEquals(600, preferences.getInt("int2"));
    }

    @Test
    void intInRangeIsValid() {
        preferences.set("int1", 400);
        preferences.set("int2", 300);

        var rule = new IntValidator(100, 1000, "int1", "int2");
        rule.validate(preferences, defaults);
        assertEquals(400, preferences.getInt("int1"));
        assertEquals(300, preferences.getInt("int2"));
    }

    // Boolean

    @Test
    void booleanTrueOrFalseIsValid() {
        preferences.set("bool1", "false");
        preferences.set("bool2", "true");

        var rule = new BooleanValidator("bool1", "bool2");
        rule.validate(preferences, defaults);
        assertFalse(preferences.getBoolean("bool1"));
        assertTrue(preferences.getBoolean("bool2"));
    }

    @Test
    void booleanIsYesOrNoIsValid() {
        preferences.set("bool1", "no");
        preferences.set("bool2", "yes");

        var rule = new BooleanValidator("bool1", "bool2");
        rule.validate(preferences, defaults);
        assertFalse(preferences.getBoolean("bool1"));
        assertTrue(preferences.getBoolean("bool2"));
    }

    @Test
    void booleanOtherStringIsNotValid() {
        preferences.set("bool1", "nah");
        preferences.set("bool2", "yeah");

        var rule = new BooleanValidator("bool1", "bool2");
        rule.validate(preferences, defaults);
        assertTrue(preferences.getBoolean("bool1"));
        assertFalse(preferences.getBoolean("bool2"));
    }

    @Test
    void booleanOneOrZeroIsValid() {
        preferences.set("bool1", 0);
        preferences.set("bool2", 1);

        var rule = new BooleanValidator("bool1", "bool2");
        rule.validate(preferences, defaults);
        assertFalse(preferences.getBoolean("bool1"));
        assertTrue(preferences.getBoolean("bool2"));
    }

}
