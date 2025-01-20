package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectilePrefabs;
import slavsquatsuperstar.demos.spacegame.combat.projectiles.ProjectileType;

/**
 * An image label representing an individual weapon hotbar slot, with additional cooldown
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
    private UISprite cooldownOverlaySprite;
    private float cooldownPercent;

    public WeaponHotbarSlot(Vec2 position, Vec2 size, ProjectileType type) {
        super(position, size,
                ProjectilePrefabs.PROJECTILE_SPRITES.getTexture(type.spriteIndex()),
                BACKGROUND_TEXTURE, UNSELECTED_BORDER_TEXTURE);
    }

    @Override
    protected void init() {
        super.init();
        setBackgroundPadding(BACKGROUND_MARGIN);
        setBorderThickness(BORDER_MARGIN);

        // Cooldown overlay
        cooldownOverlaySprite = new UISprite(
                getPosition(), getSize().add(new Vec2(BORDER_MARGIN)), Color.grayscale(76, 180)
        ) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // display above icon
            }
        };
        cooldownOverlaySprite.setAnchor(Anchor.BOTTOM);
        gameObject.addComponent(cooldownOverlaySprite);
        setCooldownPercent(0f);
    }

    // Helper Methods


    /**
     * Get the fill value of the weapon reload overlay, from bottom to top.
     *
     * @return the percent value of the slider
     */
    public float getCooldownPercent() {
        return cooldownPercent;
    }

    /**
     * Set the fill value of the weapon reload overlay, from bottom to top.
     *
     * @param cooldownPercent the percent cooldown to display
     */
    public void setCooldownPercent(float cooldownPercent) {
        // Clamp percent between 0%-100%
        this.cooldownPercent = MathUtils.clamp(cooldownPercent, 0f, 1f);
        cooldownOverlaySprite.setSize(getSize().mul(new Vec2(1f, this.cooldownPercent)));
    }

    // UIElement Methods

    @Override
    public void setPosition(Vec2 position) {
        super.setPosition(position);
        cooldownOverlaySprite.setPosition(
                position.sub(new Vec2(0, 0.5f * getSize().y))
        );
    }

    @Override
    public void setSize(Vec2 size) {
        super.setSize(size);
        cooldownOverlaySprite.setSize(getSize().mul(new Vec2(1f, this.cooldownPercent)));
    }
}
