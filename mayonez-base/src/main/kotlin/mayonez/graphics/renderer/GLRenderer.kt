package mayonez.graphics.renderer

import mayonez.assets.*
import mayonez.graphics.*
import mayonez.graphics.batch.*
import mayonez.graphics.shader.*
import java.awt.*

/**
 * A base renderer for OpenGL that uploads sprite and shape data to the
 * GPU.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
abstract class GLRenderer(shaderFile: String) : Renderer {

    // GPU Resources
    private val batches: MutableList<RenderBatch> = ArrayList()
    protected val shader: Shader = Assets.getAsset(shaderFile, Shader::class.java)!!

    protected val textureSlots: IntArray = IntArray(RenderBatch.MAX_TEXTURE_SLOTS) { it }
    // support multiple texture IDs in batch ints 0-7

    // Renderer Methods
    override fun start() {
        batches.clear()
    }

    override fun render(g2: Graphics2D?) {
        preRender()
        rebuffer()
        batches.forEach(RenderBatch::drawBatch)
        postRender()
    }

    /** Clear the screen and upload resources to the GPU. */
    protected open fun preRender() {
        shader.bind()
    }

    /** Sort all image data into batches. */
    protected open fun rebuffer() {
        batches.forEach(RenderBatch::clearVertices) // Prepare batches
        createBatches()
        batches.forEach(RenderBatch::uploadVertices) // Finalize batches
        batches.sortBy(RenderBatch::getZIndex) // Sort batches by z-index
    }

    /** Finish drawing and free resources from the GPU. */
    protected open fun postRender() {
        shader.unbind() // Unbind everything
    }

    override fun stop() {
        batches.forEach(RenderBatch::deleteBatch)
    }

    // Batch Helper Methods

    /** Sort image data into render batches. */
    protected abstract fun createBatches()

    protected fun GLRenderable.getAvailableBatch(): RenderBatch {
        return batches.find { this.fitsInBatch(it) }
            ?: this.createNewBatch() // If all batches full
    }

    private fun GLRenderable.fitsInBatch(batch: RenderBatch): Boolean {
        return batch.hasVertexRoom() && (batch.primitive == this.primitive)
                && (batch.zIndex == this.zIndex) && batch.canFitTexture(this)
    }

    private fun RenderBatch.canFitTexture(renderable: GLRenderable): Boolean {
        return this.hasTexture(renderable.texture) || this.hasTextureRoom()
    }

    private fun GLRenderable.createNewBatch(): RenderBatch {
        val batch = RenderBatch(batchSize, zIndex, primitive)
        batches.add(batch)
        return batch
    }

}