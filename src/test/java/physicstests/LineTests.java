package physicstests;

import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.primitives.Line2D;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class LineTests {

    @Test
    public void lineContainsEndpoints() {
        Line2D line = new Line2D(new Vector2(0, 0), new Vector2(10, 5));
        Vector2 start = new Vector2(0, 0);
        assertTrue(line.contains(start));
        Vector2 end = new Vector2(10, 5);
        assertTrue(line.contains(end));
    }

    @Test
    public void lineContainsPoint() {
        Line2D line = new Line2D(new Vector2(0, 0), new Vector2(10, 5));
        Vector2 point = new Vector2(5, 2.5f);
        assertTrue(line.contains(point));
    }

    @Test
    public void verticalLineContainsEndpoints() {
        Line2D line = new Line2D(new Vector2(0, 0), new Vector2(0, 10));
        Vector2 start = new Vector2(0, 0);
        assertTrue(line.contains(start));
        Vector2 end = new Vector2(0, 10);
        assertTrue(line.contains(end));
    }

    @Test
    public void verticalLineContainsPoint() {
        Line2D line = new Line2D(new Vector2(0, 0), new Vector2(0, 10));
        Vector2 point = new Vector2(0, 5);
        assertTrue(line.contains(point));
    }


}
