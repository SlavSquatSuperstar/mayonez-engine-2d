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
        batches.forEach { obj: RenderBatch -> obj.render() } // Draw everything
        postRender()
    }

    /** Upload resources to the GPU. */
    protected open fun preRender() {
        // Bind shader and upload uniforms
        shader.bind()
        val camera = camera as GLCamera
        shader.uploadMat4("uProjection", camera.projectionMatrix)
        shader.uploadMat4("uView", camera.viewMatrix)
    }

    /** Sort all image data into batches. */
    protected open fun rebuffer() {
        batches.forEach { obj: RenderBatch -> obj.clear() } // Prepare batches
        createBatches()
        batches.forEach { obj: RenderBatch -> obj.upload() } // Finalize batches
        batches.sortBy { obj: RenderBatch -> obj.zIndex } // Sort batches by z-index
    }

    /** Free resources from the GPU. */
    protected open fun postRender() {
        shader.unbind() // Unbind everything
    }

    override fun stop() {
        batches.forEach { obj: RenderBatch -> obj.delete() }
    }

    // Batch Helper Methods

    /** Sort image data into render batches. */
    protected abstract fun createBatches()

    protected fun getAvailableBatch(obj: GLRenderable): RenderBatch {
        for (batch in batches) {
            if (doesBatchFit(batch, obj)) return batch
        }
        // If all batches full
        return createNewBatch(obj)
    }

    private fun doesBatchFit(batch: RenderBatch, obj: GLRenderable): Boolean {
        val samePrimitive = batch.primitive === obj.primitive
        val sameZIndex = batch.zIndex == obj.zIndex
        val fitsTexture = batch.hasTexture(obj.texture) || batch.hasTextureRoom()
        return batch.hasVertexRoom() && samePrimitive && sameZIndex && fitsTexture
    }

    private fun createNewBatch(obj: GLRenderable): RenderBatch {
        val batch = RenderBatch(obj.batchSize, obj.zIndex, obj.primitive)
        batch.clear()
        batches.add(batch)
        return batch
    }
}