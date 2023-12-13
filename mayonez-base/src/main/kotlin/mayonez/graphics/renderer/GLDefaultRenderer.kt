package mayonez.graphics.renderer

import mayonez.*
import mayonez.graphics.*
import mayonez.graphics.batch.*
import mayonez.graphics.camera.*
import mayonez.graphics.debug.*
import mayonez.graphics.sprites.*
import mayonez.math.*
import mayonez.math.shapes.*
import org.lwjgl.opengl.GL11.*

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

    override fun start() {
        super.start()
        objects.clear()
        shapes.clear()
        bgBatch.clearVertices()
    }

    override fun preRender() {
        super.preRender()
        val cam = camera as GLCamera
        shader.uploadMat4("uView", cam.getViewMatrix())
        shader.uploadMat4("uProjection", cam.getProjectionMatrix())
        shader.uploadIntArray("uTextures", textureSlots)
        drawBackground()
        setGLProperties()
    }

    /** Clear the screen and fill the background color or image. */
    private fun drawBackground() {
        if (background.getTexture() == null) drawBackgroundColor()
        else drawBackgroundImage()
    }

    private fun drawBackgroundImage() {
        glClearColor(1f, 1f, 1f, 1f)
        bgBatch.clearVertices()
        (background as GLSprite).pushToBatch(bgBatch)
        bgBatch.uploadVertices()
        bgBatch.drawBatch()
    }

    private fun drawBackgroundColor() {
        val bgColor = background.getColor().toGL()
        glClearColor(bgColor.x, bgColor.y, bgColor.z, 1f)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    private fun setGLProperties() {
        // Note: complex transparent shapes don't work very well
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glEnable(GL_LINE_SMOOTH)
        when (lineStyle) {
            LineStyle.SINGLE -> glLineWidth(DebugDraw.DEFAULT_STROKE_SIZE)
            LineStyle.MULTIPLE -> glLineWidth(1f)
            else -> return
        }
    }

    override fun createBatches() {
        pushObjectsToBatches()
        pushShapesToBatches()
    }

    private fun pushObjectsToBatches() {
        objects.filter { it.isEnabled }
            .forEach { it.pushToBatch(it.getAvailableBatch()) }
    }

    private fun pushShapesToBatches() {
        shapes.forEach { it.pushToBatch(it.getAvailableBatch()) }
    }

    override fun postRender() {
        super.postRender()
        shapes.clear() // Clear primitives after each frame
        glDisable(GL_LINE_SMOOTH)
        glDisable(GL_BLEND)
    }

    override fun stop() {
        super.stop()
        objects.clear()
        shapes.clear()
        bgBatch.deleteBatch()
    }

}
