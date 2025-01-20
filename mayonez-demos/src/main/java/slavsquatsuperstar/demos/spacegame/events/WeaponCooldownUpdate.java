package slavsquatsuperstar.demos.spacegame.events;

/**
 * Indicates how long a weapon has until its cooldown is complete.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponCooldownUpdate extends SpaceGameEvent {

    private final int weaponIndex;
    private final float cooldownPercent;

    public WeaponCooldownUpdate(int weaponIndex, float cooldownPercent) {
        super("Slot %d recharge progress: %.2f%%"
                .formatted(weaponIndex, cooldownPercent * 100f));
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
