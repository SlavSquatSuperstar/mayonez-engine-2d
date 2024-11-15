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

    public MultiZRenderBatch(int maxBatchObjects, DrawPrimitive primitive) {
        super(maxBatchObjects, primitive);
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

    // Batch Overrides


    @Override
    public void clearVertices() {
        super.clearVertices();
        maxZIndex = minZIndex;
    }

    @Override
    public boolean canFitObject(GLRenderable r) {
        return (r.getZIndex() >= this.minZIndex)
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
