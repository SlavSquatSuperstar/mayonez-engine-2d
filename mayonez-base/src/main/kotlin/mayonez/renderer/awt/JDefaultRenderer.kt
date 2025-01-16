package mayonez.renderer.awt

import mayonez.*
import mayonez.graphics.*
import mayonez.graphics.debug.*
import mayonez.renderer.*
import java.awt.*

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
    private val objects: MutableList<JRenderable> = ArrayList() // Sprites and shapes
    private val drawObjects: MutableList<JRenderable> = ArrayList() // Enabled objects

    // Scene Information
    private lateinit var backgroundColor: MColor
    private val windowWidth: Int = Preferences.screenWidth
    private val windowHeight: Int = Preferences.screenHeight

    // Scene Renderer Methods

    override fun setBackgroundColor(backgroundColor: MColor) {
        this.backgroundColor = backgroundColor
    }

    override fun addRenderable(r: Renderable?) {
        if (r is JRenderable) objects.add(r)
    }

    override fun removeRenderable(r: Renderable?) {
        if (r is JRenderable) objects.remove(r)
    }

    // Debug Renderer Methods

    override fun addShape(shape: DebugShape?) {
        if (shape != null) objects.add(shape)
    }

    // Renderer Methods

    override fun clear() {
        drawObjects.clear()
        objects.clear()
    }

    override fun render(g2: Graphics2D?) {
        val oldXf = g2?.transform ?: return // Save a copy of the unmodified transform

        // Draw background
        g2.color = backgroundColor.toAWT()
        g2.fillRect(0, 0, windowWidth, windowHeight)
        transformScreen(g2)

        // Crate "batches" from objects and shapes
        val scale = viewport.cameraScale * viewport.zoom
        drawObjects.clear()
        objects.filter { it.isEnabled }
            .map { if (it is DebugShape) it.adjustStrokeSize(scale) else it }
            .forEach { drawObjects.add(it) }

        // Draw batches
        drawObjects.sortBy { it.zIndex }
        drawObjects.forEach { it.render(g2) }

        // Remove all shapes after drawing
        objects.removeIf { it is DebugShape }

        g2.transform = oldXf // Reset the transform to its previous state
    }

    // Pre-Render Methods

    /** Transform the screen and render everything at the new position. */
    private fun transformScreen(g2: Graphics2D) {
        val cam = this.viewport
        translateAndScaleScreen(cam, g2)
        rotateScreen(cam, g2)
    }

    private fun rotateScreen(cam: Viewport, g2: Graphics2D) {
        val camAngleRad = Math.toRadians(cam.rotation.toDouble())
        val camCenter = cam.screenCenter
        g2.rotate(-camAngleRad, camCenter.x.toDouble(), camCenter.y.toDouble())
    }

    private fun translateAndScaleScreen(cam: Viewport, g2: Graphics2D) {
        val camOffset = cam.screenOffset
        val camZoom = cam.zoom.toDouble()
        val camScale = cam.cameraScale.toDouble()
        g2.translate(-camOffset.x * camZoom, -camOffset.y * camZoom)
        g2.scale(camZoom * camScale, camZoom * camScale)
    }

    // Camera Methods

    override fun getViewport(): Viewport = SceneManager.currentScene.camera

}

private fun DebugShape.adjustStrokeSize(cameraScale: Float): DebugShape {
    val newBrush = this.brush.copy(strokeSize = this.brush.strokeSize / cameraScale)
    return this.copy(brush = newBrush)
}