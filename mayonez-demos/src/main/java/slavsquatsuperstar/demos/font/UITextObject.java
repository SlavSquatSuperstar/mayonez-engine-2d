package slavsquatsuperstar.demos.font;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;

/**
 * Draws text to the UI using a font.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class UITextObject extends TextObject {

    public UITextObject(
            String message, Vec2 position, Font font, Color color, int fontSize, int lineSpacing
    ) {
        super(message, position, font, color, fontSize, lineSpacing);
    }

    @Override
    protected Vec2 getInitialCharPosition() {
        return transform.getPosition();
    }

    @Override
    protected Component createTextSprite(Glyph glyph, Color color, int fontSize, Vec2 charPos) {
        var tex = glyph.getTexture();
        var percentWidth = (float) glyph.getWidth() / glyph.getHeight();

        var spritePos = charPos.add(new Vec2(percentWidth * fontSize * 0.5f, 0f));
        var spriteSize = new Vec2(fontSize * percentWidth, fontSize);
        var sprite = new UISprite(spritePos, spriteSize, tex);

        sprite.setColor(color);
        return sprite;
    }

}