package slavsquatsuperstar.demos.geometrydash.components;

import mayonez.*;
import mayonez.graphics.*;
import mayonez.graphics.sprites.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * A cursor that shows a preview for the tile to be placed.
 *
 * @author SlavSquatSuperstar
 */
public class PlaceBlockCursor extends GameObject {

    private static final Color CURSOR_COLOR = new Color(255, 255, 255, 127);
    private Sprite cursorSprite;

    public PlaceBlockCursor(String name) {
        super(name);
    }

    @Override
    public void init() {
        cursorSprite = Sprites.createSprite(CURSOR_COLOR);
        addComponent(cursorSprite.setEnabled(false));
    }

    public void setCursorTexture(Texture cursor) {
        cursorSprite.setTexture(cursor);
        cursorSprite.setEnabled(cursor != null); // only show if not null
    }

    public void setPosition(Vec2 position) {
        transform.setPosition(position);
    }

}
