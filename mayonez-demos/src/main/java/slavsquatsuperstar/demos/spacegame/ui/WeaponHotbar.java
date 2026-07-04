package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectileType;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

import java.util.*;

/**
 * Displays all weapons available to the player and highlights the currently selected weapon.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponHotbar extends BoxContainer {

    // Constants
    private static final Texture SELECTED_BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/black_border.png");
    private static final float BORDER_MARGIN = 8f;

    // Fields
    private Vec2 slotSize;
    private final int numSlots;

    // UI Elements
    private final List<ProjectileType> loadout;
    private UISprite selectedBorder;

    public WeaponHotbar(Vec2 position, Vec2 slotSize, int spacing, List<ProjectileType> loadout) {
        super(position, spacing, true);
        this.slotSize = slotSize;
        this.loadout = loadout;
        this.numSlots = loadout.size();
    }

    @Override
    protected void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        // Create hotbar slots
        // TODO recreate on loadout change
        for (int i = 0; i < numSlots; i++) {
            var hotbarSlot = new WeaponHotbarSlot(position, slotSize, loadout.get(i));
            addElement(hotbarSlot);
        }

        // Border over selected hotbar element
        selectedBorder = new UISprite(position, slotSize.add(new Vec2(BORDER_MARGIN)),
                SELECTED_BORDER_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 3; // Display above overlay
            }
        };
        gameObject.addComponent(selectedBorder);
        setSelection(0);
    }

    // Hotbar Methods

    /**
     * Sets the selected element of the weapon panel and display it as highlighted.
     *
     * @param index the index to select
     */
    public void setSelection(int index) {
        if (!MathUtils.inRange(index, 0, numSlots - 1)) {
            // Hide border if out of bounds
            selectedBorder.setVisible(false);
        } else {
            // Move border to selected slot
            selectedBorder.setVisible(true);
            selectedBorder.setPosition(getElement(index).getPosition());
        }
    }

    /**
     * Sets the fill value of the given weapon cooldown overlay, from bottom to top.
     *
     * @param weaponIndex     the index of the weapon
     * @param cooldownPercent the percent cooldown to display
     */
    public void setCooldownPercent(int weaponIndex, float cooldownPercent) {
        if (!MathUtils.inRange(weaponIndex, 0, numSlots - 1)) return;
        // Clamp percent between 0%-100%
        var clamped = MathUtils.clamp(cooldownPercent, 0f, 1f);
        ((WeaponHotbarSlot) getElement(weaponIndex)).setCooldownPercent(clamped);
    }

    /**
     * Sets the fill value for all the weapon cooldown overlays, from bottom to top.
     *
     * @param cooldownPercent the percent cooldown to display
     */
    public void setAllCooldownPercents(float cooldownPercent) {
        for (int i = 0; i < numSlots; i++) setCooldownPercent(i, cooldownPercent);
    }

    // UI Container Methods

    @Override
    protected void arrangeElements() {
        // Adjust slot positions and sizes
        var slotOffset = slotSize.x + spacing;
        for (int i = 0; i < numSlots; i++) {
            getElement(i).setSize(slotSize.add(new Vec2(0)));
            getElement(i).setPosition(new Vec2(position.x + slotOffset * i, position.y));
        }
        selectedBorder.setSize(slotSize.add(new Vec2(BORDER_MARGIN)));
    }

    // UI Container Methods

    @Override
    public Vec2 getSize() {
        var hotbarWidth = slotSize.x * numSlots + spacing * (numSlots - 1);
        var hotbarHeight = slotSize.y;
        return new Vec2(hotbarWidth, hotbarHeight);
    }

    @Override
    public void setVertical(boolean vertical) {
        // Do nothing
    }

    /**
     * Gets the size of an individual hotbar slot.
     *
     * @return the slot size
     */
    public Vec2 getSlotSize() {
        return slotSize;
    }

    /**
     * Sets the size of an individual hotbar slot and readjusts all element positions.
     *
     * @param slotSize the slot size
     */
    public void setSlotSize(Vec2 slotSize) {
        this.slotSize = slotSize;
        arrangeElements();
    }

}
