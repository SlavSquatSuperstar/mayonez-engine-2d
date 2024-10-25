package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
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
        setLayer(getScene().getLayer(MarioScene.CHARACTER_LAYER));

        getScene().getCamera().setSubject(this).setKeepInScene(true);
        addComponent(sprite);
        addComponent(new BoxCollider(new Vec2(0.8f, 1)));
        addComponent(new Rigidbody(1f, 0.1f, 0f).setFixedRotation(true));
        var sceneMin = getScene().getSize().mul(-0.5f).add(new Vec2(0, 4));
        var sceneMax = getScene().getSize().mul(0.5f);
        addComponent(new KeepInScene(sceneMin, sceneMax, KeepInScene.Mode.STOP));
        addComponent(new MarioController());
    }
}
