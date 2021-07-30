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

    private Vec2 normal = new Vec2();
    private float depth;
    private boolean colliding = false;
    private List<Vec2> contactPoints = new ArrayList<>();

    public CollisionManifold() {}

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

    public boolean isColliding() {
        return colliding;
    }

    public List<Vec2> getContactPoints() {
        return contactPoints;
    }

    public void addContactPoint(Vec2 contactPoint) {
        colliding = true;
        contactPoints.add(contactPoint);
    }
}
