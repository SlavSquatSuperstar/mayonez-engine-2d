package slavsquatsuperstar.demos.spacegame.scripts;

import mayonez.GameObject;
import mayonez.Script;
import mayonez.scripts.Counter;

/**
 * Gives a {@link GameObject} a health bar that can be damaged by other objects with a {@link Projectile} component.
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
    public void start() {
        gameObject.addTag("Damageable");
    }

    @Override
    public void update(float dt) {
        if (healthPoints.isAtMin()) gameObject.setDestroyed();
    }

    @Override
    public void onTriggerEnter(GameObject other) {
        if (other.hasTag("Projectile")) {
            Projectile p = other.getComponent(Projectile.class);
            if (p != null) damage(p.getDamage());
        }
    }

    // Health Methods

    public float getHealth() {
        return healthPoints.getValue();
    }

    public void setHealth(float health) {
        healthPoints.setValue(health);
    }

    public void damage(float damage) {
        healthPoints.count(-damage);
    }

    public void heal(float healing) {
        healthPoints.count(healing);
    }

}
