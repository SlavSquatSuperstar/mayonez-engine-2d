package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

/**
 * A Goomba enemy that Mario can destroy.
 *
 * @author SlavSquatSuperstar
 */
class Goomba extends GameObject {

    private final Sprite sprite;

    public Goomba(String name, Sprite sprite, Vec2 position) {
        super(name, new Transform(position, 0, new Vec2(1.5f)));
        this.sprite = sprite;
    }

    @Override
    protected void init() {
        setLayer(getScene().getLayer(MarioScene.CHARACTER_LAYER));

        addComponent(sprite);
        addComponent(new BoxCollider(new Vec2(0.8f, 1)));
        addComponent(new Rigidbody(1f, 0.5f, 0f).setFixedRotation(true));
        var sceneHalfSize = getScene().getHalfSize();
        var sceneMin = sceneHalfSize.mul(-1f).add(new Vec2(0, 4));
        addComponent(new KeepInScene(sceneMin, sceneHalfSize, KeepInScene.Mode.STOP));
        addComponent(new Script() {
            @Override
            protected void onCollisionEnter(GameObject other, Vec2 direction, Vec2 velocity) {
                if (other.getName().equals("Mario")) gameObject.destroy();
            }
        });
    }
}
