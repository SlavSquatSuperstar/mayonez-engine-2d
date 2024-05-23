package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * A slider bar that displays over a background bar and whose length can be changed.
 *
 * @author SlavSquatSuperstar
 */
public class SliderBar extends Script {

    private static final Texture BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_border_wide.png");
    private final Vec2 position, size;
    private final Color backgroundColor, sliderColor;

    private UISprite sliderSprite;

    public SliderBar(Vec2 position, Vec2 size, Color backgroundColor, Color sliderColor) {
        this.position = position;
        this.size = size;
        this.backgroundColor = backgroundColor;
        this.sliderColor = sliderColor;
    }

    @Override
    protected void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        var backgroundSprite = new UISprite(position, size, backgroundColor) {
            @Override
            public int getZIndex() {
                return super.getZIndex();
            }
        };
        gameObject.addComponent(backgroundSprite);

        sliderSprite = new UISprite(position, size, sliderColor) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 1; // display above background
            }
        };
        sliderSprite.setAnchor(Anchor.LEFT);
        sliderSprite.translateToAnchorOrigin();
        gameObject.addComponent(sliderSprite);

        var outlineSprite = new UISprite(position, size, BORDER_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // display above foreground
            }
        };
        gameObject.addComponent(outlineSprite);
    }

    /**
     * Sets the fill value of the slider, from left to right.
     *
     * @param healthPercent the percent health to display
     */
    public void setSliderValue(float healthPercent) {
        // Clamp percent between 0%-100%
        var clamped = FloatMath.clamp(healthPercent, 0f, 1f);
        sliderSprite.setSize(size.mul(new Vec2(clamped, 1f)));
    }

}
