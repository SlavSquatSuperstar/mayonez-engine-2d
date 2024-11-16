package mayonez.renderer.gl

import mayonez.graphics.*
import mayonez.graphics.debug.*
import mayonez.graphics.font.*
import mayonez.renderer.*
import mayonez.renderer.batch.*
import mayonez.renderer.shader.*
import org.lwjgl.opengl.GL11.glLineWidth

/**
 * Draws all user interface elements onto the screen using LWJGL's OpenGL
 * library.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
internal class GLUIRenderer(shader: Shader) : GLRenderer(shader), UIRenderer {

    // Renderer Parameters
    private val lineStyle: LineStyle = LineStyle.QUADS

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

    override fun preRender() {
        super.preRender()

        // Upload uniforms
        shader.uploadMat4("uProjection", viewport.projectionMatrix)
        shader.uploadIntArray("uTextures", textureSlots)

        // Set GL Properties
        when (lineStyle) {
            LineStyle.SINGLE -> glLineWidth(DebugDraw.DEFAULT_STROKE_SIZE)
            LineStyle.MULTIPLE -> glLineWidth(1f)
            else -> return
        }
    }

    override fun createBatches() {
        // Sort objects
        objects.sortBy { it.zIndex }

        // Process objects
        objects.filter { it.isEnabled }
            .forEach { it.process() }

        // Push objects
        var lastBatch: RenderBatch? = null
        // Already sorted by z-index
        drawObjects.forEach {
            // Create new batch
            if (lastBatch == null || !lastBatch.canFitObject(it)) {
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

    override fun GLRenderable.createNewBatch(): RenderBatch {
        val batch = MultiZRenderBatch(batchSize, primitive)
        batch.minZIndex = this.zIndex // Set min z-index
        batch.maxZIndex = this.zIndex // Set initial max z-index
        return batch
    }

    private fun Renderable.process() {
        when (this) {
            is GLRenderable -> drawObjects.add(this)
            is TextLabel -> drawObjects.addAll(this.glyphSprites)
        }
    }

    private fun Renderable?.isAccepted(): Boolean {
        return (this is GLRenderable) || (this is TextLabel)
    }

}
