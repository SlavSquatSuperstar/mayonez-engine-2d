package tests;

import slavsquatsuperstar.util.JSONFile;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import static org.junit.Assert.*;

public class JSONTests {

    JSONFile json;

    @Before
    public void readJSONFile() {
        json = new JSONFile("properties.json");
    }

    @Test
    public void getPropertiesFromJSON() {
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
    public void getPropertiesFromJSONFail() {
        assertNull(json.getStr("version"));
    }

    @Test
    public void savePropertiesToJSON() {
        json.setProperty("time", LocalTime.now());
        json.saveJSON();
    }

}
