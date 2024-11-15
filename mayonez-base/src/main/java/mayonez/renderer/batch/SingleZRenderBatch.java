package mayonez.renderer.batch;

import mayonez.graphics.*;
import mayonez.renderer.gl.*;

/**
 * A render batch that draws objects with the same z-index and uses the z-index
 * as its sorting order.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class SingleZRenderBatch extends RenderBatch {

    private final int zIndex;

    public SingleZRenderBatch(int maxBatchObjects, DrawPrimitive primitive, int zIndex) {
        super(maxBatchObjects, primitive);
        this.zIndex = zIndex;
    }

    // Z-Index Methods

    public int getZIndex() {
        return zIndex;
    }

    // Batch Overrides

    @Override
    public boolean canFitObject(GLRenderable r) {
        return (r.getZIndex() == this.zIndex) && super.canFitObject(r);
    }

    @Override
    public int getDrawOrder() {
        return zIndex;
    }

    @Override
    public String toString() {
        return String.format(
                "RenderBatch (Type: %s, Capacity: %d/%d, Z-Index: %d)",
                getPrimitive(), getNumBatchObjects(), getMaxBatchObjects(), zIndex
        );
    }

}
