package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.annotations.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;
import mayonez.scripts.movement.*;

@ExperimentalFeature
public class EnemyMovement extends MovementScript {

    private final float speed;
    private Rigidbody rb;

    public EnemyMovement(float speed) {
        super(UpdateOrder.PHYSICS);
        this.speed = speed;
    }

    @Override
    protected void start() {
        rb = getRigidbody();
        if (rb == null) setEnabled(false);
    }

    @Override
    protected void update(float dt) {
        rb.applyForce(new Vec2().mul(speed));
    }

}
