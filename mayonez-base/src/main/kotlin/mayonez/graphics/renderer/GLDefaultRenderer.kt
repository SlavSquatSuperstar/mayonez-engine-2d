package mayonez.graphics.renderer

import mayonez.*
import mayonez.annotations.*
import mayonez.graphics.*
import mayonez.graphics.sprites.*
import mayonez.math.*
import mayonez.math.shapes.*
import mayonez.util.*
import org.lwjgl.opengl.GL11.*
import kotlin.math.*

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
        background.setTransform<Component>(Transform.scaleInstance(newScene.size))
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
            if (shapePart is Edge) shapes.addLine(shapePart, shape, lineStyle)
            else if (shapePart is Triangle) shapes.addShapeCopy(shapePart, shape)
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
        if (background.texture == null) drawBackgroundColor()
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
        val bgColor = background.color.toGL()
        glClearColor(bgColor.x, bgColor.y, bgColor.z, 1f)
        glClear(GL_COLOR_BUFFER_BIT)
    }

    private fun setGLProperties() {
        glEnable(GL_BLEND)
        glEnable(GL_LINE_SMOOTH)
        when (lineStyle) {
            LineStyle.SINGLE -> glLineWidth(DebugShape.STROKE_SIZE)
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
            .forEach {
                if (it is ShapeSprite) {
                    addShape(it.toDebugShape()) // Break down shape into primitives then add them later
                } else {
                    it.pushToBatch(it.getAvailableBatch())
                }
            }
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
