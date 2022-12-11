package mayonez.graphics.renderer

import mayonez.*
import mayonez.annotations.EngineType
import mayonez.annotations.UsesEngine
import mayonez.graphics.Renderable
import mayonez.graphics.sprite.Sprite
import mayonez.io.image.JTexture
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

    // Render Data
    private val batches: MutableList<Pair<Int, Renderable>>
    private val objects: MutableList<Renderable>
    private val shapes: MutableList<DebugShape>
    private val stroke: Stroke = BasicStroke(DebugDraw.STROKE_SIZE)

    // Scene Information
    private lateinit var background: Sprite
    private var screenSize: Vec2 = Vec2()
    private var sceneSize: Vec2 = Vec2()
    private var sceneScale: Float = 1f

    init {
        batches = ArrayList()
        objects = ArrayList()
        shapes = ArrayList()
        screenSize = Mayonez.screenSize
    }

    // GameObject Methods
    override fun setScene(newScene: Scene) {
        newScene.objects.forEach(this::addObject)
        background = newScene.background
        sceneSize = newScene.size
        sceneScale = newScene.scale
    }

    override fun start() {
        batches.clear()
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
        if (g2 == null) return

        val oldXf = g2.transform // Save a copy of the unmodified transform
        drawBackgroundColor(g2)
        transformScreen(g2)

        drawBackgroundImage(g2)
        createBatches(g2)
        batches.forEach { p: Pair<Int, Renderable> -> p.second.render(g2) } // Draw everything

        g2.transform = oldXf // Reset the screen's transform to its previous state
    }
    private fun drawBackgroundColor(g2: Graphics2D) {
        if (background.texture == null) {
            g2.color = background.color.toAWT()
            g2.fillRect(0, 0, screenSize.x.roundToInt(), screenSize.y.roundToInt())
        }
    }

    private fun drawBackgroundImage(g2: Graphics2D) {
        if (background.texture != null) {
            val tex = background.texture as JTexture
            tex.draw(g2, Transform.scaleInstance(Vec2(sceneSize)), null, sceneScale)
        }
    }

    private fun transformScreen(g2: Graphics2D) {
        // Transform the screen and render everything at the new position
        val cam = this.camera ?: return
        val rotRad = Math.toRadians(cam.rotation.toDouble())
        val camZoom = cam.zoom.toDouble() // the zoom
        val camOffset = cam.offset
        val camCenter = cam.position * SceneManager.currentScene.scale

        // Translate, scale, then rotate
        g2.translate(-camOffset.x * camZoom, -camOffset.y * camZoom)
        g2.scale(camZoom, camZoom)
        g2.rotate(-rotRad, camCenter.x.toDouble(), camCenter.y.toDouble())
    }

    private fun createBatches(g2: Graphics2D) {
        // "Batch" and draw objects and shapes
        batches.clear()
        objects.forEach { r: Renderable -> // Add shapes
            if (r is Component) batches.add(Pair(r.gameObject.zIndex, r))
        }
        if (shapes.isNotEmpty()) { // Add objects
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
        batches.sortBy { p: Pair<Int, Renderable> -> p.first } // Sort everything by zIndex
    }

    override fun stop() {
        batches.clear()
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