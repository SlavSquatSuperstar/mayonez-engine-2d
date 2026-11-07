package slavsquatsuperstar.demos.spacegame.combat.projectiles;

import mayonez.*;
import mayonez.math.*;
import mayonez.scripts.DestroyAfterDuration;

/**
 * An impact particle caused by a projectile that follows a target node.
 *
 * @author SlavSquatSuperstar
 */
class ImpactParticle extends Node {

    private final ProjectileType type;
    private final Node target;
    private Vec2 targetPositionOffset;
    private float targetRotationOffset;

    ImpactParticle(Transform transform, ProjectileType type, Node target) {
        super("%s Impact".formatted(type.name()), transform);
        this.type = type;
        this.target = target;
    }

    @Override
    protected void init() {
        addChild(new DestroyAfterDuration(Random.randomFloat(0.1f, 0.4f)));
        addChild(ProjectilePrefabs.PARTICLE_SPRITES.getSprite(type.spriteIndex()));

        targetPositionOffset = getTransform().getPosition()
                .sub(target.getTransform().getPosition());
        targetRotationOffset = getTransform().getRotation()
                - target.getTransform().getRotation();
    }

    @Override
    protected void update(float dt) {
        // Destroy if target destroyed
        if (target.isDestroyed()) {
            setDestroyed();
            return;
        }

        // Follow target
        getTransform().setPosition(target.getTransform().getPosition()
                .add(targetPositionOffset));
        getTransform().setRotation(target.getTransform().getRotation()
                + (targetRotationOffset));
    }

}
