package mayonez.graphics.font;

import mayonez.assets.image.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

import java.util.*;

/**
 * A bitmap font created from a spritesheet of a set of characters with contiguous code points.
 * <p>
 * See also: <a href="https://minecraft.wiki/w/Font">Font - Minecraft Wiki</a>
 *
 * @author SlavSquatSuperstar
 */
// TODO make asset
public class Font {

    private final FontMetadata metadata;
    private final Texture fontTexture;
    private final Map<Character, Glyph> glyphs;

    public Font(FontMetadata metadata) {
        this.metadata = metadata;
        this.fontTexture = Textures.getTexture(metadata.fontFile());
        glyphs = new HashMap<>();

        var whitespaceGlyph = new Glyph(
                metadata.whitespaceWidth(), metadata.spriteHeight()
        );
        glyphs.put(metadata.whitespaceCharacter(), whitespaceGlyph);

        var widths = FontWidthHelper.getGlyphWidths(metadata, fontTexture);
        var blockGlyphs = createGlyphs(widths);
        blockGlyphs.forEach(glyphs::putIfAbsent); // Don't glyph override if already defined
    }

    // Create Glyphs Methods

    private Map<Character, Glyph> createGlyphs(int[] widths) {
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

    // Metadata Getters

    public int getGlyphHeight() {
        return metadata.glyphHeight();
    }

    public int getGlyphSpacing() {
        return metadata.glyphSpacing();
    }

    // Glyph Getters

    /**
     * Whether this font block supports the glyph with the given char code.
     *
     * @param charCode the char code
     * @return if the glyph is supported
     */
    public boolean hasGlyph(char charCode) {
        return glyphs.containsKey(charCode);
    }

    /**
     * Get the glyph with the given ASCII char code, if the font supports it.
     *
     * @param charCode the char code
     * @return the glyph, null if unsupported
     */
    public Glyph getGlyph(char charCode) {
        return glyphs.get(charCode);
    }

}
