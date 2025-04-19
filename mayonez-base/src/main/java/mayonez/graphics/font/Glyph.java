package mayonez.graphics.font;

import mayonez.graphics.textures.*;

/**
 * A symbol in a font used to represent a printable character.
 *
 * @param width   the width of the glyph, in pixels
 * @param height  the height of the glyph, in pixels
 * @param ascent  the height of the glyph above the baseline, in pixels
 * @param texture the sprite sheet texture used to draw the glyph, null if whitespace
 * @author SlavSquatSuperstar
 */
public record Glyph(
        int width,
        int height,
        int ascent,
        Texture texture
) {

    /**
     * Create a whitespace glyph that does not use a texture.
     */
    public Glyph(int width, int height) {
        this(width, height, height, null);
    }

    /**
     * If this glyph represents a whitespace character and should be invisible.
     * Not the same as a non-print (control) character.
     *
     * @return if whitespace
     */
    public boolean isWhitespace() {
        return texture == null;
    }

}
