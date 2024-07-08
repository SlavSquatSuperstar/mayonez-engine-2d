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
 * @param position the position of the sprite
 * @param size     the size of the sprite
 * @param texture  the texture of the sprite
 * @param color    the color of the sprite
 * @param text     the parent text component
 * @author SlavSquatSuperstar
 */
public record GlyphSprite(
        Vec2 position, Vec2 size,
        Texture texture, Color color,
        TextLabel text
) implements GLRenderable {

    // Renderable Methods

    @Override
    public void pushToBatch(RenderBatch batch) {
        if (texture instanceof GLTexture glTex) {
            var sceneScale = text.getScene().getScale();
            var sprVertices = new Rectangle(position.mul(sceneScale), size.mul(sceneScale))
                    .getVertices();
            var texCoords = glTex.getTexCoords();
            var texID = batch.getTextureSlot(glTex);
            BatchPushHelper.pushSprite(batch, sprVertices, color, texCoords, texID);
        }
    }

    @Override
    public int getBatchSize() {
        return RenderBatch.MAX_GLYPHS;
    }

    @Override
    public DrawPrimitive getPrimitive() {
        return DrawPrimitive.SPRITE;
    }

    @Override
    public int getZIndex() {
        return text.getZIndex();
    }

    @Override
    public boolean isEnabled() {
        return text.isEnabled();
    }

}
