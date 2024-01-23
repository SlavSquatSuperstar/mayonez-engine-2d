package slavsquatsuperstar.demos.spacegame.movement;

import mayonez.*;
import mayonez.math.*;
import mayonez.physics.dynamics.*;

public class EnemyMovement extends Script {

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
