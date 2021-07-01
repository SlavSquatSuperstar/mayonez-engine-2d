package physicstests;

import org.junit.Test;
import slavsquatsuperstar.mayonez.Vector2;
import slavsquatsuperstar.mayonez.physics2d.primitives.Line2D;

import static junit.framework.TestCase.assertFalse;
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

    @Test
    public void lineIntersectsOnePoint() {
        Line2D l1 = new Line2D(new Vector2(-1, -1), new Vector2(1, 1));
        Line2D l2 = new Line2D(new Vector2(1, -1), new Vector2(-1, 1));
        assertTrue(l1.intersects(l2));
    }

    @Test
    public void lineIntersectsOnePointTShape() {
        Line2D l1 = new Line2D(new Vector2(-1, 0), new Vector2(1, -0));
        Line2D l2 = new Line2D(new Vector2(0, -1), new Vector2(0, 0));
        assertTrue(l1.intersects(l2));
    }

    @Test
    public void sameLineIntersects() {
        Line2D l1 = new Line2D(new Vector2(1, 1), new Vector2(3, 1));
        Line2D l2 = new Line2D(new Vector2(1, 1), new Vector2(3, 1));
        assertTrue(l1.intersects(l2));
    }

    @Test
    public void overlappingLineIntersects() {
        Line2D l1 = new Line2D(new Vector2(1, 1), new Vector2(3, 3));
        Line2D l2 = new Line2D(new Vector2(2, 2), new Vector2(4, 4));
        assertTrue(l1.intersects(l2));
    }

    @Test
    public void parallelLineNotIntersects() {
        Line2D l1 = new Line2D(new Vector2(1, 1), new Vector2(3, 2));
        Line2D l2 = new Line2D(new Vector2(2, 2), new Vector2(4, 3));
        assertFalse(l1.intersects(l2));
    }


}
