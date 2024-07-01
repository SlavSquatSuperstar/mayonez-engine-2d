package mayonez.graphics.font;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.math.*;

import java.util.*;

/**
 * Renders a string using a font. Use {@link WorldTextLabel} to draw text inside the game world,
 * and {@link UITextLabel} to draw text in the UI.
 *
 * @author SlavSquatSuperstar
 */
public abstract class TextLabel extends Script {

    protected final String message;
    protected final Vec2 position;
    protected final Font font;
    protected final Color color;
    protected final int fontSize, lineSpacing;
    private final List<GlyphSprite> glyphSprites;
    private final List<Component> renderedGlyphSprites;

    public TextLabel(String message, Vec2 position, Font font, Color color, int fontSize, int lineSpacing) {
        this.message = message;
        this.position = position;
        this.font = font;
        this.color = color;
        this.fontSize = fontSize;
        this.lineSpacing = lineSpacing;
        glyphSprites = new ArrayList<>();
        renderedGlyphSprites = new ArrayList<>();
    }

    @Override
    protected void init() {
        var lineOffset = new Vec2(0, (float) fontSize * (1 + (float) lineSpacing / font.getGlyphHeight()));
        var lines = message.split("\n");

        var linePos = getInitialCharPosition();
        for (String line : lines) {
            addSpritesForLine(line, linePos, font.getGlyphSpacing());
            linePos = linePos.sub(lineOffset); // Move to the next line
        }
    }

    protected void addSpritesForLine(String line, Vec2 linePos, int glyphSpacing) {
        var lastCharPos = linePos;
        for (int i = 0; i < line.length(); i++) {
            var charCode = line.charAt(i);
            var glyph = font.getGlyph(charCode);
            if (glyph == null) continue;

            // Add the character glyph
            var glyphSprite = createGlyphSprite(glyph, fontSize, lastCharPos, color);
            glyphSprites.add(glyphSprite);
            var renderedSprite = getRenderedGlyphSprite(glyphSprite);
            renderedGlyphSprites.add(renderedSprite);
            gameObject.addComponent(renderedSprite);

            // Move to the next character's position
            var charOffset = getCharOffset(glyphSpacing, glyph);
            lastCharPos = lastCharPos.add(new Vec2(charOffset, 0));
        }
    }

    private float getCharOffset(int glyphSpacing, Glyph glyph) {
        if (glyph.getWidth() == 0) return 0;
        return (float) fontSize * (glyph.getWidth() + glyphSpacing) / glyph.getHeight();
    }

    // Abstract Methods

    protected Vec2 getInitialCharPosition() {
        return position;
    }

    protected GlyphSprite createGlyphSprite(Glyph glyph, int fontSize, Vec2 charPos, Color color) {
        var percentWidth = (float) glyph.getWidth() / glyph.getHeight();
        var spritePos = charPos.add(new Vec2(0.5f * percentWidth * fontSize, 0f));
        var spriteScale = new Vec2(percentWidth * fontSize, fontSize);
        return new GlyphSprite(spritePos, spriteScale, glyph.getTexture(), color);
    }

    protected abstract Component getRenderedGlyphSprite(GlyphSprite glyphSprite);

    @Override
    protected void onEnable() {
        renderedGlyphSprites.forEach(c -> c.setEnabled(true));
    }

    @Override
    protected void onDisable() {
        renderedGlyphSprites.forEach(c -> c.setEnabled(false));
    }

    // Text Methods

    public String getMessage() {
        return message;
    }

    public List<Component> getRenderedGlyphSprites() {
        return renderedGlyphSprites;
    }

}
