package mayonez.graphics.font;

import mayonez.assets.image.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

import java.io.IOException;

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
        var widths = getGlyphWidths(metadata, fontTexture);
        glyphs = createGlyphs(widths);
    }

    // Create Glyphs Methods

    private Glyph[] createGlyphs(int[] widths) {
        var numGlyphs = metadata.numCharacters();
        var glyphs = new Glyph[numGlyphs];
        // TODO Don't create if AWT for now
        if (!(fontTexture instanceof GLTexture)) return glyphs;

        // Get GL glyph regions
        var texSize = fontTexture.getSize();
        var glyphSize = metadata.glyphHeight();
        var spriteStart = new Vec2(0, texSize.y - glyphSize);
        var splitter = new TiledSpriteSplitter(
                texSize, new Vec2(glyphSize),
                new Vec2(0), numGlyphs,
                spriteStart, new Vec2(1, -1)
        );
        var regions = splitter.getSpriteRegions();

        // Create glyph textures
        for (var i = 0; i < numGlyphs; i++) {
            var glyphRegion = new ImageRegion(
                    regions[i].origin(), new Vec2(widths[i], glyphSize)
            );
            var glyphTex = fontTexture.getSubTexture(
                    glyphRegion, "Sprite " + i
            );
            glyphs[i] = new Glyph(widths[i], glyphSize, glyphTex);
        }
        return glyphs;
    }

    // Glyph Widths Methods

    // Automatically detect glyph widths from image file
    static int[] getGlyphWidths(FontMetadata metadata, Texture fontTexture) {
        var widths = new int[metadata.numCharacters()];

        // Look at AWT image since no flipping or freeing
        ImageData imgData;
        try {
            imgData = new AWTImageData(fontTexture.getFilename());
        } catch (IOException e) {
            return widths;
        }

        // Get AWT glyph regions
        var glyphSize = metadata.glyphHeight();
        var splitter = new TiledSpriteSplitter(
                fontTexture.getSize(), new Vec2(glyphSize),
                new Vec2(0), metadata.numCharacters(),
                new Vec2(0), new Vec2(1)
        );
        var regions = splitter.getSpriteRegions();

        for (var i = 0; i < widths.length; i++) {
            if (metadata.startCharacter() + i == metadata.whitespaceCharacter()) {
                widths[i] = metadata.whitespaceWidth();
            } else {
                widths[i] = getGlyphWidth(imgData, regions[i]);
            }
        }
        return widths;
    }

    // Get glyph width by finding the last column with any filled pixels
    private static int getGlyphWidth(ImageData imageData, ImageRegion region) {
        var startX = region.getX();
        var startY = region.getY();

        var lastFilled = -1;
        for (var col = 0; col < region.getWidth(); col++) {
            if (!isColumnBlank(imageData, startX + col, startY, region.getHeight())) {
                lastFilled = col;
            }
        }
        return lastFilled + 1; // Get column after last filled
    }

    private static boolean isColumnBlank(ImageData imageData, int colX, int startY, int glyphHeight) {
        for (var row = 0; row < glyphHeight; row++) {
            var pixAlpha = imageData.getPixelColor(colX, startY + row).getAlpha();
            if (pixAlpha > 0) return false; // Found a filled pixel
        }
        return true; // Found only blank pixels
    }

    // Metadata Getters

    public int getGlyphHeight() {
        return metadata.glyphHeight();
    }

    public int getGlyphSpacing() {
        return metadata.glyphSpacing();
    }

    public int numGlyphs() {
        return metadata.numCharacters();
    }

    // Glyph Getters

    public Glyph getGlyph(int charCode) {
        var index = charCode - metadata.startCharacter();
        if (!MathUtils.inRange(index, 0, metadata.numCharacters() - 1)) {
            return null;
        } else {
            return glyphs[charCode - metadata.startCharacter()];
        }
    }

}
