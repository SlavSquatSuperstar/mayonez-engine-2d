package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * Displays the health of a game object.
 *
 * @author SlavSquatSuperstar
 */
public class HealthBar extends Script {

    private static final Texture BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/health_bar_border.png");
    private final Vec2 position, size;
    private UIBox sliderBox;

    public HealthBar(Vec2 position, Vec2 size) {
        this.position = position;
        this.size = size;
    }

    @Override
    protected void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        var backgroundBox = new UIBox(position, size, Colors.RED) {
            @Override
            public int getZIndex() {
                return super.getZIndex();
            }
        };
        gameObject.addComponent(backgroundBox);

        sliderBox = new UIBox(position, size, Colors.GREEN) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 1; // display above background
            }
        };
        sliderBox.setAnchor(Anchor.LEFT);
        sliderBox.translateToAnchorOrigin();
        gameObject.addComponent(sliderBox);

        var outlineBox = new UIBox(position, size, BORDER_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // display above foreground
            }
        };
        gameObject.addComponent(outlineBox);
    }

    /**
     * Sets the fill value of this health bar, from left to right.
     *
     * @param healthPercent the percent health to display
     */
    public void setHealthPercent(float healthPercent) {
        // Clamp percent between 0%-100%
        var clamped = FloatMath.clamp(healthPercent, 0f, 1f);
        sliderBox.setSize(size.mul(new Vec2(clamped, 1f)));
    }

}
