package mayonez.graphics.renderer

import mayonez.Component
import mayonez.DebugDraw
import mayonez.GameObject
import mayonez.Scene
import mayonez.Transform.Companion.scaleInstance
import mayonez.graphics.DebugShape
import mayonez.graphics.DrawPrimitive
import mayonez.graphics.GLRenderable
import mayonez.graphics.RenderBatch
import mayonez.graphics.sprites.GLSprite
import mayonez.graphics.sprites.ShapeSprite
import mayonez.graphics.sprites.Sprite
import mayonez.math.Vec2
import mayonez.math.shapes.Edge
import mayonez.math.shapes.Rectangle
import mayonez.math.shapes.Triangle
import org.lwjgl.opengl.GL11.*
import kotlin.math.roundToInt

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
        newScene.objects.forEach { obj: GameObject -> addObject(obj) }
        background = newScene.background
        background.setTransform<Component>(scaleInstance(newScene.size))
    }

    override fun addObject(obj: GameObject) {
        obj.components.forEach { c: Component ->
            if (c is GLRenderable) objects.add(c)
        }
    }

    override fun removeObject(obj: GameObject) {
        obj.components.forEach { c: Component ->
            if (c is GLRenderable) objects.remove(c)
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
        bgBatch.clear()
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
        glClearColor(1f, 1f, 1f, 1f);
        bgBatch.clear()
        (background as GLSprite).pushToBatch(bgBatch)
        bgBatch.upload()
        bgBatch.render()
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
        objects.forEach { obj: GLRenderable ->
            if (obj.isEnabled) {
                if (obj is ShapeSprite) {
                    addShape(DebugShape(obj)) // Break down shape into primitives then add them later
                } else {
                    val batch = getAvailableBatch(obj)
                    obj.pushToBatch(batch) // Push vertices to batch
                }
            }
        }
    }

    private fun pushShapesToBatches() {
        shapes.forEach { shape: DebugShape ->
            val batch = getAvailableBatch(shape)
            shape.pushToBatch(batch)
        }
    }

    override fun postRender() {
        super.postRender()
        shapes.clear() // Clear primitives after each frame
        // Finish drawing
        glDisable(GL_LINE_SMOOTH)
        glDisable(GL_BLEND)
    }

    override fun stop() {
        super.stop()
        objects.clear()
        shapes.clear()
        bgBatch.delete()
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

        /** Draw a line as multiple lines to simulate stroke size. */
        MULTIPLE,

        /** Draw lines using thin rectangles to simulate stroke size. */
        QUADS
    }

}