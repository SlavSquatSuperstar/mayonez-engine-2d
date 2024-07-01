package mayonez.graphics.font;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;

/**
 * A sprite used to render an instance of a font glyph.
 *
 * @param position the position of the sprite
 * @param size     the size of the sprite
 * @param texture  the texture of the sprite
 * @param color    the color of the sprite
 * @author SlavSquatSuperstar
 */
public record GlyphSprite(Vec2 position, Vec2 size, Texture texture, Color color) {
}
