package slavsquatsuperstar.demos.physics;

import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.math.Vec2;
import mayonez.physics.dynamics.Rigidbody;

import java.util.List;

/**
 * Debug draws a {@link Rigidbody}'s physics information to the screen.
 *
 * @author SlavSquatSuperstar
 */
class DrawPhysicsInformation extends ShapeSprite {

    private Rigidbody rigidbody;
    private List<Vec2> contacts;

    public DrawPhysicsInformation(Color color, boolean fill) {
        super(color, fill);
    }

    @Override
    protected void start() {
        super.start();
        rigidbody = getSibling(Rigidbody.class);
    }

    @Override
    protected void debugRender() {
        super.debugRender();
        var debugDraw = getScene().getDebugDraw();
        if (!rigidbody.getStatic()) {
            // Draw center, velocity, and direction vector
            debugDraw.drawPoint(rigidbody.getPosition(), Colors.BLACK);
            debugDraw.drawVector(rigidbody.getPosition(), rigidbody.getVelocity().mul(0.1f), Colors.BLACK);
            debugDraw.drawVector(rigidbody.getPosition(), getGlobalTransform().getRight(), getColor());
        }
        if (contacts != null) {
            for (var pt : contacts) {
                getScene().getDebugDraw().drawPoint(pt, Colors.RED);
            }
        }
    }

    void setContacts(List<Vec2> contacts) {
        this.contacts = contacts;
    }

}
