package slavsquatsuperstar.demos.mario;

import mayonez.*;
import mayonez.input.KeyInput;
import mayonez.math.*;
import mayonez.math.shapes.Rectangle;
import mayonez.physics.CollisionEvent;
import mayonez.physics.CollisionEventType;
import mayonez.physics.colliders.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.*;

/**
 * A platformer character that can perform move, jump, and ground pound
 * actions.
 *
 * @author SlavSquatSuperstar
 */
class Mario extends Node {

    // Constants
    private static final float MOVE_SPEED = 15;
    private static final float JUMP_SPEED = 35;
    private static final float SLAM_SPEED = 5;

    // Fields
    private boolean onGround;
    private Rigidbody rb;

    Mario(Vec2 position) {
        super("Mario", new Transform(position, 0f, new Vec2(2f)));
    }

    @Override
    protected void init() {
        setUpdateOrder(UpdateOrder.INPUT);
        onGround = false;

        addTag(MarioScene.CHARACTERS_TAG);
        getScene().getCamera().setSubject(this);

        var sprite = MarioScene.SPRITES.getSprite(0);
        sprite.setZIndex(1);
        addChild(sprite);
        addChild(new Collider(new Rectangle(new Vec2(0.8f, 1))) {
            @Override
            public void onCollisionEvent(CollisionEvent event) {
                if (!event.trigger
                        && event.other.getParent().hasTag(MarioScene.GROUND_TAG)) {
                    if (event.type == CollisionEventType.ENTER
                            && event.direction.dot(new Vec2(0, -1)) > 0) {
                        // Direction is downward
                        onTouchGround();
                    } else if (event.type == CollisionEventType.EXIT) {
                        onLeaveGround();
                    }
                }
            }
        });
        addChild(rb = new Rigidbody(1f, 0.1f, 0f).setFixedRotation(true));

        var sceneMin = MarioScene.SCENE_HALF_SIZE.mul(-1f).add(new Vec2(0, 4));
        addChild(new KeepInScene(sceneMin, MarioScene.SCENE_HALF_SIZE, KeepInScene.Mode.STOP));
    }

    @Override
    protected void fixedUpdate(float dt) {
        // Move
        var xInput = KeyInput.getAxis("horizontal");
        getTransform().move(new Vec2(xInput * MOVE_SPEED * dt, 0));
        rb.getVelocity().x = 0;

        // Jump
        if (KeyInput.keyDown("w") && onGround) {
            rb.applyImpulse(new Vec2(0, JUMP_SPEED));
        }

        // Ground pound
        if (KeyInput.keyDown("s") && !onGround) {
            rb.applyImpulse(new Vec2(0, -SLAM_SPEED));
        }
    }

    public void onTouchGround() {
        if (!onGround) {
            onGround = true;
            rb.getVelocity().y = 0;
        }
    }

    public void onLeaveGround() {
        if (onGround) onGround = false;
    }

}
