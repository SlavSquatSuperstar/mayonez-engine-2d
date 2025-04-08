package mayonez.graphics.font;

import mayonez.assets.image.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

import java.io.IOException;

/**
 * Assists in getting glyph widths from font texture files.
 *
 * @author SlavSquatSuperstar
 */
final class FontWidthHelper {

    private FontWidthHelper() {
    }

    /**
     * Automatically detect glyph widths from a font sprite sheet.
     *
     * @param metadata    the font metadata
     * @param fontTexture the font texture
     * @return the glyph widths
     */
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
        var splitter = SpriteSplitters.getJSpriteSplitter(
                fontTexture.getSize(), new Vec2(metadata.spriteWidth()),
                new Vec2(0), metadata.numCharacters()
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

    static int[] getGlyphWidths(FontBlockMetadata metadata, Texture fontTexture) {
        var numGlyphs = metadata.numCharacters();
        var widths = new int[numGlyphs];

        // Look at AWT image since no flipping or freeing
        ImageData imgData;
        try {
            imgData = new AWTImageData(fontTexture.getFilename());
        } catch (IOException e) {
            return widths;
        }

        // Get AWT glyph regions
        var splitter = SpriteSplitters.getJSpriteSplitter(
                fontTexture.getSize(),
                new Vec2(metadata.spriteWidth(), metadata.spriteHeight()),
                new Vec2(0), numGlyphs
        );
        var regions = splitter.getSpriteRegions();

        for (var i = 0; i < widths.length; i++) {
            widths[i] = getGlyphWidth(imgData, regions[i]);
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

}
