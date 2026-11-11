package slavsquatsuperstar.demos;

import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.math.shapes.*;

import static mayonez.math.Random.*;

/**
 * Generates random shapes.
 *
 * @author SlavSquatSuperstar
 */
public final class ShapeUtils {

    private static final float MIN_INTERIOR_ANGLE = 15f;
    private static final float MAX_INTERIOR_ANGLE = 180f - MIN_INTERIOR_ANGLE;

    private ShapeUtils() {
    }

    /**
     * Generate an ellipse with a random rotation and a 25% chance of being a
     * circle.
     *
     * @param center      the center
     * @param minDiameter the minimum diameter
     * @param maxDiameter the maximum diameter
     * @return the ellipse
     */
    public static Shape getRandomEllipse(Vec2 center, float minDiameter, float maxDiameter) {
        var isCircle = randomPercent(0.25f);
        if (isCircle) {
            var radius = Random.randomFloat(minDiameter, maxDiameter) / 2f;
            return new Circle(center, radius);
        } else {
            var size = Random.randomVector(minDiameter, maxDiameter, minDiameter, maxDiameter);
            return new Ellipse(center, size, randomAngle());
        }
    }

    /**
     * Generate a rectangle with a random rotation with a 25% chance of being a
     * square.
     *
     * @param center    the center
     * @param minLength the minimum side length
     * @param maxLength the maximum side length
     * @return the rectangle
     */
    public static Shape getRandomRectangle(Vec2 center, float minLength, float maxLength) {
        var isSquare = randomPercent(0.25f);
        Vec2 size;
        if (isSquare) {
            size = new Vec2(randomFloat(minLength, maxLength));
        } else {
            size = Random.randomVector(minLength, maxLength, minLength, maxLength);
        }
        return new Rectangle(center, size, randomAngle());
    }

    /**
     * Generate a triangle with a random rotation and a 25% chance of being an
     * equilateral triangle.
     *
     * @param center    the center
     * @param minRadius the minimum radius
     * @param maxRadius the maximum radius
     * @return the triangle
     */
    public static Shape randomTriangle(Vec2 center, float minRadius, float maxRadius) {
        return new Polygon(
                center, 3, randomFloat(minRadius, maxRadius)
        ).rotate(0, null);
    }

    /**
     * Generate a regular polygon with 5-8 sides and a random rotation. The
     * radius is the distance from the center to one of the vertices.
     *
     * @param center    the center
     * @param minRadius the minimum radius
     * @param maxRadius the maximum radius
     * @return the polygon
     */
    public static Polygon randomPolygon(Vec2 center, float minRadius, float maxRadius) {
        return new Polygon(
                center, Random.randomInt(5, 8),
                randomFloat(minRadius, maxRadius)
        ).rotate(randomAngle(), null);
    }

    /**
     * Generate a random edge (line segment) with a random rotation.
     *
     * @param center    the center
     * @param minLength the minimum length
     * @param maxLength the maximum length
     * @return the edge
     */
    public static Shape getRandomEdge(Vec2 center, float minLength, float maxLength) {
        var halfLength = Random.randomFloat(minLength, maxLength) / 2f;
        var halfDirection = new Vec2(halfLength, 0f).rotate(Random.randomAngle());
        return new Edge(center.sub(halfDirection), center.add(halfDirection));
    }

}
