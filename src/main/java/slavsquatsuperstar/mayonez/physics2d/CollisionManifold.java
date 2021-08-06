package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.math.Vec2;

import java.util.ArrayList;
import java.util.List;

/**
 * A collision between two objects.
 *
 * @author SlavSquatSuperstar
 */
public class CollisionManifold {

    private List<Vec2> contactPoints = new ArrayList<>();
    private Vec2 normal = new Vec2(); // Direction of separation
    private float depth; // Penetration distance

    public CollisionManifold(Vec2 normal, float depth) {
        this.normal = normal.unitVector();
        this.depth = depth;
    }

    // Getters and Setters

    public Vec2 getNormal() {
        return normal;
    }

    public float getDepth() {
        return depth;
    }

    public int countContactPoints() {
        return contactPoints.size();
    }

    public List<Vec2> getContactPoints() {
        return contactPoints;
    }

    public void addContactPoint(Vec2 contactPoint) {
        contactPoints.add(contactPoint);
    }
}
