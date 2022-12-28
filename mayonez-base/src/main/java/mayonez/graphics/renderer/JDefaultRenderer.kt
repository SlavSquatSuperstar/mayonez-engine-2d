package mayonez.graphics.renderer

import mayonez.*
import mayonez.annotations.EngineType
import mayonez.annotations.UsesEngine
import mayonez.graphics.DebugShape
import mayonez.graphics.JRenderable
import mayonez.graphics.sprites.Sprite
import mayonez.io.image.JTexture
import mayonez.math.Vec2
import mayonez.physics.shapes.*
import java.awt.BasicStroke
import java.awt.Graphics2D
import java.awt.Stroke
import kotlin.math.roundToInt

/**
 * Draws all sprites and debug information onto the screen with Java's AWT and Swing libraries.
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
            if (c is JRenderable) objects.add(c)
        }
    }

    override fun removeObject(obj: GameObject) {
        obj.getComponents(Component::class.java).forEach { c: Component ->
            if (c is JRenderable) objects.remove(c)
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
        createBatches()
        g2.stroke = stroke
        batches.forEach { r: JRenderable -> r.render(g2) } // Draw everything

        g2.transform = oldXf // Reset the transform to its previous state
    }

    /**
     * Clear the screen and fill it with a background color.
     */
    private fun drawBackgroundColor(g2: Graphics2D) {
        if (background.texture == null) { // Only if no image set
            g2.color = background.color.toAWT()
            g2.fillRect(0, 0, screenSize.x.roundToInt(), screenSize.y.roundToInt())
        }
    }

    /**
     * Render the background image, if the scene has one.
     */
    private fun drawBackgroundImage(g2: Graphics2D) {
        if (background.texture != null) { // Only if image set
            val tex = background.texture as JTexture
            tex.draw(g2, Transform.scaleInstance(Vec2(sceneSize)), null, sceneScale)
        }
    }

    /**
     * Transform the screen and render everything at the new position.
     */
    private fun transformScreen(g2: Graphics2D) {
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

    /**
     * Sort drawable objects into render "batches".
     */
    private fun createBatches() {
        batches.clear()
        objects.forEach { r: JRenderable -> // Add shapes
            if (r.isEnabled) batches.add(r)
        }
        if (shapes.isNotEmpty()) { // Add objects
            shapes.forEach { s: DebugShape -> batches.add(s) }
            shapes.clear()
        }
        batches.sortBy { r: JRenderable -> r.zIndex } // Sort everything by zIndex
    }

    override fun stop() {
        batches.clear()
        objects.clear()
        shapes.clear()
    }

}