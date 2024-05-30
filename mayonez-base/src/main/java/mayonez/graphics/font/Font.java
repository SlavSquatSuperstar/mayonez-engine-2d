package mayonez.graphics.font;

import mayonez.assets.image.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

import java.io.IOException;

/**
 * A bitmap font created from a spritesheet of a set of characters with contiguous code points.
 *
 * @author SlavSquatSuperstar
 */
public class Font {

    private final GLTexture fontTexture;
    private final FontMetadata metadata;
    private final Glyph[] glyphs;

    public Font(GLTexture fontTexture, FontMetadata metadata, int[] widths) {
        this.fontTexture = fontTexture;
        this.metadata = metadata;
        glyphs = createGlyphs(widths);
    }

    public Font(GLTexture fontTexture, FontMetadata metadata) {
        this.fontTexture = fontTexture;
        this.metadata = metadata;
        var widths = getGlyphWidths(); // TODO test widths
        glyphs = createGlyphs(widths);
    }

    // Create Glyphs Methods

    private Glyph[] createGlyphs(int[] widths) {
        var glyphs = new Glyph[metadata.numCharacters()];
        var texSize = fontTexture.getSize();
        var glyphSize = metadata.glyphHeight();

        // GL uses bottom left as image origin
        var texTopLeft = new Vec2(0, texSize.y);
        var glyphBottomLeft = texTopLeft.sub(new Vec2(0, glyphSize));

        // Create glyph textures
        for (int i = 0; i < glyphs.length; i++) {
            var glyphTex = new GLSpriteSheetTexture(fontTexture, i, glyphBottomLeft, new Vec2(glyphSize)); // TODO fix tex coords width
            glyphs[i] = new Glyph(widths[i], glyphSize, glyphTex);

            // Move to next glyph
            // Origin at bottom left
            glyphBottomLeft.x += glyphSize;
            if (glyphBottomLeft.x >= fontTexture.getWidth()) {
                // If at end of row, go to next row
                glyphBottomLeft.x = 0;
                glyphBottomLeft.y -= glyphSize;
            }
        }
        return glyphs;
    }

    // Glyph Widths Method

    // Auto figure out glyph widths from image file
    private int[] getGlyphWidths() {
        var widths = new int[metadata.numCharacters()];

        // Look at AWT image since no flipping or freeing
        ImageData imgData;
        try {
            imgData = new AWTImageData(fontTexture.getFilename());
        } catch (IOException e) {
            return widths;
        }

        var glyphSize = metadata.glyphHeight();
        var glyphTopLeft = new Vec2(0, 0);

        for (int i = 0; i < widths.length; i++) {
            if (metadata.startCharacter() + i == metadata.whitespaceCharacter()) {
                widths[i] = metadata.whitespaceWidth();
            } else {
                widths[i] = getGlyphWidth(imgData, glyphTopLeft, glyphSize);
            }

            // Move to next glyph
            // Origin at bottom left
            glyphTopLeft.x += glyphSize;
            if (glyphTopLeft.x >= fontTexture.getWidth()) {
                // If at end of row, go to next row
                glyphTopLeft.x = 0;
                glyphTopLeft.y += glyphSize;
            }
        }
        return widths;
    }

    // Auto set glyph width by finding the last filled column
    private int getGlyphWidth(ImageData imageData, Vec2 glyphBottomLeft, int glyphSize) {
        var startX = (int) glyphBottomLeft.x;
        var startY = (int) glyphBottomLeft.y;

        // Find last column with all filled pixels
        var lastFilled = -1;
        for (var col = 0; col < glyphSize; col++) {
            if (!isColumnBlank(imageData, startX, startY, glyphSize, col)) {
                lastFilled = col;
            }
        }
        return lastFilled + 1; // Get column after last filled
    }

    private boolean isColumnBlank(ImageData imageData, int startX, int startY, int glyphHeight, int col) {
        for (var row = 0; row < glyphHeight; row++) {
            var pixAlpha = imageData.getPixelColor(startX + col, startY + row).getAlpha();
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

    public Glyph[] getGlyphs() {
        return glyphs;
    }

    public Glyph getGlyph(int charCode) {
        var index = charCode - metadata.startCharacter();
        if (!MathUtils.inRange(index, 0, metadata.numCharacters() - 1)) {
            return null;
        } else {
            return glyphs[charCode - metadata.startCharacter()];
        }
    }

}
