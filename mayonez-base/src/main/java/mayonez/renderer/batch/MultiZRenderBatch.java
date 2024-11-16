package mayonez.renderer.batch;

import mayonez.graphics.*;
import mayonez.renderer.gl.*;

/**
 * A render batch that draws objects with a range of z-indices and uses the minimum
 * z-index as its sorting order.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
public class MultiZRenderBatch extends RenderBatch {

    private int minZIndex, maxZIndex;
    private boolean closed;

    public MultiZRenderBatch(DrawPrimitive primitive, int maxBatchObjects, int maxTextureSlots) {
        super(primitive, maxBatchObjects, maxTextureSlots);
    }

    // Z-Index Methods

    public int getMinZIndex() {
        return minZIndex;
    }

    public void setMinZIndex(int minZIndex) {
        this.minZIndex = minZIndex;
    }

    public int getMaxZIndex() {
        return maxZIndex;
    }

    public void setMaxZIndex(int maxZIndex) {
        this.maxZIndex = maxZIndex;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    // Batch Overrides


    @Override
    public void clearVertices() {
        super.clearVertices();
        maxZIndex = minZIndex;
        closed = false;
    }

    @Override
    public boolean canFitObject(GLRenderable r) {
        return !closed && (r.getZIndex() >= this.minZIndex)
//                && (r.getZIndex() <= this.maxZIndex)
                && super.canFitObject(r);
    }

    @Override
    public int getDrawOrder() {
        return minZIndex;
    }

    @Override
    public String toString() {
        return String.format(
                "RenderBatch (Type: %s, Capacity: %d/%d, Z-Index: [%d, %d])",
                getPrimitive(), getNumBatchObjects(), getMaxBatchObjects(),
                minZIndex, maxZIndex
        );
    }

}
