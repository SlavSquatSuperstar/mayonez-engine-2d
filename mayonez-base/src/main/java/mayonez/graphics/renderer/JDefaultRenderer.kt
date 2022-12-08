package mayonez.graphics.renderer

import mayonez.DebugDraw
import mayonez.GameObject
import mayonez.Scene
import mayonez.annotations.EngineType
import mayonez.annotations.UsesEngine
import mayonez.math.Vec2
import mayonez.physics.shapes.*
import mayonez.util.JShape
import mayonez.util.MShape
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.Stroke
import java.awt.geom.AffineTransform
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.geom.Rectangle2D
import kotlin.math.roundToInt

/**
 * Draws all sprites and debug information onto the screen with Java's AWT and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
class JDefaultRenderer : SceneRenderer, DebugRenderer {
    private val stroke: Stroke = BasicStroke(DebugDraw.STROKE_SIZE)

    //    private val toDraw: MutableList<Pair<Int, Any>>
    private val objects: MutableList<GameObject>

    //    private val sprites: MutableList<JSprite> // if use sprites, then no object onUserRender
    private val shapes: MutableList<DebugShape>

    init {
//        toDraw = ArrayList()
        objects = ArrayList()
//        sprites = new ArrayList<>()
        shapes = ArrayList()
    }

    // GameObject Methods
    override fun setScene(newScene: Scene) {
        objects.addAll(newScene.objects)
//        newScene.objects.forEach(this::addObject)
    }

    override fun start() {
//        toDraw.clear()
        objects.clear()
//        sprites.clear()
        shapes.clear()
    }

    override fun addObject(obj: GameObject) {
        objects.add(obj)
//        val sprite = obj.getComponent(JSprite::class.java)
//        if (sprite != null) {
//            sprites.add(sprite)
//            sort = true
//        }
    }

    override fun removeObject(obj: GameObject) {
        objects.remove(obj)
//        val sprite = obj.getComponent(JSprite::class.java)
//        if (sprite != null) sprites.remove(sprite)
    }

    override fun addShape(shape: DebugShape) {
//        toDraw.add(Pair(shape.zIndex, shape))
        shapes.add(shape)
    }

    // Renderer Methods
    override fun render(g2: Graphics2D?) {
        val oldXf = g2?.transform ?: return // Save a copy of the unmodified transform

        // Transform the screen and render everything at the new position
        val cam = camera
        if (cam != null) {
            val camOffset = cam.offset
//            val camXf = cam.transform
//            val camRot = camXf.rotation
//            val camScl = camXf.scale
            g2.translate(-camOffset.x.toDouble(), -camOffset.y.toDouble())
//            g2.rotate(Math.toRadians(-camRot.toDouble()))
//            g2.scale(camScl.x.toDouble(), camScl.y.toDouble())
//            g2.color = Color.BLACK
//            g2.fillOval(((camOffset.x - 5).toInt()), ((camOffset.y - 5).toInt()), 10, 10)
        }

        // "Batch" and draw objects and shapes
        val toDraw = ArrayList<Pair<Int, Any>>()
        objects.forEach { o: GameObject -> toDraw.add(Pair(o.zIndex, o)) }
//        sprites.sortBy { s: Sprite -> s.getObject().getZIndex() }
//        sprites.forEach {s : JSprite -> s.render(g2)}
        if (shapes.isNotEmpty()) {
            shapes.forEach { s: DebugShape -> toDraw.add(Pair(s.zIndex, s)) }
            shapes.clear()
        }

        toDraw.sortBy { p: Pair<Int, Any> -> p.first } // sort everything by zIndex
        toDraw.forEach { p: Pair<Int, Any> ->
            when (p.second) {
                is GameObject -> (p.second as GameObject).render(g2)
                is DebugShape -> {
                    val s = p.second as DebugShape
                    g2.color = s.color.toAWT()
                    g2.stroke = stroke
                    val awtShape = s.shape.toAwt()
                    if (s.fill) g2.fill(awtShape) else g2.draw(awtShape)
                }
            }
        }
        toDraw.clear()

        g2.transform = oldXf // Reset the screen's transform to its unmodified state
    }

    override fun stop() {
//        toDraw.clear()
        objects.clear()
//        sprites.clear()
        shapes.clear()
    }

    // Shape Conversion Methods

    companion object {
        private fun MShape.toAwt(): JShape? {
            return when (this) {
                is Edge -> Line2D.Float(start.x, start.y, end.x, end.y)
                is Ellipse -> {
                    val min = this.center() - (size * 0.5f)
                    val ellipse = Ellipse2D.Float(min.x, min.y, size.x, size.y)
                    return if (this is Circle || this.isAxisAligned) ellipse
                    else ellipse.rotate(this.center(), angle)
                }

                is Rectangle -> {
                    val min = this.min()
                    val rect = Rectangle2D.Float(min.x, min.y, this.width, this.height)
                    return if (this.isAxisAligned) rect else rect.rotate(this.center(), this.angle)
                }

                is Polygon -> {
                    val poly = java.awt.Polygon()
                    this.vertices.forEach { poly.addPoint(it.x.roundToInt(), it.y.roundToInt()) }
                    return poly
                }

                else -> null
            }
        }

        private fun JShape.rotate(center: Vec2, angle: Float): JShape {
            val rotXf = AffineTransform.getRotateInstance(
                Math.toRadians(angle.toDouble()),
                center.x.toDouble(), center.y.toDouble()
            )
            return rotXf.createTransformedShape(this)
        }
    }
}