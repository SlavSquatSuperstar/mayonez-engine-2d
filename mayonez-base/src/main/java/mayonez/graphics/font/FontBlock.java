package mayonez.graphics.font;

import mayonez.assets.image.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

import java.util.*;

/**
 * Creates glyphs for a contiguous block of characters from a
 * {@link mayonez.graphics.font.FontBlockMetadata}.
 *
 * @author SlavSquatSuperstar
 */
public class FontBlock {

    private final FontBlockMetadata metadata;
    private final Map<Character, Glyph> glyphs;

    public FontBlock(FontBlockMetadata metadata) {
        this.metadata = metadata;
        var fontTexture = Textures.getTexture(metadata.textureFile());
        var widths = FontWidthHelper.getGlyphWidths(metadata, fontTexture);
        glyphs = createGlyphs(fontTexture, widths);
    }

    // Create Glyphs Methods

    private Map<Character, Glyph> createGlyphs(Texture fontTexture, int[] widths) {
        var numGlyphs = metadata.numCharacters();

        // Get glyph regions
        var spriteHeight = metadata.spriteHeight();
        var splitter = SpriteSplitters.getSpriteSplitter(
                fontTexture,
                new Vec2(metadata.spriteWidth(), spriteHeight),
                new Vec2(0), numGlyphs
        );
        var regions = splitter.getSpriteRegions();

        // Create glyph textures
        var glyphs = new HashMap<Character, Glyph>(numGlyphs);
        for (var i = 0; i < regions.length; i++) {
            if (widths[i] == 0) continue; // Non-print/whitespace

            var glyphRegion = new ImageRegion(
                    regions[i].origin(), new Vec2(widths[i], spriteHeight)
            );
            var glyphTex = fontTexture.getSubTexture(
                    glyphRegion, "Sprite " + i
            );

            var glyph = new Glyph(widths[i], spriteHeight, glyphTex);
            var charCode = (char) (metadata.startCharacter() + i);
            glyphs.put(charCode, glyph);
        }
        return glyphs;
    }

    public Map<Character, Glyph> getGlyphs() {
        return glyphs;
    }

}
