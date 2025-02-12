package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.math.*;

/**
 * Make a particle follow a target object until the target is destroyed.
 *
 * @author SlavSquatSuperstar
 */
class ParticleFollowTarget extends Script {

    private final GameObject target;
    private Vec2 targetPositionOffset;
    private float targetRotationOffset;

    ParticleFollowTarget(GameObject target) {
        this.target = target;
    }

    @Override
    protected void start() {
        targetPositionOffset = transform.getPosition()
                .sub(target.transform.getPosition());
        targetRotationOffset = transform.getRotation()
                - target.transform.getRotation();
    }

    @Override
    protected void debugRender() {
        // Destroy of target destroyed
        if (target.isDestroyed()) {
            gameObject.destroy();
            return;
        }

        // Follow target
        this.transform.setPosition(target.transform.getPosition()
                .add(targetPositionOffset));
        this.transform.setRotation(target.transform.getRotation()
                + (targetRotationOffset));
    }

}
