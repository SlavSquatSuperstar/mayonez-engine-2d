package slavsquatsuperstar.mayonez.physics2d;

import slavsquatsuperstar.mayonez.Vector2;

import java.util.ArrayList;
import java.util.List;

public class CollisionManifold {

    private Vector2 normal = new Vector2();
    private float depth;
    private boolean colliding = false;
    private List<Vector2> contactPoints = new ArrayList<>();

    public CollisionManifold() {}

    public CollisionManifold(Vector2 normal, float depth) {
        this.normal = normal.unitVector();
        this.depth = depth;
    }

    // Getters and Setters

    public Vector2 getNormal() {
        return normal;
    }

    public float getDepth() {
        return depth;
    }

    public boolean isColliding() {
        return colliding;
    }

    public List<Vector2> getContactPoints() {
        return contactPoints;
    }

    public void addContactPoint(Vector2 contactPoint) {
        colliding = true;
        contactPoints.add(contactPoint);
    }
}
