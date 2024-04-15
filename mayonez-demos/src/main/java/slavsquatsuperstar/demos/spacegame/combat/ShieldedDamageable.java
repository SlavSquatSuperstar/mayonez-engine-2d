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

    // Constants
    private static final float SHIELD_REGEN_RATE = 0.5f;

    // Fields
    private final Counter shieldPoints;
    private boolean regenShield;

    public ShieldedDamageable(float maxHealth, float maxShield) {
        super(maxHealth);
        shieldPoints = new Counter(0, maxShield, maxShield);
    }

    @Override
    protected void start() {
        regenShield = true;
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

    @Override
    protected void update(float dt) {
        super.update(dt);

        // Regenerate shield
        if (regenShield && !shieldPoints.isAtMax()) {
            shieldPoints.count(SHIELD_REGEN_RATE * dt);
            shieldPoints.clampValue();
        }
    }

    // Damage Callback Methods

    @Override
    public void onObjectDamaged(float damage) {
        if (isShieldDestroyed()) {
            // Shield is destroyed, health absorbs all damage
            super.onObjectDamaged(damage);
        } else {
            var shieldHealthLeft = shieldPoints.getValue();
            if (damage < shieldHealthLeft) {
                // Shield absorbs all damage
                shieldPoints.count(-damage);
            } else {
                // Shield absorbs some damage; damage health for remaining
                shieldPoints.setValue(0f);
                super.onObjectDamaged(damage - shieldHealthLeft);
            }
        }
    }

    @Override
    public void onHealthDepleted() {
        regenShield = false;
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
