package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;

/**
 * Gives a {@link mayonez.GameObject} a health bar that can be damaged by other objects with a {@link Projectile} component.
 * Once health is depleted, the object is destroyed.
 *
 * @author SlavSquatSuperstar
 */
public class Damageable extends Script {

    private final Counter healthPoints;

    public Damageable(float maxHealth) {
        healthPoints = new Counter(0, maxHealth, maxHealth);
    }

    @Override
    public void update(float dt) {
        if (healthPoints.isAtMin()) onHealthDepleted();
    }

    @Override
    public void onTriggerEnter(GameObject other) {
        if (other.hasLayer(SpaceGameLayer.PROJECTILES)) {
            var p = other.getComponent(Projectile.class);
            if (p != null && !gameObject.equals(p.getSource())) {
                damage(p.getDamage());
            }
        }
    }

    // Health Methods

    public void damage(float damage) {
        healthPoints.count(-damage);
    }

    public void heal(float healing) {
        healthPoints.count(healing);
    }

    public float getMaxHealth() {
        return healthPoints.getMax();
    }

    public float getHealth() {
        return healthPoints.getValue();
    }

    public void setHealth(float health) {
        healthPoints.setValue(health);
    }

    public void onHealthDepleted() {
        gameObject.destroy();
    }

}
