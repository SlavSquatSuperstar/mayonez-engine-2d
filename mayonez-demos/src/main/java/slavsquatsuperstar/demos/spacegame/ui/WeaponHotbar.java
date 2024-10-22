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
public class WeaponHotbar extends Script implements UIElement {

    // Constants
    private static final Texture SELECTED_BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/black_border.png");
    private static final float BORDER_MARGIN = 8f;

    // Fields
    private Vec2 position, size; // TODO rename slot size
    private final int numSlots;
    private final float boxSpacing = 16; // TODO get/set

    // UI Elements
    private final WeaponHotbarSlot[] hotbarSlots;
    private UISprite selectedBorder;

    public WeaponHotbar(Vec2 position, Vec2 size, int numSlots) {
        this.position = position;
        this.size = size; // Size of slots
        this.numSlots = numSlots;
        hotbarSlots = new WeaponHotbarSlot[numSlots];
    }

    @Override
    protected void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        // Create hotbar slots
        var boxOffset = size.x + boxSpacing;
        for (int i = 0; i < numSlots; i++) {
            hotbarSlots[i] = new WeaponHotbarSlot(position.add(new Vec2(boxOffset * i, 0)), size, i);
            gameObject.addComponent(hotbarSlots[i]);
        }

        // Border over selected hotbar element
        selectedBorder = new UISprite(position, size.add(new Vec2(BORDER_MARGIN)), SELECTED_BORDER_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 3; // display above overlay
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
        if (!MathUtils.inRange(index, 0, numSlots - 1)) return;
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
        if (!MathUtils.inRange(weaponIndex, 0, numSlots - 1)) return;
        // Clamp percent between 0%-100%
        var clamped = MathUtils.clamp(cooldownPercent, 0f, 1f);
        hotbarSlots[weaponIndex].setCooldownPercent(clamped);
    }

    /**
     * Sets the fill value for all the weapon cooldown overlays, from bottom to top.
     *
     * @param cooldownPercent the percent cooldown to display
     */
    public void setAllCooldownPercents(float cooldownPercent) {
        for (int i = 0; i < numSlots; i++) setCooldownPercent(i, cooldownPercent);
    }

    // UI Element Methods

    @Override
    public Vec2 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vec2 position) {
        this.position = position;

        // Adjust slot positions
        var boxOffset = size.x + boxSpacing;
        for (int i = 0; i < numSlots; i++) {
            hotbarSlots[i].setPosition(position.add(new Vec2(boxOffset * i, 0)));
        }
    }

    @Override
    public Vec2 getSize() {
        return size;
    }

    @Override
    public void setSize(Vec2 size) {
        this.size = size;

        // Adjust slot positions and sizes
        var boxOffset = size.x + boxSpacing;
        for (int i = 0; i < numSlots; i++) {
            hotbarSlots[i].setSize(size);
            hotbarSlots[i].setPosition(position.add(new Vec2(boxOffset * i, 0)));
        }
        selectedBorder.setSize(size.add(new Vec2(BORDER_MARGIN)));
    }

}
