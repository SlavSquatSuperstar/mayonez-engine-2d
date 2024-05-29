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
    private final int[] widths;
    private final Glyph[] glyphs;

    public Font(GLTexture fontTexture, FontMetadata metadata, int[] widths) {
        this.fontTexture = fontTexture;
        this.metadata = metadata;
        this.widths = widths;
        glyphs = createGlyphs();
    }

    public Font(GLTexture fontTexture, FontMetadata metadata) {
        this.fontTexture = fontTexture;
        this.metadata = metadata;
        this.widths = new int[metadata.numCharacters()];
        glyphs = createGlyphs3();
        // TODO test widths
    }

    // Create Glyphs Methods

    private Glyph[] createGlyphs() {
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

    private Glyph[] createGlyphs2() {
        var glyphs = new Glyph[metadata.numCharacters()];
        var glyphSize = metadata.glyphHeight();

        // GL uses bottom left as image origin
        var texTopLeft = new Vec2(0, fontTexture.getHeight());
        var glyphBottomLeft = texTopLeft.sub(new Vec2(0, glyphSize));

        // Create glyph textures
        var imgData = fontTexture.getImageData();
        for (int i = 0; i < glyphs.length; i++) {
            // GL uses bottom left as origin
            // Change to top left

            var flippedGlyphBottomLeft = new Vec2(glyphBottomLeft.x, imgData.getHeight() - glyphBottomLeft.y);
            var glyphTopLeft = new Vec2(flippedGlyphBottomLeft.x, flippedGlyphBottomLeft.y - glyphSize);
//            System.out.printf("corner = %s\n", glyphBottomLeft);
//            System.out.printf("flipped = %s\n", flippedGlyphBottomLeft);
            System.out.printf("flipped 2 = %s\n", glyphTopLeft);

            widths[i] = getWidth(imgData, glyphTopLeft, glyphSize);
            System.out.printf("width for %c = %d\n", (char) (metadata.startCharacter() + i), widths[i]);

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
        imgData.freeImage(); // Width keeps changing if image is freed
        return glyphs;
    }

    private Glyph[] createGlyphs3() {
        var glyphs = new Glyph[metadata.numCharacters()];
        var glyphSize = metadata.glyphHeight();

        // Use AWT image since no flipping or freeing
        var glyphTopLeft = new Vec2(0, 0);

        // Create glyph textures
        ImageData imgData;
        try {
            imgData = new AWTImageData(fontTexture.getFilename());
        } catch (IOException e) {
            return glyphs;
        }

        for (int i = 0; i < glyphs.length; i++) {
            if ((char) (metadata.startCharacter() + i) == ' ') {
                widths[i] = 3; // TODO whitespace widths
            } else {
                widths[i] = getWidth(imgData, glyphTopLeft, glyphSize);
            }
            var awtGlyphBottomLeftY = glyphTopLeft.y + glyphSize;
            var glGlyphBottomLeftY = fontTexture.getHeight() - awtGlyphBottomLeftY;

            System.out.println("char = " + (char) (metadata.startCharacter() + i));
            System.out.println("- width = " + widths[i]);
            System.out.println("- top left = " + glyphTopLeft);
            System.out.println("- bottom left = " + glGlyphBottomLeftY);

            var glyphTex = new GLSpriteSheetTexture(fontTexture, i, new Vec2(glyphTopLeft.x, glGlyphBottomLeftY), new Vec2(glyphSize)); // TODO fix tex coords width
            glyphs[i] = new Glyph(widths[i], glyphSize, glyphTex);

            // Move to next glyph
            // Origin at bottom left
            glyphTopLeft.x += glyphSize;
            if (glyphTopLeft.x >= fontTexture.getWidth()) {
                // If at end of row, go to next row
                glyphTopLeft.x = 0;
                glyphTopLeft.y += glyphSize;
            }
        }
        return glyphs;
    }

    // Auto set glyph width by finding the last filled column
    private int getWidth(ImageData imageData, Vec2 glyphBottomLeft, int glyphSize) {
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

    // Font Getters

    public FontMetadata getMetadata() {
        return metadata;
    }

    public int[] getWidths() {
        return widths;
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
