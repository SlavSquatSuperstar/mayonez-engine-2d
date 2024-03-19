package slavsquatsuperstar.demos.spacegame.events;

import mayonez.event.*;

/**
 * Indicates how long a weapon has until being recharged.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponCooldownUpdate extends Event {

    private final float rechargePercent;

    public WeaponCooldownUpdate(float rechargePercent) {
        super("Recharge progress: %.2f%%".formatted(rechargePercent * 100f));
        this.rechargePercent = rechargePercent;
    }

    public float getRechargePercent() {
        return rechargePercent;
    }

}
