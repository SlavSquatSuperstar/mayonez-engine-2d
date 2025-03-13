package mayonez.renderer.gl

import mayonez.*
import mayonez.graphics.*
import mayonez.renderer.*
import mayonez.renderer.batch.*
import mayonez.renderer.shader.*
import java.awt.*

/**
 * A base renderer for OpenGL that uploads sprite and shape data to the
 * GPU.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
abstract class GLRenderer(protected val shader: Shader) : Renderer {

    companion object {
        @JvmStatic
        protected val MAX_TEXTURE_SLOTS: Int = 8
    }

    // GPU Resources
    private val batches: MutableList<RenderBatch> = ArrayList()
    protected val textureSlots: IntArray = IntArray(MAX_TEXTURE_SLOTS) { it }

    // Renderer Methods

    override fun clear() {
        batches.forEach(RenderBatch::deleteBatch)
        batches.clear()
    }

    override fun render(g2: Graphics2D?) {
        // Re-buffer objects
        preRender() // Prepare batches and update uniforms
        batches.forEach(RenderBatch::clearVertices)
        createBatches()

        // Draw objects
        batches.sortBy(RenderBatch::getDrawOrder) // Sort batches by z-index
        batches.forEach {
            it.uploadUniforms(viewport, textureSlots)
            it.drawBatch()
        }
        postRender()
    }

    /** Clear the screen and upload resources to the GPU. */
    protected open fun preRender() {}

    /** Sort image data into render batches. */
    protected abstract fun createBatches()

    /** Finish drawing and free resources from the GPU. */
    protected open fun postRender() {
        shader.unbind() // Unbind everything
    }

    // Camera Methods

    // TODO maybe make field
    override fun getViewport(): Viewport = SceneManager.currentScene.camera

    // Batch Helper Methods

    /**
     * Uploads any necessary uniforms for the batch and draws its vertices.
     */
    abstract fun RenderBatch.uploadUniforms(viewport: Viewport, textureSlots: IntArray)

    // TODO see Cherno renderer class
    /**
     * Gets an available render batch for this object or creates a new one it all
     * batches are full.
     */
    protected fun GLRenderable.getAvailableBatch(): RenderBatch {
        val batch = batches.find { it.canFitObject(this) }
        if (batch != null) {
            return batch
        } else {
            val newBatch = this.createNewBatch()
            batches.add(newBatch)
            return newBatch
        }
    }

    /**
     * Creates a new render batch that can draw this object.
     */
    abstract fun GLRenderable.createNewBatch(): RenderBatch

}