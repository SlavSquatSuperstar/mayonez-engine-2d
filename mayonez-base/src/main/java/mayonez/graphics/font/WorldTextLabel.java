package mayonez.graphics.font;

import mayonez.graphics.*;
import mayonez.math.*;
import mayonez.math.shapes.*;

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
    public boolean isInUI() {
        return false;
    }

    @Override
    protected void debugRender() {
        // Check glyph positions
        getScene().getDebugDraw().drawShape(
                new Rectangle(bounds.getCenter(), getSize()), Colors.BLACK
        );
    }
}