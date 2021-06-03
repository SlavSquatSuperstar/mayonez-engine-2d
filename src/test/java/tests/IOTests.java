package tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.slavsquatsuperstar.util.TextFile;

public class IOTests {

    static final String RESOURCES = "src/test/resources/";

    @Test
    public void readTextFile() {
        TextFile file = new TextFile(RESOURCES + "properties.txt");
        String[] properties = file.readLines();
        assertTrue(properties[0].split("=")[1].equals("Mayonez Engine"));
    }

}
