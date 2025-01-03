package slavsquatsuperstar.demos.spacegame.events;

/**
 * Indicates the player has selected a new weapon from the hotbar.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponSelectedEvent extends SpaceGameEvent {

    private final int weaponIndex;

    public WeaponSelectedEvent(int weaponIndex) {
        super("Weapon selected: " + weaponIndex);
        this.weaponIndex = weaponIndex;
    }

    public int getWeaponIndex() {
        return weaponIndex;
    }

}
