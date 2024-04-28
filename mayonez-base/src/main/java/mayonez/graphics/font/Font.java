package mayonez.graphics.font;

import mayonez.graphics.sprites.*;
import mayonez.math.*;

/**
 * A bitmap font created from a spritesheet of a set of characters with contiguous code points.
 *
 * @author SlavSquatSuperstar
 */
public class Font {

    private final GLSpriteSheet fontSpriteSheet;
    private final FontMetadata metadata;
    private final int[] widths;
    private final Glyph[] glyphs;

    public Font(GLSpriteSheet fontSpriteSheet, FontMetadata metadata, int[] widths) {
        this.fontSpriteSheet = fontSpriteSheet;
        this.metadata = metadata;
        this.widths = widths;
        glyphs = createGlyphs();
    }

    private Glyph[] createGlyphs() {
        var glyphs = new Glyph[metadata.numCharacters()];
        var sheetSize = fontSpriteSheet.getSheetSize();

        for (int i = 0; i < glyphs.length; i++) {
            var tex = fontSpriteSheet.getTexture(i);
            if (tex == null) continue;
            var coords = tex.getTexCoords();
            glyphs[i] = new Glyph(widths[i], metadata.glyphHeight(), tex, coords);
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
        if (!IntMath.inRange(index, 0, metadata.numCharacters() - 1)) {
            return null;
        } else {
            return glyphs[charCode - metadata.startCharacter()];
        }
    }

}
