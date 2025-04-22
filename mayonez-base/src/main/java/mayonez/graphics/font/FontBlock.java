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
 * @param fontTexture    the glyph texture atlas
 * @param startCharacter the start character value
 * @param endCharacter   the end character value
 * @param spriteWidth    the width of a glyph sprite, in pixels
 * @param spriteHeight   the height of a glyph sprite, in pixels
 * @param baselineHeight the distance from sprite bottom to the baseline, in pixels
 * @author SlavSquatSuperstar
 */
// TODO specify characters (for non-contiguous)
// TODO specify unicode
public record FontBlock(
        String name, Texture fontTexture,
        char startCharacter, char endCharacter,
        int spriteWidth, int spriteHeight, int baselineHeight
) {

    public FontBlock(Record record) {
        this(
                record.getString("name"),
                Textures.getTexture(record.getString("texture_file")),
                (char) record.getInt("start_character"),
                (char) record.getInt("end_character"),
                record.getInt("sprite_width"),
                record.getInt("sprite_height"),
                record.getInt("baseline_height")
        );
    }

    // Create Glyphs Methods

    Map<Character, Glyph> getGlyphs() {
        var widths = FontWidthHelper.getGlyphWidths(this);
        var regions = getGlyphRegions(this);
        return getGlyphTextures(widths, regions);
    }

    private static ImageRegion[] getGlyphRegions(FontBlock block) {
        // Get glyph regions
        var splitter = SpriteSplitters.getSpriteSplitter(
                block.fontTexture,
                new Vec2(block.spriteWidth, block.spriteHeight),
                new Vec2(0), block.numCharacters()
        );
        return splitter.getSpriteRegions();
    }

    private Map<Character, Glyph> getGlyphTextures(int[] widths, ImageRegion[] regions) {
        // Create glyph textures
        var glyphs = new HashMap<Character, Glyph>(regions.length);
        for (var i = 0; i < regions.length; i++) {
            if (widths[i] == 0) continue; // Non-print/whitespace

            var glyphRegion = new ImageRegion(
                    regions[i].origin(), new Vec2(widths[i], spriteHeight)
            );
            var glyphTex = fontTexture.getSubTexture(
                    glyphRegion, "Sprite " + i
            );
            var glyph = new Glyph(widths[i], spriteHeight, baselineHeight, glyphTex);

            var charCode = (char) (startCharacter + i);
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
