package mayonez.renderer.gl

import mayonez.*
import mayonez.graphics.*
import mayonez.graphics.debug.*
import mayonez.graphics.font.*
import mayonez.graphics.sprites.*
import mayonez.math.*
import mayonez.math.shapes.*
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
internal class GLDefaultRenderer(shader: Shader) : GLRenderer(shader),
    SceneRenderer, DebugRenderer {

    // Renderer Objects
    private val objects: MutableList<Renderable> = ArrayList() // Sprites and text
    private val tempObjects: MutableList<Renderable> = ArrayList() // Debug shapes
    private val drawObjects: MutableList<GLRenderable> = ArrayList() // Objects to batch

    // Scene Background
    private lateinit var background: Sprite
    private val bgBatch: RenderBatch = RenderBatch(DrawPrimitive.SPRITE, 1, 1)

    // Scene Renderer Methods

    override fun setBackground(background: Sprite, sceneSize: Vec2, sceneScale: Float) {
        this.background = background
            .setSpriteTransform(Transform.scaleInstance(sceneSize * sceneScale))
    }

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
        bgBatch.clearVertices()
        objects.clear()
        tempObjects.clear()
        drawObjects.clear()
    }

    override fun preRender() {
        super.preRender()

        // Upload uniforms
        val cam = viewport
        shader.uploadMat4("uView", cam.viewMatrix)
        shader.uploadMat4("uProjection", cam.projectionMatrix)
        shader.uploadIntArray("uTextures", textureSlots)

        // Draw background
        if (background.getTexture() == null) {
            // Draw background color
            val bgColor = background.getColor().toGL()
            GLHelper.clearScreen(bgColor.x, bgColor.y, bgColor.z, 1f)
        } else {
            // Draw background sprite
            bgBatch.clearVertices()
            (background as GLSprite).pushToBatch(bgBatch)
            bgBatch.drawBatch()
        }
    }

    override fun createBatches() {
        // Sort objects
        objects.sortBy { it.zIndex }
        tempObjects.sortBy { it.zIndex }

        // Process objects
        objects.filter { it.isEnabled }
            .forEach { it.process() }
        tempObjects.filter { it.isEnabled }
            .forEach { it.process() }

        // Push objects
        var lastBatch: RenderBatch? = null
        // Already sorted by primitive
        drawObjects.sortBy { it.zIndex }
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

    override fun GLRenderable.createNewBatch(): RenderBatch {
        val batch: RenderBatch
        if (primitive == DrawPrimitive.SPRITE) {
            batch = MultiZRenderBatch(primitive, batchSize, MAX_TEXTURE_SLOTS)
            batch.minZIndex = this.zIndex // Set min z-index
            batch.maxZIndex = this.zIndex // Set initial max z-index
        } else {
            batch = SingleZRenderBatch(primitive, batchSize, MAX_TEXTURE_SLOTS, zIndex)
        }
        return batch
    }

    private fun Renderable.process() {
        when (this) {
            is DebugShape -> this.processShape()
            is GLRenderable -> drawObjects.add(this)
            is TextLabel -> drawObjects.addAll(this.glyphSprites)
        }
    }

    private fun DebugShape.processShape() {
        getParts().forEach { shapePart ->
            if (shapePart is Edge) {
                drawObjects.addAll(getLines(shapePart, this))
            } else if (shapePart is Triangle) {
                drawObjects.add(this.copy(shape = shapePart))
            }
        }
    }

    private fun Renderable?.isAccepted(): Boolean {
        return (this is GLRenderable) || (this is TextLabel)
    }

}