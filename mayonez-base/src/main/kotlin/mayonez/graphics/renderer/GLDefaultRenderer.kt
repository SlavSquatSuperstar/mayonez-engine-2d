package mayonez.graphics.renderer

import mayonez.*
import mayonez.graphics.*
import mayonez.graphics.sprites.*
import mayonez.math.*
import mayonez.math.shapes.*
import org.lwjgl.opengl.GL11.*
import kotlin.math.*

/**
 * Draws all sprites and debug information onto the screen using LWJGL's
 * OpenGL library.
 *
 * @author SlavSquatSuperstar
 */
class GLDefaultRenderer : GLRenderer("assets/shaders/default.glsl"), SceneRenderer,
    DebugRenderer {

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
        obj.components.forEach { comp ->
            if (comp is GLRenderable) objects.add(comp)
        }
    }

    override fun removeObject(obj: GameObject) {
        obj.components.forEach { comp ->
            if (comp is GLRenderable) objects.remove(comp)
        }
    }

    // Debug Renderer Methods

    override fun addShape(shape: DebugShape) {
        for (shapePart in shape.splitIntoParts()) {
            when (shapePart) {
                is Edge -> addLine(shapePart, shape)
                is Triangle -> shapes.add(DebugShape(shapePart, shape))
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

    private fun setGLProperties() {
        glEnable(GL_BLEND)
        glEnable(GL_LINE_SMOOTH)
        when (lineStyle) {
            LineStyle.SINGLE -> glLineWidth(DebugDraw.STROKE_SIZE)
            LineStyle.MULTIPLE -> glLineWidth(1f)
            else -> return
        }
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

    override fun createBatches() {
        pushObjectsToBatches()
        pushShapesToBatches()
    }

    private fun pushObjectsToBatches() {
        objects.forEach { obj ->
            if (obj.isEnabled) {
                if (obj is ShapeSprite) {
                    addShape(DebugShape(obj)) // Break down shape into primitives then add them later
                } else {
                    obj.pushToBatch(obj.getAvailableBatch())
                }
            }
        }
    }

    private fun pushShapesToBatches() {
        shapes.forEach { shape ->
            shape.pushToBatch(shape.getAvailableBatch())
        }
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

    // Batch Helper Methods

    private fun addLine(edge: Edge, shape: DebugShape) {
        when (lineStyle) {
            LineStyle.SINGLE -> shapes.add(shape)
            LineStyle.MULTIPLE -> addLineAsMultiple(edge, shape)
            LineStyle.QUADS -> addLineAsQuads(edge, shape)
        }
    }

    private fun addLineAsQuads(edge: Edge, shape: DebugShape) {
        val len = edge.length
        val stroke = DebugDraw.STROKE_SIZE

        val stretchedLen = len + stroke - 1f
        val rect = Rectangle(edge.center(), Vec2(stretchedLen, stroke), edge.toVector().angle())
        for (tri in rect.triangles) shapes.add(DebugShape(tri, shape.color, true, DrawPriority.LINE))
    }

    private fun addLineAsMultiple(line: Edge, shape: DebugShape) {
        val len = line.length
        val stroke = DebugDraw.STROKE_SIZE
        val stretched = line.scale(Vec2((len + stroke - 1f) / len), null)

        val norm = line.unitNormal()
        val start = (1 - stroke) * 0.5f // -(stroke - 1) / 2

        for (i in 0 until stroke.roundToInt()) {
            val lineElem = stretched.translate(norm * (start + i))
            shapes.add(DebugShape(lineElem, shape))
        }
    }

    // Helper Enum

    private enum class LineStyle {
        /**
         * Draw a single line and set the thickness using glLineWidth() (may not
         * work on all platforms).
         */
        SINGLE,

        /** Draw each line as multiple lines to simulate stroke size. */
        MULTIPLE,

        /** Draw each line using a thin quad (rectangle) to simulate stroke size. */
        QUADS
    }

}