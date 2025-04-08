package mayonez.graphics.font;

import mayonez.assets.image.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

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
    private final Glyph[] glyphs;

    public Font(FontMetadata metadata) {
        this.metadata = metadata;
        this.fontTexture = Textures.getTexture(metadata.fontFile());
        var widths = FontWidthHelper.getGlyphWidths(metadata, fontTexture);
        glyphs = createGlyphs(widths);
    }

    // Create Glyphs Methods

    private Glyph[] createGlyphs(int[] widths) {
        var numGlyphs = metadata.numCharacters();

        // Get glyph regions
        var glyphHeight = metadata.glyphHeight();
        var splitter = SpriteSplitters.getSpriteSplitter(
                fontTexture,
                new Vec2(metadata.glyphMaxWidth(), glyphHeight),
                new Vec2(0), numGlyphs
        );
        var regions = splitter.getSpriteRegions();

        // Create glyph textures
        var glyphs = new Glyph[numGlyphs];
        for (var i = 0; i < regions.length; i++) {
            var glyphRegion = new ImageRegion(
                    regions[i].origin(), new Vec2(widths[i], glyphHeight)
            );
            var glyphTex = fontTexture.getSubTexture(
                    glyphRegion, "Sprite " + i
            );
            glyphs[i] = new Glyph(widths[i], glyphHeight, glyphTex);
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
     * Get the glyph with the given ASCII char code, if the font supports it.
     *
     * @param charCode the char code
     * @return the glyph, null if unsupported
     */
    public Glyph getGlyph(int charCode) {
        var index = charCode - metadata.startCharacter();
        if (!MathUtils.inRange(index, 0, metadata.numCharacters() - 1)) {
            return null;
        } else {
            return glyphs[charCode - metadata.startCharacter()];
        }
    }

}
