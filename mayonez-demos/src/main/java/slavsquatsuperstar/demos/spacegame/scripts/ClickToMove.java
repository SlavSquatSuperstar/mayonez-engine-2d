package slavsquatsuperstar.demos.spacegame.scripts;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.input.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.physics.*;
import mayonez.scripts.movement.*;

/**
 * Moves an object towards a location set by mouse pointer.
 *
 * @author SlavSquatSuperstar
 */
@ExperimentalFeature
public class ClickToMove extends Script {

    // Component Fields
    private Rigidbody rb;

    // Mouse Fields
    private final String button;

    // Movement Fields
    private final boolean rotate;
    private final float speed;
    private final MoveMode mode;
    private Vec2 destPos, lastPos, moveDir;
    private float destAngle, lastAngle, turnDir;
    private boolean moving, turning;

    public ClickToMove(float speed, MoveMode mode, boolean rotate) {
        this.speed = speed;
        this.mode = mode;
        this.rotate = rotate;
        this.button = "left mouse";

        lastPos = new Vec2();
        destPos = new Vec2();
    }

    @Override
    public void init() {
        lastPos.set(transform.getPosition());
        destPos.set(lastPos);
        moving = turning = false;
    }

    @Override
    public void start() {
        rb = getRigidbody();
    }

    // TODO will break with KeepInScene
    // TODO will break if has velocity
    @Override
    public void update(float dt) {
        if (MouseInput.buttonPressed(button)) {
            updateLastPosition(); // Save old destination
            setDestination(MouseInput.getPosition()); // Set new destination and calculate displacement
            moving = turning = true;
        }

//        getScene().getDebugDraw().drawLine(lastPos, destPos, Colors.RED);
//        getScene().getDebugDraw().drawVector(transform.getPosition(), transform.getUp(), Colors.LIGHT_GREEN);

        if (moving) moveToDestination(dt);
        if (turning) turnToDestination(dt);
    }

    // Move Methods

    private void moveToDestination(float dt) {
        var moveProgress = new Edge(lastPos, destPos).invLerp(transform.getPosition());
//        System.out.println(moveProgress);

        switch (mode) {
            case POSITION -> {
                if (moveProgress < 1) transform.move(moveDir.mul(speed * dt));
                else {
                    transform.setPosition(new Edge(lastPos, destPos).lerp(1f));
                    moving = false;
                }
            }
            case VELOCITY -> {
                if (moveProgress < 0.5f) rb.addVelocity(moveDir.mul(speed * dt));
                else if (moveProgress < 1f) {
                    rb.addVelocity(moveDir.mul(-speed * dt));
                    if (rb.getVelocity().dot(moveDir) < 0) { // stop it from pendulum-ing
                        rb.setVelocity(new Vec2());
                        moving = false;
                    }
                } else {
                    rb.setVelocity(new Vec2());
                    moving = false;
                }
            }
        }
    }

    private void turnToDestination(float dt) {
        var turnProgress = new Interval(destAngle, lastAngle).invLerp(transform.getRotation()) * turnDir;
        if (turnDir < 0) turnProgress += 1f; // if decreasing

        switch (mode) {
            case POSITION -> {
                if (turnProgress < 1) transform.rotate(turnDir * speed * 10 * dt);
                else turning = false;
            }
            case VELOCITY -> {
                if (turnProgress < 0.5f) rb.addAngularVelocity(turnDir * speed * 10 * dt);
                else if (turnProgress < 1f) {
                    rb.addAngularVelocity(-turnDir * speed * 10 * dt);
                    if (rb.getAngVelocity() * turnDir < 0) { // stop it from pendulum-ing
                        rb.setAngVelocity(0);
                        turning = false;
                    }
                } else {
                    rb.setAngVelocity(0f);
                    turning = false;
                }
            }
        }
    }

    // Location Methods

    private void updateLastPosition() {
        lastPos.set(transform.getPosition());
        lastAngle = transform.getRotation(); // use up as 0
    }

    private void setDestination(Vec2 destination) {
        destPos.set(destination);
        moveDir = destPos.sub(lastPos).unit();
        destAngle = moveDir.angle() - 90f;
        if (destAngle < -180f) destAngle += 360f; // keep between ±180
        turnDir = Math.signum(destAngle - lastAngle);
    }

}
