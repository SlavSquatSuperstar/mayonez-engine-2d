package mayonez.renderer.gl;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.math.*;
import mayonez.renderer.batch.*;

/**
 * A renderable shape with four vertices that contain position, color, UV, and texture
 * components.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public interface GLQuad extends GLRenderable {

    int MAX_BATCH_SPRITES = 100;

    // Quad Getters

    /**
     * The color of this object ,which should not be null.
     *
     * @return the color
     */
    Color getColor();

    /**
     * Get the on-screen positions of the vertices, in pixels.
     *
     * @return the vertex positions
     */
    Vec2[] getVertexPositions();

    // TODO check vertex count
    @Override
    default void pushToBatch(RenderBatch batch) {
        var glTex = getTexture();
        var texCoords = (glTex != null)
                ? glTex.getTexCoords()
                : GLTexture.DEFAULT_TEX_COORDS;
        // TODO default color
        pushSprite(batch, getVertexPositions(), getColor(),
                texCoords, batch.getTextureSlot(glTex));
    }

    private static void pushSprite(
            RenderBatch batch, Vec2[] positions, Color color, Vec2[] texCoords, int texSlot
    ) {
        var glColor = ColorHelpers.toGLColor(color);
        for (int i = 0; i < positions.length; i++) {
            batch.pushVec2(positions[i]);
            batch.pushVec4(glColor);
            batch.pushVec2(texCoords[i]);
            batch.pushInt(texSlot);
        }
    }

    // GL Getters

    @Override
    default int getBatchSize() {
        return MAX_BATCH_SPRITES;
    }

    @Override
    default DrawPrimitive getPrimitive() {
        return DrawPrimitive.SPRITE;
    }

}
