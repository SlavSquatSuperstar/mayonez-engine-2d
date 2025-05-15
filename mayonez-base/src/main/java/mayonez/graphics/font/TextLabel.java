package mayonez.graphics.font;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;
import mayonez.renderer.*;
import mayonez.renderer.gl.*;

import java.util.*;

/**
 * Renders a text string to the game world or user interface.
 * The font and style of the text can be controlled.
 *
 * @author SlavSquatSuperstar
 */
public class TextLabel extends Script implements Renderable {

    // Text Fields
    private String message;
    private final UIBounds bounds; // Text bounding box
    private boolean inUI;

    // Text Style
    private Font font;
    private Color color;
    private int fontSize; // Glyph height in pixels/world units
    private float lineSpacing; // Leading in line heights
    private TextAlignment alignment;

    // Glyph Fields
    private final List<GLRenderable> glyphSprites;
    private final List<TextLine> lines;
    private float lineOffset;

    // Dirty Flags
    private boolean widthAndCharsChanged, heightChanged, posAndColorChanged;

    public TextLabel(String message, Vec2 position) {
        this.message = message;
        bounds = new UIBounds(position, new Vec2(), Anchor.CENTER);
        inUI = true;

        setStyle(TextStyle.DEFAULT_STYLE);

        glyphSprites = new ArrayList<>();
        lines = new ArrayList<>();
        lineOffset = fontSize * lineSpacing;
    }

    @Override
    protected void init() {
        // (Re-)Generate glyph positions and sizes from message
        regenerateGlyphs();
    }

    private void regenerateGlyphs() {
        if (widthAndCharsChanged) {
            calculateLineWidths(); // Depends on message, font, and font size
        }
        if (widthAndCharsChanged || heightChanged) {
            lineOffset = fontSize * lineSpacing;
            calculateTextBounds(); // Depends on line spacing
        }
        if (widthAndCharsChanged || heightChanged || posAndColorChanged) {
            generateGlyphSprites(); // Depends on position, anchor, alignment, and color
        }
        widthAndCharsChanged = heightChanged = posAndColorChanged = false;
    }

    // Glyph Size Methods

    private void calculateLineWidths() {
        lines.clear();
        var strLines = message.split("\n");
        for (var strLine : strLines) {
            lines.add(getGlyphOffsets(strLine));
        }
    }

    private TextLine getGlyphOffsets(String line) {
        List<Glyph> glyphs = new ArrayList<>();
        List<Float> glyphOffsets = new ArrayList<>();

        for (int i = 0; i < line.length(); i++) {
            var glyph = font.getGlyph(line.charAt(i));
            if (glyph == null) continue; // Don't make glyphs for non-printable characters

            // Calculate glyph offset
            glyphs.add(glyph);
            glyphOffsets.add(getGlyphOffset(glyph));
        }
        return new TextLine(glyphs, glyphOffsets);
    }

    private float getGlyphOffset(Glyph glyph) {
        if (glyph.width() == 0) return 0; // If zero-width return 0
        // Glyph width plus spacing relative to cap height
        return (float) fontSize * (glyph.width() + font.getGlyphSpacing()) / font.getGlyphHeight();
    }

    // Text Bounds Methods

    private void calculateTextBounds() {
        var maxLineWidth = lines.stream()
                .map(TextLine::getLineWidth)
                .max(Comparator.naturalOrder())
                .orElse(0f); // Get max line width
        var textHeight = lines.size() * lineOffset; // Get height of all lines
        bounds.setSize(new Vec2(maxLineWidth, textHeight)); // Get text bounding box dimensions
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
            // Add the character glyph if not whitespace
            if (!glyph.isWhitespace()) {
                var glyphSprite = createGlyphSprite(glyph, fontSize, charPos, color);
                glyphSprites.add(glyphSprite);
            }
            // Move to the next glyph
            var offset = line.getGlyphOffset(i);
            charPos = charPos.add(new Vec2(offset, 0));
        }
    }

    private GlyphSprite createGlyphSprite(Glyph glyph, int fontSize, Vec2 charPos, Color color) {
        var relativeSize = (float) fontSize / font.getGlyphHeight(); // Percent of cap height
        var spriteScale = new Vec2(glyph.width() * relativeSize, glyph.height() * relativeSize);
        // Line up glyph baselines
        var spriteAscent = new Vec2(0, -glyph.baseline() * relativeSize);
        // Move glyph center to correct position
        var spritePos = charPos.add(spriteScale.mul(0.5f)).add(spriteAscent);
        return new GlyphSprite(spritePos, spriteScale, glyph.texture(), color, this);
    }

    // Text Methods

    public String getMessage() {
        return message;
    }

    public TextLabel setMessage(String message) {
        this.message = message;
        widthAndCharsChanged = true;
        return this;
    }

    public Vec2 getPosition() {
        return bounds.getAnchorPos();
    }

    public TextLabel setPosition(Vec2 position) {
        bounds.setAnchorPos(position);
        posAndColorChanged = true;
        return this;
    }

    public Vec2 getSize() {
        return bounds.getSize();
    }

    // Can't change size without auto resize font or clip lines

    public Anchor getAnchor() {
        return bounds.getAnchorDir();
    }

    public TextLabel setAnchor(Anchor anchor) {
        bounds.setAnchorDir(anchor);
        // Doesn't actually change position
        return this;
    }

    // Text Style Methods

    public Font getFont() {
        return font;
    }

    public TextLabel setFont(Font font) {
        this.font = font;
        widthAndCharsChanged = true;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public TextLabel setColor(Color color) {
        this.color = (color == null) ? Colors.WHITE : color;
        posAndColorChanged = true;
        return this;
    }

    public int getFontSize() {
        return fontSize;
    }

    public TextLabel setFontSize(int fontSize) {
        this.fontSize = fontSize;
        widthAndCharsChanged = true;
        return this;
    }

    public float getLineSpacing() {
        return lineSpacing;
    }

    public TextLabel setLineSpacing(float lineSpacing) {
        this.lineSpacing = lineSpacing;
        heightChanged = true;
        return this;
    }

    public TextAlignment getAlignment() {
        return alignment;
    }

    public TextLabel setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
        posAndColorChanged = true;
        return this;
    }

    /**
     * Get all style attributes of this label.
     *
     * @return the style
     */
    public TextStyle getStyle() {
        return new TextStyle(font, color, fontSize, lineSpacing, alignment);
    }

    /**
     * Set all style attributes from the given style.
     *
     * @param style the style
     * @return this label
     */
    public TextLabel setStyle(TextStyle style) {
        this.font = style.font();
        this.color = style.color();
        this.fontSize = style.fontSize();
        this.lineSpacing = style.lineSpacing();
        this.alignment = style.alignment();

        widthAndCharsChanged = heightChanged = posAndColorChanged = true;
        return this;
    }

    // Renderable Methods

    /**
     * Get the list of glyph sprites used to draw this label and regenerate them
     * if the label has been modified.
     *
     * @return the glyph sprites
     */
    public List<GLRenderable> getGlyphSprites() {
        regenerateGlyphs();
        return glyphSprites;
    }

    @Override
    public int getZIndex() {
        return gameObject.getZIndex();
    }

    @Override
    public boolean isInUI() {
        return inUI;
    }

    /**
     * Set whether to draw this label in the UI rather than the world,
     * true by default.
     *
     * @param inUI if in the UI
     * @return this label
     */
    public TextLabel setInUI(boolean inUI) {
        this.inUI = inUI;
        return this;
    }

}
