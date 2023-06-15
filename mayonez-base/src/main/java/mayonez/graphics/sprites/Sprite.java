package mayonez.graphics.sprites;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.io.*;

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

    /**
     * Returns the texture this sprite holds.
     *
     * @return the texture, or null if drawing a color
     */
    public abstract Texture getTexture();

    /**
     * Returns the color this sprite holds.
     *
     * @return the color, or white if drawing a texture
     */
    public final Color getColor() {
        return this.color;
    }

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
     * Returns a new sprite with the same image but not attached to any {@link mayonez.GameObject}.
     *
     * @return a copy of this image
     */
    public abstract Sprite copy();

    // Factory Methods

    /**
     * Automatically creates a AWT or GL sprite with a given filename depending on the current engine instance.
     *
     * @param filename the texture location
     * @return a sprite
     */
    public static Sprite create(String filename) {
        if (Mayonez.getUseGL()) return new GLSprite(Assets.getGLTexture(filename));
        else return new JSprite(Assets.getJTexture(filename));
    }

    /**
     * Automatically creates a AWT or GL sprite with a given texture depending on the current engine instance.
     *
     * @param texture an existing texture
     * @return a sprite
     */
    public static Sprite create(Texture texture) {
        if (texture instanceof GLTexture glTexture) return new GLSprite(glTexture);
        else if (texture instanceof JTexture jTexture) return new JSprite(jTexture);
        else return Sprite.create(Colors.WHITE);
    }

    /**
     * Automatically creates a AWT or GL sprite with a given colordepending on the current engine instance.
     *
     * @param color the color
     * @return a sprite
     */
    public static Sprite create(Color color) {
        if (Mayonez.getUseGL()) return new GLSprite(color);
        else return new JSprite(color);
    }

}