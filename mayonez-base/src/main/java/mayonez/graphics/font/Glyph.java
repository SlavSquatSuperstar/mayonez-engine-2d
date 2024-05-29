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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GLTexture getTexture() {
        return glyphTexture;
    }

}
