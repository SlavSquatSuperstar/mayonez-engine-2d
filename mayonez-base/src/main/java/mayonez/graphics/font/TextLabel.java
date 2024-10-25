package mayonez.graphics.font;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import mayonez.renderer.*;

import java.util.*;

/**
 * Renders a string using a font. Use {@link WorldTextLabel} to draw text inside the game world,
 * and {@link UITextLabel} to draw text in the UI.
 *
 * @author SlavSquatSuperstar
 */
// TODO font transform, set size
public abstract class TextLabel extends Script implements Renderable {

    // Text Fields
    private String message;
    protected final Font font;
    protected final UIBounds bounds; // Text bounding box

    // Text Style
    private Color color;
    private int fontSize, lineSpacing;
    private TextAlignment alignment;

    // Glyph Fields
    private final List<GlyphSprite> glyphSprites;
    private final List<TextLine> lines;
    private float lineOffset;

    public TextLabel(String message, Vec2 position, Font font, Color color, int fontSize, int lineSpacing) {
        this.message = message;
        this.font = font;
        this.color = color;
        this.fontSize = fontSize;
        this.lineSpacing = lineSpacing;
        alignment = TextAlignment.LEFT;

        bounds = new UIBounds(position, new Vec2(), Anchor.CENTER);
        glyphSprites = new ArrayList<>();
        lines = new ArrayList<>();
    }

    @Override
    protected void init() {
        // (Re-)Generate glyph positions and sizes from message
        calculateGlyphSizes();
        calculateTextBounds();
        generateGlyphSprites();
    }

    // Glyph Size Methods

    private void calculateGlyphSizes() {
        lines.clear();
        var strLines = message.split("\n");
        for (var strLine : strLines) {
            var line = getGlyphOffsets(strLine, font.getGlyphSpacing());
            lines.add(line);
        }
    }

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

    private void calculateTextBounds() {
        var lineWidths = lines.stream().map(TextLine::getLineWidth).toList();
        var maxWidth = lineWidths.stream().max(Comparator.naturalOrder()).orElse(0f); // Get max line width
        lineOffset = (float) fontSize * (1 + (float) lineSpacing / font.getGlyphHeight());
        var textHeight = lines.size() * lineOffset; // Get height of all lines
        var boundsSize = new Vec2(maxWidth, textHeight); // Get text bounding box dimensions
        bounds.setSize(boundsSize);
    }

    // Glyph Sprite Methods

    private void generateGlyphSprites() {
        glyphSprites.clear();
        // Add glyphs from top left
        var lineY = getInitialLineY();
        for (var line : lines) {
            var lineStartX = getInitialGlyphX(line);
            addLineSprites(line, new Vec2(lineStartX, lineY));
            lineY -= lineOffset; // Move to the next line
        }
    }

    private float getInitialLineY() {
        return bounds.getPosition(Anchor.TOP).y - fontSize * 0.5f; // Move down by half glyph height
    }

    private float getInitialGlyphX(TextLine line) {
        switch (alignment) {
            case CENTER -> {
                return bounds.getPosition(Anchor.CENTER).x - line.getLineWidth() * 0.5f;
            }
            case RIGHT -> {
                return bounds.getPosition(Anchor.RIGHT).x - line.getLineWidth();
            }
            default -> { // Left or null
                return bounds.getPosition(Anchor.LEFT).x;
            }
        }
    }

    private void addLineSprites(TextLine line, Vec2 lineStartPos) {
        var charPos = lineStartPos;
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
        var spritePos = charPos.add(new Vec2(0.5f * percentWidth * fontSize, 0f)); // Move right by half glyph width
        var spriteScale = new Vec2(percentWidth * fontSize, fontSize);
        return new GlyphSprite(spritePos, spriteScale, glyph.getTexture(), color, this);
    }

    // Text Methods

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        calculateGlyphSizes();
        calculateTextBounds();
        generateGlyphSprites();
    }

    public Vec2 getPosition() {
        return bounds.getAnchorPos();
    }

    public void setPosition(Vec2 position) {
        bounds.setAnchorPos(position);
    }

    public Vec2 getSize() {
        return bounds.getSize();
    }

    public Anchor getAnchor() {
        return bounds.getAnchorDir();
    }

    public void setAnchor(Anchor anchor) {
        bounds.setAnchorDir(anchor);
    }

    // Text Style Methods

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = (color == null) ? Colors.WHITE : color;
        generateGlyphSprites();
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        calculateGlyphSizes();
        calculateTextBounds();
        generateGlyphSprites();
    }

    public int getLineSpacing() {
        return lineSpacing;
    }

    public void setLineSpacing(int lineSpacing) {
        this.lineSpacing = lineSpacing;
        calculateTextBounds();
        generateGlyphSprites();
    }

    public TextAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
        generateGlyphSprites();
    }

    // Renderable Methods

    public List<GlyphSprite> getGlyphSprites() {
        return glyphSprites;
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }

}
