package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.math.*;

/**
 * Make a particle follow a target object until the target is destroyed.
 *
 * @author SlavSquatSuperstar
 */
class ParticleFollowTarget extends Script {

    private final Node target;
    private Vec2 targetPositionOffset;
    private float targetRotationOffset;

    ParticleFollowTarget(Node target) {
        this.target = target;
    }

    @Override
    protected void start() {
        targetPositionOffset = getTransform().getPosition()
                .sub(target.getTransform().getPosition());
        targetRotationOffset = getTransform().getRotation()
                - target.getTransform().getRotation();
    }

    @Override
    protected void update(float dt) {
        // Destroy if target destroyed
        if (target.isDestroyed()) {
            getParent().setDestroyed();
            return;
        }

        // Follow target
        this.getTransform().setPosition(target.getTransform().getPosition()
                .add(targetPositionOffset));
        this.getTransform().setRotation(target.getTransform().getRotation()
                + (targetRotationOffset));
    }

}
