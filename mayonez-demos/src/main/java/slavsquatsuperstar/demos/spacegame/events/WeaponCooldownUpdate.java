package slavsquatsuperstar.demos.spacegame.events;

import mayonez.event.*;

/**
 * Indicates how long a weapon has until its cooldown is complete.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponCooldownUpdate extends Event {

    private final int weaponIndex;
    private final float cooldownPercent;

    public WeaponCooldownUpdate(int weaponIndex, float cooldownPercent) {
        super("Recharge progress for slot %d: %.2f%%".formatted(weaponIndex, cooldownPercent * 100f));
        this.weaponIndex = weaponIndex;
        this.cooldownPercent = cooldownPercent;
    }

    public int getWeaponIndex() {
        return weaponIndex;
    }

    public float getCooldownPercent() {
        return cooldownPercent;
    }

}
