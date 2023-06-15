package mayonez.graphics.renderer

import mayonez.*
import mayonez.annotations.*
import mayonez.graphics.*
import mayonez.graphics.textures.*
import mayonez.graphics.camera.*
import mayonez.io.*
import mayonez.io.image.*
import java.awt.*

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
        shader = Assets.getAsset(shaderFile, Shader::class.java)!!
        textureSlots = IntArray(Preferences.maxTextureSlots) { it } // ints 0-7
    }

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
        shader.uploadUniforms(camera as GLCamera)
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
        batch.clearVertices()
        batches.add(batch)
        return batch
    }

}