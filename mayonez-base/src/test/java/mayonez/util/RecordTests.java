package mayonez.util;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.util.Record} class.
 *
 * @author SlavSquatSuperstar
 */
public class RecordTests {

    private Record rec1, rec2;

    @BeforeEach
    void createRecords() {
        Map<String, Object> map1 = Map.of("name", "Linus Torvalds", "age", 53, "version", "0.7.6", "height", 1.78f);
        map1 = new HashMap<>(map1);
        rec1 = new Record(map1);

        Map<String, Object> map2 = Map.of("name", "James Gosling", "age", 69, "useGL", true, "saveLogs", "no");
        rec2 = new Record(map2);
    }

    @Test
    public void getStringCorrect() {
        assertEquals("Linus Torvalds", rec1.getString("name"));
        assertEquals("53", rec1.getString("age"));
        assertEquals("0.7.6", rec1.getString("version"));
        assertEquals("1.78", rec1.getString("height"));
        assertEquals("", rec1.getString("date"));
        assertEquals("true", rec2.getString("useGL"));
        assertEquals("no", rec2.getString("saveLogs"));
    }

    // Get Value Tests

    @Test
    public void getIntCorrect() {
        assertEquals(0, rec1.getInt("name"));
        assertEquals(53, rec1.getInt("age"));
        assertEquals(1, rec1.getInt("height"));
        assertEquals(0, rec1.getInt("version"));
        assertEquals(0, rec2.getInt("useGL"));
    }

    @Test
    public void getFloatCorrect() {
        assertEquals(0f, rec1.getFloat("name"));
        assertEquals(53f, rec1.getFloat("age"));
        assertEquals(1.78f, rec1.getFloat("height"));
        assertEquals(0f, rec1.getFloat("version"));
        assertEquals(0f, rec2.getFloat("useGL"));
    }

    @Test
    public void getBooleanCorrect() {
        assertFalse(rec1.getBoolean("name"));
        assertTrue(rec1.getBoolean("age"));
        assertTrue(rec2.getBoolean("useGL"));
        assertFalse(rec2.getBoolean("saveLogs"));
    }

    // Copy/Add Tests

    @Test
    public void copyFromSuccess() {
        rec1.copyFrom(rec2);
        assertEquals(4, rec1.size());
        assertEquals("James Gosling", rec1.getString("name"));
        assertEquals(69, rec1.getInt("age"));
        assertTrue(rec1.getBoolean("useGL"));
        assertFalse(rec1.getBoolean("saveLogs"));
    }

    @Test
    public void addAllSuccess() {
        rec1.addAll(rec2);
        assertEquals(6, rec1.size());
        assertEquals("James Gosling", rec1.getString("name"));
        assertEquals(69, rec1.getInt("age"));
        assertEquals("0.7.6", rec1.getString("version"));
        assertEquals(1.78f, rec1.getFloat("height"));
        assertTrue(rec1.getBoolean("useGL"));
        assertFalse(rec1.getBoolean("saveLogs"));
    }

}
