package tests;

import static org.junit.Assert.assertTrue;

import org.json.simple.JSONArray;
import org.junit.Test;

import com.slavsquatsuperstar.mayonez.Logger;
import com.slavsquatsuperstar.util.JSONFile;
import com.slavsquatsuperstar.util.TextFile;

public class IOTests {

    static final String RESOURCES = "src/test/resources/";

    @Test
    public void readTextFile() {
        TextFile file = new TextFile(RESOURCES + "properties.txt");
        String[] properties = file.readLines();
        assertTrue(properties[0].split("=")[1].equals("Mayonez Engine"));
    }

    @Test
    public void writeToTextFile() {
        String[] lines = {"foo", "bar"};
        TextFile file = new TextFile(RESOURCES + "property.txt");
        file.save(lines);
    }

    @Test
    public void readJSONFile() {
        JSONFile properties = new JSONFile(RESOURCES + "properties.json");
        assertTrue(properties.getStr("name").equals("Mayonez Engine"));
        JSONArray languages = properties.getArr("languages");
        assertTrue(languages.get(0).equals("Java"));
        assertTrue(properties.getBool("in_progress"));
        Logger.log(properties.getFloat("version"));
        Logger.log(properties.getObj(null));
    }

}
