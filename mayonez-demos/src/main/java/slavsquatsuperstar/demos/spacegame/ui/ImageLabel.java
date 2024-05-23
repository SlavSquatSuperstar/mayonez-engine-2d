package slavsquatsuperstar.demos.spacegame.ui;

import mayonez.*;
import mayonez.graphics.textures.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import slavsquatsuperstar.demos.spacegame.objects.SpaceGameZIndex;

/**
 * A UI element that displays an icon sprite, background sprite, border sprite.
 * The thickness of the background and border can be set, similar to the CSS box model.
 *
 * @author SlavSquatSuperstar
 */
public class ImageLabel extends Script {

    private final Vec2 position, size;
    private float backgroundPadding, borderThickness;
    private UISprite iconSprite, backgroundSprite, borderSprite;
    private Texture iconTexture, backgroundTexture, borderTexture;

    public ImageLabel(Vec2 position, Vec2 size, Texture iconTexture, Texture backgroundTexture, Texture borderTexture) {
        this.position = position;
        this.size = size;
        this.iconTexture = iconTexture;
        this.backgroundTexture = backgroundTexture;
        this.borderTexture = borderTexture;
    }

    @Override
    protected void init() {
        gameObject.setZIndex(SpaceGameZIndex.UI);

        // Uses the size of the label
        backgroundSprite = new UISprite(position, size, backgroundTexture);
        gameObject.addComponent(backgroundSprite);

        // Can be made smaller by setting padding
        iconSprite = new UISprite(position, size.sub(new Vec2(backgroundPadding)), iconTexture) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 1; // Display above background
            }
        };
        gameObject.addComponent(iconSprite);

        // Can be made larger by setting thickness
        borderSprite = new UISprite(position, size.add(new Vec2(borderThickness)), borderTexture) {
            @Override
            public int getZIndex() {
                return super.getZIndex() + 2; // Display above icon
            }
        };
        gameObject.addComponent(borderSprite);
    }

    @Override
    protected void start() {
        if (backgroundTexture == null) backgroundSprite.setEnabled(false);
        if (borderTexture == null) borderSprite.setEnabled(false);
    }

    // Set Texture Methods

    /**
     * Set the texture to display on the icon.
     *
     * @param iconTexture the icon texture
     */
    public void setIconTexture(Texture iconTexture) {
        this.iconTexture = iconTexture;
        iconSprite.setTexture(iconTexture);
    }

    /**
     * Set the texture to display on the background. If the texture is set to
     * null, the background will be invisible.
     *
     * @param backgroundTexture the background texture, or null
     */
    public void setBackgroundTexture(Texture backgroundTexture) {
        this.backgroundTexture = backgroundTexture;
        backgroundSprite.setTexture(backgroundTexture);
        backgroundSprite.setEnabled(backgroundTexture != null);
    }

    /**
     * Set the texture to display on the background. If the texture is set to
     * null, the border will be invisible.
     *
     * @param borderTexture the border texture, or null
     */
    public void setBorderTexture(Texture borderTexture) {
        this.borderTexture = borderTexture;
        borderSprite.setTexture(borderTexture);
        borderSprite.setEnabled(borderTexture != null);
    }


    // Set Size Methods

    /**
     * Set the padding between the background and icon. The padding can be used
     * to shrink the icon inside the label.
     *
     * @param backgroundPadding the padding size
     */
    public void setBackgroundPadding(float backgroundPadding) {
        this.backgroundPadding = backgroundPadding;
        iconSprite.setSize(size.sub(new Vec2(backgroundPadding)));
    }

    /**
     * Set the thickness of the border. The thickness can be used to extend the
     * border outside the label.
     *
     * @param borderThickness the border thickness
     */
    public void setBorderThickness(float borderThickness) {
        this.borderThickness = borderThickness;
        borderSprite.setSize(size.add(new Vec2(borderThickness)));
    }

}
