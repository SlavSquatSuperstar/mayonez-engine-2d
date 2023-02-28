package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
import mayonez.physics.colliders.*;
import mayonez.scripts.movement.*;

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
        addComponent(new Rigidbody(1f).setFixedRotation(true));
        addComponent(new DragAndDrop("left mouse"));
        addComponent(new Script() {
            @Override
            public void onCollisionStay(GameObject other) {
                if (other.getName().equals("Mario")) setDestroyed();
            }
        });
    }
}
