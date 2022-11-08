package slavsquatsuperstar.mayonez.graphics.sprites;

import slavsquatsuperstar.mayonez.Component;
import slavsquatsuperstar.mayonez.GameObject;
import slavsquatsuperstar.mayonez.Mayonez;
import slavsquatsuperstar.mayonez.io.Assets;
import slavsquatsuperstar.mayonez.io.GLTexture;
import slavsquatsuperstar.mayonez.io.JTexture;
import slavsquatsuperstar.mayonez.io.Texture;
import slavsquatsuperstar.util.Color;
import slavsquatsuperstar.util.Colors;

/**
 * A visual representation of a GameObject.
 *
 * @author SlavSquatSuperstar
 */
public abstract sealed class Sprite extends Component permits GLSprite, JSprite {

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
    public abstract Color getColor();

    /**
     * Returns a new sprite with the same image but not attached to any {@link GameObject}.
     *
     * @return a copy of this image
     */
    public abstract Sprite copy();

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