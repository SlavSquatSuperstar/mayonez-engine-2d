package slavsquatsuperstar.demos;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;

/**
 * Draws a text message to the UI using a font.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class TextObject extends GameObject {

    private final String message;
    private final Font font;
    private final Color color;
    private final int fontSize, lineSpacing;

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
        // TODO convert to screen units and transform
        var metadata = font.getMetadata();
        var lineOffset = new Vec2(0, (float) fontSize * (1 + (float) lineSpacing / metadata.glyphHeight()));
        var lines = message.split("\n");

        var linePos = new Vec2();
        for (String line : lines) {
            addSpritesForLine(line, linePos, metadata);
            linePos = linePos.sub(lineOffset);
        }
    }

    private void addSpritesForLine(String line, Vec2 linePos, FontMetadata metadata) {
        var lastCharPos = linePos;
        for (int i = 0; i < line.length(); i++) {
            var charCode = line.charAt(i);
            var glyph = font.getGlyph(charCode);
            if (glyph == null) continue;

            var sprite = createTextSprite(glyph, color, fontSize, lastCharPos);
            addComponent(sprite);

            var charOffset = (float) fontSize * (glyph.getWidth() + metadata.glyphSpacing()) / (float) glyph.getHeight();
            lastCharPos = lastCharPos.add(new Vec2(charOffset, 0));
        }
    }

    private static Sprite createTextSprite(Glyph glyph, Color color, int fontSize, Vec2 charPos) {
        var tex = glyph.getTexture();
        var sprite = Sprites.createSprite(tex);
        sprite.setColor(color);
        sprite.setSpriteTransform(new Transform(charPos, 0f, new Vec2(fontSize)));
        return sprite;
    }

}