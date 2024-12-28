package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;
import mayonez.physics.*;
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
        getScene().getCamera().setSubject(this);

        var script = new MarioController();
        addComponent(script);

        addComponent(sprite);
        addComponent(new BoxCollider(new Vec2(0.8f, 1)) {
            @Override
            public void onCollisionEvent(CollisionEvent event) {
                // On collision
                if (!event.trigger
                        && event.other.hasLayer(MarioScene.GROUND_LAYER)) {
                    if (event.type == CollisionEventType.ENTER
                            && event.direction.dot(new Vec2(0, -1)) > 0) {
                        // Direction is downward
                        script.onTouchGround();
                    } else if (event.type == CollisionEventType.EXIT) {
                        script.onLeaveGround();
                    }
                }
            }
        });
        addComponent(new Rigidbody(1f, 0.1f, 0f).setFixedRotation(true));

        var sceneHalfSize = getScene().getHalfSize();
        var sceneMin = sceneHalfSize.mul(-1f).add(new Vec2(0, 4));
        addComponent(new KeepInScene(sceneMin, sceneHalfSize, KeepInScene.Mode.STOP));

    }
}
