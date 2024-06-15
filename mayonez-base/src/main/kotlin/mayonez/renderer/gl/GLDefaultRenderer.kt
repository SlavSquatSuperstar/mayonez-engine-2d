package mayonez.renderer.gl

import mayonez.*
import mayonez.graphics.*
import mayonez.graphics.debug.*
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
    private val objects: MutableList<GLRenderable> = ArrayList() // Drawable objects
    private val shapes: MutableList<DebugShape> = ArrayList() // Temporary shapes

    // Scene Background
    private lateinit var background: Sprite
    private val bgBatch: RenderBatch =
        RenderBatch(1, 0, DrawPrimitive.SPRITE)

    // Scene Renderer Methods

    override fun setBackground(background: Sprite, sceneSize: Vec2, sceneScale: Float) {
        this.background = background
            .setSpriteTransform(Transform.scaleInstance(sceneSize * sceneScale))
    }

    override fun addRenderable(r: Renderable?) {
        if (r is GLRenderable) objects.add(r)
    }

    override fun removeRenderable(r: Renderable?) {
        if (r is GLRenderable) objects.remove(r)
    }

    // Debug Renderer Methods

    override fun addShape(shape: DebugShape) {
        for (shapePart in shape.splitIntoParts()) {
            if (shapePart is Edge) {
                shapes.addLine(shapePart, shape, lineStyle)
            } else if (shapePart is Triangle) {
                shapes.addShapeAndCopyBrush(shapePart, shape)
            }
        }
    }

    // Renderer Methods

    override fun clear() {
        super.clear()
        objects.clear()
        shapes.clear()
        bgBatch.clearVertices()
    }

    override fun preRender() {
        super.preRender()

        // Upload uniforms
        val cam = viewport
        shader.uploadMat4("uView", cam.viewMatrix)
        shader.uploadMat4("uProjection", cam.projectionMatrix)
        shader.uploadIntArray("uTextures", textureSlots)

        // Draw background
        if (background.getTexture() == null) drawBackgroundColor()
        else drawBackgroundImage()

        // Set GL Properties
        when (lineStyle) {
            LineStyle.SINGLE -> glLineWidth(DebugDraw.DEFAULT_STROKE_SIZE)
            LineStyle.MULTIPLE -> glLineWidth(1f)
            else -> return
        }
    }

    private fun drawBackgroundImage() {
        bgBatch.clearVertices()
        (background as GLSprite).pushToBatch(bgBatch)
        bgBatch.uploadVertices()
        bgBatch.drawBatch()
    }

    private fun drawBackgroundColor() {
        val bgColor = background.getColor().toGL()
        GLHelper.clearScreen(bgColor.x, bgColor.y, bgColor.z, 1f)
    }

    override fun createBatches() {
        // Push objects
        objects.sortBy { it.zIndex }
        objects.filter { it.isEnabled }
            .forEach { it.pushToBatch(it.getAvailableBatch()) }
        // Push shapes
        shapes.sortBy { it.zIndex }
        shapes.forEach { it.pushToBatch(it.getAvailableBatch()) }
    }

    override fun postRender() {
        super.postRender()
        shapes.clear() // Clear primitives after each frame
    }

}
