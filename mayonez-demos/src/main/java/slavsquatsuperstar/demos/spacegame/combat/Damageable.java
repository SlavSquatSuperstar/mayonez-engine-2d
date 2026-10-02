package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.physics.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.SpaceGameScene;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.Projectile;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;

/**
 * Gives a {@link mayonez.Node} a health bar that can be damaged by other objects with a {@link Projectile}
 * component. Once health is depleted, the object is destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class Damageable extends Node {

    private final Counter healthPoints;

    public Damageable(float maxHealth) {
        healthPoints = new Counter(0, maxHealth, maxHealth);
    }

    @Override
    protected void update(float dt) {
        if (healthPoints.isAtMin()) onHealthDepleted();
    }

    // TODO should listen to collision
    public void onImpactProjectile(CollisionEvent event) {
        if (!event.trigger || event.type != CollisionEventType.ENTER) return;

        if (event.other.getParent() instanceof Projectile proj) {
            if (proj.hasTag(SpaceGameScene.PROJECTILE_TAG)) {
                if (!getParent().equals(proj.getSource())) {
                    onObjectDamaged(proj.getDamage());
                }
            }
        }
    }

    // Damage Callback Methods

    /**
     * Behavior for when this object takes damage from any source.
     *
     * @param damage the hit points of damage
     */
    public void onObjectDamaged(float damage) {
        healthPoints.count(-damage);
    }

    /**
     * Behavior for when this object's health reaches zero. Destroys the object
     * by default.
     */
    public void onHealthDepleted() {
        getParent().setDestroyed();
    }

    // Health Getter Methods

    public float getMaxHealth() {
        return healthPoints.getMax();
    }

    public float getHealth() {
        return healthPoints.getValue();
    }

}
