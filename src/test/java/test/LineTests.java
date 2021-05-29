package test;

import com.slavsquatsuperstar.mayonez.Vector2;
import com.slavsquatsuperstar.mayonez.physics2d.Line2D;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LineTests {
    @Test
    public void pointIsOnVertLine() {
        Line2D line = new Line2D(new Vector2(), new Vector2(0, 10));
        Vector2 point = new Vector2(0, 5);
        assertTrue(line.contains(point));
    }

    @Test
    public void pointNotOnLine() {
        Line2D line = new Line2D(new Vector2(), new Vector2(5, 5));
        Vector2 point = new Vector2(0, 1);
        assertFalse(line.contains(point));
    }
}
