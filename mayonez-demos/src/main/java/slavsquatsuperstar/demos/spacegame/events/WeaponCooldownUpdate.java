package slavsquatsuperstar.demos.spacegame.events;

import mayonez.event.*;

/**
 * Indicates how long a weapon has until being recharged.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponCooldownUpdate extends Event {

    private final int weaponIndex;
    private final float rechargePercent;

    public WeaponCooldownUpdate(int weaponIndex, float rechargePercent) {
        super("Recharge progress for slot %d: %.2f%%".formatted(weaponIndex, rechargePercent * 100f));
        this.weaponIndex = weaponIndex;
        this.rechargePercent = rechargePercent;
    }

    public int getWeaponIndex() {
        return weaponIndex;
    }

    public float getRechargePercent() {
        return rechargePercent;
    }

}
