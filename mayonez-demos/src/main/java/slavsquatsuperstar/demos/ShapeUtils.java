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

    private ShapeUtils() {
    }

    /**
     * Generates an ellipse with a random rotation and a 25% chance of being a
     * circle.
     *
     * @param center      the center
     * @param minDiameter the minimum diameter
     * @param maxDiameter the maximum diameter
     * @return the ellipse
     */
    public static Ellipse getRandomEllipse(Vec2 center, float minDiameter, float maxDiameter) {
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
     * Generates a rectangle with a random rotation with a 25% chance of being
     * a square.
     *
     * @param center    the center
     * @param minLength the minimum side length
     * @param maxLength the maximum side length
     * @return the rectangle
     */
    public static Rectangle getRandomRectangle(Vec2 center, float minLength, float maxLength) {
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
     * Generates an arbitrary triangle with a random rotation and a 25% chance
     * of being an equilateral triangle. The radius is roughly the distance
     * from the center to any of the vertices.
     *
     * @param center    the center
     * @param minRadius the minimum radius
     * @param maxRadius the maximum radius
     * @return the triangle
     */
    public static Triangle randomTriangle(Vec2 center, float minRadius, float maxRadius) {
        Triangle triangle;
        var equilateral = randomPercent(0.25f);
        if (equilateral) {
            triangle = new Triangle(
                    center, randomFloat(minRadius, maxRadius)
            );
        } else {
            triangle = randomTriangleInscribed(center, minRadius, maxRadius);
        }
        return triangle.rotate(0, null);
    }

    private static Triangle randomTriangleInscribed(Vec2 center, float minRadius, float maxRadius) {
        /*
         * Inscribe a triangle inside a circle with the given circumcenter,
         * which should somewhat equal the triangle's centroid. To ensure the
         * center remains inside the triangle and avoid very thin triangles,
         * keep the all angles between radii within 90 and 180 degrees. Vary
         * the radii to allow for obtuse triangles.
         */
        float minAngle = 90f, maxAngle = 180f;
        // a1 = rand(90, 180)
        var angle1 = randomFloat(minAngle, maxAngle);
        var max2 = Math.min(maxAngle, 360f - angle1 - minAngle);
        // a2 = [90, min(180, 360 - 90 - a1)]
        // Make sure there is third angle meets the minimum
        var angle2 = randomFloat(minAngle, max2);

        var angles = new float[]{0f, angle1, angle2};
        var vertices = new Vec2[3];
        var totalAngle = 0f;
        for (int i = 0; i < vertices.length; i++) {
            // Use origin as initial circumcenter
            var radius = randomFloat(minRadius, maxRadius);
            totalAngle += angles[i];
            vertices[i] = new Vec2(radius, 0f).rotate(totalAngle);
        }
        return new Triangle(vertices[0], vertices[1], vertices[2]).translate(center);
    }

    /**
     * Generates an arbitrary convex quadrilateral with a random rotation. The
     * radius is roughly the distance from the center to any of the vertices.
     *
     * @param center    the center
     * @param minRadius the minimum radius
     * @param maxRadius the maximum radius
     * @return the rectangle
     */
    public static Polygon randomQuadrilateral(Vec2 center, float minRadius, float maxRadius) {
        /*
         * To ensure the generated shape is convex, use two intersecting
         * diagonals to define the vertices. Move each of the diagonals
         * along the other to vary the side lengths, and rotate one of the
         * diagonals to create shapes without parallel edges. Limit the
         * displacements and rotations to avoid degenerate shapes (triangles)
         * and very thin shapes.
         */
        // Lengths from the center to the vertices
        var radii = new float[4];
        for (int i = 0; i < radii.length; i++) {
            radii[i] = randomFloat(minRadius, maxRadius);
        }
        var diagonal1 = new Edge(new Vec2(radii[0], 0), new Vec2(-radii[2], 0));
        var diagonal2 = new Edge(new Vec2(0, radii[1]), new Vec2(0, -radii[3]))
                .rotate(randomFloat(-60, 60), new Vec2());
        return new Polygon(
                diagonal1.getStart(), diagonal1.getEnd(),
                diagonal2.getStart(), diagonal2.getEnd()
        ).translate(center);
    }

    /**
     * Generates a regular polygon with 5-8 sides and a random rotation. The
     * radius is roughly the distance from the center to any of the vertices.
     *
     * @param center    the center
     * @param minRadius the minimum radius
     * @param maxRadius the maximum radius
     * @return the polygon
     */
    public static Polygon randomRegularPolygon(Vec2 center, float minRadius, float maxRadius) {
        return new Polygon(
                center, Random.randomInt(5, 8),
                randomFloat(minRadius, maxRadius)
        ).rotate(randomAngle(), null);
    }

    /**
     * Generates an arbitrary polygon with 3-8 sides and a random rotation. The
     * resulting shape has a 30% chance of being a triangle, 30% of being a
     * quadrilateral, and a 40% chance of being some other polygon. The radius
     * is roughly the distance from the center to any of the vertices.
     *
     * @param center    the center
     * @param minRadius the minimum radius
     * @param maxRadius the maximum radius
     * @return the polygon
     */
    public static Polygon getRandomPolygon(Vec2 center, float minRadius, float maxRadius) {
        var rand = Random.randomFloat();
        if (rand < 0.30f) {
            return ShapeUtils
                    .randomTriangle(center, minRadius, maxRadius);
        } else if (rand < 0.60f) {
            return ShapeUtils
                    .randomQuadrilateral(center, minRadius, maxRadius);
        } else {
            return ShapeUtils
                    .randomRegularPolygon(center, minRadius, maxRadius);
        }
    }

    /**
     * Generate a random edge (line segment) with a random rotation.
     *
     * @param center    the center
     * @param minLength the minimum length
     * @param maxLength the maximum length
     * @return the edge
     */
    public static Edge getRandomEdge(Vec2 center, float minLength, float maxLength) {
        var length = Random.randomFloat(minLength, maxLength);
        return Edge.fromLength(center, length, Random.randomAngle());
    }

}
