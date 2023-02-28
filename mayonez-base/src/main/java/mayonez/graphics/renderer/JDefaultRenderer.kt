package mayonez.graphics.renderer

import mayonez.*
import mayonez.annotations.EngineType
import mayonez.annotations.UsesEngine
import mayonez.graphics.camera.Camera
import mayonez.graphics.DebugShape
import mayonez.graphics.JRenderable
import mayonez.graphics.sprites.Sprite
import mayonez.io.image.JTexture
import mayonez.math.Vec2
import mayonez.math.shapes.*
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.Stroke
import kotlin.math.roundToInt

/**
 * Draws all sprites and debug information onto the screen with Java's AWT
 * and Swing libraries.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
class JDefaultRenderer : SceneRenderer, DebugRenderer {

    // Render Data
    private val batches: MutableList<JRenderable>
    private val objects: MutableList<JRenderable> // permanent components
    private val shapes: MutableList<DebugShape> // temporary shapes
    private val stroke: Stroke = BasicStroke(DebugDraw.STROKE_SIZE)

    // Scene Information
    private lateinit var background: Sprite
    private var screenSize: Vec2
    private var sceneSize: Vec2
    private var sceneScale: Float

    init {
        batches = ArrayList()
        objects = ArrayList()
        shapes = ArrayList()
        screenSize = Mayonez.screenSize
        sceneSize = Vec2(1f)
        sceneScale = 1f
    }

    // Start Renderer Methods
    override fun start() {
        batches.clear()
        objects.clear()
        shapes.clear()
    }

    override fun setScene(newScene: Scene) {
        newScene.objects.forEach(this::addObject)
        background = newScene.background
        sceneSize = newScene.size
        sceneScale = newScene.scale
    }

    override fun addObject(obj: GameObject) {
        obj.components.forEach { comp ->
            if (comp is JRenderable) objects.add(comp)
        }
    }

    override fun removeObject(obj: GameObject) {
        obj.components.forEach { comp ->
            if (comp is JRenderable) objects.remove(comp)
        }
    }

    override fun addShape(shape: DebugShape) {
        shapes.add(shape)
    }

    // Render Loop Methods

    override fun render(g2: Graphics2D?) {
        val oldXf = g2?.transform ?: return // Save a copy of the unmodified transform

        drawBackgroundColor(g2)
        transformScreen(g2)
        drawBackgroundImage(g2)

        createBatches()
        drawBatches(g2)

        g2.transform = oldXf // Reset the transform to its previous state
    }

    // Pre-Render Methods

    /** Clear the screen and fill it with a background color. */
    private fun drawBackgroundColor(g2: Graphics2D) {
        if (background.texture == null) { // Only if no image set
            g2.color = background.color.toAWT()
            g2.fillRect(0, 0, screenSize.x.roundToInt(), screenSize.y.roundToInt())
        }
    }

    /** Render the background image, if the scene has one. */
    private fun drawBackgroundImage(g2: Graphics2D) {
        if (background.texture != null) { // Only if image set
            val tex = background.texture as JTexture
            tex.draw(g2, Transform.scaleInstance(Vec2(sceneSize)), null, sceneScale)
        }
    }

    /** Transform the screen and render everything at the new position. */
    private fun transformScreen(g2: Graphics2D) {
        val cam = this.camera ?: return
        translateAndScaleScreen(cam, g2)
        rotateScreen(cam, g2)
    }

    private fun rotateScreen(cam: Camera, g2: Graphics2D) {
        val camAngleRad = Math.toRadians(cam.rotation.toDouble())
        val camCenter = cam.position * SceneManager.currentScene.scale
        g2.rotate(-camAngleRad, camCenter.x.toDouble(), camCenter.y.toDouble())
    }

    private fun translateAndScaleScreen(cam: Camera, g2: Graphics2D) {
        val camOffset = cam.screenOffset
        val camZoom = cam.zoom.toDouble() // the zoom
        g2.translate(-camOffset.x * camZoom, -camOffset.y * camZoom)
        g2.scale(camZoom, camZoom)
    }

    // Batch Methods

    /** Sort drawable objects into render "batches". */
    private fun createBatches() {
        batches.clear()
        objects.forEach { obj -> // Add objects
            if (obj.isEnabled) batches.add(obj)
        }
        if (shapes.isNotEmpty()) { // Add shapes
            shapes.forEach(batches::add)
            shapes.clear()
        }
        batches.sortBy(JRenderable::getZIndex)
    }

    private fun drawBatches(g2: Graphics2D) {
        g2.stroke = stroke
        batches.forEach { obj -> obj.render(g2) }
    }

    // Stop Renderer Methods

    override fun stop() {
        batches.clear()
        objects.clear()
        shapes.clear()
    }

}