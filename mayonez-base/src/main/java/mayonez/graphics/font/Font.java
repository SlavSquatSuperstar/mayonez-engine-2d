package mayonez.graphics.font;

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
    private final Map<Character, Glyph> glyphs;

    public Font(FontMetadata metadata) {
        this.metadata = metadata;
        glyphs = new HashMap<>();

        var whitespaceGlyph = new Glyph(
                metadata.whitespaceWidth(), metadata.spriteHeight()
        );
        glyphs.put(metadata.whitespaceCharacter(), whitespaceGlyph);

        var blockMetadata = new FontBlockMetadata(
                "Default block", metadata.fontFile(),
                metadata.startCharacter(), metadata.endCharacter(),
                metadata.spriteWidth(), metadata.spriteHeight(),
                metadata.glyphAscent()
        ) ;
        var block = new FontBlock(blockMetadata);
        var blockGlyphs = block.getGlyphs();
        blockGlyphs.forEach(glyphs::putIfAbsent); // Don't glyph override if already defined
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
