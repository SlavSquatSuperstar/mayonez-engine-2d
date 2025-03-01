package mayonez.util;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link mayonez.util.Record} class.
 *
 * @author SlavSquatSuperstar
 */
class RecordTest {

    private Record rec1, rec2;

    @BeforeEach
    void createRecords() {
        var innerRecord = new Record(Map.of(
                "quux", "baz",
                "meaning", "42"
        ));
        rec1 = new Record(Map.of(
                "str1", "foo",
                "int1", 420,
                "float1", 6.9f,
                "bool1", true,
                "list1", List.of("bar", 123, 4.5f, true),
                "rec1", innerRecord
        ));
        rec1.set("null1", (String) null);

        rec2 = new Record(Map.of(
                "str1", "foobar",
                "str2", "spam eggs",
                "bool2", false
        ));
        rec2.set("int1", (String) null);
        rec2.set("null2", (String) null);
    }

    // Get Single Tests

    @Test
    void getNullCorrect() {
        assertTrue(rec1.contains("null1"));
        assertNull(rec1.get("null1"));
    }

    @Test
    void getStringCorrect() {
        assertEquals("foo", rec1.getString("str1"));
        assertEquals("420", rec1.getString("int1"));
        assertEquals("6.9", rec1.getString("float1"));
        assertEquals("true", rec1.getString("bool1"));
        assertEquals("", rec1.getString("null1"));
    }

    @Test
    void getIntCorrect() {
        assertEquals(0, rec1.getInt("str1"));
        assertEquals(420, rec1.getInt("int1"));
        assertEquals(6, rec1.getInt("float1"));
        assertEquals(0, rec1.getInt("bool1"));
        assertEquals(0, rec1.getInt("null1"));
    }

    @Test
    void getFloatCorrect() {
        assertEquals(0f, rec1.getFloat("str1"));
        assertEquals(420f, rec1.getFloat("int1"));
        assertEquals(6.9f, rec1.getFloat("float1"));
        assertEquals(0f, rec1.getFloat("bool1"));
        assertEquals(0f, rec1.getFloat("null1"));
    }

    @Test
    void getBooleanCorrect() {
        assertFalse(rec1.getBoolean("str1"));
        assertTrue(rec1.getBoolean("int1"));
        assertTrue(rec1.getBoolean("float1"));
        assertTrue(rec1.getBoolean("bool1"));
        assertFalse(rec1.getBoolean("null1"));
    }

    @Test
    void getMissingValuesCorrect() {
        assertEquals("", rec1.getString("missing"));
        assertEquals(0, rec1.getInt("missing"));
        assertEquals(0f, rec1.getFloat("missing"));
        assertFalse(rec1.getBoolean("missing"));
        assertNull(rec1.get("missing"));
    }

    // Get Array Tests

    @Test
    void getArrayCorrect() {
        var array = rec1.getArray("list1");
        assertNotNull(array);
        assertEquals(4, array.size());
        assertEquals("bar", array.get(0));
        assertEquals(123, array.get(1));
        assertEquals(4.5f, array.get(2));
        assertEquals(true, array.get(3));
    }

    @Test
    void getNonArrayNull() {
        assertNull(rec1.getArray("str1"));
        assertNull(rec1.getArray("int1"));
        assertNull(rec1.getArray("float"));
        assertNull(rec1.getArray("bool1"));
        assertNull(rec1.getArray("rec1"));
        assertNull(rec1.getArray("null1"));
    }

    // Get Object Tests

    @Test
    void getObjectCorrect() {
        var record = rec1.getObject("rec1");
        assertNotNull(record);
        assertEquals(2, record.size());
        assertEquals("baz", record.get("quux"));
        assertEquals("42", record.get("meaning"));
    }

    @Test
    void getNonObjectNull() {
        assertNull(rec1.getObject("str1"));
        assertNull(rec1.getObject("int1"));
        assertNull(rec1.getObject("float"));
        assertNull(rec1.getObject("bool1"));
        assertNull(rec1.getObject("list1"));
        assertNull(rec1.getObject("null1"));
    }

    // Set From Tests

    @Test
    void addAllSuccess() {
        rec1.setFrom(rec2);
        assertEquals("foobar", rec1.getString("str1")); // overridden
        assertEquals("spam eggs", rec1.getString("str2")); // added
        assertFalse(rec1.getBoolean("bool2"));
        assertEquals(420, rec1.getInt("int1")); // not replaced
        assertNull(rec1.get("null2")); // added null
    }

}
