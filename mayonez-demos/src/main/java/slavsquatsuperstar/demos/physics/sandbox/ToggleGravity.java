package slavsquatsuperstar.demos.physics.sandbox;

import mayonez.*;
import mayonez.graphics.font.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;

/**
 * Toggles gravity within the scene.
 *
 * @author SlavSquatSupertar
 */
class ToggleGravity extends Script {

    private final TextLabel gravityText;
    private boolean enabledGravity;

    ToggleGravity(TextLabel gravityText) {
        this.gravityText = gravityText;
    }

    @Override
    protected void start() {
        setGravityEnabled(true);
    }

    @Override
    protected void update(float dt) {
        if (KeyInput.keyPressed("space")) {
            setGravityEnabled(!enabledGravity);
        }
    }

    private void setGravityEnabled(boolean enabled) {
        enabledGravity = enabled;
        if (enabledGravity) {
            getScene().setGravity(new Vec2(-0, -PhysicsWorld.GRAVITY_CONSTANT));
            gravityText.setMessage("Gravity: On");
        } else {
            getScene().setGravity(new Vec2());
            gravityText.setMessage("Gravity: Off");
        }
    }

}
