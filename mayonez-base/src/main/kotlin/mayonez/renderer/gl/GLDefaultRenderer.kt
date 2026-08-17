package mayonez.renderer.gl

import mayonez.graphics.*
import mayonez.graphics.debug.*
import mayonez.graphics.font.*
import mayonez.renderer.*
import mayonez.renderer.batch.*
import mayonez.renderer.shader.*

/**
 * Draws all sprites and debug information onto the screen using LWJGL's
 * OpenGL library.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
internal class GLDefaultRenderer() : GLRenderer(),
    SceneRenderer, DebugRenderer {

    // Renderer Objects
    private val objects: MutableList<Renderable> = ArrayList() // Sprites and text
    private val tempObjects: MutableList<Renderable> = ArrayList() // Debug shapes
    private val drawObjects: MutableList<GLRenderable> = ArrayList() // Objects to batch

    // Scene Renderer Methods

    override fun addRenderable(r: Renderable?) {
        if (r.isAccepted()) objects.add(r!!)
    }

    override fun removeRenderable(r: Renderable?) {
        if (r.isAccepted()) objects.remove(r!!)
    }

    // Debug Renderer Methods

    override fun addShape(shape: DebugShape?) {
        if (shape != null) tempObjects.add(shape)
    }

    // Renderer Methods

    override fun clear() {
        super.clear()
        objects.clear()
        tempObjects.clear()
        drawObjects.clear()
    }

    override fun preRender() {
        // Update view matrix
        val cam = viewport
        cam.updateViewMatrix()

        // Draw background color
        val bgColor = cam.backgroundColor.toGL()
        GLHelper.clearScreen(bgColor.x, bgColor.y, bgColor.z, 1f)
    }

    override fun createBatches() {
        // Sort objects by z-index
        objects.sortBy { it.zIndex }
        tempObjects.sortBy { it.zIndex }

        // Process objects
        objects.filter { it.shouldRender() }
            .forEach { it.addDrawParts() }
        tempObjects.filter { it.shouldRender() }
            .forEach { it.addDrawParts() }
        drawObjects.sortBy { it.zIndex }

        // Push objects
        var lastBatch: RenderBatch? = null
        drawObjects.forEach {
            // Create new batch
            if (lastBatch == null || !lastBatch.canFitObject(it)) {
                if (lastBatch is MultiZRenderBatch) {
                    lastBatch.isClosed = true // Don't use this batch anymore
                }
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
        super.postRender()
        tempObjects.clear() // Clear debug shapes after each frame
        drawObjects.clear() // Clear batch objects after each frame
    }

    // Helper Methods

    override fun RenderBatch.uploadUniforms(viewport: Viewport, textureSlots: IntArray) {
        // Upload uniforms
        shader.bind()
        shader.uploadMat4("uTransform", viewport.viewProjectionMatrix)
        shader.uploadIntArray("uTextures", textureSlots)
    }

    override fun GLRenderable.createNewBatch(): RenderBatch {
        val batch: RenderBatch
        if (primitive == DrawPrimitive.SPRITE) {
            batch = MultiZRenderBatch(
                Shaders.DEFAULT_SHADER, primitive, batchSize, MAX_TEXTURE_SLOTS
            )
            batch.minZIndex = this.zIndex // Set min z-index
            batch.maxZIndex = this.zIndex // Set initial max z-index
        } else {
            val shader = when (primitive) {
                DrawPrimitive.CIRCLE -> Shaders.CIRCLE_SHADER
                DrawPrimitive.ELLIPSE -> Shaders.ELLIPSE_SHADER
                else -> Shaders.DEBUG_SHADER
            }
            batch = SingleZRenderBatch(
                shader, primitive, batchSize, 0, zIndex
            )
        }
        return batch
    }

    private fun Renderable.addDrawParts() {
        val cam = viewport
        val zoom = cam.zoom * cam.cameraScale
        when (this) {
            is DebugShape -> drawObjects.addAll(this.getDrawParts(zoom))
            is TextLabel -> drawObjects.addAll(this.glyphSprites)
            is GLRenderable -> drawObjects.add(this)
        }
    }

    private fun Renderable?.isAccepted(): Boolean {
        return (this is GLRenderable) || (this is TextLabel)
    }

}