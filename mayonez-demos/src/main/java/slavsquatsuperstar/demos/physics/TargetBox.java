package slavsquatsuperstar.demos.physics;

import mayonez.GameObject;
import mayonez.Script;
import mayonez.Transform;
import mayonez.graphics.Colors;
import mayonez.graphics.debug.ShapeSprite;
import mayonez.input.Key;
import mayonez.input.KeyAxis;
import mayonez.input.KeyInput;
import mayonez.math.Vec2;
import mayonez.physics.CollisionEvent;
import mayonez.physics.CollisionEventType;
import mayonez.physics.colliders.BoxCollider;
import mayonez.physics.dynamics.PhysicsMaterial;
import mayonez.physics.dynamics.Rigidbody;
import mayonez.scripts.Counter;

import static slavsquatsuperstar.demos.physics.ProjectileTestScene.TARGET_LAYER;

/**
 * A target box with controllable position, rotation, and size.
 *
 * @author SlavSquatSupertar
 */
public class TargetBox extends GameObject {

    private Counter flashCounter;
    private ShapeSprite shapeSprite;

    public TargetBox() {
        super("Target Box", new Transform(new Vec2(50f, 0f), 0f));
    }

    @Override
    protected void init() {
        flashCounter = new Counter(0, 10, 10);
        setLayer(getScene().getLayer(TARGET_LAYER));

        addComponent(new DrawPhysicsInformation());
        addComponent(new Rigidbody(0f).setMaterial(PhysicsMaterial.DEFAULT_MATERIAL));
        addComponent(new BoxCollider(new Vec2(10f, 12f)) {
            @Override
            public void onCollisionEvent(CollisionEvent event) {
                if (event.type == CollisionEventType.ENTER) {
                    resetFlashCounter();
                }
            }
        });
        addComponent(shapeSprite = new ShapeSprite(Colors.DARK_GRAY, true));

        addComponent(new Script() {
            @Override
            protected void update(float dt) {
                var yInput = KeyInput.getAxis("arrows vertical");
                transform.move(new Vec2(0f, 20f * yInput * dt));

                var xInput = KeyInput.getAxis("arrows horizontal");
                transform.rotate(-90f * xInput * dt);

                var x2Input = KeyInput.getAxis(new KeyAxis(Key.MINUS, Key.PLUS));
                transform.scale(new Vec2(1f + 0.5f * x2Input * dt));

                // Flash red when hit
                flashCounter.count(1);
                if (!flashCounter.isAtMax()) shapeSprite.setColor(Colors.RED);
                else shapeSprite.setColor(Colors.DARK_GRAY);
            }
        });
    }

    private void resetFlashCounter() {
        flashCounter.resetToMin();
    }

}
