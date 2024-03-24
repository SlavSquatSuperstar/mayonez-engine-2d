package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * An individual weapon hotbar slot with background, icon, and cooldown
 * overlay elements.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponHotbarSlot extends Script {

    // Constants
    private static final Texture BACKGROUND_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/hotbar_background.png");
    private static final float BACKGROUND_MARGIN = 5f;

    // Fields
    private UIBox cooldownOverlayBox;
    private final Vec2 position, size;
    private final int weaponIndex;

    public WeaponHotbarSlot(Vec2 position, Vec2 size, int weaponIndex) {
        this.position = position;
        this.size = size;
        this.weaponIndex = weaponIndex;
    }

    @Override
    public void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        // Background element
        var backgroundBox = new UIBox(position, size.add(new Vec2(BACKGROUND_MARGIN)), BACKGROUND_TEXTURE);
        gameObject.addComponent(backgroundBox);

        // Weapon icon
        var weaponIcon = new UIBox(position, size, ProjectilePrefabs.PROJECTILE_SPRITES.getTexture(weaponIndex)) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 1; // display above background
            }
        };
        gameObject.addComponent(weaponIcon);

        // Cooldown overlay
        cooldownOverlayBox = new UIBox(
                position, size.add(new Vec2(BACKGROUND_MARGIN)), new Color(76, 76, 76, 180)
        ) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // display above icon
            }
        };
        cooldownOverlayBox.setAnchor(Anchor.BOTTOM);
        cooldownOverlayBox.translateToAnchorOrigin();
        gameObject.addComponent(cooldownOverlayBox);

        setCooldownPercent(0f);
    }

    // Helper Methods

    /**
     * Set the fill value of the given weapon recharge overlay, from bottom to top.
     *
     * @param cooldownPercent the percent cooldown to display
     */
    public void setCooldownPercent(float cooldownPercent) {
        // Clamp percent between 0%-100%
        var clamped = FloatMath.clamp(cooldownPercent, 0f, 1f);
        cooldownOverlayBox.setSize(size.mul(new Vec2(1f, clamped)));
    }

    /**
     * Get the position of the hotbar slot's UI element.
     *
     * @return the UI position
     */
    public Vec2 getPosition() {
        return position;
    }

}
