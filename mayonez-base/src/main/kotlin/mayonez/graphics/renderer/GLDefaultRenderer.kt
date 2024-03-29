package mayonez.graphics.renderer

import mayonez.*
import mayonez.annotations.*
import mayonez.graphics.batch.*
import mayonez.graphics.debug.*
import mayonez.graphics.sprites.*
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

    private val lineStyle = LineStyle.QUADS
    private var objects: MutableList<GLRenderable> = ArrayList() // drawable objects
    private var shapes: MutableList<DebugShape> = ArrayList() // temporary shapes
    private var bgBatch: RenderBatch =
        RenderBatch(1, 0, DrawPrimitive.SPRITE)
    private lateinit var background: Sprite

    // Scene Renderer Methods

    override fun setScene(newScene: Scene) {
        newScene.objects.forEach(this::addObject)
        background = newScene.background
        background.setSpriteTransform(Transform.scaleInstance(newScene.size))
    }

    override fun addObject(obj: GameObject) {
        obj.components
            .filterIsInstance(GLRenderable::class.java)
            .forEach { objects.add(it) }
    }

    override fun removeObject(obj: GameObject) {
        obj.components
            .filterIsInstance(GLRenderable::class.java)
            .forEach { objects.remove(it) }
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
        shapes.clear()
        bgBatch.clearVertices()
    }

    override fun preRender() {
        super.preRender()
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
