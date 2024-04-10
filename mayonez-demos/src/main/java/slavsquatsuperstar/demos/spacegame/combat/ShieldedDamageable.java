package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.scripts.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.Projectile;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameLayer;

/**
 * Gives a {@link mayonez.GameObject} a rechargeable shield on top of a health bar.
 *
 * @author SlavSquatSuperstar
 */
public class ShieldedDamageable extends Damageable {

    private final Counter shieldPoints;
    // TODO recharge shield

    public ShieldedDamageable(float maxHealth, float maxShield) {
        super(maxHealth);
        shieldPoints = new Counter(0, maxShield, maxShield);
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

    @Override
    public void onObjectDamaged(float damage) {
        if (isShieldDestroyed()) {
            super.onObjectDamaged(damage);
        } else {
            shieldPoints.count(-damage);
        }
    }

    // Shield Getter Methods

    public float getMaxShield() {
        return shieldPoints.getMax();
    }

    public float getShield() {
        return shieldPoints.getValue();
    }

    public boolean isShieldDestroyed() {
        return shieldPoints.isAtMin();
    }

}
