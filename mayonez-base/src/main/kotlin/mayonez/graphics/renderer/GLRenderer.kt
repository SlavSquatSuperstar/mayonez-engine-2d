package mayonez.graphics.renderer

import mayonez.Preferences.maxTextureSlots
import mayonez.annotations.EngineType
import mayonez.annotations.UsesEngine
import mayonez.graphics.GLCamera
import mayonez.graphics.GLRenderable
import mayonez.graphics.RenderBatch
import mayonez.io.Assets.getShader
import mayonez.io.image.Shader
import java.awt.Graphics2D

/**
 * Contains methods for uploading sprite and shape data to the GPU for
 * renderers using OpenGL.
 */
@UsesEngine(EngineType.GL)
abstract class GLRenderer(shaderFile: String) : Renderer {

    // GPU Resources
    private val batches: MutableList<RenderBatch>
    protected val shader: Shader
    protected val textureSlots: IntArray // support multiple texture IDs in batch

    init {
        batches = ArrayList()
        shader = getShader(shaderFile)!!
        textureSlots = IntArray(maxTextureSlots) { it } // ints 0-7
    }

    // Renderer Methods
    override fun start() {
        batches.clear()
    }

    override fun render(g2: Graphics2D?) {
        preRender()
        rebuffer()
        batches.forEach { batch: RenderBatch -> batch.drawBatch() }
        postRender()
    }

    /** Clear the screen and upload resources to the GPU. */
    protected open fun preRender() {
        shader.bind()
        shader.uploadUniforms(camera as GLCamera)
    }

    /** Sort all image data into batches. */
    protected open fun rebuffer() {
        batches.forEach { obj: RenderBatch -> obj.clear() } // Prepare batches
        createBatches()
        batches.forEach { obj: RenderBatch -> obj.uploadVertices() } // Finalize batches
        batches.sortBy { obj: RenderBatch -> obj.zIndex } // Sort batches by z-index
    }

    /** Finish drawing and free resources from the GPU. */
    protected open fun postRender() {
        shader.unbind() // Unbind everything
    }

    override fun stop() {
        batches.forEach { obj: RenderBatch -> obj.deleteBatch() }
    }

    // Batch Helper Methods

    /** Sort image data into render batches. */
    protected abstract fun createBatches()

    protected fun GLRenderable.getAvailableBatch(): RenderBatch {
        for (batch in batches) {
            if (this.fitsInBatch(batch)) return batch
        }
        return this.createNewBatch() // If all batches full
    }

    private fun GLRenderable.fitsInBatch(batch: RenderBatch): Boolean {
        val samePrimitive = batch.primitive === this.primitive
        val sameZIndex = batch.zIndex == this.zIndex
        val fitsTexture = batch.hasTexture(this.texture) || batch.hasTextureRoom()
        return batch.hasVertexRoom() && samePrimitive && sameZIndex && fitsTexture
    }

    private fun GLRenderable.createNewBatch(): RenderBatch {
        val batch = RenderBatch(batchSize, zIndex, primitive)
        batch.clear()
        batches.add(batch)
        return batch
    }

}