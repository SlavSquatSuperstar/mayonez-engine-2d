package mayonez.graphics.renderer

import mayonez.*
import mayonez.annotations.EngineType
import mayonez.annotations.UsesEngine
import mayonez.graphics.Renderable
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

    private val objects: MutableList<Renderable>
    private val shapes: MutableList<DebugShape>

    init {
        objects = ArrayList()
        shapes = ArrayList()
    }

    // GameObject Methods
    override fun setScene(newScene: Scene) {
        newScene.objects.forEach(this::addObject)
    }

    override fun start() {
        objects.clear()
        shapes.clear()
    }

    override fun addObject(obj: GameObject) {
        obj.getComponents(Component::class.java).forEach { c: Component ->
            if (c is Renderable) objects.add(c)
        }
    }

    override fun removeObject(obj: GameObject) {
        obj.getComponents(Component::class.java).forEach { c: Component ->
            if (c is Renderable) objects.remove(c)
        }
    }

    override fun addShape(shape: DebugShape) {
        shapes.add(shape)
    }

    // Renderer Methods
    override fun render(g2: Graphics2D?) {
        val oldXf = g2?.transform ?: return // Save a copy of the unmodified transform
        val cam = camera

        if (cam != null) {
            // Transform the screen and render everything at the new position
            val rotRad = Math.toRadians(cam.rotation.toDouble())
            val camZoom = cam.zoom.toDouble() // the zoom
            val camOffset = cam.offset
            val camCenter = camera.position * SceneManager.currentScene.scale

            // Translate, scale, then rotate
            g2.translate(-camOffset.x * camZoom, -camOffset.y * camZoom)
            g2.scale(camZoom, camZoom)
            g2.rotate(-rotRad, camCenter.x.toDouble(), camCenter.y.toDouble())
        }

        // "Batch" and draw objects and shapes
        val batches = ArrayList<Pair<Int, Renderable>>()
        objects.forEach { r: Renderable ->
            if (r is Component) batches.add(Pair(r.gameObject.zIndex, r))
        }
        if (shapes.isNotEmpty()) {
            shapes.forEach { s: DebugShape ->
                batches.add(Pair(s.zIndex, Renderable() {
                    g2.color = s.color.toAWT()
                    g2.stroke = stroke
                    val awtShape = s.shape.toAwt()
                    if (s.fill) g2.fill(awtShape) else g2.draw(awtShape)
                }))
            }
            shapes.clear()
        }

        batches.sortBy { p: Pair<Int, Renderable> -> p.first } // sort everything by zIndex
        batches.forEach { p: Pair<Int, Renderable> -> p.second.render(g2) }
        batches.clear()
        g2.transform = oldXf // Reset the screen's transform to its previous state
    }

    override fun stop() {
        objects.clear()
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