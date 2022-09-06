package slavsquatsuperstar.test.detectiontests;

import org.junit.jupiter.api.Test;
import slavsquatsuperstar.math.Vec2;
import slavsquatsuperstar.mayonez.physics.detection.GJKDetector;
import slavsquatsuperstar.mayonez.physics.shapes.Polygon;
import slavsquatsuperstar.mayonez.physics.shapes.Triangle;
import slavsquatsuperstar.test.TestUtils;

/**
 * Unit Tests for the {@link GJKDetector} class.
 *
 * @author SlavSquatSuperstar
 */
public class GJKTests {

    @Test
    public void getSimplexCorrect() {
        Polygon p1 = new Triangle(new Vec2(4, 5), new Vec2(9, 9), new Vec2(4, 11));
        Polygon p2 = new Polygon(new Vec2(5, 7), new Vec2(7, 3), new Vec2(10, 2), new Vec2(12, 7));
        TestUtils.assertShapeCollision(p1, p2);
    }

}
