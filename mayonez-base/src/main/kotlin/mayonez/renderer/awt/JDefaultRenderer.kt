package mayonez.renderer.awt

import mayonez.*
import mayonez.graphics.*
import mayonez.graphics.debug.*
import mayonez.graphics.sprites.*
import mayonez.graphics.textures.*
import mayonez.math.*
import mayonez.renderer.*
import java.awt.*

private val DEFAULT_STROKE: Stroke = BasicStroke(DebugDraw.DEFAULT_STROKE_SIZE)

/**
 * Draws all sprites and debug information onto the screen with Java's AWT
 * library.
 *
 * @author SlavSquatSuperstar
 */
@UsesEngine(EngineType.AWT)
internal class JDefaultRenderer : SceneRenderer,
    DebugRenderer {

    // Renderer Objects
    private val objects: MutableList<JRenderable> = ArrayList() // permanent components
    private val shapes: MutableList<DebugShape> = ArrayList() // temporary shapes
    private val batches: MutableList<JRenderable> = ArrayList()

    // Scene Information
    private lateinit var background: Sprite
    private val windowWidth: Int = Preferences.screenWidth
    private val windowHeight: Int = Preferences.screenHeight

    // Scene Renderer Methods

    override fun setBackground(background: Sprite, sceneSize: Vec2, sceneScale: Float) {
        this.background = background
            .setSpriteTransform(Transform.scaleInstance(sceneSize * sceneScale))
    }

    override fun addRenderable(r: Renderable?) {
        if (r is JRenderable) objects.add(r)
    }

    override fun removeRenderable(r: Renderable?) {
        if (r is JRenderable) objects.remove(r)
    }

    // Debug Renderer Methods

    override fun addShape(shape: DebugShape) {
        shapes.add(shape)
    }

    // Renderer Methods

    override fun clear() {
        batches.clear()
        objects.clear()
        shapes.clear()
    }

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
        if (background.getTexture() == null) { // Only if no image set
            g2.color = background.getColor().toAWT()
            g2.fillRect(0, 0, windowWidth, windowHeight)
        }
    }

    /** Render the background image, if the scene has one. */
    private fun drawBackgroundImage(g2: Graphics2D) {
        val tex = background.getTexture() as? JTexture ?: return // Only if image is set
        tex.draw(
            g2, background.getSpriteTransform(), null,
            background.getColor(), 1f
        )
    }

    /** Transform the screen and render everything at the new position. */
    private fun transformScreen(g2: Graphics2D) {
        val cam = this.viewport ?: return
        translateAndScaleScreen(cam, g2)
        rotateScreen(cam, g2)
    }

    private fun rotateScreen(cam: Viewport, g2: Graphics2D) {
        val camAngleRad = Math.toRadians(cam.rotation.toDouble())
        val camCenter = cam.viewCenter
        g2.rotate(-camAngleRad, camCenter.x.toDouble(), camCenter.y.toDouble())
    }

    private fun translateAndScaleScreen(cam: Viewport, g2: Graphics2D) {
        val camOffset = cam.screenOffset
        val camZoom = cam.zoom.toDouble() // the zoom
        g2.translate(-camOffset.x * camZoom, -camOffset.y * camZoom)
        g2.scale(camZoom, camZoom)
    }

    // Camera Methods

    override fun getViewport(): Viewport = SceneManager.currentScene.camera

    // Batch Helper Methods

    /** Sort drawable objects into render "batches". */
    private fun createBatches() {
        batches.clear()
        pushObjectsToBatches()
        pushShapesToBatches()
        batches.sortBy(JRenderable::getZIndex)
    }

    private fun pushObjectsToBatches() {
        objects.filter { it.isEnabled }
            .forEach { batches.add(it) }
    }

    private fun pushShapesToBatches() {
        if (shapes.isNotEmpty()) {
            batches.addAll(shapes)
            shapes.clear()
        }
    }

    private fun drawBatches(g2: Graphics2D) {
        g2.stroke = DEFAULT_STROKE
        batches.forEach { it.render(g2) }
    }

}