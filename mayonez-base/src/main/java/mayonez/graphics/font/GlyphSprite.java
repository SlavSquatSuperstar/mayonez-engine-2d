package mayonez.graphics.font;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.renderer.gl.*;

/**
 * A sprite used to render an instance of a font glyph.
 *
 * @param position the center of the sprite
 * @param size     the size of the sprite
 * @param texture  the texture of the sprite
 * @param color    the color of the sprite
 * @param text     the parent text component
 * @author SlavSquatSuperstar
 */
record GlyphSprite(
        Vec2 position, Vec2 size,
        Texture texture, Color color,
        TextLabel text
) implements GLQuad {

    private static final int MAX_BATCH_GLYPHS = 200; // currently does nothing

    // Quad Methods

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Vec2[] getVertexPositions() {
        return new BoundingBox(position, size).getVertices();
    }

    // GL Methods

    @Override
    public int getBatchSize() {
        return MAX_BATCH_GLYPHS;
    }

    @Override
    public GLTexture getTexture() {
        return (texture instanceof GLTexture glTex) ? glTex : null;
    }

    // Renderable Methods

    @Override
    public int getZIndex() {
        return text.getZIndex();
    }

    @Override
    public boolean isEnabled() {
        return text.isEnabled();
    }

    @Override
    public boolean isInUI() {
        return text.isInUI();
    }

}
