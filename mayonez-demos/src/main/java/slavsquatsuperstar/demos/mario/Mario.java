package slavsquatsuperstar.demos.mario;

import mayonez.GameObject;
import mayonez.Script;
import mayonez.Transform;
import mayonez.graphics.sprite.Sprite;
import mayonez.input.KeyInput;
import mayonez.math.Vec2;
import mayonez.physics.Rigidbody;
import mayonez.physics.colliders.BoxCollider;
import mayonez.scripts.KeepInScene;
import mayonez.scripts.movement.KeyMovement;
import mayonez.scripts.movement.KeyRotation;
import mayonez.scripts.movement.MoveMode;

/**
 * A controllable Mario character.
 *
 * @author SlavSquatSuperstar
 */
class Mario extends GameObject {

    private final Sprite sprite;

    public Mario(Sprite sprite) {
        super("Mario", Transform.scaleInstance(new Vec2(2)), 1);
        this.sprite = sprite;
    }

    @Override
    protected void init() {
        getScene().getCamera().setSubject(this).setFollowAngle(false).setKeepInScene(true);
        addComponent(sprite);
        addComponent(new BoxCollider(new Vec2(0.8f, 1)));
        addComponent(new Rigidbody(1f).setFixedRotation(true));
        addComponent(new KeepInScene(KeepInScene.Mode.STOP).setEnabled(true));
        addComponent(new KeyMovement(MoveMode.POSITION, 5).setObjectAligned(false));
        addComponent(new KeyRotation(MoveMode.POSITION, 180));
        addComponent(new Script() {
            @Override
            public void update(float dt) {
                transform.scale(new Vec2(1 + 0.05f * KeyInput.getAxis("plus minus")));
            }
        });
    }
}
