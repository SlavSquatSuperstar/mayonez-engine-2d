package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.CollisionEvent;
import mayonez.physics.CollisionEventType;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

/**
 * A controllable Mario character.
 *
 * @author SlavSquatSuperstar
 */
class Mario extends GameObject {

    Mario(Vec2 position) {
        super("Mario", new Transform(position, 0f, new Vec2(2f)));
    }

    @Override
    protected void init() {
        addTag(MarioScene.CHARACTERS_TAG);
        getScene().getCamera().setSubject(this);

        var controller = new MarioController();
        addComponent(controller);

        var sprite = MarioScene.SPRITES.getSprite(0);
        sprite.setZIndex(1);
        addComponent(sprite);
        addComponent(new BoxCollider(new Vec2(0.8f, 1)) {
            @Override
            public void onCollisionEvent(CollisionEvent event) {
                if (!event.trigger
                        && event.other.getParent().hasTag(MarioScene.GROUND_TAG)) {
                    if (event.type == CollisionEventType.ENTER
                            && event.direction.dot(new Vec2(0, -1)) > 0) {
                        // Direction is downward
                        controller.onTouchGround();
                    } else if (event.type == CollisionEventType.EXIT) {
                        controller.onLeaveGround();
                    }
                }
            }
        });
        addComponent(new Rigidbody(1f, 0.1f, 0f).setFixedRotation(true));

        var sceneMin = MarioScene.SCENE_HALF_SIZE.mul(-1f).add(new Vec2(0, 4));
        addComponent(new KeepInScene(sceneMin, MarioScene.SCENE_HALF_SIZE, KeepInScene.Mode.STOP));
    }

}
