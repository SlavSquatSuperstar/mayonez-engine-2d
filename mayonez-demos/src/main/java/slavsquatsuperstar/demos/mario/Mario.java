package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.*;

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
        addComponent(new Rigidbody(1f, 0.1f, 0f).setFixedRotation(true));
        addComponent(new KeepInScene(KeepInScene.Mode.STOP));
        addComponent(new MarioController());
    }
}
