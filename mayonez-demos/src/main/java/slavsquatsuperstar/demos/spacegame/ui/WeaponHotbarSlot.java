package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;

/**
 * A label representing an individual weapon hotbar slot, with additional cooldown
 * overlay.
 *
 * @author SlavSquatSuperstar
 */
public class WeaponHotbarSlot extends ImageLabel {

    // Constants
    private static final Texture BACKGROUND_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_background.png");
    private static final Texture UNSELECTED_BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_border.png");

    private static final float BACKGROUND_MARGIN = 4f;
    private static final float BORDER_MARGIN = 4f;

    // Fields
    private final Vec2 position, size;
    private UISprite cooldownOverlaySprite;

    public WeaponHotbarSlot(Vec2 position, Vec2 size, int weaponIndex) {
        super(position, size,
                ProjectilePrefabs.PROJECTILE_SPRITES.getTexture(weaponIndex),
                BACKGROUND_TEXTURE, UNSELECTED_BORDER_TEXTURE);
        this.position = position;
        this.size = size;
    }

    @Override
    protected void init() {
        super.init();
        setBackgroundPadding(BACKGROUND_MARGIN);
        setBorderThickness(BORDER_MARGIN);

        // Cooldown overlay
        cooldownOverlaySprite = new UISprite(
                position, size.add(new Vec2(BORDER_MARGIN)), Color.grayscale(76, 180)
        ) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // display above icon
            }
        };
        cooldownOverlaySprite.setAnchor(Anchor.BOTTOM);
        cooldownOverlaySprite.translateToAnchorOrigin();
        gameObject.addComponent(cooldownOverlaySprite);
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
        var clamped = MathUtils.clamp(cooldownPercent, 0f, 1f);
        cooldownOverlaySprite.setSize(size.mul(new Vec2(1f, clamped)));
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
