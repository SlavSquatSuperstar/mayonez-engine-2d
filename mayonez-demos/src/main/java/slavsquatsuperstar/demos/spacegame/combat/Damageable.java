package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.Projectile;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;

/**
 * Gives a {@link mayonez.GameObject} a health bar that can be damaged by other objects with a {@link Projectile}
 * component. Once health is depleted, the object is destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class Damageable extends Script {

    private final Counter healthPoints;

    public Damageable(float maxHealth) {
        healthPoints = new Counter(0, maxHealth, maxHealth);
    }

    @Override
    protected void update(float dt) {
        if (healthPoints.isAtMin()) onHealthDepleted();
    }

    @Override
    protected void onTriggerEnter(GameObject other) {
        if (other.hasLayer(SpaceGameLayer.PROJECTILES)) {
            var p = other.getComponent(Projectile.class);
            if (p != null && !gameObject.equals(p.getSource())) {
                onObjectDamaged(p.getDamage());
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
        gameObject.destroy();
    }

    // Health Getter Methods

    public float getMaxHealth() {
        return healthPoints.getMax();
    }

    public float getHealth() {
        return healthPoints.getValue();
    }

}
