package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.debug.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;

/**
 * Controls the target's position, rotation, and size.
 *
 * @author SlavSquatSupertar
 */
class TargetController extends Script {

    private final GameObject targetBox;
    private Counter flashCounter;
    private ShapeSprite shapeSprite;

    TargetController(GameObject targetBox) {
        this.targetBox = targetBox;
    }

    @Override
    protected void start() {
        flashCounter = new Counter(0, 10, 10);
        shapeSprite = targetBox.getComponent(ShapeSprite.class);
        targetBox.getComponent(Collider.class)
                .addCollisionCallback(event -> {
                    if (event.type == CollisionEventType.ENTER)
                        flashCounter.resetToMin();
                });
    }

    @Override
    protected void update(float dt) {
        var yInput = KeyInput.getAxis("arrows vertical");
        targetBox.transform.move(new Vec2(0f, 20f * yInput * dt));

        var xInput = KeyInput.getAxis("arrows horizontal");
        targetBox.transform.rotate(-90f * xInput * dt);

        var x2Input = KeyInput.getAxis(new KeyAxis(Key.MINUS, Key.PLUS));
        targetBox.transform.scale(new Vec2(1f + 0.5f * x2Input * dt));

        // Flash red when hit
        flashCounter.count(1);
        if (!flashCounter.isAtMax()) shapeSprite.setColor(Colors.RED);
        else shapeSprite.setColor(Colors.DARK_GRAY);
    }

}
