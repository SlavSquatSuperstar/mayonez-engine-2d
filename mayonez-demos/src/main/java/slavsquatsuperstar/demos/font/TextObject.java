package slavsquatsuperstar.demos.font;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.math.*;

/**
 * Renders a string using a font. Use {@link SceneTextObject} to draw text at a game object's position, and
 * {@link UITextObject} to draw text in the UI.
 *
 * @author SlavSquatSuperstar
 */
// TODO scale with transform
@UsesEngine(EngineType.GL)
public abstract class TextObject extends GameObject {

    protected final String message;
    protected final Font font;
    protected final Color color;
    protected final int fontSize, lineSpacing;

    public TextObject(String message, Vec2 position, Font font, Color color, int fontSize, int lineSpacing) {
        super("Text Object", position);
        this.message = message;
        this.font = font;
        this.color = color;
        this.fontSize = fontSize;
        this.lineSpacing = lineSpacing;
    }

    @Override
    protected void init() {
        var metadata = font.getMetadata();
        var lineOffset = new Vec2(0, (float) fontSize * (1 + (float) lineSpacing / metadata.glyphHeight()));
        var lines = message.split("\n");

        var linePos = getInitialCharPosition();
        for (String line : lines) {
            addSpritesForLine(line, linePos, metadata);
            linePos = linePos.sub(lineOffset); // Move to the next line
        }
    }

    protected void addSpritesForLine(String line, Vec2 linePos, FontMetadata metadata) {
        var lastCharPos = linePos;
        for (int i = 0; i < line.length(); i++) {
            var charCode = line.charAt(i);
            var glyph = font.getGlyph(charCode);
            if (glyph == null) {
                System.out.printf("char '%c' (%d) skipped\n", charCode, (int) charCode);
                continue;
            }

            // Add the character glyph
            var charSprite = createTextSprite(glyph, color, fontSize, lastCharPos);
            addComponent(charSprite);

            // Move to the next character's position
            var charOffset = getCharOffset(metadata, glyph);
            System.out.printf("char '%c' (%d) offset = %.2f\n", charCode, (int) charCode, charOffset);
            lastCharPos = lastCharPos.add(new Vec2(charOffset, 0));
        }
    }

    private float getCharOffset(FontMetadata metadata, Glyph glyph) {
        if (glyph.getWidth() == 0) return 0;
        return (float) fontSize * (glyph.getWidth() + metadata.glyphSpacing()) / glyph.getHeight();
    }

    // Abstract Methods

    protected abstract Vec2 getInitialCharPosition();

    protected abstract Component createTextSprite(Glyph glyph, Color color, int fontSize, Vec2 charPos);

}

