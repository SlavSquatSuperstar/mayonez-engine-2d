package mayonez.graphics.font;

import mayonez.assets.*;
import mayonez.assets.text.*;

import java.util.*;

/**
 * A bitmap font created from one or more sprite sheets of character glyphs.
 * <p>
 * See also:
 * <ul>
 *     <li><a href="https://minecraft.wiki/w/Font#Bitmap_provider">
 *         Font § Bitmap - Minecraft Wiki</a></li>
 *     <li><a href="https://en.wikipedia.org/wiki/Font#Metrics">
 *         Font § Metrics - Wikipedia</a></li>
 *     <li><a href="https://en.wikipedia.org/wiki/Leading">
 *         Leading - Wikipedia</a></li>
 *     <li><a href="https://en.wikipedia.org/wiki/Point_(typography)">
 *         Point - Wikipedia</a></li>
 *     <li><a href="https://en.wikipedia.org/wiki/Typeface">
 *         Typeface - Wikipedia</a></li>
 *     <li><a href="https://en.wikipedia.org/wiki/Typeface_anatomy">
 *         Typeface Anatomy - Wikipedia</a></li>
 * </ul>
 *
 * @author SlavSquatSuperstar
 */
public class Font extends Asset {

    private final FontMetadata metadata;
    private final Map<Character, Glyph> glyphs;

    public Font(String filename) {
        super(filename);

        // Read font metadata
        var json = new JSONFile(filename);
        this.metadata = new FontMetadata(json.readJSON());
        glyphs = readFontGlyphs(metadata);
    }

    private Map<Character, Glyph> readFontGlyphs(FontMetadata metadata) {
        final Map<Character, Glyph> glyphs;
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
        return glyphs;
    }

    // TODO free block textures

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
