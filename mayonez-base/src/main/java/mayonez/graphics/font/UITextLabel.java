package mayonez.graphics.font;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.ui.*;
import mayonez.math.*;

/**
 * Draws text to the UI using a font.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class UITextLabel extends TextLabel {

    public UITextLabel(
            String message, Vec2 position, Font font, Color color, int fontSize, int lineSpacing
    ) {
        super(message, position, font, color, fontSize, lineSpacing);
    }

    @Override
    protected Vec2 getInitialCharPosition() {
        return position;
    }

    @Override
    protected Component createTextSprite(Glyph glyph, Color color, int fontSize, Vec2 charPos) {
        var tex = glyph.getTexture();
        var percentWidth = (float) glyph.getWidth() / glyph.getHeight();

        var spritePos = charPos.add(new Vec2(0.5f * percentWidth * fontSize, 0f));
        var spriteSize = new Vec2(percentWidth * fontSize, fontSize);
        var sprite = new UISprite(spritePos, spriteSize, tex);

        sprite.setColor(color);
        return sprite;
    }

}