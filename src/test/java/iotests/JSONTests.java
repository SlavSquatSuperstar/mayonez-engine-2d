package iotests;

import com.slavsquatsuperstar.mayonez.assets.JSONFile;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class JSONTests {

    JSONFile json;

    @Before
    public void readLocalJSONFile() {
        json = new JSONFile("src/test/resources/properties.json", false);
    }

    @Before
    public void readClasspathJSONFile() {
        JSONFile json = new JSONFile("properties.json", true);
        json.read();
    }

    @Test
    public void getJSONPropertiesSuccess() {
        assertTrue(json.getBool("in_progress"));
        assertTrue(json.getBool("uses_dependencies"));
        assertEquals("Java", json.getArray("languages").get(0));
        assertEquals(0, json.getInt("version"));

        assertEquals("Mayonez Engine", json.getStr("name"));
        assertTrue(json.getBool("in_progress"));
        assertEquals(0.5f, json.getFloat("version"), 0.0f);
        assertNull(json.getObj(null));
    }

    @Test
    public void getJSONPropertiesFail() {
        assertNull(json.getStr("version"));
    }

    @Test
    public void savePropertiesToJSON() {
        JSONFile json = new JSONFile("logs/foobar.json", false);
        json.setProperty("time", LocalTime.now());
        json.setProperty("date", LocalDate.now());
        json.save();
    }

}
