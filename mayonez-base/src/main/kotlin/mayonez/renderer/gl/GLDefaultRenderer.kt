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
import org.lwjgl.opengl.GL11.glLineWidth

/**
 * Draws all sprites and debug information onto the screen using LWJGL's
 * OpenGL library.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.GL)
internal class GLDefaultRenderer : GLRenderer("assets/shaders/default.glsl"),
    SceneRenderer, DebugRenderer {

    // Renderer Parameters
    private val lineStyle: LineStyle = LineStyle.QUADS

    // Renderer Objects
    /*
     * - sprites are permanent
     * - debug shapes are temporary and need to be broken up
     * - texts are permanent and need to be broken up
     */
    private val objects: MutableList<Renderable> = ArrayList() // Sprites and text
    private val tempObjects: MutableList<Renderable> = ArrayList() // Debug shapes
    private val drawObjects: MutableList<GLRenderable> = ArrayList() // Objects to batch

    // Scene Background
    private lateinit var background: Sprite
    private val bgBatch: RenderBatch = RenderBatch(1, DrawPrimitive.SPRITE)

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
            bgBatch.uploadVertices()
            bgBatch.drawBatch()
        }

        // Set GL Properties
        when (lineStyle) {
            LineStyle.SINGLE -> glLineWidth(DebugDraw.DEFAULT_STROKE_SIZE)
            LineStyle.MULTIPLE -> glLineWidth(1f)
            else -> return
        }
    }

    override fun createBatches() {
        /*
         * - sort all objects by z-index and primitive
         * - for each object:
         *   - check that last used batch:
         *     - has vertex room,
         *     - has texture room, and
         *     - has same primitive
         *   - else create new batch with primitive
         *   - push object into batch
         * - delete empty leftover batches?
         */

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
        drawObjects.sortBy { it.zIndex }
        drawObjects.sortBy { it.primitive }
        drawObjects.forEach {
            // Get available batch
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
        super.postRender()
        tempObjects.clear() // Clear debug shapes after each frame
        drawObjects.clear() // Clear batch objects after each frame
    }

    // Helper Methods

    override fun GLRenderable.createNewBatch(): RenderBatch {
        val batch: RenderBatch
        if (this.primitive == DrawPrimitive.SPRITE) {
            batch = MultiZRenderBatch(batchSize, primitive)
            batch.minZIndex = this.zIndex // Set min z-index
            batch.maxZIndex = this.zIndex // Set initial max z-index
        } else {
            batch = SingleZRenderBatch(batchSize, primitive, zIndex)
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
                drawObjects.addAll(getLines(shapePart, this, lineStyle))
            } else if (shapePart is Triangle) {
                drawObjects.add(this.copy(shape = shapePart))
            }
        }
    }

    private fun Renderable?.isAccepted(): Boolean {
        return (this is GLRenderable) || (this is TextLabel)
    }

}