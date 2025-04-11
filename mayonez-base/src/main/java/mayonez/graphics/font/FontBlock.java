package mayonez.graphics.font;

import mayonez.assets.image.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.util.Record;

import java.util.*;

/**
 * Specifies a contiguous block of characters and their glyph dimensions and
 * textures.
 *
 * @param name           the name or description of the block
 * @param textureFile    the filename of the glyph texture atlas
 * @param startCharacter the start character value
 * @param endCharacter   the end character value
 * @param spriteWidth    the width of a glyph sprite, in pixels
 * @param spriteHeight   the height of a glyph sprite, in pixels
 * @param glyphAscent    the height of a character glyph above the baseline, in pixels
 * @author SlavSquatSuperstar
 */
// TODO specify characters (for non-contiguous)
// TODO specify unicode
public record FontBlock(
        String name, String textureFile,
        char startCharacter, char endCharacter,
        int spriteWidth, int spriteHeight, int glyphAscent
) {

    public FontBlock(Record record) {
        this(
                record.getString("name"),
                record.getString("texture_file"),
                (char) record.getInt("start_character"),
                (char) record.getInt("end_character"),
                record.getInt("sprite_width"),
                record.getInt("sprite_height"),
                record.getInt("glyph_ascent")
        );
    }

    // Create Glyphs Methods

    Map<Character, Glyph> getGlyphs() {
        var fontTexture = Textures.getTexture(textureFile());
        var widths = FontWidthHelper.getGlyphWidths(this, fontTexture);
        return createGlyphs(fontTexture, widths);
    }

    private Map<Character, Glyph> createGlyphs(Texture fontTexture, int[] widths) {
        var numGlyphs = numCharacters();

        // Get glyph regions
        var spriteHeight = spriteHeight();
        var splitter = SpriteSplitters.getSpriteSplitter(
                fontTexture,
                new Vec2(spriteWidth(), spriteHeight),
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
            var charCode = (char) (startCharacter() + i);
            glyphs.put(charCode, glyph);
        }
        return glyphs;
    }

    /**
     * Get the number of characters this font block provides.
     *
     * @return the number of characters
     */
    int numCharacters() {
        return endCharacter - startCharacter + 1;
    }

}
