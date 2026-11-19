package slavsquatsuperstar.demos;

import mayonez.math.Random;
import mayonez.math.Vec2;
import mayonez.math.shapes.*;

import java.util.function.Supplier;

import static mayonez.math.Random.*;

/**
 * Generates random shapes.
 *
 * @author SlavSquatSuperstar
 */
public class RandomShapeGenerator {

    private final Supplier<Vec2> positionGenerator;
    private final Supplier<Float> angleGenerator, lengthGenerator;

    /**
     * Creates a random shape generator with the given position, angle, and
     * length generator functions. The position is usually the center of a
     * shape. The angle is the rotation of shape. The length may be used as the
     * width and height of a shape. Alternatively, half the length may be used
     * as the radius, or roughly the distance from the center to any one of a
     * shape's vertices.
     *
     * @param positionGenerator the position (x, y) generator
     * @param angleGenerator    the angle (theta) generator
     * @param lengthGenerator   the length (l) generator
     */
    public RandomShapeGenerator(
            Supplier<Vec2> positionGenerator,
            Supplier<Float> angleGenerator,
            Supplier<Float> lengthGenerator
    ) {
        this.positionGenerator = positionGenerator;
        this.angleGenerator = angleGenerator;
        this.lengthGenerator = lengthGenerator;
    }

    /**
     * Generates an ellipse with a random rotation and a 25% chance of being a
     * circle.
     *
     * @return the ellipse
     */
    public Ellipse getRandomEllipse() {
        var isCircle = randomPercent(0.25f);
        if (isCircle) {
            return new Circle(center(), radius());
        } else {
            return new Ellipse(center(), size(), angle());
        }
    }

    /**
     * Generates a rectangle with a random rotation with a 25% chance of being
     * a square.
     *
     * @return the rectangle
     */
    public Rectangle getRandomRectangle() {
        var isSquare = randomPercent(0.25f);
        if (isSquare) {
            return new Rectangle(center(), new Vec2(length()), angle());
        } else {
            return new Rectangle(center(), size(), angle());
        }
    }

    /**
     * Generates an arbitrary triangle with a random rotation and a 25% chance
     * of being an equilateral triangle.
     *
     * @return the triangle
     */
    public Triangle randomTriangle() {
        Triangle triangle;
        var equilateral = randomPercent(0.25f);
        if (equilateral) {
            triangle = new Triangle(center(), radius());
        } else {
            triangle = randomTriangleInscribed();
        }
        return triangle.rotate(angle(), null);
    }

    private Triangle randomTriangleInscribed() {
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
            totalAngle += angles[i];
            vertices[i] = new Vec2(radius(), 0f).rotate(totalAngle);
        }
        return new Triangle(vertices[0], vertices[1], vertices[2]).translate(center());
    }

    /**
     * Generates an arbitrary convex quadrilateral with a random rotation. The
     * radius is roughly the distance from the center to any of the vertices.
     *
     * @return the rectangle
     */
    public Polygon randomQuadrilateral() {
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
            radii[i] = radius();
        }
        var diagonal1 = new Edge(new Vec2(radii[0], 0), new Vec2(-radii[2], 0));
        var diagonal2 = new Edge(new Vec2(0, radii[1]), new Vec2(0, -radii[3]))
                .rotate(randomFloat(-60, 60), new Vec2());
        return new Polygon(
                diagonal1.getStart(), diagonal1.getEnd(),
                diagonal2.getStart(), diagonal2.getEnd()
        ).translate(center());
    }

    /**
     * Generates a regular polygon with 5-8 sides and a random rotation. The
     * radius is roughly the distance from the center to any of the vertices.
     *
     * @return the polygon
     */
    public Polygon randomRegularPolygon() {
        return new Polygon(
                center(), Random.randomInt(5, 8), radius()
        ).rotate(angle(), null);
    }

    /**
     * Generates an arbitrary polygon with 3-8 sides and a random rotation. The
     * resulting shape has a 30% chance of being a triangle, 30% of being a
     * quadrilateral, and a 40% chance of being some other polygon.
     *
     * @return the polygon
     */
    public Polygon getRandomPolygon() {
        var rand = Random.randomFloat();
        if (rand < 0.30f) {
            return randomTriangle();
        } else if (rand < 0.60f) {
            return randomQuadrilateral();
        } else {
            return randomRegularPolygon();
        }
    }

    /**
     * Generate a random edge (line segment) with a random rotation.
     *
     * @return the edge
     */
    public Edge getRandomEdge() {
        return Edge.fromLength(center(), length(), angle());
    }

    // Helper Functions

    private Vec2 center() {
        return positionGenerator.get();
    }

    private float angle() {
        return angleGenerator.get();
    }

    private float length() {
        return lengthGenerator.get();
    }

    private Vec2 size() {
        return new Vec2(length(), length());
    }

    private float radius() {
        return length() * 0.5f;
    }

}
