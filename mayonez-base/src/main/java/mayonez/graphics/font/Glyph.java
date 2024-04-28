package mayonez.graphics.font;

import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * A symbol in a font used to represent a printable character.
 *
 * @author SlavSquatSuperstar
 */
public class Glyph {

    private final int width, height;
    private final GLTexture glyphTexture;
    private final Vec2[] texCoords;

    public Glyph(int width, int height, GLTexture glyphTexture, Vec2[] texCoords) {
        this.width = width;
        this.height = height;
        this.glyphTexture = glyphTexture;
        this.texCoords = texCoords;
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

    public Vec2[] getTexCoords() {
        return texCoords;
    }

}
