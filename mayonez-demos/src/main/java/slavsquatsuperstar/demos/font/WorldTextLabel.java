package slavsquatsuperstar.demos.font;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.font.*;
import mayonez.graphics.sprites.*;
import mayonez.math.*;

/**
 * Draws text inside the world using a font.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class WorldTextLabel extends TextLabel {

    public WorldTextLabel(
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
        var sprite = Sprites.createSprite(tex);

        var percentWidth = (float) glyph.getWidth() / glyph.getHeight();
        var spritePos = charPos.add(new Vec2(0.5f * percentWidth * fontSize, 0f));
        var spriteScale = new Vec2(percentWidth * fontSize, fontSize);
        sprite.setSpriteTransform(new Transform(spritePos, 0f, spriteScale));

        sprite.setColor(color);
        return sprite;
    }

}