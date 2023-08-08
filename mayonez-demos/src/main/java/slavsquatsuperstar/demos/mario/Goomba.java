package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
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
        addComponent(sprite);
        addComponent(new BoxCollider(new Vec2(0.8f, 1)));
        addComponent(new Rigidbody(1f, 0.5f, 0f).setFixedRotation(true));
        var sceneMin = getScene().getSize().mul(-0.5f).add(new Vec2(0, 4));
        var sceneMax = getScene().getSize().mul(0.5f);
        addComponent(new KeepInScene(sceneMin, sceneMax, KeepInScene.Mode.STOP));
        addComponent(new Script() {
            @Override
            public void onCollisionEnter(GameObject other, Vec2 direction) {
                if (other.getName().equals("Mario")) gameObject.destroy();
            }
        });
    }
}
