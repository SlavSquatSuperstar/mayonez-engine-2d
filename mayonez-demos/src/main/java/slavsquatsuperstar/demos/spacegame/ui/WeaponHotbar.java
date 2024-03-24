package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * Displays all weapons available to the player and highlights the currently selected weapon.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponHotbar extends Script {

    // Constants
    private static final Texture BACKGROUND_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/hotbar_background.png");
    private static final Texture BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/hotbar_border.png");

    private static final float BACKGROUND_MARGIN = 5f;
    private static final float BORDER_MARGIN = 10f;

    // Fields
    private final Vec2 position, size;
    private final int numWeapons;

    // UI Elements
    private final UIBox[] backgrounds, rechargeOverlays;
    private UIBox selectedBorder;

    public WeaponHotbar(Vec2 position, Vec2 size, int numWeapons) {
        this.position = position;
        this.size = size;
        this.numWeapons = numWeapons;
        backgrounds = new UIBox[numWeapons];
        rechargeOverlays = new UIBox[numWeapons];
    }

    @Override
    public void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        var boxSpacing = new Vec2(50, 0);
        for (int i = 0; i < numWeapons; i++) {
            var currPos = position.add(boxSpacing.mul(i));

            // Background element
            backgrounds[i] = new UIBox(currPos, size.add(new Vec2(BACKGROUND_MARGIN)), BACKGROUND_TEXTURE);
            gameObject.addComponent(backgrounds[i]);

            // Weapon icon over background
            var weaponIcon = new UIBox(currPos, size, ProjectilePrefabs.PROJECTILE_SPRITES.getTexture(i)) {
                @Override
                public int getZIndex() {
                    return super.getZIndex() + 1; // display above background
                }
            };
            gameObject.addComponent(weaponIcon);

            // Recharge overlay over icon
            var rechargeOverlay = new UIBox(currPos, size.add(new Vec2(BACKGROUND_MARGIN)), new Color(76, 76, 76, 180)) {
                @Override
                public int getZIndex() {
                    return super.getZIndex() + 2; // display above icon
                }
            };
            rechargeOverlay.setAnchor(Anchor.BOTTOM);
            rechargeOverlay.translateToAnchorOrigin();

            gameObject.addComponent(rechargeOverlay);
            rechargeOverlays[i] = rechargeOverlay;
            setCooldownPercent(i, 0f);
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

    // UI Methods

    /**
     * Sets the selected element of the weapon panel and display it as highlighted.
     *
     * @param index the index to select
     */
    public void setSelection(int index) {
        if (!IntMath.inRange(index, 0, numWeapons - 1)) return;

        // Move border
        selectedBorder.setPosition(backgrounds[index].getPosition());
    }

    /**
     * Sets the fill value of the given weapon recharge overlay, from bottom to top.
     *
     * @param weaponIndex     the index of the weapon
     * @param cooldownPercent the percent cooldown to display
     */
    public void setCooldownPercent(int weaponIndex, float cooldownPercent) {
        // Clamp percent between 0%-100%
        var clamped = FloatMath.clamp(cooldownPercent, 0f, 1f);
        rechargeOverlays[weaponIndex].setSize(size.mul(new Vec2(1f, clamped)));
    }

    /**
     * Sets the fill value for all the weapon recharge overlays, from bottom to top.
     *
     * @param cooldownPercent the percent cooldown to display
     */
    public void setAllCooldownPercents(float cooldownPercent) {
        for (int i = 0; i < numWeapons; i++) {
            setCooldownPercent(i, cooldownPercent);
        }
    }

    public int getNumWeapons() {
        return numWeapons;
    }

}
