package mayonez.renderer.gl

import mayonez.application.*
import mayonez.graphics.font.*
import mayonez.renderer.*
import mayonez.renderer.batch.*
import mayonez.renderer.shader.*

/**
 * Draws all user interface elements onto the screen using LWJGL's OpenGL
 * library.
 *
 * @author SlavSquatSuperstar
 */
@UsesBackend(Backend.GL)
internal class GLUIRenderer : GLRenderer(), UIRenderer {

    // Renderer Objects
    private val objects: MutableList<Renderable> = ArrayList()
    private val drawObjects: MutableList<GLRenderable> = ArrayList()

    // Scene Renderer Methods

    override fun addUIElement(r: Renderable?) {
        if (r.isAccepted()) objects.add(r!!)
    }

    override fun removeUIElement(r: Renderable?) {
        if (r.isAccepted()) objects.remove(r!!)
    }

    // Renderer Methods

    override fun clear() {
        super.clear()
        objects.clear()
        drawObjects.clear()
    }

    override fun createBatches() {
        // Sort objects by z-index
        objects.sortBy { it.zIndex }

        // Process objects
        objects.filter { it.shouldRender() }
            .forEach { it.addDrawParts() }

        // Push objects
        var lastBatch: RenderBatch? = null
        drawObjects.forEach {
            // Create new batch
            if (lastBatch == null || !lastBatch.canFitObject(it)) {
                // Don't need to check if batch is closed
                lastBatch = it.getAvailableBatch()
            }
            // Push to batch
            it.pushToBatch(lastBatch)
            if (lastBatch is MultiZRenderBatch) {
                lastBatch.maxZIndex = it.zIndex // Update max z-index
            }
        }
    }

    override fun postRender() {
        drawObjects.clear()
    }

    // Helper Functions

    override fun RenderBatch.uploadUniforms(viewport: Viewport, textureSlots: IntArray) {
        // Upload uniforms
        shader.bind()
        shader.uploadMat4("uTransform", viewport.projectionMatrix)
        shader.uploadIntArray("uTextures", textureSlots)
    }

    override fun GLRenderable.createNewBatch(): RenderBatch {
        val batch = MultiZRenderBatch(
            Shaders.UI_SHADER, primitive, batchSize, MAX_TEXTURE_SLOTS
        )
        batch.minZIndex = this.zIndex // Set min z-index
        batch.maxZIndex = this.zIndex // Set initial max z-index
        return batch
    }

    private fun Renderable.addDrawParts() {
        when (this) {
            is TextLabel -> drawObjects.addAll(this.glyphSprites)
            is GLRenderable -> drawObjects.add(this)
        }
    }

    private fun Renderable?.isAccepted(): Boolean {
        return (this is GLRenderable) || (this is TextLabel)
    }

}
