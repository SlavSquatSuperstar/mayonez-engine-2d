package mayonez.graphics.font;

import mayonez.*;
import mayonez.graphics.*;
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
    protected Component getRenderedGlyphSprite(GlyphSprite glyphSprite) {
        var sprite = Sprites.createSprite(glyphSprite.texture());
        sprite.setColor(glyphSprite.color());
        sprite.setSpriteTransform(new Transform(glyphSprite.position(),
                0f, glyphSprite.size()));
        return sprite;
    }

}