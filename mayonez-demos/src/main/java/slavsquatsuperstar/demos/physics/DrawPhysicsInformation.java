package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.Rigidbody;

/**
 * Debug draws a {@link Rigidbody}'s physics information to the screen.
 *
 * @author SlavSquatSuperstar
 */
class DrawPhysicsInformation extends ShapeSprite {

    private Rigidbody rigidbody;

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
            debugDraw.drawVector(rigidbody.getPosition(), rigidbody.getVelocity().mul(0.1f), getColor());
            debugDraw.drawVector(rigidbody.getPosition(), getGlobalTransform().getRight(), Colors.BLACK);
        }
    }


}
