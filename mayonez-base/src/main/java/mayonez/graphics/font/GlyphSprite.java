package mayonez.graphics.font;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.math.shapes.*;
import mayonez.renderer.batch.*;
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

    // Quad Methods

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Vec2[] getVertexPositions() {
        var sceneScale = isInUI() ? 1 : text.getScene().getScale();
        return new BoundingBox(position.mul(sceneScale), size.mul(sceneScale))
                .getVertices();
    }

    // GL Methods

    @Override
    public int getBatchSize() {
        return RenderBatch.MAX_GLYPHS;
    }

    @Override
    public DrawPrimitive getPrimitive() {
        return DrawPrimitive.SPRITE;
    }

    @Override
    public GLTexture getTexture() {
        return (texture instanceof GLTexture glTex) ? glTex : null;
    }

    // Quad Methods

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
