package mayonez.graphics.font;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.math.*;
import mayonez.renderer.*;

import java.util.*;

/**
 * Renders a string using a font. Use {@link WorldTextLabel} to draw text inside the game world,
 * and {@link UITextLabel} to draw text in the UI.
 *
 * @author SlavSquatSuperstar
 */
public abstract class TextLabel extends Script implements Renderable {

    // Font Fields
    protected String message;
    protected final Vec2 position;
    protected final Font font;
    protected final Color color;
    protected final int fontSize, lineSpacing;

    // Glyph Fields
    private final List<GlyphSprite> glyphSprites;
    private final List<TextLine> lines;
    private Vec2 textSize; // Text bounding box dimension

    public TextLabel(String message, Vec2 position, Font font, Color color, int fontSize, int lineSpacing) {
        this.message = message;
        this.position = position;
        this.font = font;
        this.color = color;
        this.fontSize = fontSize;
        this.lineSpacing = lineSpacing;

        glyphSprites = new ArrayList<>();
        lines = new ArrayList<>();
    }

    @Override
    protected void init() {
        generateGlyphs();
    }

    // (Re-)Generate glyph positions and sizes from message
    private void generateGlyphs() {
        glyphSprites.clear();
        lines.clear();

        // Get glyph offsets
        var strLines = message.split("\n");
        for (var strLine : strLines) {
            var line = getGlyphOffsets(strLine, font.getGlyphSpacing());
            lines.add(line);
        }

        // Calculate text size
        var lineWidths = lines.stream().map(TextLine::getLineWidth).toList();
        var maxWidth = lineWidths.stream().max(Comparator.naturalOrder()).orElse(0f); // Get max line width
        var lineOffset = new Vec2(0, (float) fontSize * (1 + (float) lineSpacing / font.getGlyphHeight()));
        var textHeight = strLines.length * lineOffset.y; // Get height of all lines
        textSize = new Vec2(maxWidth, textHeight);

        System.out.println("widths = " + lineWidths);
        System.out.println("max width = " + maxWidth);
        System.out.println("height = " + textHeight);

        // Add glyph sprites
        var linePos = getInitialGlyphPosition();
        for (var line : lines) {
            addLineSprites(line, linePos);
            linePos = linePos.sub(lineOffset); // Move to the next line
        }
    }

    // Glyph Width Methods

    private TextLine getGlyphOffsets(String line, int glyphSpacing) {
        List<Glyph> glyphs = new ArrayList<>();
        List<Float> glyphOffsets = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            var glyph = font.getGlyph(line.charAt(i));
            if (glyph == null) continue; // Don't make glyphs for non-printable characters

            // Calculate glyph offset
            glyphs.add(glyph);
            glyphOffsets.add(getGlyphOffset(glyphSpacing, glyph));
        }
        return new TextLine(glyphs, glyphOffsets);
    }

    private float getGlyphOffset(int glyphSpacing, Glyph glyph) {
        if (glyph.getWidth() == 0) return 0; // If zero-width return 0
        // Glyph width plus spacing
        return (float) fontSize * (glyph.getWidth() + glyphSpacing) / glyph.getHeight();
    }

    // Glyph Sprite Methods

    private Vec2 getInitialGlyphPosition() {
        return new Vec2(position.x, position.y - fontSize * 0.5f);
    }

    private void addLineSprites(TextLine line, Vec2 linePos) {
        var charPos = linePos;
        for (int i = 0; i < line.numGlyphs(); i++) {
            var glyph = line.getGlyph(i);
            var offset = line.getGlyphOffset(i);

            // Add the character glyph
            var glyphSprite = createGlyphSprite(glyph, fontSize, charPos, color);
            glyphSprites.add(glyphSprite);
            charPos = charPos.add(new Vec2(offset, 0)); // Move to the next glyph
        }
    }

    private GlyphSprite createGlyphSprite(Glyph glyph, int fontSize, Vec2 charPos, Color color) {
        var percentWidth = (float) glyph.getWidth() / glyph.getHeight();
        var spritePos = charPos.add(new Vec2(0.5f * percentWidth * fontSize, 0f));
        var spriteScale = new Vec2(percentWidth * fontSize, fontSize);
        return new GlyphSprite(spritePos, spriteScale, glyph.getTexture(), color, this);
    }

    // Text Methods

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        generateGlyphs();
    }

    public List<GlyphSprite> getGlyphSprites() {
        return glyphSprites;
    }

    // Renderable Methods

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }

}
