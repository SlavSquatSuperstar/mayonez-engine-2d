package mayonez.graphics.font;

import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * A bitmap font created from a spritesheet of a set of characters with contiguous code points.
 *
 * @author SlavSquatSuperstar
 */
public class Font {

    private final GLTexture fontTexture;
    private final FontMetadata metadata;
    private int[] widths;
    private final Glyph[] glyphs;

    public Font(GLTexture fontTexture, FontMetadata metadata, int[] widths) {
        this.fontTexture = fontTexture;
        this.metadata = metadata;
        this.widths = widths;
        glyphs = createGlyphs();
    }

    private Glyph[] createGlyphs() {
        var glyphs = new Glyph[metadata.numCharacters()];
        var texSize = fontTexture.getSize();
        var glyphSize = metadata.glyphHeight();

        // GL uses bottom left as image origin
        var texTopLeft = new Vec2(0, texSize.y);
        var glyphBottomLeft = texTopLeft.sub(new Vec2(0, glyphSize));

        // Create glyph textures
        for (int i = 0; i < glyphs.length; i++) {
            // TODO auto set width by counting pixels
            var glyphTex = new GLSpriteSheetTexture(fontTexture, i, glyphBottomLeft, new Vec2(glyphSize));
            glyphs[i] = new Glyph(widths[i], metadata.glyphHeight(), glyphTex);

            // Move to next glyph
            // Origin at bottom left
            glyphBottomLeft.x += glyphSize;
            if (glyphBottomLeft.x >= fontTexture.getWidth()) {
                // If at end of row, go to next row
                glyphBottomLeft.x = 0;
                glyphBottomLeft.y -= glyphSize;
            }
        }
        return glyphs;
    }

    public FontMetadata getMetadata() {
        return metadata;
    }

    public int[] getWidths() {
        return widths;
    }

    public Glyph getGlyph(int charCode) {
        var index = charCode - metadata.startCharacter();
        if (!MathUtils.inRange(index, 0, metadata.numCharacters() - 1)) {
            return null;
        } else {
            return glyphs[charCode - metadata.startCharacter()];
        }
    }

}
