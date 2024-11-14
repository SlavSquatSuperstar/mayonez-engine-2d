package mayonez.renderer.gl;

import mayonez.graphics.*;
import mayonez.graphics.textures.*;
import mayonez.renderer.*;
import mayonez.renderer.batch.*;

/**
 * An object or component that can be drawn to the screen using the GL engine.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public interface GLRenderable extends Renderable {

    // TODO store shader ID

    // Render Batch Methods

    /**
     * Push this object's vertices to the GPU.
     *
     * @param batch the render batch drawing this object
     */
    void pushToBatch(RenderBatch batch);

    // GL Getters

    /**
     * The max number of objects of this type a {@link RenderBatch} can hold.
     *
     * @return the batch capacity
     */
    int getBatchSize();

    /**
     * Which GL primitive should be used to draw this object.
     *
     * @return the primitive type
     */
    DrawPrimitive getPrimitive();

    /**
     * What texture this object should be drawn with. May be null if drawing a
     * solid color.
     *
     * @return the texture
     */
    default GLTexture getTexture() {
        return null;
    }

}
