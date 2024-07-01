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
    protected Component getRenderedGlyphSprite(GlyphSprite glyphSprite) {
        var sprite = new UISprite(glyphSprite.position(), glyphSprite.size(), glyphSprite.texture());
        sprite.setColor(color);
        return sprite;
    }

}