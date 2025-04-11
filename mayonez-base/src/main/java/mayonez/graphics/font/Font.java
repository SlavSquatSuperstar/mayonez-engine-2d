package mayonez.graphics.font;

import java.util.*;

/**
 * A bitmap font created from one or more sprite sheets of character glyphs.
 * <p>
 * See also: <a href="https://minecraft.wiki/w/Font">Font - Minecraft Wiki</a>
 *
 * @author SlavSquatSuperstar
 */
// TODO make asset
public class Font {

    private final FontMetadata metadata;
    private final Map<Character, Glyph> glyphs;

    public Font(FontMetadata metadata) {
        this.metadata = metadata;
        glyphs = new HashMap<>();

        // Create whitespace glyph
        var whitespaceGlyph = new Glyph(
                metadata.whitespaceWidth(), metadata.glyphHeight()
        );
        glyphs.put(metadata.whitespaceCharacter(), whitespaceGlyph);

        // Read glyphs from blocks
        var blocks = metadata.blocks();
        for (var block : blocks) {
            var blockGlyphs = block.getGlyphs();
            blockGlyphs.forEach(glyphs::putIfAbsent); // Don't glyph override if already defined
        }
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
