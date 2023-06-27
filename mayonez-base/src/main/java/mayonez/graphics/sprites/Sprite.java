package mayonez.graphics.sprites;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;

/**
 * A visual representation of a GameObject.
 *
 * @author SlavSquatSuperstar
 */
// TODO make color and texture changeable
public abstract class Sprite extends Component {

    protected Transform spriteXf;

    // Getters and Setters

    /**
     * Returns the color of this sprite, white by default if drawing a texture.
     *
     * @return the color
     */
    public abstract Color getColor();

    /**
     * Set the color of this sprite, or "dye" the texture if drawing one.
     *
     * @param color the color
     */
    public abstract void setColor(Color color);

    /**
     * Get the width of this sprite's stored texture in pixels.
     *
     * @return the image width, or 0 if drawing a color
     */
    public abstract int getImageWidth();

    /**
     * Get the height of this sprite's stored texture in pixels.
     *
     * @return the image height, or 0 if drawing a color
     */
    public abstract int getImageHeight();

    /**
     * The sprite's transform in the parent object's local space.
     *
     * @return the sprite transform
     */
    public final Transform getSpriteTransform() {
        return spriteXf;
    }

    /**
     * Set additional position, rotation, and size modifiers for the sprite.
     *
     * @param spriteXf the transform
     */
    public final Sprite setSpriteTransform(Transform spriteXf) {
        this.spriteXf = spriteXf;
        return this;
    }

    /**
     * Returns the texture this sprite holds.
     *
     * @return the texture, or null if drawing a color
     */
    public abstract Texture getTexture();

    // Copy Methods

    /**
     * Returns a new sprite with the same image but not attached to any {@link mayonez.GameObject}.
     *
     * @return a copy of this image
     */
    public abstract Sprite copy();

}