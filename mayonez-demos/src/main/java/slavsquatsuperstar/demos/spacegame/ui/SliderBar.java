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
public class SliderBar extends Script implements UIElement {

    private static final Texture BORDER_TEXTURE = Textures.getTexture(
            "assets/spacegame/textures/ui/gray_border_wide.png");
    private Vec2 position, size;
    private final Color backgroundColor, sliderColor;

    // Slider Fields
    private UISprite backgroundSprite, sliderSprite, outlineSprite;
    private float value;

    public SliderBar(Vec2 position, Vec2 size, Color backgroundColor, Color sliderColor) {
        this.position = position;
        this.size = size;
        this.backgroundColor = backgroundColor;
        this.sliderColor = sliderColor;
    }

    @Override
    protected void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        backgroundSprite = new UISprite(position, size, backgroundColor) {
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
        gameObject.addComponent(sliderSprite);
        value = 1f;

        outlineSprite = new UISprite(position, size, BORDER_TEXTURE) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // display above foreground
            }
        };
        gameObject.addComponent(outlineSprite);
    }

    // Slider Methods

    /**
     * Get the fill value of the slider, from left to right.
     *
     * @return the percent value of the slider
     */
    public float getSliderValue() {
        return value;
    }

    /**
     * Set the fill value of the slider, from left to right.
     *
     * @param value the percent value of the slider
     */
    public void setSliderValue(float value) {
        // Clamp percent between 0%-100%
        this.value = MathUtils.clamp(value, 0f, 1f);
        updateSliderSize();
    }

    private void updateSliderPosition() {
        sliderSprite.setPosition(
                backgroundSprite.getBounds().getPosition(Anchor.LEFT)
        );
    }

    private void updateSliderSize() {
        sliderSprite.setSize(size.mul(new Vec2(value, 1f)));
    }

    // UI Element Methods

    @Override
    public Vec2 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vec2 position) {
        this.position = position;
        backgroundSprite.setPosition(position);
        updateSliderPosition();
        outlineSprite.setPosition(position);
    }

    @Override
    public Vec2 getSize() {
        return size;
    }

    @Override
    public void setSize(Vec2 size) {
        this.size = size;
        backgroundSprite.setSize(size);
        updateSliderSize();
        updateSliderPosition();
        outlineSprite.setSize(size);
    }

}
