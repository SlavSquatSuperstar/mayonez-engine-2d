package slavsquatsuperstar.mayonez.graphics.sprite;

import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.Transform;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.GLTexture;
import slavsquatsuperstar.mayonez.io.JTexture;
import slavsquatsuperstar.mayonez.io.Texture;
import slavsquatsuperstar.mayonez.util.Color;
import slavsquatsuperstar.mayonez.util.Colors;

/**
 * A visual representation of a GameObject.
 *
 * @author SlavSquatSuperstar
 */
// TODO set sprite scale
public abstract sealed class Sprite extends Component permits GLSprite, JSprite {

    protected final Color color;
    protected Transform spriteXf;

    public Sprite(Color color) {
        this.color = color;
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
     * @return the color, or white is drawing a texture
     */
    public final Color getColor() {
        return this.color;
    }

    /**
     * Returns the transform that describe how the sprite is drawn in relation to the game object.
     *
     * @return the transform
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
     * Returns a new sprite with the same image but not attached to any {@link GameObject}.
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
        if (Boolean.TRUE.equals(Mayonez.getUseGL())) {
            return new GLSprite(Assets.getGLTexture(filename));
        } else {
            return new JSprite(Assets.getJTexture(filename));
        }
    }

    /**
     * Automatically creates a AWT or GL sprite with a given texture depending on the current engine instance.
     *
     * @param texture an existing texture
     * @return a sprite
     */
    public static Sprite create(Texture texture) {
        if (texture instanceof GLTexture glTexture) {
            return new GLSprite(glTexture);
        } else if (texture instanceof JTexture jTexture) {
            return new JSprite(jTexture);
        }
        return Sprite.create(new Color(Colors.WHITE));
    }

    /**
     * Automatically creates a AWT or GL sprite with a given colordepending on the current engine instance.
     *
     * @param color the color
     * @return a sprite
     */
    public static Sprite create(Color color) {
        if (Boolean.TRUE.equals(Mayonez.getUseGL())) {
            return new GLSprite(color);
        } else {
            return new JSprite(color);
        }
    }

}