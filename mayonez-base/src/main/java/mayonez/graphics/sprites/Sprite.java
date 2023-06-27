package mayonez.graphics.sprites;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.util.*;

/**
 * A visual representation of a GameObject.
 *
 * @author SlavSquatSuperstar
 */
// TODO make color and texture changeable
public abstract class Sprite extends Component {

    protected final Color color;
    protected Transform spriteXf;

    public Sprite(Color color) {
        this.color = (color != null) ? color : Colors.WHITE;
    }

    // Getters and Setters

    /**
     * Returns the color this sprite holds.
     *
     * @return the color, or white if drawing a texture
     */
    public final Color getColor() {
        return this.color;
    }

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