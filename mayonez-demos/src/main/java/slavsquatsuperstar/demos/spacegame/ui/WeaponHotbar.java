package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * Displays all weapons available to the player and highlights the currently selected weapon.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponHotbar extends Script {

    // Constants
    private static final Texture BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/hotbar_border.png");
    private static final float BORDER_MARGIN = 10f;

    // Fields
    private final Vec2 position, size;
    private final int numWeapons;

    // UI Elements
    private final WeaponHotbarSlot[] hotbarSlots;
    private UIBox selectedBorder;

    public WeaponHotbar(Vec2 position, Vec2 size, int numWeapons) {
        this.position = position;
        this.size = size;
        this.numWeapons = numWeapons;
        hotbarSlots = new WeaponHotbarSlot[numWeapons];
    }

    @Override
    public void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        // Create hotbar slots
        var boxSpacing = new Vec2(50, 0);
        for (int i = 0; i < numWeapons; i++) {
            hotbarSlots[i] = new WeaponHotbarSlot(position.add(boxSpacing.mul(i)), size, i);
            gameObject.addComponent(hotbarSlots[i]);
        }

        // Border over selected hotbar element
        selectedBorder = new UIBox(position, size.add(new Vec2(BORDER_MARGIN)), BORDER_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 3; // display above overlay
            }
        };
        gameObject.addComponent(selectedBorder);

        setSelection(0);
    }

    // UI Helper Methods

    /**
     * Sets the selected element of the weapon panel and display it as highlighted.
     *
     * @param index the index to select
     */
    public void setSelection(int index) {
        if (!IntMath.inRange(index, 0, numWeapons - 1)) return;
        // Move border to selected slot
        selectedBorder.setPosition(hotbarSlots[index].getPosition());
    }

    /**
     * Sets the fill value of the given weapon cooldown overlay, from bottom to top.
     *
     * @param weaponIndex     the index of the weapon
     * @param cooldownPercent the percent cooldown to display
     */
    public void setCooldownPercent(int weaponIndex, float cooldownPercent) {
        if (!IntMath.inRange(weaponIndex, 0, numWeapons - 1)) return;
        // Clamp percent between 0%-100%
        var clamped = FloatMath.clamp(cooldownPercent, 0f, 1f);
        hotbarSlots[weaponIndex].setCooldownPercent(clamped);
    }

    /**
     * Sets the fill value for all the weapon cooldown overlays, from bottom to top.
     *
     * @param cooldownPercent the percent cooldown to display
     */
    public void setAllCooldownPercents(float cooldownPercent) {
        for (int i = 0; i < numWeapons; i++) setCooldownPercent(i, cooldownPercent);
    }

}
