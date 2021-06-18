package tests;

import com.slavsquatsuperstar.util.TextFile;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class IOTests {

    TextFile file;

    @Before
    public void getFile() {
        file = new TextFile("properties.txt");
    }

    @Test
    public void readTextFile() {
        String[] properties = file.readLines();
        assertEquals("Mayonez Engine", properties[0].split("=")[1]);
    }

    @Ignore
    @Test
    public void saveTextFile() {
        file.append("date=" + LocalDate.now(), "time=" + LocalTime.now());
        System.out.println("saved to txt");
    }

}
