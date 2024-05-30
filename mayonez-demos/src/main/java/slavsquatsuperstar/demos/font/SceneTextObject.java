package slavsquatsuperstar.demos.font;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;

/**
 * Draws text inside the scene using a font.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class SceneTextObject extends TextObject {

    public SceneTextObject(
            String message, Vec2 position, Font font, Color color, int fontSize, int lineSpacing
    ) {
        super(message, position, font, color, fontSize, lineSpacing);
    }

    @Override
    protected Vec2 getInitialCharPosition() {
        return new Vec2();
    }

    @Override
    protected Component createTextSprite(Glyph glyph, Color color, int fontSize, Vec2 charPos) {
        var tex = glyph.getTexture();
        var sprite = Sprites.createSprite(tex);

        var percentWidth = (float) glyph.getWidth() / glyph.getHeight();
        var spritePos = charPos.add(new Vec2(percentWidth * fontSize * 0.5f, 0f));
        var spriteScale = new Vec2(fontSize * percentWidth, fontSize);
        sprite.setSpriteTransform(new Transform(spritePos, 0f, spriteScale));

        sprite.setColor(color);
        return sprite;
    }

}