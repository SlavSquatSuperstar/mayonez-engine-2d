package mayonez.graphics.renderer

import mayonez.*
import mayonez.SceneManager.currentScene
import mayonez.Transform.Companion.scaleInstance
import mayonez.graphics.sprite.GLSprite
import mayonez.graphics.sprite.ShapeSprite
import mayonez.graphics.sprite.Sprite
import mayonez.math.Vec2
import mayonez.physics.shapes.*
import mayonez.physics.shapes.Rectangle.Companion.rectangleVertices
import org.lwjgl.opengl.GL11
import kotlin.math.roundToInt

/**
 * Draws all sprites and debug information onto the screen using LWJGL's OpenGL library.
 *
 * @author SlavSquatSuperstar
 */
class GLDefaultRenderer : GLRenderer("assets/shaders/default.glsl", MAX_BATCH_SIZE), SceneRenderer, DebugRenderer {

    companion object {
        private val MAX_BATCH_SIZE = Preferences.maxBatchSize
        private const val MAX_LINES = 500
        private const val MAX_TRIANGLES = 1000
    }

    private val lineStyle = LineStyle.QUADS
    private var sprites: MutableList<Any> = ArrayList()
    private var shapes: MutableList<DebugShape> = ArrayList()
    private var bgBatch: RenderBatch = RenderBatch(1, 0, DrawPrimitive.SPRITE)
    private lateinit var background: Sprite

    // Game Object Methods

    override fun setScene(newScene: Scene) {
        newScene.objects.forEach { obj: GameObject -> addObject(obj) }
        background = newScene.background
        background.setTransform<Component>(scaleInstance(newScene.size))
    }

    override fun addObject(obj: GameObject) {
        val sprite = obj.getComponent(GLSprite::class.java)
        if (sprite != null) sprites.add(sprite)
        val shape = obj.getComponent(ShapeSprite::class.java)
        if (shape != null) sprites.add(shape)
    }

    override fun removeObject(obj: GameObject) {
        val sprite = obj.getComponent(GLSprite::class.java)
        if (sprite != null) sprites.remove(sprite)
        val shape = obj.getComponent(ShapeSprite::class.java)
        if (shape != null) sprites.remove(shape)
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
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glEnable(GL11.GL_BLEND)
        when (lineStyle) {
            LineStyle.SINGLE -> GL11.glLineWidth(DebugDraw.STROKE_SIZE)
            LineStyle.MULTIPLE -> GL11.glLineWidth(1f)
            else -> return
        }
    }

    private fun drawBackground() {
        if (background.texture == null) {
            val bgColor = background.color.toGL()
            GL11.glClearColor(bgColor.x, bgColor.y, bgColor.z, 1f)
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
        } else {
            // Rebuffer and draw the background
            bgBatch.clear()
            pushSprite(bgBatch, background as GLSprite)
            bgBatch.upload()
            bgBatch.render()
        }
    }

    override fun createBatches() {
        sprites.forEach { spr: Any ->
            if (spr is GLSprite) {
                if (!spr.isEnabled) return
                val batch = getAvailableBatch(spr.texture, spr.gameObject.zIndex, DrawPrimitive.SPRITE)
                pushSprite(batch, spr) // Push vertices to batch
            } else if (spr is ShapeSprite) {
                if (!spr.isEnabled) return
                addShape(DebugShape(spr))
//                shapes.add(DebugShape(spr))
            }
        }
        shapes.forEach { ds: DebugShape -> // Push debug shape vertices to GPU
            when (val shape = ds.shape) {
                is Edge -> {
                    val batch = getAvailableBatch(null, ds.zIndex, DrawPrimitive.LINE, MAX_LINES)
                    batch.pushVec2(shape.start)
                    batch.pushVec4(ds.color.toGL())
                    batch.pushVec2(shape.end)
                    batch.pushVec4(ds.color.toGL())
                }

                is Triangle -> {
                    val batch = getAvailableBatch(null, ds.zIndex, DrawPrimitive.TRIANGLE, MAX_TRIANGLES)
                    for (v in shape.vertices) {
                        batch.pushVec2(v)
                        batch.pushVec4(ds.color.toGL())
                    }
                }
            }
        }
    }

    override fun postRender() {
        super.postRender()
        shapes.clear() // Clear primitives after each frame
        // Finish drawing
        GL11.glEnable(GL11.GL_LINE_SMOOTH)
        GL11.glEnable(GL11.GL_BLEND)
    }

    override fun stop() {
        super.stop()
        sprites.clear()
        shapes.clear()
        bgBatch.delete()
    }

    // Batch Helper Methods

    /**
     * Adds a sprite to a render batch and pushes its vertex data and texture.
     *
     * @param batch the batch
     * @param spr   the sprite
     */
    private fun pushSprite(batch: RenderBatch, spr: GLSprite) {
        // Add sprite vertex data
        val objXf = spr.transform.combine(spr.spriteTransform)
        val color = spr.color.toGL()
        val texCoords = spr.texCoords
        val texID = batch.addTexture(spr.texture)

        // Render sprite at object center and rotate according to object
        val sprVertices = rectangleVertices(Vec2(0f), Vec2(1f), objXf.rotation)
        for (i in sprVertices.indices) {
            // sprite_pos = (obj_pos + vert_pos * obj_scale) * world_scale
            val sprPos = objXf.position.add(sprVertices[i].mul(objXf.scale)).mul(currentScene.scale)
            batch.pushVec2(sprPos)
            batch.pushVec4(color)
            batch.pushVec2(texCoords[i])
            batch.pushInt(texID)
        }
    }

    override fun addShape(shape: DebugShape) { // break down a shape into primitives and add to list
        when (val s = shape.shape) {
            is Edge -> addLine(s, shape)
            is Polygon -> addPolygon(s, shape)
            is Ellipse -> addPolygon(s.toPolygon(), shape) // might be too many polys
        }
    }

    private fun addLine(edge: Edge, shape: DebugShape) {
        val stroke = DebugDraw.STROKE_SIZE
        val len = edge.length // Want to stretch lines to look flush
        when (lineStyle) {
            LineStyle.SINGLE -> shapes.add(shape)
            LineStyle.MULTIPLE -> {
                val stretched = edge.scale(Vec2((len + stroke - 1f) / len), null)
                val norm = edge.unitNormal()
                val start = (1 - stroke) * 0.5f // -(stroke - 1) / 2
                for (i in 0 until stroke.roundToInt()) {
                    val e = stretched.translate(norm.mul(start + i))
                    shapes.add(DebugShape(e, shape))
                }
            }

            LineStyle.QUADS -> {
                val stretchedLen = len + stroke - 1f
                val rect = Rectangle(edge.center(), Vec2(stretchedLen, stroke), edge.toVector().angle())
                for (tri in rect.triangles) shapes.add(DebugShape(tri, shape.color, true, DebugShape.Priority.LINE))
            }
        }
    }

    private fun addPolygon(poly: Polygon, shape: DebugShape) {
        if (shape.fill) for (tri in poly.triangles) shapes.add(DebugShape(tri, shape)) // Create a triangle fan
        else for (edge in poly.edges) addLine(edge, shape) // Create a wireframe
    }

    private enum class LineStyle {
        /**
         * Method 1. Draw a single line using glLineWidth() (may not work on all platforms)
         */
        SINGLE,

        /**
         * Method 2. Draw extra lines to simulate stroke size.
         */
        MULTIPLE,

        /**
         * Method 3. Draw lines using thin rectangles
         */
        QUADS
    }

}