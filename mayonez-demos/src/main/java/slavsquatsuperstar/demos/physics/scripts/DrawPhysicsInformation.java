package slavsquatsuperstar.demos.physics.scripts;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.physics.colliders.*;
import mayonez.util.*;

/**
 * Debug draws a game object's physics information
 *
 * @author SlavSquatSuperstar
 */
public class DrawPhysicsInformation extends Script {

    private Collider collider;
    private ShapeSprite sprite;

    @Override
    public void start() {
        collider = getCollider();
        sprite = gameObject.getComponent(ShapeSprite.class);
    }

    @Override
    public void debugRender() {
        if (collider != null && sprite != null) {
            drawDebugInformation(collider, sprite.getColor());
        }
    }

    private void drawDebugInformation(Collider col, Color color) {
        var rb = col.getRigidbody();
        if (color != null && rb != null) {
            // Draw center, velocity, direction vector
            getScene().getDebugDraw().drawPoint(col.center(), Colors.BLACK);
            getScene().getDebugDraw().drawVector(col.center(), rb.getVelocity().mul(0.1f), color);
            getScene().getDebugDraw().drawVector(rb.getPosition(), transform.getRight(), Colors.BLACK);
        }
    }


}
