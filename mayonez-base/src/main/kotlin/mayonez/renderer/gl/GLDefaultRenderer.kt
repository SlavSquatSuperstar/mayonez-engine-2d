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

    // Scene Background
    private lateinit var background: Sprite
    private val bgBatch: RenderBatch = RenderBatch(1, 0, DrawPrimitive.SPRITE)

    // Scene Renderer Methods

    override fun setBackground(background: Sprite, sceneSize: Vec2, sceneScale: Float) {
        this.background = background
            .setSpriteTransform(Transform.scaleInstance(sceneSize * sceneScale))
    }

    override fun addRenderable(r: Renderable?) {
        if (r is Renderable) objects.add(r)
    }

    override fun removeRenderable(r: Renderable?) {
        if (r is Renderable) objects.remove(r)
    }

    // Debug Renderer Methods

    override fun addShape(shape: DebugShape) {
        tempObjects.add(shape)
    }

    // Renderer Methods

    override fun clear() {
        super.clear()
        bgBatch.clearVertices()
        objects.clear()
        tempObjects.clear()
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
         * TODO
         * - sort objects by z-index and primitive
         * - for each object:
         *   - check that last batch:
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

        // Pre-process and objects
        objects.filter { it.isEnabled }
            .forEach { it.pushToBatch() }
        tempObjects.filter { it.isEnabled }
            .forEach { it.pushToBatch() }
    }

    override fun postRender() {
        super.postRender()
        tempObjects.clear() // Clear debug shapes after each frame
    }

    private fun Renderable.pushToBatch() {
        when (this) {
            is DebugShape -> this.processShape()
            is GLRenderable -> this.pushToBatch(this.getAvailableBatch())
            is TextLabel -> this.glyphSprites.forEach { glyph ->
                glyph.pushToBatch(glyph.getAvailableBatch())
            }
        }
    }

    private fun DebugShape.processShape() {
        getParts().forEach { shapePart ->
            if (shapePart is Edge) {
                getLines(shapePart, this, lineStyle).forEach {
                    line -> line.pushToBatch(line.getAvailableBatch())
                }
            } else if (shapePart is Triangle) {
                val tri = this.copy(shape = shapePart)
                tri.pushToBatch(tri.getAvailableBatch())
            }
        }
    }

}