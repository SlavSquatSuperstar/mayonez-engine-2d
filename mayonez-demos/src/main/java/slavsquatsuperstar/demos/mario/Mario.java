package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;
import mayonez.scripts.movement.*;

/**
 * A controllable Mario character.
 *
 * @author SlavSquatSuperstar
 */
class Mario extends GameObject {

    private final Sprite sprite;

    public Mario(Vec2 position, Sprite sprite) {
        super("Mario", new Transform(position, 0f, new Vec2(2f)), 1);
        this.sprite = sprite;
    }

    @Override
    protected void init() {
        getScene().getCamera().setSubject(this).setFollowAngle(false).setKeepInScene(true);
        addComponent(sprite);
        addComponent(new BoxCollider(new Vec2(0.8f, 1)));
        addComponent(new Rigidbody(1f).setFixedRotation(true));
        addComponent(new KeepInScene(KeepInScene.Mode.STOP).setEnabled(true));
        addComponent(new KeyMovement(MoveMode.POSITION, 15).setObjectAligned(false));
        addComponent(new KeyRotation(MoveMode.POSITION, 360));
        addComponent(new Script() {
            @Override
            public void update(float dt) {
                transform.scale(new Vec2(1 + 0.05f * KeyInput.getAxis("plus minus")));
            }
        });
    }
}
