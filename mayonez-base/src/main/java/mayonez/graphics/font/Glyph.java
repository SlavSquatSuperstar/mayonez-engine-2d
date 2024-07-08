package mayonez.graphics.font;

import mayonez.graphics.textures.*;

/**
 * A symbol in a font used to represent a printable character.
 *
 * @author SlavSquatSuperstar
 */
public class Glyph {

    private final int width, height;
    private final GLTexture glyphTexture;

    public Glyph(int width, int height, GLTexture glyphTexture) {
        this.width = width;
        this.height = height;
        this.glyphTexture = glyphTexture;
    }

    /**
     * The width of the glyph, in pixels.
     *
     * @return the glyph width
     */
    public int getWidth() {
        return width;
    }

    /**
     * The height of the glyph, in pixels.
     *
     * @return the glyph height
     */
    public int getHeight() {
        return height;
    }

    /**
     * The sprite sheet texture used to draw the glyph.
     *
     * @return the glyph texture
     */
    public GLTexture getTexture() {
        return glyphTexture;
    }

}
