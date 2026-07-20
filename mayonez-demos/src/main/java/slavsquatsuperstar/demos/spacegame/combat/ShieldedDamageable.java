package slavsquatsuperstar.demos.spacegame.combat;

import mayonez.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.scripts.*;

/**
 * Gives a {@link mayonez.GameObject} a rechargeable shield on top of a health bar.
 *
 * @author SlavSquatSuperstar
 */
public class ShieldedDamageable extends Damageable {

    // Constants
    private static final float SHIELD_WAIT_TIME = 1.2f;
    private static final float SHIELD_FLASH_TIME = 0.1f;
    private static final Texture SHIELD_EFFECT_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/combat/shield_effect.png"
    );

    // Fields
    private final Counter shieldPoints;
    private final Timer shieldWaitTimer; // Wait to recharge after getting hit
    private final Timer shieldFlashTimer; // Flash shield when getting hit
    private final float shieldRegen; // Shield hp per second
    private Sprite shieldEffect; // Shield effect sprite

    public ShieldedDamageable(float maxHealth, float maxShield, float shieldRegen) {
        super(maxHealth);
        shieldPoints = new Counter(0, maxShield, maxShield);
        shieldWaitTimer = new Timer(SHIELD_WAIT_TIME);
        shieldFlashTimer = new Timer(SHIELD_FLASH_TIME);
        this.shieldRegen = shieldRegen;
    }

    @Override
    protected void init() {
        shieldEffect = Sprites.createSprite(SHIELD_EFFECT_TEXTURE)
                .setSpriteTransform(Transform.scaleInstance(new Vec2(1.1f)));
        gameObject.addComponent(shieldEffect);
        shieldEffect.setZIndex(gameObject.getZIndex() + 1);
        shieldEffect.setVisible(false);
    }

    @Override
    protected void start() {
        super.start();
        shieldWaitTimer.setValue(0f);
        shieldFlashTimer.setValue(0f);
    }

    @Override
    protected void update(float dt) {
        super.update(dt);

        // Regenerate shield
        if (canRegenShield() && !shieldPoints.isAtMax()) {
            shieldPoints.count(shieldRegen * dt);
            shieldPoints.clampValue();
        } else {
            shieldWaitTimer.countDown(dt);
        }

        // Wait to hide shield effect
        if (shieldFlashTimer.isReady()) {
            shieldEffect.setVisible(false);
        } else {
            shieldFlashTimer.countDown(dt);
        }
    }

    // Damage Callback Methods

    @Override
    public void onObjectDamaged(float damage) {
        shieldWaitTimer.reset();

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

            // Flash shield
            shieldFlashTimer.reset();
            shieldEffect.setVisible(true);
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

    private boolean canRegenShield() {
        return shieldWaitTimer.isReady();
    }

}
