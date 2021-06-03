package tests;

import com.slavsquatsuperstar.util.JSONFile;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class JSONTests {

    private static JSONFile json;

    @BeforeClass
    public static void openJson() {
        json = new JSONFile("src/test/resources/properties.json");
    }

    @Test
    public void getProperties() {
        assertTrue(json.getBool("in_progress"));
        assertTrue(json.getBool("uses_dependencies"));
        assertEquals("Java", json.getArr("languages").get(0));
        assertEquals(0, json.getInt("version"));

        assertEquals("Mayonez Engine", json.getStr("name"));
        assertTrue(json.getBool("in_progress"));
        assertTrue(json.getFlt("version") == 0.5f);
        assertNull(json.getObj(null));
    }

    @Test
    public void getPropertiesFail() {
        assertNull(json.getStr("version"));
    }

}
