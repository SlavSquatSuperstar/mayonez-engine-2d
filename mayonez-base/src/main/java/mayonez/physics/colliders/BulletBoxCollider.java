package mayonez.physics.colliders;

import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.physics.dynamics.*;

/**
 * A box collider for fast moving objects that automatically extends itself along its path
 * to avoid tunneling.
 *
 * @author SlavSquatSuperstar
 */
public class BulletBoxCollider extends BoxCollider {

    private Vec2 displacement;
    private PhysicsBody rb;
    private boolean primaryAxisX; // Whether 0º is at +x or +y

    public BulletBoxCollider(Vec2 size) {
        super(size);
        primaryAxisX = true;
    }

    @Override
    protected void start() {
        super.start();
        displacement = new Vec2();
        rb = getPhysicsBody();
        if (rb == null) setEnabled(false);
    }

    @Override
    protected void update(float dt) {
        // Assume velocity direction is along object orientation
        // Assume no rotational velocity
        displacement.set(rb.getVelocity().mul(dt));
    }

    @Override
    public Shape getShape() {
        var addedSize = primaryAxisX
                ? new Vec2(displacement.len(), 0f)
                : new Vec2(0f, displacement.len());

        // New center is midpoint
        // New length is length + displacement
        return new Rectangle(
                transform.getPosition().add(displacement.mul(0.5f)),
                this.getSize().mul(transform.getScale()).add(addedSize),
                transform.getRotation()
        );
    }

//    @Override
//    protected void debugRender() {
//        var originalShape = super.getShape();
//        var nextShape = originalShape.translate(displacement);
//        var sweptShape = this.getShape();
//
//        getScene().getDebugDraw().drawShape(originalShape, Colors.BLUE);
//        getScene().getDebugDraw().drawShape(nextShape, Colors.RED);
//        getScene().getDebugDraw().drawShape(sweptShape, Colors.LIGHT_GREEN);
//    }

    // Setter Methods

    public void setPrimaryAxisX(boolean primaryAxisX) {
        this.primaryAxisX = primaryAxisX;
    }

}
