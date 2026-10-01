package slavsquatsuperstar.demos.physics;

import mayonez.*;
import mayonez.graphics.Colors;
import mayonez.graphics.debug.ShapeSprite;
import mayonez.input.*;
import mayonez.math.Vec2;
import mayonez.physics.*;
import mayonez.physics.colliders.BoxCollider;
import mayonez.physics.dynamics.*;
import mayonez.scripts.Counter;

/**
 * A target box with controllable position, rotation, and size.
 *
 * @author SlavSquatSupertar
 */
public class TargetBox extends Node {

    private Counter flashCounter;
    private ShapeSprite shapeSprite;

    public TargetBox() {
        super("Target Box", new Transform(new Vec2(50f, 0f), 0f));
    }

    @Override
    protected void init() {
        flashCounter = new Counter(0, 10, 10);

        addChild(new DrawPhysicsInformation());
        addChild(new Rigidbody(0f).setMaterial(PhysicsMaterial.DEFAULT_MATERIAL));
        addChild(new BoxCollider(new Vec2(10f, 12f)) {
            @Override
            protected void init() {
                setLayer(getScene().getLayer(ProjectileTestScene.TARGET_LAYER));
            }

            @Override
            public void onCollisionEvent(CollisionEvent event) {
                if (event.type == CollisionEventType.ENTER) {
                    resetFlashCounter();
                }
            }
        });
        addChild(shapeSprite = new ShapeSprite(Colors.DARK_GRAY, true));
    }

    @Override
    protected void update(float dt) {
        var yInput = KeyInput.getAxis("arrows vertical");
        getTransform().move(new Vec2(0f, 20f * yInput * dt));

        var xInput = KeyInput.getAxis("arrows horizontal");
        getTransform().rotate(-90f * xInput * dt);

        var x2Input = KeyInput.getAxis(new KeyAxis(Key.MINUS, Key.PLUS));
        getTransform().scale(new Vec2(1f + 0.5f * x2Input * dt));

        // Flash red when hit
        flashCounter.count(1);
        if (!flashCounter.isAtMax()) shapeSprite.setColor(Colors.RED);
        else shapeSprite.setColor(Colors.DARK_GRAY);
    }

    private void resetFlashCounter() {
        flashCounter.resetToMin();
    }

}
